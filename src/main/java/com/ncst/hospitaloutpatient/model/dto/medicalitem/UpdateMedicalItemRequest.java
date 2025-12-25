package com.ncst.hospitaloutpatient.model.dto.medicalitem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "更新医疗项目请求")
public class UpdateMedicalItemRequest {

    @Schema(description = "项目编码", example = "EX001")
    @NotBlank(message = "项目编码不能为空")
    private String itemCode;

    @Schema(description = "项目名称", example = "胸部CT平扫")
    @NotBlank(message = "项目名称不能为空")
    private String itemName;

    @Schema(description = "项目类型 (通常不建议修改，但视业务而定)", example = "EXAM")
    @NotBlank(message = "项目类型不能为空")
    private String itemType;

    @Schema(description = "单价", example = "150.00")
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.00", message = "价格不能为负数")
    private BigDecimal price;

    @Schema(description = "执行科室ID", example = "4")
    @NotNull(message = "执行科室不能为空")
    private Integer departmentId;

    @Schema(description = "项目描述", example = "包含平扫及三维重建")
    private String description;
}