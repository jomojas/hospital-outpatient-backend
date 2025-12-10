package com.ncst.hospitaloutpatient.model.dto.casefile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "医疗项目费用明细")
public class ItemFeeDTO {
    @Schema(description = "项目ID", example = "101")
    private Long itemId;

    @Schema(description = "项目名称", example = "胸片")
    private String itemName;

    @Schema(description = "单价（字符串）", example = "50.00")
    private String price;

    @Schema(description = "数量", example = "1")
    private Integer unit;

    @Schema(description = "总价（字符串）", example = "50.00")
    private String amount;

    @Schema(description = "费用状态", example = "UNPAID")
    private String status;

    @Schema(description = "开立时间", example = "2025-12-09 10:30:00")
    private String createTime;
}