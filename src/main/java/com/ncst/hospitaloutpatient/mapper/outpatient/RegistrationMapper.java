package com.ncst.hospitaloutpatient.mapper.outpatient;

import com.ncst.hospitaloutpatient.model.dto.outpatient.CreateRegistrationRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper
public interface RegistrationMapper {

    int insertRegistration(CreateRegistrationRequest request);

    int insertPatientVisit(
            @Param("patientId") Integer patientId,
            @Param("registrationId") Integer registrationId,
            @Param("departmentId") Integer departmentId,
            @Param("doctorId") Integer doctorId,
            @Param("visitDate") LocalDate visitDate,
            @Param("currentStatus") String currentStatus,
            @Param("statusChangedAt") LocalDateTime statusChangedAt,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("updatedAt") LocalDateTime updatedAt
    );

}
