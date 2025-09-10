package com.ncst.hospitaloutpatient.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "待缴费状态枚举")
public enum PaymentStatus {
    @Schema(description = "待缴费")
    PENDING_PAYMENT("待缴费"),
    @Schema(description = "已缴费待完成")
    UNFINISHED("待完成/待发药"),
    @Schema(description = "已完成")
    FINISHED("已完成/已发药"),
    @Schema(description = "已退回/撤销")
    RETURNED("已退回/撤销"),
    @Schema(description = "已退费/作废")
    CANCELLED("已退费/作废");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
}