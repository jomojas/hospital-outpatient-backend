package com.ncst.hospitaloutpatient.controller.hr;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.hr.CreateEmployeeRequest;
import com.ncst.hospitaloutpatient.model.dto.hr.StaffDetailResponse;
import com.ncst.hospitaloutpatient.service.hr.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ApiResponse<List<StaffDetailResponse>> listEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer roleId,
            @RequestParam(defaultValue = "createTime") String sortBy,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        List<StaffDetailResponse> employees = employeeService.listEmployees(
                keyword, departmentId, roleId, sortBy, order, page, pageSize
        );
        long total = employeeService.countEmployees(keyword, departmentId, roleId);
        return ApiResponse.pageOk(employees, page, pageSize, total);
    }


    @PostMapping
    public ApiResponse<String> createEmployee(@RequestBody CreateEmployeeRequest request) {
        employeeService.createEmployee(request);
        return ApiResponse.ok();
    }

    @GetMapping("/accountName")
    public ApiResponse<String> getAccountName(@RequestParam Integer departmentId) {
        String accountName = employeeService.getAccountName(departmentId);
        return ApiResponse.ok(accountName);
    }
}
