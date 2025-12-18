package com.ncst.hospitaloutpatient.model.dto.medicalitem;

import lombok.Data;
//import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 通用医疗项目操作记录DTO（适用于检查/检验/处置）
 */
@Data
public class MedicalItemOperateLogDTO {

    @Schema(description = "日志主键ID")
    private Integer logId;

    @Schema(description = "申请表ID")
    private Integer applyId;

    @Schema(description = "操作员工ID")
    private Integer operatorId;

    @Schema(description = "操作员工姓名")
    private String operatorName;

    @Schema(description = "操作时间")
    private String operateTime;

    @Schema(description = "操作类型(EXECUTE/INPUT_RESULT)")
    private String operateType;

    @Schema(description = "医疗项目ID")
    private Integer itemId;

    @Schema(description = "医疗项目名称")
    private String itemName;

    @Schema(description = "医疗项目类型(EXAM/LAB/DISPOSAL)")
    private String itemType;

    @Schema(description = "患者编号")
    private String patientNo;

    @Schema(description = "患者姓名")
    private String patientName;

    @Schema(description = "操作备注")
    private String remark;
}