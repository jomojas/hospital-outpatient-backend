package com.ncst.hospitaloutpatient.model.dto.outpatient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateRegistrationResponse {
    @Schema(description = "挂号记录ID")
    private Integer registrationId;

    @Schema(description = "就诊ID")
    private Integer visitId;

    @Schema(description = "交易记录ID")
    private Integer transactionId;

    @Schema(description = "科室名称")
    private String departmentName;

    @Schema(description = "医生姓名")
    private String doctorName;

    @Schema(description = "就诊日期")
    private String visitDate; // 可以使用 LocalDateTime 或 Date 根据需求调整

    @Schema(description = "时段")
    private String period;

    @Schema(description = "号别")
    private String numberType;

    @Schema(description = "结算类型名称")
    private String settlementTypeName;

    @Schema(description = "支付方式名称")
    private String paymentMethodName;

    @Schema(description = "病历本")
    private Integer medicalRecordBook;

    @Schema(description = "应付金额")
    private BigDecimal payableAmount; // 金额可以使用 BigDecimal 类型更准确
}