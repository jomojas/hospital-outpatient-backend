package com.ncst.hospitaloutpatient.mapper.organization;

import com.ncst.hospitaloutpatient.model.dto.organization.DoctorResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DoctorMapper {

    List<DoctorResponse> selectDoctorsByDepartmentId(@Param("departmentId") Integer departmentId, @Param("visitDate") String visitDate);
}
