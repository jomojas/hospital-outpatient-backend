package com.ncst.hospitaloutpatient.controller.analytics;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.analytics.*;
import com.ncst.hospitaloutpatient.service.analytics.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/stats")
public class StatsController {
    @Autowired
    private StatsService statsService;

    @Operation(
            summary = "挂号趋势统计",
            description = "用于趋势图，横坐标为时间，纵坐标为挂号数量。period可选值：month, season, year, all, auto。auto时需提供startDate和endDate。"
    )
    @GetMapping("/registrations/trend")
    public ApiResponse<RegistrationsTrendResponse> registrationsTrend(
            @Parameter(description = "周期类型（month/season/year/all/auto）", required = true)
            @RequestParam String period,
            @Parameter(description = "起始日期（period=auto时必填，如2025-08-01）")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期（period=auto时必填，如2025-09-11）")
            @RequestParam(required = false) String endDate
    ) {
        RegistrationsTrendResponse resp = statsService.registrationsTrend(period, startDate, endDate);
        return ApiResponse.ok(resp);
    }

    @Operation(
            summary = "获取挂号类型分布",
            description = "根据周期period或指定时间区间统计各挂号类型数量分布，适用于饼图"
    )
    @GetMapping("/registrations/type-breakdown")
    public ApiResponse<List<RegistrationsTypeBreakdownDTO>> registrationsTypeBreakdown(
            @Parameter(description = "统计周期（month/season/year/all/auto）", example = "month")
            @RequestParam String period,
            @Parameter(description = "起始日期，格式yyyy-MM-dd，仅当period=auto时生效", example = "2025-09-01")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期，格式yyyy-MM-dd，仅当period=auto时生效", example = "2025-09-10")
            @RequestParam(required = false) String endDate) {
        List<RegistrationsTypeBreakdownDTO> data = statsService.registrationsTypeBreakdown(period, startDate, endDate);
        return ApiResponse.ok(data);
    }

    @Operation(
            summary = "获取科室挂号分布",
            description = "根据周期period或指定时间区间统计各科室挂号数量分布，适用于饼图"
    )
    @GetMapping("/registrations/by-department")
    public ApiResponse<List<RegistrationsByDepartmentDTO>> registrationsByDepartment(
            @Parameter(description = "统计周期（month/season/year/all/auto）", example = "month")
            @RequestParam String period,
            @Parameter(description = "起始日期，格式yyyy-MM-dd，仅当period=auto时生效", example = "2025-09-01")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期，格式yyyy-MM-dd，仅当period=auto时生效", example = "2025-09-10")
            @RequestParam(required = false) String endDate) {
        List<RegistrationsByDepartmentDTO> data = statsService.registrationsByDepartment(period, startDate, endDate);
        return ApiResponse.ok(data);
    }

    @Operation(
            summary = "获取医生挂号分布",
            description = "根据周期period或指定时间区间统计各医生挂号数量分布，适用于饼图"
    )
    @GetMapping("/registrations/by-doctor")
    public ApiResponse<List<RegistrationsByDoctorDTO>> registrationsByDoctor(
            @Parameter(description = "统计周期（month/season/year/all/auto）", example = "month")
            @RequestParam String period,
            @Parameter(description = "起始日期，格式yyyy-MM-dd，仅当period=auto时生效", example = "2025-09-01")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期，格式yyyy-MM-dd，仅当period=auto时生效", example = "2025-09-10")
            @RequestParam(required = false) String endDate) {
        List<RegistrationsByDoctorDTO> data = statsService.registrationsByDoctor(period, startDate, endDate);
        return ApiResponse.ok(data);
    }


    @Operation(
            summary = "获取收入趋势",
            description = "根据周期period或指定时间区间统计医院总收入变化趋势（挂号费+项目费+药品费），适用于折线图"
    )
    @GetMapping("/revenue/trend")
    public ApiResponse<RevenueTrendResponse> revenueTrend(
            @Parameter(description = "统计周期（month/season/year/all/auto）", example = "month")
            @RequestParam String period,
            @Parameter(description = "起始日期，格式yyyy-MM-dd，仅当period=auto时生效", example = "2025-09-01")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期，格式yyyy-MM-dd，仅当period=auto时生效", example = "2025-09-10")
            @RequestParam(required = false) String endDate) {
        RevenueTrendResponse data = statsService.revenueTrend(period, startDate, endDate);
        return ApiResponse.ok(data);
    }

    @Operation(
            summary = "按类型统计收入",
            description = "统计一段时间内各业务类型（挂号费、医疗项目、药品等）的收入对比，已扣除退款"
    )
    @GetMapping("/revenue/by-type")
    public ApiResponse<RevenueByTypeResponse> revenueByType(
            @Parameter(description = "统计周期（month/season/year/all/auto）", example = "month")
            @RequestParam String period,
            @Parameter(description = "起始日期，yyyy-MM-dd，仅period=auto时生效", example = "2025-09-01")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期，yyyy-MM-dd，仅period=auto时生效", example = "2025-09-11")
            @RequestParam(required = false) String endDate
    ) {
        RevenueByTypeResponse data = statsService.revenueByType(period, startDate, endDate);
        return ApiResponse.ok(data);
    }

    @Operation(
            summary = "按科室统计收入",
            description = "统计一段时间内各科室的收入对比，已扣除退款"
    )
    @GetMapping("/revenue/by-department")
    public ApiResponse<RevenueByDepartmentResponse> revenueByDepartment(
            @Parameter(description = "统计周期（month/season/year/all/auto）", example = "month")
            @RequestParam String period,
            @Parameter(description = "起始日期，yyyy-MM-dd，仅period=auto时生效", example = "2025-09-01")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期，yyyy-MM-dd，仅period=auto时生效", example = "2025-09-11")
            @RequestParam(required = false) String endDate
    ) {
        RevenueByDepartmentResponse data = statsService.revenueByDepartment(period, startDate, endDate);
        return ApiResponse.ok(data);
    }

    @Operation(
            summary = "退费趋势统计",
            description = "按天统计一段时间内的退费总额趋势"
    )
    @GetMapping("/refund/trend")
    public ApiResponse<RefundTrendResponse> refundTrend(
            @Parameter(description = "统计周期（month/season/year/all/auto）", example = "auto")
            @RequestParam String period,
            @Parameter(description = "起始日期，yyyy-MM-dd，仅period=auto时生效", example = "2025-09-01")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期，yyyy-MM-dd，仅period=auto时生效", example = "2025-09-11")
            @RequestParam(required = false) String endDate
    ) {
        RefundTrendResponse data = statsService.refundTrend(period, startDate, endDate);
        return ApiResponse.ok(data);
    }
}
