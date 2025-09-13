package com.ncst.hospitaloutpatient.controller.outpatient;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.outpatient.CreateRegistrationRequest;
import com.ncst.hospitaloutpatient.model.dto.outpatient.CreateRegistrationResponse;
import com.ncst.hospitaloutpatient.model.dto.outpatient.RegistrationDetailResponse;
import com.ncst.hospitaloutpatient.service.outpatient.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/registrations")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @Operation(summary = "创建挂号记录", description = "创建一条新的挂号记录")
    @PostMapping
    public ApiResponse<CreateRegistrationResponse> createRegistration(@RequestBody CreateRegistrationRequest request) {
        CreateRegistrationResponse response = registrationService.createRegistration(request);
        return ApiResponse.ok(response);
    }

    @Operation(
        summary = "获取挂号详情",
        description = "根据挂号ID获取挂号的详细信息"
    )
    @GetMapping("/{id}")
    public ApiResponse<RegistrationDetailResponse> getRegistration(
        @Parameter(description = "挂号ID", required = true, example = "1")
        @PathVariable Integer id
    ) {
        RegistrationDetailResponse response = registrationService.getRegistration(id);
        return ApiResponse.ok(response);
    }

    @Operation(summary = "分页获取挂号记录", description = "分页、按条件获取挂号记录列表")
    @GetMapping
    public ApiResponse<List<RegistrationDetailResponse>> listRegistrations(
            @Parameter(description = "页码(从1开始)", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "挂号日期(yyyy-MM-dd)", example = "2025-09-09")
            @RequestParam(required = false) LocalDate date,
            @Parameter(description = "科室ID", example = "2")
            @RequestParam(required = false) Integer deptId,
            @Parameter(description = "医生ID", example = "5")
            @RequestParam(required = false) Integer doctorId,
            @Parameter(description = "挂号状态", example = "WAITING_FOR_CONSULTATION")
            @RequestParam(required = false) String status,
            @Parameter(description = "关键词(科室名/患者姓名/医生姓名)", example = "张三")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "排序字段，可选：date、patientName、doctorName", example = "date")
            @RequestParam(defaultValue = "date") String sortBy,
            @Parameter(description = "排序顺序，可选：asc、desc", example = "desc")
            @RequestParam(defaultValue = "desc") String order
    ) {
        int offset = (page - 1) * pageSize;
        List<RegistrationDetailResponse> data = registrationService.listRegistrations(
                offset, pageSize, date, deptId, doctorId, status, keyword, sortBy, order
        );
        long total = registrationService.countRegistrations(
                date, deptId, doctorId, status, keyword
        );
        return ApiResponse.pageOk(data, page, pageSize, total);
    }

    @Operation(summary = "取消挂号", description = "根据挂号ID取消挂号，自动退费")
    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancelRegistration(
            @Parameter(description = "挂号ID", required = true, example = "1")
            @PathVariable("id") Integer id
    ) {
        registrationService.cancelRegistration(id);
        return ApiResponse.ok();
    }

    @Operation(summary = "生成新的病历号", description = "根据现有最大病历号生成新的病历号")
    @GetMapping("/generate-patient-no")
    public ApiResponse<Map<String, String>> generatePatientNo() {
        String newPatientNo = registrationService.generatePatientNo();
        Map<String, String> data = Map.of("patientNo", newPatientNo);
        return ApiResponse.ok(data);
    }
}
