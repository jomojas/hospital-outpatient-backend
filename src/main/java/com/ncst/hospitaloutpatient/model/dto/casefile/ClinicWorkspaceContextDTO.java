package com.ncst.hospitaloutpatient.model.dto.casefile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "诊疗工作台上下文数据")
public class ClinicWorkspaceContextDTO {

    @Schema(description = "挂号单ID", example = "12345")
    private Integer registrationId;

    @Schema(description = "病历号", example = "100200300")
    private String medicalNo;

    @Schema(description = "患者姓名", example = "张三")
    private String patientName;

    @Schema(description = "性别", example = "男")
    private String patientGender;

    @Schema(description = "年龄", example = "25")
    private String patientAge;

    @Schema(description = "当前挂号状态", example = "WAITING_FOR_CONSULTATION")
    private String visitStatus;

    @Schema(description = "病案ID(为空表示未建档)", example = "45678")
    private Integer caseId;
}