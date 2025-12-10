package com.ncst.hospitaloutpatient.model.dto.casefile;

import lombok.Data;

@Data
public class PrescriptionHistoryDTO {
    private Integer prescriptionId; // apply_id
    private Integer drugId;
    private String drugName;
    private String drugCode;
    private String specification;
    private String unit;
    private String price; // 字符串以匹配前端
    private String usage; // dosage
    private Integer quantity;
    private String status; // UNFINISHED, FINISHED, RETURNED, REVOKED
    private String createTime;
}