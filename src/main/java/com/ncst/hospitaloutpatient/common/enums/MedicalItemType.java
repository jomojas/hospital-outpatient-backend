package com.ncst.hospitaloutpatient.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "医疗项目类型")
public enum MedicalItemType {
    @Schema(description = "检查")
    EXAM("检查"),
    @Schema(description = "检验")
    LAB("检验"),
    @Schema(description = "处置")
    DISPOSAL("处置");

    private final String label;

    MedicalItemType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}