package com.ncst.hospitaloutpatient.controller.hr;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.hr.CreateEmployeeRequest;
import com.ncst.hospitaloutpatient.model.dto.hr.StaffDetailResponse;
import com.ncst.hospitaloutpatient.model.dto.hr.UpdateEmployeeDTO;
import com.ncst.hospitaloutpatient.service.hr.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Operation(
        summary = "员工列表查询",
        description = "分页获取员工列表，可按关键字、部门、角色等条件筛选"
    )
    @GetMapping
    public ApiResponse<List<StaffDetailResponse>> listEmployees(
            @Parameter(description = "搜索关键字", required = false)
            @RequestParam(required = false) String keyword,
            @Parameter(description = "部门ID", required = false)
            @RequestParam(required = false) Integer departmentId,
            @Parameter(description = "角色ID", required = false)
            @RequestParam(required = false) Integer roleId,
            @Parameter(description = "排序字段", required = false, example = "createTime")
            @RequestParam(defaultValue = "createTime") String sortBy,
            @Parameter(description = "排序方式（asc或desc）", required = false, example = "desc")
            @RequestParam(defaultValue = "desc") String order,
            @Parameter(description = "页码", required = false, example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", required = false, example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        List<StaffDetailResponse> employees = employeeService.listEmployees(
                keyword, departmentId, roleId, sortBy, order, page, pageSize
        );
        long total = employeeService.countEmployees(keyword, departmentId, roleId);
        return ApiResponse.pageOk(employees, page, pageSize, total);
    }

    @Operation(
        summary = "创建新员工",
        description = "添加新员工信息，包括基本资料和账户信息"
    )
    @PostMapping
    public ApiResponse<String> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        employeeService.createEmployee(request);
        return ApiResponse.ok();
    }

    @Operation(
        summary = "获取账号名",
        description = "根据部门ID自动生成账号名"
    )
    @GetMapping("/accountName")
    public ApiResponse<String> getAccountName(
        @Parameter(description = "部门ID", required = true, example = "1")
        @RequestParam Integer departmentId
    ) {
        String accountName = employeeService.getAccountName(departmentId);
        return ApiResponse.ok(accountName);
    }

    @Operation(
        summary = "更新员工信息",
        description = "根据员工ID更新员工的基本信息和岗位信息"
    )
    @PutMapping("/{id}")
    public ApiResponse<Void> updateEmployee(
        @Parameter(description = "员工ID", required = true, example = "1")
        @PathVariable Integer id,
        @Valid @RequestBody UpdateEmployeeDTO dto
    ) {
        employeeService.updateEmployee(id, dto);
        return ApiResponse.ok();
    }

    @Operation(
            summary = "删除员工",
            description = "根据员工ID逻辑删除员工（将staff_account.status设为1）"
    )
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEmployee(
            @Parameter(description = "员工ID", required = true, example = "1")
            @PathVariable Integer id
    ) {
        employeeService.deleteEmployee(id);
        return ApiResponse.ok();
    }

    @Operation(
            summary = "恢复员工",
            description = "根据员工ID恢复员工（将staff_account.status设为0）"
    )
    @PutMapping("/{id}/restore")
    public ApiResponse<Void> restoreEmployee(
            @Parameter(description = "员工ID", required = true, example = "1")
            @PathVariable Integer id
    ) {
        employeeService.restoreEmployee(id);
        return ApiResponse.ok();
    }

    @Operation(
            summary = "获取员工详情",
            description = "根据员工ID获取员工的详细信息"
    )
    @GetMapping("/{id}")
    public ApiResponse<StaffDetailResponse> getEmployee(
            @Parameter(description = "员工ID", required = true, example = "1")
            @PathVariable Integer id
    ) {
        StaffDetailResponse response = employeeService.getEmployee(id);
        return ApiResponse.ok(response);
    }
}
