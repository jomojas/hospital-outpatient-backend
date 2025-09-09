package com.ncst.hospitaloutpatient.model.dto.reference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SettlementCategoryResponse {
    @Schema(description = "结算类型ID", example = "1")
    private Integer settlementTypeId;

    @Schema(description = "结算方式名称", example = "自费")
    private String name;

    @Schema(description = "结算方式描述", example = "自费患者")
    private String description;
}