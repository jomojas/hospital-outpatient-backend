package com.ncst.hospitaloutpatient.model.dto.casefile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "处方药品费用明细")
public class DrugFeeDTO {
    @Schema(description = "药品名称", example = "阿莫西林胶囊")
    private String drugName;

    @Schema(description = "单价", example = "22.50")
    private BigDecimal price;

    @Schema(description = "数量", example = "6")
    private Double quantity;

    @Schema(description = "总价", example = "135.00")
    private BigDecimal amount;
}