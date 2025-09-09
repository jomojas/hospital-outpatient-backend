package com.ncst.hospitaloutpatient.model.dto.reference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NumberTypeResponse {
    @Schema(description = "号别类型（GENERAL/SPECIALIST）", example = "GENERAL")
    private String numberType;

    @Schema(description = "号别名称", example = "普通号")
    private String displayName;

    @Schema(description = "挂号费", example = "10.00")
    private BigDecimal fee;
}