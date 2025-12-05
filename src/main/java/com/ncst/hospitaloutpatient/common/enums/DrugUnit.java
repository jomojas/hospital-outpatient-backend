package com.ncst.hospitaloutpatient.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Drug Unit")
public enum DrugUnit {
    @Schema(description = "盒")
    BOX,

    @Schema(description = "瓶")
    BOTTLE,

    @Schema(description = "片")
    PIECE,

    @Schema(description = "粒")
    CAPSULE
}