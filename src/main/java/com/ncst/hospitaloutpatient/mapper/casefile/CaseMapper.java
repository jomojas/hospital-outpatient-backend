package com.ncst.hospitaloutpatient.mapper.casefile;

import com.ncst.hospitaloutpatient.model.dto.casefile.*;
import com.ncst.hospitaloutpatient.model.entity.casefile.MedicalItemApply;
import com.ncst.hospitaloutpatient.model.entity.casefile.MedicalRecord;
import com.ncst.hospitaloutpatient.model.entity.casefile.Prescription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CaseMapper {

    int insertMedicalRecord(MedicalRecord medicalRecord);

    Integer selectRecordIdByRegistrationId(@Param("registrationId") Integer registrationId);

    CaseDetailDTO selectCaseDetailById(@Param("caseId") Integer caseId);

    void updateCase(@Param("caseId") Integer caseId, @Param("dto") CaseRequestDTO dto);

    int insertMedicalItemApply(MedicalItemApply apply);

    List<CaseApplyResultDTO> selectCaseApplyResults(@Param("recordId") Integer recordId);

    List<PrescriptionHistoryDTO> selectPrescriptionsByCaseId(@Param("caseId") Integer caseId);

    int insertPrescription(Prescription prescription);

    Integer getRegistrationIdByRecordId(@Param("recordId") Integer recordId);

    BigDecimal selectRegistrationFee(@Param("registrationId") Integer registrationId);

    List<ItemFeeDTO> selectMedicalItemFees(@Param("recordId") Integer recordId);

    List<DrugFeeDTO> selectPrescriptionFees(@Param("recordId") Integer recordId);

    int updateStatusToInitialConsultationDone(@Param("registrationId") Integer registrationId);

    int updateStatusToWaitingForProjectPayment(@Param("registrationId") Integer registrationId);

//    int updatePatientVisitStatus(@Param("recordId") Integer recordId,
//                                 @Param("status") String status);

    List<CaseItemHistoryDTO> selectCaseItemsHistory(@Param("recordId") Integer recordId);

    int updateApplyStatusToRevoked(@Param("applyId") Integer applyId);

    Integer selectRegistrationIdByApplyId(@Param("applyId") Integer applyId);

    int countPendingOrUnfinishedAppliesByRegistrationId(@Param("registrationId") Integer registrationId);

    int updatePatientVisitStatusByRegistrationId(@Param("registrationId") Integer registrationId,
                                                 @Param("status") String status);

    // Reduce drug stock by quantity, only when stock is sufficient
    int reduceDrugStock(@Param("drugId") Integer drugId, @Param("quantity") Double quantity);

    int updatePrescriptionStatusToRevoked(@Param("prescriptionId") Integer prescriptionId);

    Prescription selectPrescriptionById(@Param("prescriptionId") Integer prescriptionId);

    int increaseDrugStock(@Param("drugId") Integer drugId, @Param("quantity") Double quantity);

    // 新增：查 registration_id
    Integer selectRegistrationIdByRecordId(@Param("recordId") Integer recordId);

    String selectPatientVisitStatusByRegistrationId(@Param("registrationId") Integer registrationId);

    List<DoctorPatientDTO> selectRegisteredPatientsByDoctor(
        @Param("doctorId") Integer doctorId,
        @Param("keyword") String keyword,
        @Param("offset") int offset,
        @Param("limit") int limit,
        @Param("statusList") List<String> statusList,
        @Param("visitDate") java.time.LocalDate visitDate
    );

    List<DoctorPatientDTO> selectAllRegisteredPatientsByDoctor(
        @Param("doctorId") Integer doctorId,
        @Param("statusList") List<String> statusList,
        @Param("visitDate") java.time.LocalDate visitDate
    );

    long countRegisteredPatientsByDoctor(
        @Param("doctorId") Integer doctorId,
        @Param("keyword") String keyword,
        @Param("statusList") List<String> statusList,
        @Param("visitDate") java.time.LocalDate visitDate
    );

    DoctorPatientDetailDTO selectPatientDetailByMedicalNo(@Param("medicalNo") String medicalNo);

    ClinicWorkspaceContextRawDTO selectClinicContextByRegistrationId(@Param("registrationId") Integer registrationId);

    int updateVisitStatusToFinished(@Param("registrationId") Integer registrationId);
}
