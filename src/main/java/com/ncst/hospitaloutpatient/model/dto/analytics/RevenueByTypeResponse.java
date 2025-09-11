package com.ncst.hospitaloutpatient.model.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "按业务类型统计的收入响应")
public class RevenueByTypeResponse {

    @Schema(description = "各业务类型收入明细")
    private List<RevenueTypeItem> items;

    @Data
    @Schema(description = "单项业务类型收入")
    public static class RevenueTypeItem {
        @Schema(description = "业务类型（如REGISTRATION/MEDICAL_ITEM/DRUG）")
        private String type;
        @Schema(description = "该类型的收入总额")
        private Double amount;
    }
}