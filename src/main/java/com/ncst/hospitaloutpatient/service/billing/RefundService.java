package com.ncst.hospitaloutpatient.service.billing;

import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.billing.RefundMapper;
import com.ncst.hospitaloutpatient.model.dto.billing.ChargeRefundableItemResponse;
import com.ncst.hospitaloutpatient.model.dto.billing.ChargeSettleItemDTO;
import com.ncst.hospitaloutpatient.model.entity.billing.Registration;
import com.ncst.hospitaloutpatient.model.entity.casefile.Prescription;
import com.ncst.hospitaloutpatient.model.entity.outpatient.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RefundService {

    @Autowired
    private RefundMapper refundMapper;

    public Integer getCurrentStaffId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            // 你在 filter 里 set 的是字符串 staffId
            String staffIdStr = authentication.getPrincipal().toString();
            try {
                return Integer.parseInt(staffIdStr);
            } catch (NumberFormatException e) {
                // 可以做异常处理
            }
        }
        return null;
    }

    public List<ChargeRefundableItemResponse> listRefundableItems(
            String type, String keyword, String itemType, Integer drugCategory,
            Integer page, Integer pageSize, String sortBy, String order) {

        int offset = (page != null && page > 0 ? page - 1 : 0) * (pageSize != null ? pageSize : 10);
        int limit = pageSize != null ? pageSize : 10;

        return refundMapper.selectRefundableItems(type, keyword, itemType, drugCategory, offset, limit, sortBy, order);
    }

    public long countRefundableItems(
            String type, String keyword, String itemType, Integer drugCategory) {
        return refundMapper.countRefundableItems(type, keyword, itemType, drugCategory);
    }

    @Transactional
    public void refund(List<ChargeSettleItemDTO> refundItems) {
        if (refundItems == null || refundItems.isEmpty()) return;

        Integer cashierId = getCurrentStaffId();
        Map<Integer, RegistrationRefundFlags> flagsByReg = new HashMap<>();

        for (ChargeSettleItemDTO item : refundItems) {
            Integer registrationId = item.getRegistrationId();
            flagsByReg.computeIfAbsent(registrationId, k -> new RegistrationRefundFlags());
            RegistrationRefundFlags flags = flagsByReg.get(registrationId);

            Registration registration = refundMapper.selectRegistrationById(registrationId);
            String visitStatus = refundMapper.selectPatientVisitStatus(registrationId);

            if ("ITEM".equals(item.getType())) {
                flags.hasItemRefund = true;

//                if (!"WAITING_FOR_CHECKUP".equals(visitStatus)) {
//                    throw new BusinessException(40001, "当前状态不可退医疗项目！");
//                }
                if (!refundMapper.hasPayTransactionForMedicalItem(item)) {
                    throw new BusinessException(40001, "未找到缴费记录，不能退费！");
                }
                if (refundMapper.hasRefundTransactionForMedicalItem(item)) {
                    throw new BusinessException(40001, "该项目已退费！");
                }

                Transaction tx = new Transaction();
                tx.setBusinessType("MEDICAL_ITEM");
                tx.setRegistrationId(registrationId);
                tx.setPatientId(item.getPatientId());
                tx.setAmount(item.getTotalAmount());
                tx.setTransactionType("REFUND");
                tx.setTransactionTime(LocalDateTime.now());
                tx.setRemark("医疗项目退费");
                tx.setCashierId(cashierId);
                tx.setPaymentMethodId(registration.getPaymentMethodId());
                tx.setSettlementTypeId(registration.getSettlementTypeId());
                refundMapper.insertTransaction(tx);

                refundMapper.updateMedicalItemApplyStatus(item.getApplyId(), "CANCELLED");
            } else if ("DRUG".equals(item.getType())) {
                flags.hasDrugRefund = true;

                if (!("WAITING_FOR_MEDICINE".equals(visitStatus) || "MEDICINE_RETURNED".equals(visitStatus))) {
                    throw new BusinessException(40001, "该患者当前不可退药品！");
                }
                if (!refundMapper.hasPayTransactionForDrug(item)) {
                    throw new BusinessException(40001, "未找到缴费记录，不能退费！");
                }
                if (refundMapper.hasRefundTransactionForDrug(item)) {
                    throw new BusinessException(40001, "该药品已退费！");
                }

                Transaction tx = new Transaction();
                tx.setBusinessType("DRUG");
                tx.setRegistrationId(registrationId);
                tx.setPatientId(item.getPatientId());
                tx.setAmount(item.getTotalAmount());
                tx.setTransactionType("REFUND");
                tx.setTransactionTime(LocalDateTime.now());
                tx.setRemark("药品退费");
                tx.setCashierId(cashierId);
                tx.setPaymentMethodId(registration.getPaymentMethodId());
                tx.setSettlementTypeId(registration.getSettlementTypeId());
                refundMapper.insertTransaction(tx);

                // Update prescription status to RETURNED (only if not already RETURNED)
                int updated = refundMapper.updatePrescriptionStatusIfNot(item.getApplyId(), "RETURNED");
                if (updated == 0) {
                    // No status change -> do not adjust stock
                    continue;
                }

                // Restore drug stock by prescription quantity
                Prescription p = refundMapper.selectPrescriptionById(item.getApplyId());
                if (p == null || p.getDrugId() == null || p.getQuantity() == null) {
                    throw new BusinessException(404, "处方不存在或数据不完整");
                }
                int inc = refundMapper.increaseDrugStock(p.getDrugId(), p.getQuantity());
                if (inc != 1) {
                    throw new BusinessException(500, "恢复药品库存失败");
                }
            }
        }

        for (Map.Entry<Integer, RegistrationRefundFlags> entry : flagsByReg.entrySet()) {
            Integer registrationId = entry.getKey();
            RegistrationRefundFlags flags = entry.getValue();

            if (flags.hasItemRefund) {
                int unfinishedMedical = refundMapper.countUnfinishedMedicalItems(registrationId);
                if (unfinishedMedical == 0) {
                    int row = refundMapper.updateStatus(registrationId, "WAITING_FOR_REVISIT");
                    if (row != 1) throw new BusinessException(500, "更新就诊状态失败");
                }
            }

            if (flags.hasDrugRefund) {
                int unfinishedPrescriptions = refundMapper.countUnfinishedPrescriptions(registrationId);
                if (unfinishedPrescriptions == 0) {
                    int row = refundMapper.updateStatus(registrationId, "FINISHED");
                    if (row != 1) throw new BusinessException(500, "更新就诊状态失败");
                }
            }
        }
    }

    private static class RegistrationRefundFlags {
        boolean hasItemRefund = false;
        boolean hasDrugRefund = false;
    }
}
