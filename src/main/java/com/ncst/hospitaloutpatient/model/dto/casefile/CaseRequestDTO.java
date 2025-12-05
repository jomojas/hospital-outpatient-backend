package com.ncst.hospitaloutpatient.model.dto.casefile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新病案请求体")
public class CaseRequestDTO {
    @Schema(description = "患者编号", example = "PNO001")
    private String patientNo;

    @Schema(description = "挂号ID", example = "1")
    private Integer registrationId;

    @Schema(description = "主诉", example = "咳嗽")
    private String chiefComplaint;

    @Schema(description = "现病史", example = "咳嗽3天，无发热")
    private String presentHistory;

    @Schema(description = "体格检查", example = "咽部充血")
    private String physicalExam;

    @Schema(description = "诊断", example = "上呼吸道感染")
    private String diagnosis;

    @Schema(description = "治疗方案", example = "口服阿莫西林")
    private String treatmentPlan;
}