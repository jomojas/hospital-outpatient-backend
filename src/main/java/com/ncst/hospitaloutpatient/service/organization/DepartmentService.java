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

    public List<DepartmentResponse> listDepartments(String type) {
        List<DepartmentResponse> list = departmentMapper.selectDepartmentsByType(type);
        // 设置typeName
        for (DepartmentResponse dep : list) {
            dep.setTypeName(DepartmentType.getChineseNameByCode(dep.getType()));
        }
        return list;
    }

    public List<DepartmentRoleResponse> listDepartmentRoles(Integer departmentId) {
        return departmentMapper.selectRolesByDepartmentId(departmentId);
    }

    @Transactional
    public void createDepartment(CreateDepartmentRequest request) {
        // 校验type是否合法
        boolean validType = false;
        for (DepartmentType type : DepartmentType.values()) {
            if (type.name().equalsIgnoreCase(request.getType())) {
                validType = true;
                break;
            }
        }
        if (!validType) {
            throw new BusinessException(400, "科室类型不合法: " + request.getType());
        }
        int row = departmentMapper.insertDepartment(request);
        if (row == 0) {
            throw new BusinessException(500, "新增科室失败");
        }
    }

    @Transactional
    public void updateDepartment(Integer departmentId, UpdateDepartmentRequest request) {
        // 校验type是否合法
        boolean validType = false;
        for (DepartmentType type : DepartmentType.values()) {
            if (type.name().equalsIgnoreCase(request.getType())) {
                validType = true;
                break;
            }
        }
        if (!validType) {
            throw new BusinessException(400, "科室类型不合法: " + request.getType());
        }
        int row = departmentMapper.updateDepartment(departmentId, request);
        if (row == 0) {
            throw new BusinessException(500, "科室信息更新失败");
        }
    }
}
