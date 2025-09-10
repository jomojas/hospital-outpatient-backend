package com.ncst.hospitaloutpatient.model.dto.billing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "费用交易记录返回DTO")
public class FeeTransactionDTO {

    @Schema(description = "交易ID", example = "12")
    private Integer transactionId;

    @Schema(description = "患者姓名", example = "张三")
    private String patientName;

    @Schema(description = "挂号ID", example = "5")
    private Integer registrationId;

    @Schema(description = "交易类型（PAID-已支付，REFUND-已退费）", example = "PAID")
    private String status;

    @Schema(description = "交易金额", example = "135.00")
    private BigDecimal amount;

    @Schema(description = "交易时间", example = "2025-09-10T10:57:01")
    private LocalDateTime transactionTime;

    @Schema(description = "交易说明", example = "处方缴费")
    private String remark;
}