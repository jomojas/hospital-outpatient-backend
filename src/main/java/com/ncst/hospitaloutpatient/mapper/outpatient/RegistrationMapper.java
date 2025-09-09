package com.ncst.hospitaloutpatient.mapper.outpatient;

import com.ncst.hospitaloutpatient.model.dto.outpatient.CreateRegistrationRequest;
import com.ncst.hospitaloutpatient.model.entity.outpatient.PatientVisit;
import com.ncst.hospitaloutpatient.model.entity.outpatient.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper
public interface RegistrationMapper {

    int insertRegistration(CreateRegistrationRequest request);

    int insertPatientVisit(PatientVisit patientVisit);

    int incrementDoctorQuotaUsed(@Param("staffId") Integer staffId, @Param("quotaDate") LocalDate quotaDate);

    int insertTransaction(Transaction transaction);
}
