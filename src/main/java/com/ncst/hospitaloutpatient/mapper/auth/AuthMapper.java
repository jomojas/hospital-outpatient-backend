package com.ncst.hospitaloutpatient.mapper.auth;

import com.ncst.hospitaloutpatient.model.entity.auth.StaffAccountEntity;
import com.ncst.hospitaloutpatient.model.entity.auth.StaffEntity;
import com.ncst.hospitaloutpatient.model.entity.auth.StaffRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface AuthMapper {
    StaffAccountEntity selectStaffAccountByUsername(@Param("username") String username);

    StaffEntity selectStaffByStaffId(@Param("staffId") long staffId);

    StaffRoleEntity selectStaffRoleByStaffId(@Param("roleId") long roleId);

    StaffAccountEntity selectStaffAccountByStaffId(@Param("staffId") String staffId);

    int updatePasswordByStaffId(@Param("staffId") String staffId, @Param("password") String password);

    String selectTypeByDepartmentId(@Param("departmentId") Integer departmentId);

    String getLastLoginTime(@Param("staffId") Long staffId);

    /**
     * 更新员工账户表的 last_login 字段
     * @param staffId 员工ID
     * @param newLoginTime 当前登录时间
     */
    void updateLastLoginTime(@Param("staffId") long staffId, @Param("newLoginTime") Date newLoginTime);

    String getDepartmentNameByDepartmentId(@Param("departmentId") Integer departmentId);
}
