package com.ncst.hospitaloutpatient.service.hr;

import com.ncst.hospitaloutpatient.common.enums.DepartmentType;
import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.hr.EmployeeMapper;
import com.ncst.hospitaloutpatient.model.dto.hr.CreateEmployeeRequest;
import com.ncst.hospitaloutpatient.model.dto.hr.StaffDetailResponse;
import com.ncst.hospitaloutpatient.model.dto.hr.UpdateEmployeeDTO;
import com.ncst.hospitaloutpatient.model.entity.auth.StaffEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<StaffDetailResponse> listEmployees(String keyword, Integer departmentId, Integer roleId, String sortBy, String order, Integer page, Integer pageSize) {
        int offset = (page - 1) * pageSize;
        // 查询数据库
        List<StaffDetailResponse> result = employeeMapper.selectStaffDetailByFilter(keyword, departmentId, roleId, sortBy, order, offset, pageSize);
        for (StaffDetailResponse resp : result) {
            resp.setDepartmentType(DepartmentType.getChineseNameByCode(resp.getDepartmentType()));
        }
        return result;
    }

    @Transactional
    public void createEmployee(CreateEmployeeRequest request) {
        // 1. 插入 staff 表，自动回填 staffId
        StaffEntity staff = new StaffEntity();
        staff.setName(request.getName());
        staff.setPhone(request.getPhone());
        staff.setIdCard(request.getIdCard());
        staff.setDepartmentId(request.getDepartmentId());
        staff.setDescription(request.getDescription());
        staff.setRoleId(request.getRoleId());
        int resultStaff = employeeMapper.insertStaff(staff);
        if (resultStaff != 1 || staff.getStaffId() == null) {
            throw new BusinessException(500, "员工基本信息插入失败");
        }
        Integer staffId = staff.getStaffId();

        // 2. 密码加密
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. 插入 staff_account 表
        int resultAccount = employeeMapper.insertStaffAccount(
                staffId,
                request.getAccountName(),
                encodedPassword
        );
        if (resultAccount != 1) {
            throw new BusinessException(500, "员工账号信息插入失败");
        }

        // 4. 判断是否为门诊医生并插入 staff_doctor_ext 表
        if (isOutpatientDoctor(request.getDepartmentId())) {
            int isExpertValue = (request.getIsExpert() != null && request.getIsExpert()) ? 1 : 0;
            int resultExt = employeeMapper.insertStaffDoctorExt(staffId, isExpertValue);
            if (resultExt != 1) {
                throw new BusinessException(500, "医生扩展信息插入失败");
            }
        }
    }

    /**
     * 生成员工账号名。
     * 规则：根据 roleId -> staff_role.role_name，取前三位字母并转大写作为前缀（如 doctor -> DOC）。
     * 然后在 staff_account.account_name 中找该前缀开头的账号，取最大后三位数字 + 1，格式化为 3 位。
     * 示例：DOC001, DOC002, MAN003 ...
     */
    public String getAccountName(Integer roleId) {
        if (roleId == null) {
            throw new BusinessException(400, "roleId不能为空");
        }

        String roleName = employeeMapper.getRoleNameById(roleId);
        if (roleName == null || roleName.isBlank()) {
            throw new BusinessException(400, "无效的roleId=" + roleId);
        }
        if (roleName.length() < 3) {
            throw new BusinessException(400, "role_name长度不足3位，roleId=" + roleId);
        }

        String prefix = roleName.substring(0, 3).toUpperCase();

        Integer maxSeq = employeeMapper.getMaxAccountSeqByPrefix(prefix);
        int nextSeq = (maxSeq == null ? 1 : maxSeq + 1);

        if (nextSeq > 999) {
            throw new BusinessException(500, "账号序号已超过999，prefix=" + prefix);
        }

        return prefix + String.format("%03d", nextSeq);
    }

    public void updateEmployee(Integer id, UpdateEmployeeDTO dto) {
        // 1. 更新 staff 基础信息
        int count = employeeMapper.updateStaff(
                id,
                dto.getDepartmentId(),
                dto.getRoleId(),
                dto.getName(),
                dto.getPhone(),
                dto.getIdCard()
        );
        if (count <= 0) {
            throw new BusinessException(500, "员工信息更新失败");
        }

        // 2. 如果 isExpert 不为 null，更新 staff_doctor_ext
        if (dto.getIsExpert() != null) {
            int updated = employeeMapper.updateDoctorExt(id, dto.getIsExpert());
            if (updated == 0) {
                throw new BusinessException(500, "专家信息更新失败");
            }
        }
    }

    public void deleteEmployee(Integer staffId) {
        int affected = employeeMapper.deleteEmployeeByStaffId(staffId);
        if (affected <= 0) {
            throw new BusinessException(500, "删除员工失败");
        }
    }

    public void restoreEmployee(Integer staffId) {
        int affected = employeeMapper.restoreEmployeeByStaffId(staffId);
        if (affected <= 0) {
            throw new BusinessException(500, "恢复员工失败");
        }
    }

    public StaffDetailResponse getEmployee(Integer staffId) {
        return employeeMapper.selectStaffDetailById(staffId);
    }

    public long countEmployees(String keyword, Integer departmentId, Integer roleId) {
        return employeeMapper.countStaffDetailByFilter(keyword, departmentId, roleId);
    }

    // 判断是否为门诊医生
    private boolean isOutpatientDoctor(Integer departmentId) {
        if (departmentId == null) return false;
        String deptType = employeeMapper.getDepartmentType(departmentId);
        return "OUTPATIENT".equalsIgnoreCase(deptType);
    }

    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Integer staffId, String newPassword) {
        if (staffId == null) {
            throw new BusinessException(400, "员工id不能为空");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new BusinessException(400, "新密码不能为空");
        }

        boolean exists = employeeMapper.existsStaffById(staffId);
        if (!exists) {
            throw new BusinessException(404, "员工不存在，id=" + staffId);
        }

        String encoded = passwordEncoder.encode(newPassword);
        int rows = employeeMapper.updatePasswordByStaffId(staffId, encoded);
        if (rows != 1) {
            throw new BusinessException(500, "重置密码失败");
        }
    }

    public java.util.List<com.ncst.hospitaloutpatient.model.dto.hr.StaffRoleResponse> listRoles() {
        return employeeMapper.selectAllRoles();
    }
}
