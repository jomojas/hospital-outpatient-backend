package com.ncst.hospitaloutpatient.model.dto.outpatient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "新增交易记录DTO")
public class TransactionInsertDTO {
    @Schema(description = "业务类型", example = "REGISTRATION")
    private String businessType;

    @Schema(description = "挂号ID", example = "1")
    private Integer registrationId;

    @Schema(description = "患者ID", example = "1")
    private Integer patientId;

    @Schema(description = "金额", example = "20.00")
    private BigDecimal amount;

    @Schema(description = "交易类型", example = "REFUND")
    private String transactionType;

    @Schema(description = "支付方式ID", example = "1")
    private Integer paymentMethodId;

    @Schema(description = "交易时间", example = "2025-09-09T22:22:16")
    private LocalDateTime transactionTime;

    @Schema(description = "收银员ID", example = "7")
    private Integer cashierId;

    @Schema(description = "备注", example = "退挂号费")
    private String remark;
}