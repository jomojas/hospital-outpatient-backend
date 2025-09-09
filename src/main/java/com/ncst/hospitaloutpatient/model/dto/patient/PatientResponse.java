package com.ncst.hospitaloutpatient.model.dto.patient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PatientResponse {
    @Schema(description = "患者ID")
    private Integer patientId;

    @Schema(description = "患者编号")
    private String patientNo;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "出生日期")
    private String birthday;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "地址")
    private String address;
}