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

import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto login(String username, String password) {
        // 1. 账号密码验证
        StaffAccountEntity userAccount = authMapper.selectStaffAccountByUsername(username);
        if (userAccount == null || !passwordEncoder.matches(password, userAccount.getPassword())) {
            throw new BusinessException(40103, "账号不存在或密码错误");
        }

        // 2. 获取 staff_id
        long staffId = userAccount.getStaffId();

        // 3. 查 staff 表 (获取员工基本信息、roleId 和 departmentId)
        StaffEntity staff = authMapper.selectStaffByStaffId(staffId);
        if (staff == null) {
            throw new BusinessException(40401, "员工信息不存在");
        }
        long roleId = staff.getRoleId();

        // 4. 查 staff_role 表 (获取角色名称)
        StaffRoleEntity role = authMapper.selectStaffRoleByStaffId(roleId);
        if (role == null) {
            throw new BusinessException(40401, "角色不存在");
        }
        String roleName = role.getRoleName();

        // 5. 获取科室类型和科室名称
        String departmentType = authMapper.selectTypeByDepartmentId(staff.getDepartmentId());
        String departmentName = authMapper.getDepartmentNameByDepartmentId(staff.getDepartmentId());

        // --- 【关键修改部分】 ---

        // 6. 获取上次登录时间 (DB中的 last_login 字段)
        String lastLoginTime = authMapper.getLastLoginTime(staffId);

        // 7. 处理 lastLoginTime 的返回逻辑：
        String returnLastLoginTime = lastLoginTime;
        if (lastLoginTime == null || lastLoginTime.isEmpty()) {
            // 如果是首次登录，返回一个默认值给前端
            returnLastLoginTime = "N/A";
        }

        // 8. 更新数据库中的 last_login 字段为当前时间
        // 假设您在 authMapper 中新增了一个方法来更新 staff_account 表的 last_login 字段
        authMapper.updateLastLoginTime(staffId, new Date()); // 传入当前时间，或使用 MyBatis 接收 NOW()

        // 9. 组装 UserDto
        return new UserDto(
                staffId,
                userAccount.getAccountName(),
                roleName,
                departmentType,
                staff.getName(),
                returnLastLoginTime, // 使用经过处理的值
                departmentName
        );
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
