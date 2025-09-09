package com.ncst.hospitaloutpatient.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "就诊状态")
public enum VisitStatus {
    WAITING_FOR_CONSULTATION("待看诊"),
    INITIAL_CONSULTATION_DONE("已初诊"),
    WAITING_FOR_PROJECT_PAYMENT("待项目缴费"),
    WAITING_FOR_CHECKUP("待检查"),
    CHECKING("检查中"),
    WAITING_FOR_REVISIT("待复诊"),
    REVISITED("已复诊"),
    WAITING_FOR_PRESCRIPTION_PAYMENT("待处方缴费"),
    WAITING_FOR_MEDICINE("待取药"),
    MEDICINE_TAKEN("已取药"),
    MEDICINE_RETURNED("已退药"),
    FINISHED("诊疗结束");

    private final String label;

    VisitStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static String getLabelByValue(String value) {
        for (VisitStatus status : VisitStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status.getLabel();
            }
        }
        return value;
    }
}