package com.ncst.hospitaloutpatient.model.dto.analytics;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class RegistrationsTrendResponse {
    @Schema(description = "横坐标（日期/月份/年份等）")
    @JsonProperty("xAxis")
    private List<String> xAxis;

    @Schema(description = "挂号数量")
    private List<Integer> series;
}