package com.ncst.hospitaloutpatient.model.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
public class RegistrationsTrendResponse {
    @Schema(description = "横坐标（日期/月份/年份等）")
    private List<String> xAxis;

    @Schema(description = "挂号数量")
    private List<Integer> series;
}