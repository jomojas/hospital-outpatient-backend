package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "挂号级别及价格信息")
public class RegistrationLevelResponse {

    @Schema(description = "号别代码 (主键)", example = "GENERAL")
    private String code; // 对应 number_type

    @Schema(description = "号别名称", example = "普通号")
    private String name; // 对应 display_name

    @Schema(description = "挂号费", example = "15.00")
    private BigDecimal price; // 对应 fee

    @Schema(description = "状态：1启用，0停用", example = "1")
    private Integer status; // 对应 status
}