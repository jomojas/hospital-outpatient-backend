package com.ncst.hospitaloutpatient.service.auth;

import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.auth.AuthMapper;
import com.ncst.hospitaloutpatient.model.dto.auth.UserDto;
import com.ncst.hospitaloutpatient.model.entity.auth.StaffAccountEntity;
import com.ncst.hospitaloutpatient.model.entity.auth.StaffEntity;
import com.ncst.hospitaloutpatient.model.entity.auth.StaffRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto login(String username, String password) {
        StaffAccountEntity userAccount = authMapper.selectStaffAccountByUsername(username);
        if (userAccount == null || !passwordEncoder.matches(password, userAccount.getPassword())) {
            throw new BusinessException(40103, "账号不存在或密码错误");
        }

        // 1. 获取staff_id
        long staffId = userAccount.getStaffId();

        // 2. 查staff表
        StaffEntity staff = authMapper.selectStaffByStaffId(staffId);
        if (staff == null) {
            throw new BusinessException(40401, "员工信息不存在");
        }
        long roleId = staff.getRoleId();

        // 3. 查staff_role表
        StaffRoleEntity role = authMapper.selectStaffRoleByStaffId(roleId);
        if (role == null) {
            throw new BusinessException(40401, "角色不存在");
        }
        String roleName = role.getRoleName();

        // 4. 获取科室类型
        String departmentType = authMapper.selectTypeByDepartmentId(staff.getDepartmentId());

        // 4. 组装UserDto
        return new UserDto(staffId, userAccount.getAccountName(), roleName, departmentType);
    }

    public void changePassword(String staffId, String oldPassword, String newPassword) {
        // 1. 查询用户（根据 staffId）
        StaffAccountEntity userAccount = authMapper.selectStaffAccountByStaffId(staffId);
        if (userAccount == null) {
            throw new BusinessException(40401, "用户不存在");
        }
        // 2. 校验原密码
        if (!passwordEncoder.matches(oldPassword, userAccount.getPassword())) {
            throw new BusinessException(40001, "原密码错误");
        }
        // 3. 加密新密码
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        // 4. 更新数据库
        int count = authMapper.updatePasswordByStaffId(staffId, encodedNewPassword);
        if (count != 1) {
            throw new BusinessException(500, "密码修改失败，请稍后再试");
        }
    }
}
