package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Schema(description = "排班查询请求")
public class ScheduleQueryDTO {

    @Schema(description = "科室ID", required = true)
    @NotNull(message = "科室ID不能为空")
    private Integer departmentId;

    @Schema(description = "开始日期 (yyyy-MM-dd)", required = true)
    @NotNull(message = "开始日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "结束日期 (yyyy-MM-dd)", required = true)
    @NotNull(message = "结束日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}