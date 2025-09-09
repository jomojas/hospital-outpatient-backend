package com.ncst.hospitaloutpatient.model.dto.reference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PaymentMethodResponse {
    @Schema(description = "收费方式ID", example = "1")
    private Integer paymentMethodId;

    @Schema(description = "收费方式名称", example = "微信")
    private String name;

    @Schema(description = "收费方式描述", example = "微信支付")
    private String description;
}