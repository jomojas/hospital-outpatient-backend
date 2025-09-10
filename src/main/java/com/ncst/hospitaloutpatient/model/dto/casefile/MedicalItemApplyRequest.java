package com.ncst.hospitaloutpatient.model.dto.casefile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "病案医疗项目批量申请请求体")
public class MedicalItemApplyRequest {

    @Schema(description = "挂号ID", example = "1")
    private Integer registrationId;

    @Schema(description = "申请项目列表")
    private List<ApplyItem> items;

    @Data
    @Schema(description = "单个医疗项目申请")
    public static class ApplyItem {

        @Schema(description = "医疗项目ID", example = "3")
        private Integer itemId;

        @Schema(description = "申请类型(EXAM-检查/LAB-检验/DISPOSAL-处置)", example = "LAB")
        private String applyType;

        @Schema(description = "申请目的", example = "辅助诊断")
        private String applyPurpose;

        @Schema(description = "申请部位", example = "血液")
        private String applySite;

        @Schema(description = "项目单位数量", example = "1")
        private Integer unit;

        @Schema(description = "备注", example = "需空腹")
        private String remark;
    }
}