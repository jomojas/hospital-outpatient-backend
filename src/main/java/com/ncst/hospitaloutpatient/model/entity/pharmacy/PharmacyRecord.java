package com.ncst.hospitaloutpatient.model.entity.pharmacy;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PharmacyRecord {
    private Integer recordId;       // 主键，自增可不赋值
    private Integer prescriptionId;
    private String patientNo;
    private String patientName;
    private Integer drugId;
    private String drugName;
    private BigDecimal quantity;
    private BigDecimal amount;
    private String operateType;
    private Integer operatorId;
    private Date operateTime;
}