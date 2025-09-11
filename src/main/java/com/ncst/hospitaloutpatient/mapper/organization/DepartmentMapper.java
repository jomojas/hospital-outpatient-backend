package com.ncst.hospitaloutpatient.mapper.organization;

import com.ncst.hospitaloutpatient.model.dto.organization.CreateDepartmentRequest;
import com.ncst.hospitaloutpatient.model.dto.organization.DepartmentResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.DepartmentRoleResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.UpdateDepartmentRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    // 只查去重type
    List<String> selectDistinctTypes();

    List<DepartmentResponse> selectDepartmentsByType(@Param("type") String type);

    List<DepartmentRoleResponse> selectRolesByDepartmentId(@Param("departmentId") Integer departmentId);

    int insertDepartment(CreateDepartmentRequest request);

    int updateDepartment(@Param("departmentId") Integer departmentId, @Param("req") UpdateDepartmentRequest req);
}
