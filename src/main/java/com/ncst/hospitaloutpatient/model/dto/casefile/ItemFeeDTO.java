package com.ncst.hospitaloutpatient.model.dto.casefile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "医疗项目费用明细")
public class ItemFeeDTO {
    @Schema(description = "项目名称", example = "胸片")
    private String itemName;

    @Schema(description = "单价", example = "50.00")
    private BigDecimal price;

    @Schema(description = "数量", example = "1")
    private Integer unit;

    @Schema(description = "总价", example = "50.00")
    private BigDecimal amount;
}