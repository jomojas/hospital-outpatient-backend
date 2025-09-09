package com.ncst.hospitaloutpatient.model.dto.outpatient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "挂号详情响应对象")
public class RegistrationDetailResponse {

    @Schema(description = "挂号ID", example = "1")
    private Integer registrationId;

    @Schema(description = "患者ID", example = "1")
    private Integer patientId;

    @Schema(description = "患者姓名", example = "张三")
    private String patientName;

    @Schema(description = "科室ID", example = "2")
    private Integer departmentId;

    @Schema(description = "科室名称", example = "内科")
    private String departmentName;

    @Schema(description = "医生ID", example = "5")
    private Integer doctorId;

    @Schema(description = "医生姓名", example = "李四")
    private String doctorName;

    @Schema(description = "挂号日期", example = "2025-09-09")
    private LocalDate visitDate;

    @Schema(description = "时段", example = "MORNING")
    private String period;

    @Schema(description = "号源类型", example = "GENERAL")
    private String numberType;

    @Schema(description = "初始号源数", example = "20")
    private Integer initQuota;

    @Schema(description = "已用号源数", example = "6")
    private Integer usedQuota;

    @Schema(description = "结算方式ID", example = "1")
    private Integer settlementTypeId;

    @Schema(description = "结算方式名称", example = "医保")
    private String settlementTypeName;

    @Schema(description = "支付方式ID", example = "1")
    private Integer paymentMethodId;

    @Schema(description = "支付方式名称", example = "现金")
    private String paymentMethodName;

    @Schema(description = "应支付金额", example = "20.00")
    private BigDecimal payableAmount;

    @Schema(description = "是否需要病历本(0-否,1-是)", example = "1")
    private Integer medicalRecordBook;

    @Schema(description = "挂号当前状态", example = "WAITING_FOR_CONSULTATION")
    private String currentStatus;
}