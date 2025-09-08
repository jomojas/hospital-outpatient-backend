package com.ncst.hospitaloutpatient.mapper.auth;

import com.ncst.hospitaloutpatient.model.entity.auth.StaffAccountEntity;
import com.ncst.hospitaloutpatient.model.entity.auth.StaffEntity;
import com.ncst.hospitaloutpatient.model.entity.auth.StaffRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthMapper {
    StaffAccountEntity selectStaffAccountByUsername(@Param("username") String username);

    StaffEntity selectStaffByStaffId(@Param("staffId") long staffId);

    StaffRoleEntity selectStaffRoleByStaffId(@Param("roleId") long roleId);

    StaffAccountEntity selectStaffAccountByStaffId(@Param("staffId") String staffId);

    int updatePasswordByStaffId(@Param("staffId") String staffId, @Param("password") String password);

}
