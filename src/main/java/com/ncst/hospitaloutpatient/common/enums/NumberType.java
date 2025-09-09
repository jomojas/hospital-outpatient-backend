package com.ncst.hospitaloutpatient.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "号源类型（普通号/专家号）")
public enum NumberType {
    @Schema(description = "普通号")
    GENERAL("普通号"),

    @Schema(description = "专家号")
    SPECIALIST("专家号");

    private final String label;

    NumberType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static String getLabelByValue(String value) {
        for (NumberType type : NumberType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type.getLabel();
            }
        }
        return value;
    }
}