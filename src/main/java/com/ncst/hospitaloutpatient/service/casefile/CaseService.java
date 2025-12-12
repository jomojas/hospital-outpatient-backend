package com.ncst.hospitaloutpatient.service.casefile;

import com.ncst.hospitaloutpatient.common.enums.VisitStatus;
import com.ncst.hospitaloutpatient.common.enums.FeeStatus;
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
import java.math.RoundingMode;
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
        // 先查挂号ID
        Integer registrationId = caseMapper.selectRegistrationIdByRecordId(caseId);
        if (registrationId == null) {
            throw new BusinessException(404, "未找到对应的挂号记录");
        }

        // 校验当前就诊状态
        String currentStatus = caseMapper.selectPatientVisitStatusByRegistrationId(registrationId);
        if (currentStatus == null || !"WAITING_FOR_REVISIT".equals(currentStatus)) {
            throw new BusinessException(400, "请先完成所有医疗项目后再进行确诊");
        }

        // 状态正确才更新病案并置为 REVISITED
        caseMapper.updateCase(caseId, request);
        int row = caseMapper.updatePatientVisitStatusByRegistrationId(registrationId, "REVISITED");
        if (row != 1) {
            throw new BusinessException(500, "更新就诊状态失败");
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

    public List<CaseItemHistoryDTO> getCaseItemsHistory(Integer recordId) {
        return caseMapper.selectCaseItemsHistory(recordId);
    }

    @Transactional
    public void revokeMedicalItemApply(Integer applyId) {
        int updated = caseMapper.updateApplyStatusToRevoked(applyId);
        if (updated == 0) {
            throw new BusinessException(400, "撤销医疗项目申请失败");
        }

        // Find registrationId by applyId
        Integer registrationId = caseMapper.selectRegistrationIdByApplyId(applyId);
        if (registrationId == null) {
            throw new BusinessException(404, "未找到对应的挂号记录");
        }

        // Check if there are still pending/unfinished applies for this registration
        int remaining = caseMapper.countPendingOrUnfinishedAppliesByRegistrationId(registrationId);
        if (remaining == 0) {
            // No remaining applies -> set to WAITING_FOR_REVISIT
            int row = caseMapper.updatePatientVisitStatusByRegistrationId(registrationId, "WAITING_FOR_REVISIT");
            if (row != 1) {
                throw new BusinessException(500, "更新就诊状态失败");
            }
        }
    }

    public List<CaseApplyResultDTO> listCaseResults(Integer recordId) {
        // 先查结果
        return caseMapper.selectCaseApplyResults(recordId);
    }

    public List<PrescriptionHistoryDTO> listCasePrescriptions(Integer caseId) {
        List<PrescriptionHistoryDTO> list = caseMapper.selectPrescriptionsByCaseId(caseId);
        if (list != null) {
            for (PrescriptionHistoryDTO dto : list) {
                dto.setUnit(com.ncst.hospitaloutpatient.common.enums.DrugUnit.toLabel(dto.getUnit()));
            }
        }
        return list;
    }

    @Transactional
    public void createPrescriptions(Integer recordId, com.ncst.hospitaloutpatient.model.dto.casefile.PrescriptionCreateRequest request) {
        for (com.ncst.hospitaloutpatient.model.dto.casefile.PrescriptionCreateRequest.PrescriptionItem item : request.getPrescriptions()) {
            // 1) Reduce drug stock atomically, ensuring sufficient stock
            Double qty = item.getQuantity() == null ? 0.0 : item.getQuantity();
            if (qty <= 0) {
                throw new BusinessException(400, "处方数量必须大于0");
            }
            int stockUpdated = caseMapper.reduceDrugStock(item.getDrugId(), qty);
            if (stockUpdated != 1) {
                throw new BusinessException(400, "药品库存不足或药品不存在");
            }

            // 2) Insert prescription
            Prescription prescription = new Prescription();
            prescription.setRecordId(recordId);
            prescription.setRegistrationId(request.getRegistrationId());
            prescription.setDrugId(item.getDrugId());
            prescription.setDosage(item.getDosage());
            prescription.setQuantity(qty);
            prescription.setPrescribeTime(LocalDateTime.now());
            prescription.setStatus("PENDING_PAYMENT");
            prescription.setRemark(item.getRemark());
            int result = caseMapper.insertPrescription(prescription);
            if (result != 1) {
                throw new BusinessException(500, "处方开具失败");
            }
        }
        // 3) Update visit status after all prescriptions created
        Integer registrationId = caseMapper.selectRegistrationIdByRecordId(recordId);
        if (registrationId != null) {
            int row = caseMapper.updatePatientVisitStatusByRegistrationId(registrationId, "WAITING_FOR_PRESCRIPTION_PAYMENT");
            if (row != 1) {
                throw new BusinessException(500, "更新就诊状态失败");
            }
        }
    }

    @Transactional
    public void revokePrescription(Integer prescriptionId) {
        // 1) Set status to REVOKED only if not already REVOKED
        int updated = caseMapper.updatePrescriptionStatusToRevoked(prescriptionId);
        if (updated == 0) {
            // Not found or already revoked -> no-op
            return;
        }

        // 2) Read prescription to restore stock
        Prescription p = caseMapper.selectPrescriptionById(prescriptionId);
        if (p == null || p.getDrugId() == null || p.getQuantity() == null) {
            throw new BusinessException(404, "处方不存在或数据不完整");
        }

        int inc = caseMapper.increaseDrugStock(p.getDrugId(), p.getQuantity());
        if (inc != 1) {
            throw new BusinessException(500, "恢复药品库存失败");
        }
    }

    private static FeeStatus mapFeeStatus(String backend) {
        if (backend == null) return null;
        return switch (backend) {
            case "PENDING_PAYMENT" -> FeeStatus.UNPAID;
            case "UNFINISHED", "FINISHED" -> FeeStatus.PAID;
            case "CANCELLED", "RETURNED" -> FeeStatus.REFUNDED;
            case "REVOKED" -> FeeStatus.REVOKED;
            default -> null;
        };
    }

    private static BigDecimal bd(String v) {
        if (v == null || v.isBlank()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(v);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    public CaseFeeDTO listCaseFees(Integer recordId) {
        Integer registrationId = caseMapper.getRegistrationIdByRecordId(recordId);

        CaseFeeDTO dto = new CaseFeeDTO();
        dto.setRegistrationFee(caseMapper.selectRegistrationFee(registrationId).toString());

        List<ItemFeeDTO> items = caseMapper.selectMedicalItemFees(recordId);
        if (items != null) {
            for (ItemFeeDTO fee : items) {
                // format amount to 2 decimals (truncate extra digits)
                String amtStr = fee.getAmount();
                if (amtStr != null && !amtStr.isBlank()) {
                    try {
                        java.math.BigDecimal amt = new java.math.BigDecimal(amtStr).setScale(2, java.math.RoundingMode.DOWN);
                        fee.setAmount(amt.toPlainString());
                    } catch (NumberFormatException ignore) {
                        fee.setAmount("0.00");
                    }
                } else {
                    fee.setAmount("0.00");
                }
                // map raw backend status to FeeStatus and set mapped name back
                FeeStatus mapped = mapFeeStatus(fee.getStatus());
                fee.setStatus(mapped != null ? mapped.name() : null);
            }
        }
        dto.setMedicalItemFees(items);

        List<DrugFeeDTO> drugs = caseMapper.selectPrescriptionFees(recordId);
        if (drugs != null) {
            for (DrugFeeDTO fee : drugs) {
                // format amount to 2 decimals (truncate extra digits)
                String amtStr = fee.getAmount();
                if (amtStr != null && !amtStr.isBlank()) {
                    try {
                        java.math.BigDecimal amt = new java.math.BigDecimal(amtStr).setScale(2, java.math.RoundingMode.DOWN);
                        fee.setAmount(amt.toPlainString());
                    } catch (NumberFormatException ignore) {
                        fee.setAmount("0.00");
                    }
                } else {
                    fee.setAmount("0.00");
                }
                // map raw backend status to FeeStatus and set mapped name back
                FeeStatus mapped = mapFeeStatus(fee.getStatus());
                fee.setStatus(mapped != null ? mapped.name() : null);
            }
        }
        dto.setPrescriptionFees(drugs);

        java.math.BigDecimal total = bd(dto.getRegistrationFee());
        java.math.BigDecimal unpaid = java.math.BigDecimal.ZERO;

        if (items != null) {
            for (ItemFeeDTO fee : items) {
                java.math.BigDecimal amt = bd(fee.getAmount());
                try {
                    FeeStatus fs = FeeStatus.valueOf(fee.getStatus());
                    // exclude REFUNDED and REVOKED from total
                    if (fs != FeeStatus.REFUNDED && fs != FeeStatus.REVOKED) {
                        total = total.add(amt);
                    }
                    if (fs == FeeStatus.UNPAID) {
                        unpaid = unpaid.add(amt);
                    }
                } catch (Exception ignore) {
                    // status unknown -> include in total by default
                    total = total.add(amt);
                }
            }
        }
        if (drugs != null) {
            for (DrugFeeDTO fee : drugs) {
                java.math.BigDecimal amt = bd(fee.getAmount());
                try {
                    FeeStatus fs = FeeStatus.valueOf(fee.getStatus());
                    // exclude REFUNDED and REVOKED from total
                    if (fs != FeeStatus.REFUNDED && fs != FeeStatus.REVOKED) {
                        total = total.add(amt);
                    }
                    if (fs == FeeStatus.UNPAID) {
                        unpaid = unpaid.add(amt);
                    }
                } catch (Exception ignore) {
                    // status unknown -> include in total by default
                    total = total.add(amt);
                }
            }
        }

        dto.setTotalAmount(total.toPlainString());
        dto.setUnpaidAmount(unpaid.toPlainString());
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

    public void finishVisit(Integer registrationId) {
        caseMapper.updateVisitStatusToFinished(registrationId);
    }
}
