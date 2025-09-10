package com.ncst.hospitaloutpatient.model.dto.medicalitem;

import lombok.Data;

@Data
public class ItemApplyInfoForLog {
    private Integer itemId;
    private String itemName;
    private String itemType;
    private String patientNo;
    private String patientName;
}