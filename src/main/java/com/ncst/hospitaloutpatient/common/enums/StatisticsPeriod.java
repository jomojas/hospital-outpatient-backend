package com.ncst.hospitaloutpatient.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "统计图表时间区间")
public enum StatisticsPeriod {
    @Schema(description = "最近1月")
    MONTH,

    @Schema(description = "最近1季度")
    SEASON,

    @Schema(description = "最近1年")
    YEAR,

    @Schema(description = "全部")
    ALL,

    @Schema(description = "自定义")
    AUTO
}