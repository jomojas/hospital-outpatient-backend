package com.ncst.hospitaloutpatient.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "午别（上午/下午）")
public enum Period {
    @Schema(description = "上午")
    MORNING("上午"),

    @Schema(description = "下午")
    AFTERNOON("下午");

    private final String label;

    Period(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}