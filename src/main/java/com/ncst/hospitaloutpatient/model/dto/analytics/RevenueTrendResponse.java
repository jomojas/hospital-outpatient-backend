package com.ncst.hospitaloutpatient.model.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "收入趋势统计响应")
public class RevenueTrendResponse {

    @Schema(description = "横坐标（时间序列）", example = "[\"2025-09-01\", \"2025-09-02\"]")
    private List<String> xAxis;

    @Schema(description = "收入数据序列", example = "[1000, 1200]")
    private List<Double> series;
}