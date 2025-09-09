package com.ncst.hospitaloutpatient.controller.organization;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.SetDoctorQuotaRequest;
import com.ncst.hospitaloutpatient.model.dto.organization.SetRegistrationPriceRequest;
import com.ncst.hospitaloutpatient.service.organization.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Operation(summary = "设置医生每日号额", description = "设置指定医生在指定日期的号额")
    @PutMapping("/{doctorId}/quota")
    public ApiResponse<?> setDoctorQuota(
            @Parameter(description = "医生ID", example = "1")
            @PathVariable Integer doctorId,
            @RequestBody SetDoctorQuotaRequest request
    ) {
        adminService.setDoctorQuota(doctorId, request.getQuotaDate(), request.getQuota());
        return ApiResponse.ok();
    }

    @Operation(summary = "设置挂号类型价格", description = "批量设置各类挂号类型的价格")
    @PutMapping("/registration/prices")
    public ApiResponse<?> updateRegistrationPrices(
            @RequestBody SetRegistrationPriceRequest request
    ) {
        adminService.updateRegistrationPrices(request.getPrices());
        return ApiResponse.ok();
    }
}
