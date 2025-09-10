package com.ncst.hospitaloutpatient.service.billing;

import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.billing.ChargeMapper;
import com.ncst.hospitaloutpatient.model.dto.billing.ChargeItemResponse;
import com.ncst.hospitaloutpatient.model.dto.billing.ChargeSettleItemDTO;
import com.ncst.hospitaloutpatient.model.dto.billing.ChargeSettleRequestDTO;
import com.ncst.hospitaloutpatient.model.entity.outpatient.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChargeService {
    @Autowired
    private ChargeMapper chargeMapper;

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

    public List<ChargeItemResponse> listChargeItems(String type, String keyword, String itemType, Integer drugCategory,
                                                    Integer page, Integer size, String sortBy, String order) {
        int offset = (page - 1) * size;
        return chargeMapper.selectChargeItems(type, keyword, itemType, drugCategory, size, offset, sortBy, order);
    }

    public long countChargeItems(String type, String keyword, String itemType, Integer drugCategory) {
        return chargeMapper.countChargeItems(type, keyword, itemType, drugCategory);
    }

    @Transactional
    public void settleCharges(ChargeSettleRequestDTO request) {
        List<ChargeSettleItemDTO> items = request.getItems();
        Integer paymentMethodId = request.getPaymentMethodId();
        Integer settlementTypeId = request.getSettlementTypeId();
        Integer cashierId = getCurrentStaffId();
        for (ChargeSettleItemDTO item : items) {
            if ("ITEM".equals(item.getType())) {
                // 校验待缴费项目
                ChargeSettleItemDTO dbItem = chargeMapper.selectMedicalItemForSettle(item.getApplyId());
                if (dbItem == null || !"PENDING_PAYMENT".equals(dbItem.getStatus())) {
                    throw new BusinessException(500, "医疗项目[" + item.getApplyId() + "]状态异常，无法结算");
                }
                // 防止金额被篡改
                if (dbItem.getPrice().compareTo(item.getPrice()) != 0
                        || dbItem.getQuantity().compareTo(item.getQuantity()) != 0) {
                    throw new BusinessException(500, "医疗项目[" + item.getApplyId() + "]金额信息异常");
                }
                // 生成交易记录
                Transaction tx = new Transaction();
                tx.setBusinessType("MEDICAL_ITEM");
                tx.setRegistrationId(item.getRegistrationId());
                tx.setPatientId(item.getPatientId());
                tx.setAmount(dbItem.getTotalAmount());
                tx.setTransactionType("PAY");
                tx.setTransactionTime(LocalDateTime.now());
                tx.setRemark("医疗项目缴费");
                // cashierId、paymentMethodId 等可根据登录用户设定
                tx.setCashierId(cashierId);
                tx.setPaymentMethodId(paymentMethodId);
                tx.setSettlementTypeId(settlementTypeId);

                int txRes = chargeMapper.insertTransaction(tx);
                if (txRes <= 0) {
                    throw new BusinessException(500, "医疗项目[" + item.getApplyId() + "]交易记录插入失败");
                }

                int updateItemRes = chargeMapper.updateMedicalItemApplyStatus(item.getApplyId(), "UNFINISHED");
                if (updateItemRes <= 0) {
                    throw new BusinessException(500, "医疗项目[" + item.getApplyId() + "]状态更新失败");
                }

                // 检查是否所有项目已缴费，决定是否更新 patient_visit 状态
                int pending = chargeMapper.countUnpaidMedicalItem(item.getRegistrationId());
                if (pending == 0) {
                    int updateVisitRes = chargeMapper.updatePatientVisitStatus(item.getRegistrationId(), "WAITING_FOR_CHECKUP");
                    if (updateVisitRes <= 0) {
                        throw new BusinessException(500, "患者就诊单[" + item.getRegistrationId() + "]状态更新失败");
                    }
                }
            } else if ("DRUG".equals(item.getType())) {
                ChargeSettleItemDTO dbDrug = chargeMapper.selectPrescriptionForSettle(item.getApplyId());
                if (dbDrug == null || !"PENDING_PAYMENT".equals(dbDrug.getStatus())) {
                    throw new BusinessException(500, "处方[" + item.getApplyId() + "]状态异常，无法结算");
                }
                if (dbDrug.getPrice().compareTo(item.getPrice()) != 0
                        || dbDrug.getQuantity().compareTo(item.getQuantity()) != 0) {
                    throw new BusinessException(500, "处方[" + item.getApplyId() + "]金额信息异常");
                }
                Transaction tx = new Transaction();
                tx.setBusinessType("DRUG");
                tx.setRegistrationId(item.getRegistrationId());
                tx.setPatientId(item.getPatientId());
                tx.setAmount(dbDrug.getTotalAmount());
                tx.setTransactionType("PAY");
                tx.setTransactionTime(LocalDateTime.now());
                tx.setRemark("处方缴费");
                tx.setCashierId(cashierId);
                tx.setPaymentMethodId(paymentMethodId);
                tx.setSettlementTypeId(settlementTypeId);

                int txRes = chargeMapper.insertTransaction(tx);
                if (txRes <= 0) {
                    throw new BusinessException(500, "处方[" + item.getApplyId() + "]交易记录插入失败");
                }

                int updateDrugRes = chargeMapper.updatePrescriptionStatus(item.getApplyId(), "UNFINISHED");
                if (updateDrugRes <= 0) {
                    throw new BusinessException(500, "处方[" + item.getApplyId() + "]状态更新失败");
                }

                int pending = chargeMapper.countUnpaidPrescription(item.getRegistrationId());
                if (pending == 0) {
                    int updateVisitRes = chargeMapper.updatePatientVisitStatus(item.getRegistrationId(), "WAITING_FOR_MEDICINE");
                    if (updateVisitRes <= 0) {
                        throw new BusinessException(500, "患者就诊单[" + item.getRegistrationId() + "]状态更新失败");
                    }
                }
            } else {
                throw new BusinessException(40001, "未知类型：" + item.getType());
            }
        }
    }
}
