package com.ncst.hospitaloutpatient.service.casefile;

import com.ncst.hospitaloutpatient.common.enums.VisitStatus;
import com.ncst.hospitaloutpatient.model.dto.casefile.*;
import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.casefile.CaseMapper;
import com.ncst.hospitaloutpatient.model.entity.casefile.MedicalItemApply;
import com.ncst.hospitaloutpatient.model.entity.casefile.MedicalRecord;
import com.ncst.hospitaloutpatient.model.entity.casefile.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CaseService {
    @Autowired
    private CaseMapper caseMapper;

    private enum FrontendPatientStatus {
        WAITING_INITIAL, AFTER_INITIAL, WAITING_REVISIT, REVISIT_COMPLETED
    }

    private static final List<VisitStatus> ALL_BACKEND_STATUSES = List.of(VisitStatus.values());

    private static final Map<FrontendPatientStatus, List<VisitStatus>> STATUS_MAP = Map.of(
        FrontendPatientStatus.WAITING_INITIAL, List.of(VisitStatus.WAITING_FOR_CONSULTATION),
        FrontendPatientStatus.AFTER_INITIAL, List.of(
            VisitStatus.INITIAL_CONSULTATION_DONE,
            VisitStatus.WAITING_FOR_PROJECT_PAYMENT,
            VisitStatus.WAITING_FOR_CHECKUP,
            VisitStatus.CHECKING
        ),
        FrontendPatientStatus.WAITING_REVISIT, List.of(VisitStatus.WAITING_FOR_REVISIT, VisitStatus.REVISITED),
        FrontendPatientStatus.REVISIT_COMPLETED, ALL_BACKEND_STATUSES.subList(
            VisitStatus.WAITING_FOR_PRESCRIPTION_PAYMENT.ordinal(), ALL_BACKEND_STATUSES.size()
        )
    );

    private List<VisitStatus> convertFrontendStatus(String frontendStatus) {
        try {
            FrontendPatientStatus status = FrontendPatientStatus.valueOf(frontendStatus);
            return STATUS_MAP.getOrDefault(status, List.of());
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

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


    @Transactional
    public Integer createMedicalRecord(MedicalRecordCreateRequest request) {
        MedicalRecord record = new MedicalRecord();
        record.setPatientNo(request.getPatientNo());
        record.setRegistrationId(request.getRegistrationId());
        record.setChiefComplaint(request.getChiefComplaint());
        record.setPresentHistory(request.getPresentHistory());
        record.setPhysicalExam(request.getPhysicalExam());
        record.setDiagnosis(request.getDiagnosis());
        record.setTreatmentPlan(request.getTreatmentPlan());
        record.setDoctorId(getCurrentStaffId());
        record.setRecordTime(LocalDateTime.now());
        int result = caseMapper.insertMedicalRecord(record);
        if (result != 1) {
            throw new BusinessException(500, "创建病案失败");
        }

        Integer recordId = record.getRecordId();

        // 更新patient_visit的status为“已初诊”
        int updateResult = caseMapper.updateStatusToInitialConsultationDone(request.getRegistrationId());
        if (updateResult != 1) {
            throw new BusinessException(500, "更新初诊状态失败");
        }

        return recordId;
    }

    public Integer getRecordIdByRegistrationId(Integer registrationId) {
        return caseMapper.selectRecordIdByRegistrationId(registrationId);
    }

    public CaseDetailDTO getCaseDetailById(Integer caseId) {
        return caseMapper.selectCaseDetailById(caseId);
    }

    public void updateCase(Integer caseId, CaseRequestDTO request) {
        caseMapper.updateCase(caseId, request);
    }

    public void confirmCase(Integer caseId, CaseRequestDTO request) {
        caseMapper.updateCase(caseId, request);
        // 更新 patient_visit 状态为 REVISITED
        Integer registrationId = caseMapper.selectRegistrationIdByRecordId(caseId);
        if (registrationId != null) {
            int row = caseMapper.updatePatientVisitStatusByRegistrationId(registrationId, "REVISITED");
            if (row != 1) {
                throw new BusinessException(500, "更新就诊状态失败");
            }
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
        // 先查结果
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
        // 处方全部开具成功后，更新 patient_visit 状态为 WAITING_FOR_PRESCRIPTION_PAYMENT
        Integer registrationId = caseMapper.selectRegistrationIdByRecordId(recordId);
        if (registrationId != null) {
            int row = caseMapper.updatePatientVisitStatusByRegistrationId(registrationId, "WAITING_FOR_PRESCRIPTION_PAYMENT");
            if (row != 1) {
                throw new BusinessException(500, "更新就诊状态失败");
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

    public List<DoctorPatientDTO> getRegisteredPatientsByDoctor(int page, int pageSize, String keyword, String frontendStatus) {
        Integer doctorId = getCurrentStaffId();
        int offset = (page - 1) * pageSize;
        List<String> statusList = null;
        if (frontendStatus != null) {
            List<VisitStatus> backendStatuses = convertFrontendStatus(frontendStatus);
            statusList = backendStatuses.stream().map(Enum::name).toList();
        }
        java.time.LocalDate visitDate = java.time.LocalDate.now();
        return caseMapper.selectRegisteredPatientsByDoctor(doctorId, keyword, offset, pageSize, statusList, visitDate);
    }

    public long countRegisteredPatientsByDoctor(String keyword, String frontendStatus) {
        Integer doctorId = getCurrentStaffId();
        List<String> statusList = null;
        if (frontendStatus != null) {
            List<VisitStatus> backendStatuses = convertFrontendStatus(frontendStatus);
            statusList = backendStatuses.stream().map(Enum::name).toList();
        }
        java.time.LocalDate visitDate = java.time.LocalDate.now();
        return caseMapper.countRegisteredPatientsByDoctor(doctorId, keyword, statusList, visitDate);
    }

    public Map<String, Long> countPatientsByFrontendStatus() {
        Integer doctorId = getCurrentStaffId();
        java.time.LocalDate visitDate = java.time.LocalDate.now();
        List<DoctorPatientDTO> patients = caseMapper.selectAllRegisteredPatientsByDoctor(doctorId, null, visitDate);

        Map<String, Long> result = new LinkedHashMap<>();
        for (FrontendPatientStatus frontendStatus : FrontendPatientStatus.values()) {
            List<VisitStatus> backendStatuses = STATUS_MAP.getOrDefault(frontendStatus, List.of());
            long count = patients.stream()
                    .filter(p -> {
                        try {
                            VisitStatus status = VisitStatus.valueOf(p.getStatus());
                            return backendStatuses.contains(status);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .count();
            result.put(frontendStatus.name(), count);
        }
        return result;
    }

    public DoctorPatientDetailDTO getPatientDetailByMedicalNo(String medicalNo) {
        return caseMapper.selectPatientDetailByMedicalNo(medicalNo);
    }

    public ClinicWorkspaceContextDTO getClinicContextByRegistrationId(Integer registrationId) {
        ClinicWorkspaceContextRawDTO raw = caseMapper.selectClinicContextByRegistrationId(registrationId);
        if (raw == null) {
            return null;
        }
        ClinicWorkspaceContextDTO dto = new ClinicWorkspaceContextDTO();
        dto.setRegistrationId(raw.getRegistrationId());
        dto.setMedicalNo(raw.getMedicalNo());
        dto.setPatientName(raw.getPatientName());
        dto.setPatientGender(raw.getPatientGender());
        dto.setVisitStatus(raw.getVisitStatus());
        dto.setCaseId(raw.getCaseId());
        dto.setPatientAge(formatAge(raw.getBirthDate()));
        return dto;
    }

    private String formatAge(LocalDate birthDate) {
        if (birthDate == null) return null;
        LocalDate now = LocalDate.now();
        if (birthDate.isAfter(now)) return null;
        Period p = Period.between(birthDate, now);
        if (p.getYears() > 0) {
            return String.valueOf(p.getYears());
        }
        // For infants, show X岁Y月 style as requested; here simplified to months.
        int months = p.getMonths() + p.getYears() * 12;
        if (months > 0) {
            return months + "月";
        }
        return "0";
    }
}
