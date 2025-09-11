package com.ncst.hospitaloutpatient.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "统计粒度")
public enum Granularity {
    @Schema(description = "按天")
    DAY,

    @Schema(description = "按月")
    MONTH,

    @Schema(description = "按年")
    YEAR
}