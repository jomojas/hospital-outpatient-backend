package com.ncst.hospitaloutpatient.model.dto.billing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "结算缴费请求DTO")
public class ChargeSettleRequestDTO {

    @Schema(description = "缴费项目列表")
    private List<ChargeSettleItemDTO> items;

    @Schema(description = "支付方式ID")
    private Integer paymentMethodId;

    @Schema(description = "结算类别ID")
    private Integer settlementTypeId;
}