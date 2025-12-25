package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class CopyScheduleRequest {

    @Schema(description = "科室ID", example = "1")
    @NotNull(message = "departmentId不能为空")
    private Integer departmentId;

    @Schema(description = "源周周一", example = "2025-12-22")
    @NotNull(message = "sourceStartDate不能为空")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate sourceStartDate;

    @Schema(description = "目标周周一", example = "2025-12-29")
    @NotNull(message = "targetStartDate不能为空")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate targetStartDate;
}

