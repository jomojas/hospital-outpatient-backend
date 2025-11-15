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

    void updateCase(@Param("caseId") Long caseId, @Param("dto") CaseRequestDTO dto);

    int insertMedicalItemApply(MedicalItemApply apply);

    List<CaseApplyResultDTO> selectCaseApplyResults(@Param("recordId") Integer recordId);

    int insertPrescription(Prescription prescription);

    Integer getRegistrationIdByRecordId(@Param("recordId") Integer recordId);

    BigDecimal selectRegistrationFee(@Param("registrationId") Integer registrationId);

    List<ItemFeeDTO> selectMedicalItemFees(@Param("recordId") Integer recordId);

    List<DrugFeeDTO> selectPrescriptionFees(@Param("recordId") Integer recordId);

    int updateStatusToInitialConsultationDone(@Param("registrationId") Integer registrationId);

    int updateStatusToWaitingForProjectPayment(@Param("registrationId") Integer registrationId);

//    int updatePatientVisitStatus(@Param("recordId") Integer recordId,
//                                 @Param("status") String status);


    // 新增：查 registration_id
    Integer selectRegistrationIdByRecordId(@Param("recordId") Integer recordId);

    // 新增：根据 registration_id 更新 patient_visit 状态
    int updatePatientVisitStatusByRegistrationId(@Param("registrationId") Integer registrationId,
                                                 @Param("status") String status);

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
}
