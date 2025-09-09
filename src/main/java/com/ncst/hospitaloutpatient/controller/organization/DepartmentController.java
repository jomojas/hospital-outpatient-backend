package com.ncst.hospitaloutpatient.controller.organization;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.DepartmentResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.DepartmentRoleResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.DepartmentTypeResponse;
import com.ncst.hospitaloutpatient.service.organization.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Operation(summary = "获取所有科室类型", description = "去重返回所有科室类型（英文和中文）")
    @GetMapping("/types")
    public ApiResponse<List<DepartmentTypeResponse>> listDepartmentTypes() {
        List<DepartmentTypeResponse> types = departmentService.listDepartmentTypes();
        return ApiResponse.ok(types);
    }

    @Operation(summary = "获取指定类型的科室", description = "根据科室类型获取所有科室")
    @GetMapping
    public ApiResponse<List<DepartmentResponse>> listDepartments(
            @Parameter(description = "科室类型", example = "OUTPATIENT")
            @RequestParam(required = false) String type
    ) {
        List<DepartmentResponse> departments = departmentService.listDepartments(type);
        return ApiResponse.ok(departments);
    }

    @Operation(summary = "获取指定科室的角色", description = "根据科室ID获取该科室下所有角色")
    @GetMapping("/{departmentId}/roles")
    public ApiResponse<List<DepartmentRoleResponse>> listDepartmentRoles(
            @Parameter(description = "科室ID", example = "1")
            @PathVariable Integer departmentId
    ) {
        List<DepartmentRoleResponse> roles = departmentService.listDepartmentRoles(departmentId);
        return ApiResponse.ok(roles);
    }
}
