package com.ncst.hospitaloutpatient.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum DepartmentType {
    OUTPATIENT("门诊"),
    EXAM("检查"),
    LAB("检验"),
    PROCEDURE("处置"),
    PHARMACY("药房"),
    REGISTRATION("挂号收费"),
    INFORMATION("信息科");

    private final String chineseName;

    DepartmentType(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }

    // 通过英文名查中文
    private static final Map<String, DepartmentType> ENGLISH_MAP = new HashMap<>();
    static {
        for (DepartmentType type : DepartmentType.values()) {
            ENGLISH_MAP.put(type.name(), type);
        }
    }
    public static String getChineseNameByCode(String code) {
        DepartmentType type = ENGLISH_MAP.get(code);
        return type == null ? code : type.getChineseName();
    }
}