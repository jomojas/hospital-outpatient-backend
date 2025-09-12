package com.ncst.hospitaloutpatient.mapper.organization;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface DepartmentMapper {
    // ...existing code...
    /**
     * 根据科室ID查询科室类型
     */
    String selectTypeByDepartmentId(@Param("departmentId") Long departmentId);
    // ...existing code...
}

