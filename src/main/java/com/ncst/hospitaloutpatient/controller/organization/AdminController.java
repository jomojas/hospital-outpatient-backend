package com.ncst.hospitaloutpatient.controller.organization;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.CreateRegistrationLevelRequest;
import com.ncst.hospitaloutpatient.model.dto.organization.RegistrationLevelResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.SetRegistrationPriceRequest;
import com.ncst.hospitaloutpatient.service.organization.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(summary = "获取挂号级别列表", description = "返回所有号别及其当前价格")
    @GetMapping("/registration/levels")
    public ApiResponse<List<RegistrationLevelResponse>> listRegistrationLevels() {
        List<RegistrationLevelResponse> list = adminService.listRegistrationLevels();
        return ApiResponse.ok(list);
    }

    @Operation(summary = "设置挂号类型价格", description = "批量设置各类挂号类型的价格")
    @PutMapping("/registration/prices")
    public ApiResponse<?> updateRegistrationPrices(
            @RequestBody SetRegistrationPriceRequest request
    ) {
        adminService.updateRegistrationPrices(request.getPrices());
        return ApiResponse.ok();
    }

    @Operation(summary = "新增挂号级别", description = "创建一个新的号别，默认启用")
    @PostMapping("/registration/levels")
    public ApiResponse<Void> createRegistrationLevel(
            @Valid @RequestBody CreateRegistrationLevelRequest request
    ) {
        adminService.createRegistrationLevel(request);
        return ApiResponse.ok();
    }

    @Operation(summary = "更新挂号级别状态", description = "启用或停用指定号别 (1:启用, 0:停用)")
    @PatchMapping("/registration/levels/{code}/status")
    public ApiResponse<Void> updateRegistrationLevelStatus(
            @Parameter(description = "号别代码", example = "GENERAL")
            @PathVariable String code,
            @Parameter(description = "状态 (1或0)", example = "0")
            @RequestParam Integer status
    ) {
        adminService.updateRegistrationLevelStatus(code, status);
        return ApiResponse.ok();
    }
}