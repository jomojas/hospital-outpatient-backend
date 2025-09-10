package com.ncst.hospitaloutpatient.mapper.casefile;

import com.ncst.hospitaloutpatient.model.dto.casefile.CaseApplyResultDTO;
import com.ncst.hospitaloutpatient.model.dto.casefile.CaseFeeDTO;
import com.ncst.hospitaloutpatient.model.dto.casefile.DrugFeeDTO;
import com.ncst.hospitaloutpatient.model.dto.casefile.ItemFeeDTO;
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

    int insertMedicalItemApply(MedicalItemApply apply);

    List<CaseApplyResultDTO> selectCaseApplyResults(@Param("recordId") Integer recordId);

    int insertPrescription(Prescription prescription);

    Integer getRegistrationIdByRecordId(@Param("recordId") Integer recordId);

    BigDecimal selectRegistrationFee(@Param("registrationId") Integer registrationId);

    List<ItemFeeDTO> selectMedicalItemFees(@Param("recordId") Integer recordId);

    List<DrugFeeDTO> selectPrescriptionFees(@Param("recordId") Integer recordId);

    int updateStatusToInitialConsultationDone(@Param("registrationId") Integer registrationId);

    int updateStatusToWaitingForProjectPayment(@Param("registrationId") Integer registrationId);
}
