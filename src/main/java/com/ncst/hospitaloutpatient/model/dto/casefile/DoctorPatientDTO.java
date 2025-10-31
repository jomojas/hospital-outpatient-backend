package com.ncst.hospitaloutpatient.model.dto.casefile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "医生挂号患者信息")
public class DoctorPatientDTO {

    @Schema(description = "患者姓名", example = "张三")
    private String name;

    @Schema(description = "病历号", example = "100200300")
    private String medicalNo;

    @Schema(description = "挂号状态", example = "WAITING_FOR_CONSULTATION")
    private String status;

    @Schema(description = "挂号日期", example = "2024-06-20")
    private LocalDate registrationDate;

    @Schema(description = "主诉", example = "头痛、发热")
    private String complaint;
}