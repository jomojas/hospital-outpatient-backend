package com.ncst.hospitaloutpatient.model.dto.medicalitem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "检查申请简要DTO")
public class ExamApplyDTO {
    @Schema(description = "申请ID", example = "1")
    private Integer applyId;

    @Schema(description = "病案ID", example = "1")
    private Integer recordId;

    @Schema(description = "挂号ID", example = "1")
    private Integer registrationId;

    @Schema(description = "患者编号", example = "PNO001")
    private String patientNo;

    @Schema(description = "患者姓名", example = "张三")
    private String patientName;

    @Schema(description = "性别", example = "男")
    private String gender;

    @Schema(description = "医疗项目ID", example = "3")
    private Integer itemId;

    @Schema(description = "医疗项目名称", example = "血常规")
    private String itemName;

    @Schema(description = "申请目的", example = "辅助诊断")
    private String applyPurpose;

    @Schema(description = "申请部位", example = "胸部")
    private String applySite;

    @Schema(description = "申请时间", example = "2025-09-01T09:40:00")
    private LocalDateTime applyTime;

    @Schema(description = "申请状态", example = "UNFINISHED")
    private String status;

    @Schema(description = "数量", example = "1")
    private Integer unit;

    @Schema(description = "备注", example = "常规检查")
    private String remark;
}