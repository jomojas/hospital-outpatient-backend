package com.ncst.hospitaloutpatient.service.organization;

import com.ncst.hospitaloutpatient.common.enums.DepartmentType;
import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.organization.DepartmentMapper;
import com.ncst.hospitaloutpatient.model.dto.organization.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    public List<DepartmentTypeResponse> listDepartmentTypes() {
        List<String> typeCodes = departmentMapper.selectDistinctTypes();
        return typeCodes.stream().map(code -> {
            DepartmentTypeResponse resp = new DepartmentTypeResponse();
            resp.setType(code);
            resp.setTypeName(DepartmentType.getChineseNameByCode(code));
            return resp;
        }).collect(Collectors.toList());
    }

    public List<DepartmentResponse> listDepartments(String type, Integer status) {
        List<DepartmentResponse> list = departmentMapper.selectDepartmentsByType(type, status);
        // 设置typeName
        for (DepartmentResponse dep : list) {
            dep.setTypeName(DepartmentType.getChineseNameByCode(dep.getType()));
        }
        return list;
    }

    public List<DepartmentRoleResponse> listDepartmentRoles(Integer departmentId) {
        return departmentMapper.selectRolesByDepartmentId(departmentId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createDepartment(CreateDepartmentRequest request) {
        boolean validType = false;
        for (DepartmentType t : DepartmentType.values()) {
            if (t.name().equalsIgnoreCase(request.getType())) {
                validType = true;
                break;
            }
        }
        if (!validType) {
            throw new BusinessException(400, "Invalid department type: " + request.getType());
        }

        int row = departmentMapper.insertDepartment(request);
        if (row == 0) {
            throw new BusinessException(500, "Create department failed");
        }

        Integer departmentId = departmentMapper.selectLastInsertId();
        List<Integer> roleIds = request.getRoleIds();
        if (departmentId != null && roleIds != null && !roleIds.isEmpty()) {
            int relRows = departmentMapper.insertDepartmentRoles(departmentId, roleIds);
            if (relRows == 0) {
                throw new BusinessException(500, "Bind department roles failed");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDepartment(Integer departmentId, UpdateDepartmentRequest request) {
        // Validate type only when provided
        if (request.getType() != null) {
            boolean validType = false;
            for (DepartmentType t : DepartmentType.values()) {
                if (t.name().equalsIgnoreCase(request.getType())) {
                    validType = true;
                    break;
                }
            }
            if (!validType) {
                throw new BusinessException(400, "科室类型不合法: " + request.getType());
            }
        }

        int row = departmentMapper.updateDepartment(departmentId, request);
        if (row == 0) {
            throw new BusinessException(500, "科室信息更新失败");
        }

        // If roleIds is provided, reset bindings: delete then batch insert
        if (request.getRoleIds() != null) {
            departmentMapper.deleteDepartmentRolesByDepartmentId(departmentId);
            List<Integer> roleIds = request.getRoleIds();
            if (!roleIds.isEmpty()) {
                int relRows = departmentMapper.insertDepartmentRoles(departmentId, roleIds);
                if (relRows == 0) {
                    throw new BusinessException(500, "科室角色关联更新失败");
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartment(Integer departmentId) {
        // 1. 核心校验：检查该科室下是否还有有效员工
        // 假设员工表有 department_id 字段，且 status=1 表示在职
        int employeeCount = departmentMapper.countActiveEmployeesByDeptId(departmentId);

        if (employeeCount > 0) {
            throw new BusinessException(400, "该科室下仍有在职员工，无法停用！请先调整人员归属。");
        }

        // 2. (可选) 检查是否有未完成的挂号单
         int activeRegistrations = departmentMapper.countByDeptAndStatus(departmentId);
         if (activeRegistrations > 0) throw new BusinessException(400, "该科室下有未完成的挂号单，无法删除！");

        // 3. 执行逻辑删除 (更新状态为 0 或 DISABLED)
        int res = departmentMapper.updateStatus(departmentId, 0);
        if (res == 0) {
            throw new BusinessException(500, "科室删除失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void restoreDepartment(Integer departmentId) {
        // 恢复逻辑：将 status 从 0 改为 1（启用）
        // 这里不做“有员工/挂号”的限制，因为恢复不会破坏约束；如果需要可再补充校验
        int res = departmentMapper.updateStatus(departmentId, 1);
        if (res == 0) {
            throw new BusinessException(404, "科室不存在或恢复失败");
        }
    }
}
