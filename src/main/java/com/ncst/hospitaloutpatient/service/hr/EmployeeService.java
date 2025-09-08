package com.ncst.hospitaloutpatient.service.hr;

import com.ncst.hospitaloutpatient.common.enums.DepartmentType;
import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.hr.EmployeeMapper;
import com.ncst.hospitaloutpatient.model.dto.hr.CreateEmployeeRequest;
import com.ncst.hospitaloutpatient.model.dto.hr.StaffDetailResponse;
import com.ncst.hospitaloutpatient.model.entity.auth.StaffEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.SslBundleSslEngineFactory;
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

    public String getAccountName(Integer departmentId) {
        // 1. 获取科室前缀
        String prefix = employeeMapper.getDepartmentPrefix(departmentId);
        if (prefix == null) {
            throw new BusinessException(40001, "无效的departmentId");
        }
        // 2. 获取当前年份后两位
        String year = String.valueOf(java.time.Year.now().getValue()).substring(2);
        // 3. 查找今年该科室已有的最大序号
        Integer maxSerial = employeeMapper.getMaxSerialNumber(prefix, year);
        int nextSerial = (maxSerial == null ? 1 : maxSerial + 1);
        // 4. 拼接
        return String.format("%s%s%04d", prefix, year, nextSerial);
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
}
