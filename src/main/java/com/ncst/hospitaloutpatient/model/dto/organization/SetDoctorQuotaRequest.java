package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

@Data
public class SetDoctorQuotaRequest {
    @Schema(description = "日期（格式: yyyy-MM-dd）", example = "2025-09-10")
    private LocalDate quotaDate;

    @Schema(description = "号额", example = "30")
    private Integer quota;
}