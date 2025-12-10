package com.ncst.hospitaloutpatient.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Drug Unit")
public enum DrugUnit {
    @Schema(description = "盒")
    BOX("盒"),

    @Schema(description = "瓶")
    BOTTLE("瓶"),

    @Schema(description = "片")
    PIECE("片"),

    @Schema(description = "粒")
    CAPSULE("粒");

    private final String label;

    DrugUnit(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static String toLabel(String code) {
        if (code == null) return null;
        try {
            return DrugUnit.valueOf(code).getLabel();
        } catch (IllegalArgumentException ex) {
            return code;
        }
    }
}