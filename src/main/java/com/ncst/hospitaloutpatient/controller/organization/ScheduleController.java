package com.ncst.hospitaloutpatient.controller.organization;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.BatchSetQuotaRequest;
import com.ncst.hospitaloutpatient.model.dto.organization.CopyScheduleRequest;
import com.ncst.hospitaloutpatient.model.dto.organization.DoctorScheduleResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.ScheduleQueryDTO;
import com.ncst.hospitaloutpatient.model.dto.organization.SetQuotaRequest;
import com.ncst.hospitaloutpatient.service.organization.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Operation(summary = "查询排班表", description = "获取指定科室、时间段内的医生排班矩阵")
    @GetMapping
    public ApiResponse<List<DoctorScheduleResponse>> listSchedules(
            @Valid ScheduleQueryDTO query
    ) {
        List<DoctorScheduleResponse> list = scheduleService.listSchedules(query);
        return ApiResponse.ok(list);
    }

    @Operation(summary = "设置单日号源", description = "设置某位医生某天的号额")
    @PutMapping("/quota")
    public ApiResponse<Void> setQuota(
            @Valid @RequestBody SetQuotaRequest request
    ) {
        scheduleService.setQuota(request);
        return ApiResponse.ok();
    }

    @Operation(summary = "批量设置号源", description = "按日期区间批量设置医生号额，可按星期过滤")
    @PostMapping("/quota/batch")
    public ApiResponse<Void> batchSetQuota(@Valid @RequestBody BatchSetQuotaRequest request) {
        scheduleService.batchSetQuota(request);
        return ApiResponse.ok();
    }

    @Operation(summary = "复制排班", description = "将源周的排班复制到目标周，复用 setQuota 逻辑")
    @PostMapping("/copy")
    public ApiResponse<Void> copySchedule(@Valid @RequestBody CopyScheduleRequest request) {
        scheduleService.copySchedule(request);
        return ApiResponse.ok();
    }
}