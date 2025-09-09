package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SetRegistrationPriceRequest {
    @Schema(description = "挂号类型价格列表")
    private List<RegistrationTypePrice> prices;

    @Data
    public static class RegistrationTypePrice {
        @Schema(description = "号类型", example = "GENERAL")
        private String numberType;
        @Schema(description = "价格", example = "20.00")
        private BigDecimal fee;
    }
}