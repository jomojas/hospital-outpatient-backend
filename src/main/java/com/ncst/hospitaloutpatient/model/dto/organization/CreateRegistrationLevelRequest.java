package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "新增挂号级别请求")
public class CreateRegistrationLevelRequest {

    @Schema(description = "号别代码 (唯一标识，如 VIP)", example = "VIP")
    @NotBlank(message = "号别代码不能为空")
    private String code;

    @Schema(description = "显示名称", example = "特需专家号")
    @NotBlank(message = "显示名称不能为空")
    private String name;

    @Schema(description = "挂号费", example = "100.00")
    @NotNull(message = "挂号费不能为空")
    @DecimalMin(value = "0.00", message = "费用不能为负数")
    private BigDecimal price;
}