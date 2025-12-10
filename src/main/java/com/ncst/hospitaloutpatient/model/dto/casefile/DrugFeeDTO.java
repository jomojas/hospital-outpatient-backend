package com.ncst.hospitaloutpatient.model.dto.casefile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "药品费用明细")
public class DrugFeeDTO {
    @Schema(description = "药品ID", example = "2001")
    private Long drugId;

    @Schema(description = "药品名称", example = "阿莫西林")
    private String drugName;

    @Schema(description = "药品规格", example = "0.5g*20片/盒")
    private String specification;

    @Schema(description = "单价（字符串）", example = "12.80")
    private String price;

    @Schema(description = "数量", example = "2")
    private Integer quantity;

    @Schema(description = "总价（字符串）", example = "25.60")
    private String amount;

    @Schema(description = "费用状态", example = "PAID")
    private String status;

    @Schema(description = "开立时间", example = "2025-12-09 10:30:00")
    private String createTime;
}