package com.ncst.hospitaloutpatient.model.dto.outpatient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateRegistrationRequest {
    @Schema(description = "患者ID", example = "1")
    private Integer patientId;

    @Schema(description = "科室ID", example = "2")
    private Integer departmentId;

    @Schema(description = "医生ID", example = "3")
    private Integer doctorId;

    @Schema(description = "看诊日期", example = "2025-09-10")
    private LocalDate visitDate;

    @Schema(description = "午别", example = "上午")
    private String period;

    @Schema(description = "号别", example = "GENERAL")
    private String numberType;

    @Schema(description = "初始号额", example = "20")
    private Integer initQuota;

    @Schema(description = "已用号额", example = "1")
    private Integer usedQuota;

    @Schema(description = "结算类别ID", example = "1")
    private Integer settlementTypeId;

    @Schema(description = "收费方式ID", example = "1")
    private Integer paymentMethodId;

    @Schema(description = "应收金额", example = "20.00")
    private BigDecimal payableAmount;

    @Schema(description = "是否购买病历本（0=否，1=是）", example = "1")
    private Integer medicalRecordBook;
}