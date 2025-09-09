package com.ncst.hospitaloutpatient.mapper.organization;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper
public interface AdminMapper {
    Boolean isDoctorRole(@Param("staffId") Integer staffId);

    int updateDoctorQuota(@Param("doctorId") Integer doctorId, @Param("quotaDate") LocalDate quotaDate, @Param("quota") Integer quota);

    int insertDoctorQuota(@Param("doctorId") Integer doctorId, @Param("quotaDate") LocalDate quotaDate, @Param("quota") Integer quota);

    int updateRegistrationFee(@Param("numberType") String numberType, @Param("fee") BigDecimal fee);
}
