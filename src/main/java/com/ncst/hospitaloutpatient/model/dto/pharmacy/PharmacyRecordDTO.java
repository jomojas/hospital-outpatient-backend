package com.ncst.hospitaloutpatient.model.dto.pharmacy;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PharmacyRecordDTO {
    private Integer recordId;
    private Integer prescriptionId;
    private String patientNo;
    private String patientName;
    private Integer drugId;
    private String drugName;
    private String specification;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal amount;
    private String operateType;
    private Integer operatorId;
    private String operatorName;
    private Date operateTime;
    private String remark;
}