package com.ncst.hospitaloutpatient.model.dto.casefile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "病案医疗项目检查结果DTO")
public class CaseApplyResultDTO {
    @Schema(description = "申请ID", example = "1")
    private Integer applyId;

    @Schema(description = "医疗项目ID", example = "3")
    private Integer itemId;

    @Schema(description = "申请类型(EXAM-检查/LAB-检验/DISPOSAL-处置)", example = "LAB")
    private String applyType;

    @Schema(description = "申请目的", example = "辅助诊断")
    private String applyPurpose;

    @Schema(description = "申请部位", example = "血液")
    private String applySite;

    @Schema(description = "申请时间", example = "2025-09-01T09:40:00")
    private LocalDateTime applyTime;

    @Schema(description = "执行人ID", example = "4")
    private Integer performerId;

    @Schema(description = "结果记录人ID", example = "1")
    private Integer resultRecorderId;

    @Schema(description = "执行时间", example = "2025-09-01T10:10:00")
    private LocalDateTime performTime;

    @Schema(description = "检查结果", example = "正常")
    private String result;

    @Schema(description = "申请状态", example = "UNFINISHED")
    private String status;

    @Schema(description = "单位", example = "1")
    private Integer unit;

    @Schema(description = "备注", example = "血常规")
    private String remark;
}