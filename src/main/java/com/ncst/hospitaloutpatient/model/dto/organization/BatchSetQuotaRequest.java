package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class BatchSetQuotaRequest {

    @Schema(description = "医生/员工ID", example = "1001")
    @NotNull(message = "staffId不能为空")
    private Integer staffId;

    @Schema(description = "开始日期", example = "2025-12-22")
    @NotNull(message = "startDate不能为空")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Schema(description = "结束日期", example = "2025-12-28")
    @NotNull(message = "endDate不能为空")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @Schema(description = "号额", example = "20")
    @NotNull(message = "quota不能为空")
    private Integer quota;

    @Schema(description = "星期过滤（1=周一..7=周日），为空表示每天", example = "[1,3,5]")
    @Size(max = 7, message = "weekDays最多7个")
    private List<Integer> weekDays;
}

