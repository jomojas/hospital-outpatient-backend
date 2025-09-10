package com.ncst.hospitaloutpatient.model.entity.medicalItem;

import lombok.Data;

import java.util.Date;

@Data
public class MedicalItemOperationLog {
    private Integer logId;
    private Integer applyId;
    private Integer operatorId;
    private Date operateTime;
    private String operateType;
    private Integer itemId;
    private String itemName;
    private String itemType;
    private String patientNo;
    private String patientName;
    private String remark;
}