package com.ncst.hospitaloutpatient.model.entity.casefile;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Prescription {
    private Integer prescriptionId;
    private Integer recordId;
    private Integer registrationId;
    private Integer drugId;
    private String dosage;
    private Double quantity;
    private LocalDateTime prescribeTime;
    private String status;
    private String remark;
}