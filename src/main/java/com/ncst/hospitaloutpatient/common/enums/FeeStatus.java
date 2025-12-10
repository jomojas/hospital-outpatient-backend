package com.ncst.hospitaloutpatient.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "费用状态")
public enum FeeStatus {
    @Schema(description = "未缴费")
    UNPAID,
    @Schema(description = "已缴费")
    PAID,
    @Schema(description = "已退费")
    REFUNDED,
    @Schema(description = "已撤销")
    REVOKED
}