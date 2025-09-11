package com.ncst.hospitaloutpatient.model.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "挂号类型分布数据")
public class RegistrationsTypeBreakdownDTO {

    @Schema(description = "挂号类型，如GENERAL、SPECIALIST", example = "GENERAL")
    private String name;

    @Schema(description = "该类型挂号数量", example = "123")
    private Integer value;
}