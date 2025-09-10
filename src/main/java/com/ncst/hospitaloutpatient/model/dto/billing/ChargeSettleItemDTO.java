package com.ncst.hospitaloutpatient.model.dto.billing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "结算缴费项精简DTO")
public class ChargeSettleItemDTO {
    @Schema(description = "类型（ITEM-医疗项目, DRUG-药品）", example = "ITEM")
    private String type;

    @Schema(description = "申请/处方ID", example = "1")
    private Integer applyId;

    @Schema(description = "挂号ID", example = "1")
    private Integer registrationId;

    @Schema(description = "项目/药品ID", example = "3")
    private Integer itemId;

    @Schema(description = "患者ID", example = "1")
    private Integer patientId;

    @Schema(description = "数量", example = "6.00")
    private BigDecimal quantity;

    @Schema(description = "单价", example = "22.50")
    private BigDecimal price;

    @Schema(description = "总金额", example = "135.00")
    private BigDecimal totalAmount;

    @Schema(description = "状态", example = "PENDING_PAYMENT")
    private String status;
}