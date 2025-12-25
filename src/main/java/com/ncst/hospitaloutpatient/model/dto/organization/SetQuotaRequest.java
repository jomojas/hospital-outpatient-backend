package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Schema(description = "设置号源请求")
public class SetQuotaRequest {

    @Schema(description = "医生ID", required = true)
    @NotNull
    private Integer staffId;

    @Schema(description = "排班日期", required = true)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(description = "号源数量 (设为0表示停诊/取消排班)", required = true)
    @NotNull
    @Min(0)
    private Integer quota;
}