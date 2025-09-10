package com.ncst.hospitaloutpatient.service.casefile;

import com.ncst.hospitaloutpatient.model.dto.casefile.*;
import com.ncst.hospitaloutpatient.model.dto.casefile.MedicalItemApplyRequest.ApplyItem;
import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.casefile.CaseMapper;
import com.ncst.hospitaloutpatient.model.entity.casefile.MedicalItemApply;
import com.ncst.hospitaloutpatient.model.entity.casefile.MedicalRecord;
import com.ncst.hospitaloutpatient.model.entity.casefile.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CaseService {
    @Autowired
    private CaseMapper caseMapper;

    @Transactional
    public void createMedicalRecord(MedicalRecordCreateRequest request) {
        MedicalRecord record = new MedicalRecord();
        record.setPatientNo(request.getPatientNo());
        record.setRegistrationId(request.getRegistrationId());
        record.setChiefComplaint(request.getChiefComplaint());
        record.setPresentHistory(request.getPresentHistory());
        record.setPhysicalExam(request.getPhysicalExam());
        record.setDiagnosis(request.getDiagnosis());
        record.setTreatmentPlan(request.getTreatmentPlan());
        record.setDoctorId(request.getDoctorId());
        record.setRecordTime(LocalDateTime.parse(request.getRecordTime()));
        int result = caseMapper.insertMedicalRecord(record);
        if (result != 1) {
            throw new BusinessException(500, "创建病案失败");
        }

        // 更新patient_visit的status为“已初诊”
        int updateResult = caseMapper.updateStatusToInitialConsultationDone(request.getRegistrationId());
        if (updateResult != 1) {
            throw new BusinessException(500, "更新初诊状态失败");
        }
    }

    @Transactional
    public void submitApplies(Integer recordId, MedicalItemApplyRequest request) {
        for (MedicalItemApplyRequest.ApplyItem item : request.getItems()) {
            MedicalItemApply apply = new MedicalItemApply();
            apply.setRecordId(recordId);
            apply.setRegistrationId(request.getRegistrationId());
            apply.setItemId(item.getItemId());
            apply.setApplyType(item.getApplyType());
            apply.setApplyPurpose(item.getApplyPurpose());
            apply.setApplySite(item.getApplySite());
            apply.setApplyTime(LocalDateTime.now());
            apply.setUnit(item.getUnit() != null ? item.getUnit() : 1);
            apply.setStatus("PENDING_PAYMENT");
            apply.setRemark(item.getRemark());
            int result = caseMapper.insertMedicalItemApply(apply);
            if(result != 1) {
                throw new BusinessException(500, "医疗项目申请提交失败");
            }
        }

        // 更新 patient_visit 的 current_status 和 status_changed_at
        int updateResult = caseMapper.updateStatusToWaitingForProjectPayment(request.getRegistrationId());
        if(updateResult != 1) {
            throw new BusinessException(500, "更新就诊状态失败");
        }
    }

    public List<CaseApplyResultDTO> listCaseResults(Integer recordId) {
        return caseMapper.selectCaseApplyResults(recordId);
    }

    public void createPrescriptions(Integer recordId, PrescriptionCreateRequest request) {
        for (PrescriptionCreateRequest.PrescriptionItem item : request.getPrescriptions()) {
            Prescription prescription = new Prescription();
            prescription.setRecordId(recordId);
            prescription.setRegistrationId(request.getRegistrationId());
            prescription.setDrugId(item.getDrugId());
            prescription.setDosage(item.getDosage());
            prescription.setQuantity(item.getQuantity());
            prescription.setPrescribeTime(LocalDateTime.now());
            prescription.setStatus("PENDING_PAYMENT");
            prescription.setRemark(item.getRemark());
            int result = caseMapper.insertPrescription(prescription);
            if(result != 1) {
                throw new BusinessException(500, "处方开具失败");
            }
        }
    }

    public CaseFeeDTO listCaseFees(Integer recordId) {
        Integer registrationId = caseMapper.getRegistrationIdByRecordId(recordId);

        CaseFeeDTO dto = new CaseFeeDTO();
        dto.setRegistrationFee(caseMapper.selectRegistrationFee(registrationId));
        dto.setMedicalItemFees(caseMapper.selectMedicalItemFees(recordId));
        dto.setPrescriptionFees(caseMapper.selectPrescriptionFees(recordId));

        BigDecimal total = dto.getRegistrationFee() == null ? BigDecimal.ZERO : dto.getRegistrationFee();
        if(dto.getMedicalItemFees() != null) {
            for (ItemFeeDTO fee : dto.getMedicalItemFees()) {
                total = total.add(fee.getAmount() == null ? BigDecimal.ZERO : fee.getAmount());
            }
        }
        if(dto.getPrescriptionFees() != null) {
            for (DrugFeeDTO fee : dto.getPrescriptionFees()) {
                total = total.add(fee.getAmount() == null ? BigDecimal.ZERO : fee.getAmount());
            }
        }
        dto.setTotal(total);

        return dto;
    }
}
