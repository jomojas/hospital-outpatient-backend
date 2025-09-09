package com.ncst.hospitaloutpatient.service.organization;

import com.ncst.hospitaloutpatient.common.enums.DepartmentType;
import com.ncst.hospitaloutpatient.mapper.organization.DepartmentMapper;
import com.ncst.hospitaloutpatient.model.dto.organization.DepartmentResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.DepartmentRoleResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.DepartmentTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
