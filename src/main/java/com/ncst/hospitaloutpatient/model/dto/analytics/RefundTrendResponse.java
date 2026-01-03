package com.ncst.hospitaloutpatient.model.dto.analytics;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "退费趋势响应")
public class RefundTrendResponse {
    @Schema(description = "横坐标（日期列表）")
    @JsonProperty("xAxis")
    private List<String> xAxis;

    @Schema(description = "每个点的退费总额")
    private List<Double> series;
}
