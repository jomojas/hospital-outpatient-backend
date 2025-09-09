package com.ncst.hospitaloutpatient.model.dto.reference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicalItemResponse {
    @Schema(description = "项目ID", example = "1")
    private Integer itemId;

    @Schema(description = "项目编码", example = "EX001")
    private String itemCode;

    @Schema(description = "项目名称", example = "胸片")
    private String itemName;

    @Schema(description = "项目类型", example = "EXAM")
    private String itemType;

    @Schema(description = "类型中文", example = "检查")
    private String itemTypeLabel;

    @Schema(description = "价格", example = "50.00")
    private BigDecimal price;

    @Schema(description = "描述", example = "胸部X光")
    private String description;
}