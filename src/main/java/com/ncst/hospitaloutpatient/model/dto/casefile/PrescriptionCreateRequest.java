package com.ncst.hospitaloutpatient.model.dto.casefile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "批量处方开具请求体")
public class PrescriptionCreateRequest {
    @Schema(description = "挂号ID", example = "1")
    private Integer registrationId;

    @Schema(description = "药品处方列表")
    private List<PrescriptionItem> prescriptions;

    @Data
    @Schema(description = "单个药品处方")
    public static class PrescriptionItem {
        @Schema(description = "药品ID", example = "1")
        private Integer drugId;

        @Schema(description = "用法用量", example = "口服 1粒 每日3次")
        private String dosage;

        @Schema(description = "数量", example = "6")
        private Double quantity;

        @Schema(description = "备注", example = "按疗程服用")
        private String remark;
    }
}