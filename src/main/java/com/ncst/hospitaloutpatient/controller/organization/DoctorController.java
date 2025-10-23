package com.ncst.hospitaloutpatient.controller.organization;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.DoctorResponse;
import com.ncst.hospitaloutpatient.service.organization.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @Operation(summary = "根据科室ID获取医生列表", description = "根据科室ID获取所有医生信息")
    @GetMapping
    public ApiResponse<List<DoctorResponse>> listDoctorsByDepartment(
            @Parameter(description = "科室ID", example = "1")
            @RequestParam(required = false) Integer departmentId
    ) {
        List<DoctorResponse> doctors = doctorService.listDoctorsByDepartment(departmentId);
        return ApiResponse.ok(doctors);
    }

    @Operation(summary = "获取医生列表", description = "获取所有医生信息")
    @GetMapping("/all")
    public ApiResponse<List<DoctorResponse>> listDoctors() {
        List<DoctorResponse> doctors = doctorService.listDoctors();
        return ApiResponse.ok(doctors);
    }
}
