package com.ncst.hospitaloutpatient.service.pharmacy;

import com.ncst.hospitaloutpatient.common.enums.DrugUnit;
import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.pharmacy.PharmacyMapper;
import com.ncst.hospitaloutpatient.model.dto.pharmacy.*;
import com.ncst.hospitaloutpatient.model.entity.pharmacy.PharmacyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PharmacyService {
    @Autowired
    private PharmacyMapper pharmacyMapper;

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

    public List<DispensePendingDTO> listDispensePending(String keyword, String sortBy, String order, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<DispensePendingDTO> list = pharmacyMapper.listDispensePending(keyword, sortBy, order, offset, pageSize);
        if (list != null) {
            for (DispensePendingDTO dto : list) {
                dto.setUnit(DrugUnit.toLabel(dto.getUnit()));
            }
        }
        return list;
    }

    public long countDispensePending(String keyword) {
        return pharmacyMapper.countDispensePending(keyword);
    }

    @Transactional
    public void dispenseDrugs(DispenseDrugsRequest request) {
        Set<Integer> registrationIds = new HashSet<>();
        Date now = new Date();
        for (DispenseDrugItemRequest item : request.getPrescriptions()) {
            // 1. 更新处方为已发药
            int result = pharmacyMapper.updatePrescriptionStatusToFinished(item.getPrescriptionId());
            if(result == 0) {
                throw new BusinessException(500, "更新处方状态失败：" + item.getPrescriptionId());
            }

            // 2. 查询用于pharmacy_record的相关字段
            PrescriptionInfo prescriptionInfo = pharmacyMapper.selectPrescriptionInfo(item.getPrescriptionId());
            // PrescriptionInfo包含 prescriptionId, patientNo, patientName, drugId, drugName, quantity, amount

            // 3. 插入pharmacy_record
            PharmacyRecord record = new PharmacyRecord();
            record.setPrescriptionId(prescriptionInfo.getPrescriptionId());
            record.setPatientNo(prescriptionInfo.getPatientNo());
            record.setPatientName(prescriptionInfo.getPatientName());
            record.setDrugId(prescriptionInfo.getDrugId());
            record.setDrugName(prescriptionInfo.getDrugName());
            record.setQuantity(prescriptionInfo.getQuantity());
            record.setAmount(prescriptionInfo.getAmount());
            record.setOperateType("DISPENSE");
            record.setOperatorId(getCurrentStaffId());
            record.setOperateTime(now);
            int row = pharmacyMapper.insertPharmacyRecord(record);
            if (row == 0) {
                throw new BusinessException(500, "插入药房记录失败：" + item.getPrescriptionId());
            }

            // 4. 减少库存
            int updated = pharmacyMapper.decreaseDrugStock(prescriptionInfo.getDrugId(), prescriptionInfo.getQuantity());
            if (updated == 0) {
                throw new BusinessException(500, "减少药品库存失败：" + prescriptionInfo.getDrugId());
            }

            // 5. 收集registrationId
            Integer registrationId = pharmacyMapper.selectRegistrationIdByPrescriptionId(item.getPrescriptionId());
            if (registrationId != null) {
                registrationIds.add(registrationId);
            }
        }
        // 6. 检查所有处方是否已取药，更新patient_visit状态
        for (Integer registrationId : registrationIds) {
            int unfinished = pharmacyMapper.countUnfinishedPrescriptions(registrationId);
            if (unfinished == 0) {
                int result = pharmacyMapper.updatePatientVisitStatusByRegistrationId(registrationId, "FINISHED");
                if(result == 0) {
                    throw new BusinessException(500, "更新患者就诊状态失败：" + registrationId);
                }
            }
        }
    }

    @Transactional
    public void returnDrugs(DispenseDrugsRequest request) {
        Date now = new Date();
        Integer operatorId = getCurrentStaffId();

        for (DispenseDrugItemRequest item : request.getPrescriptions()) {
            // 1. 查询当前处方状态
            String status = pharmacyMapper.selectPrescriptionStatus(item.getPrescriptionId());

            // 2. 检查状态是否合法
            if (!"UNFINISHED".equals(status) && !"FINISHED".equals(status)) {
                throw new BusinessException(400, "处方" + item.getPrescriptionId() + "当前状态不可退药：" + status);
            }

            // 3. 更新为已退药
            int result = pharmacyMapper.updatePrescriptionStatusToReturned(item.getPrescriptionId());
            if (result == 0) {
                throw new BusinessException(500, "更新处方状态失败：" + item.getPrescriptionId());
            }

            // 4. 查询处方详细信息，用于写入pharmacy_record
            PrescriptionInfo prescriptionInfo = pharmacyMapper.selectPrescriptionInfo(item.getPrescriptionId());

            // 5. 插入pharmacy_record
            PharmacyRecord record = new PharmacyRecord();
            record.setPrescriptionId(prescriptionInfo.getPrescriptionId());
            record.setPatientNo(prescriptionInfo.getPatientNo());
            record.setPatientName(prescriptionInfo.getPatientName());
            record.setDrugId(prescriptionInfo.getDrugId());
            record.setDrugName(prescriptionInfo.getDrugName());
            record.setQuantity(prescriptionInfo.getQuantity());
            record.setAmount(prescriptionInfo.getAmount());
            record.setOperateType("RETURN");
            record.setOperatorId(operatorId);
            record.setOperateTime(now);
            int row = pharmacyMapper.insertPharmacyRecord(record);
            if (row == 0) {
                throw new BusinessException(500, "插入药房记录失败：" + item.getPrescriptionId());
            }

            // 6. 如果是已拿药再退药，需要回补库存
            if ("FINISHED".equals(status)) {
                int updated = pharmacyMapper.increaseDrugStock(
                        prescriptionInfo.getDrugId(), prescriptionInfo.getQuantity());
                if (updated == 0) {
                    throw new BusinessException(500, "回补药品库存失败：" + prescriptionInfo.getDrugId());
                }
            }
        }
    }

    public List<PharmacyRecordDTO> listPharmacyRecords(String keyword,
                                                       String type,
                                                       String sortBy,
                                                       String order,
                                                       int page,
                                                       int pageSize) {
        int offset = Math.max(page - 1, 0) * pageSize;
        List<PharmacyRecordDTO> list = pharmacyMapper.listPharmacyRecords(
                keyword, type, sortBy, order, offset, pageSize);
        if (list != null) {
            for (PharmacyRecordDTO dto : list) {
                dto.setUnit(DrugUnit.toLabel(dto.getUnit()));
            }
        }
        return list;
    }

    public long countPharmacyRecords(String keyword, String type) {
        return pharmacyMapper.countPharmacyRecords(keyword, type);
    }
}
