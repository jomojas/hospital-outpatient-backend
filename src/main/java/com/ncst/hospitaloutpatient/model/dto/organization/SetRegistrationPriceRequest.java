package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
@Data
public class SetRegistrationPriceRequest {
    private List<PriceItem> prices;

    @Data
    public static class PriceItem {
        // ✅ 修改为:
        private String code; // 对应 number_type，如 "GENERAL"

        private BigDecimal price;
    }
}