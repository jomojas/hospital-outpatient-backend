package com.ncst.hospitaloutpatient.model.dto.patient;

import lombok.Data;

@Data
public class PatientRequest {
    private Long patientId;
    private String patientNo;
    private String name;
    private String gender;
    private String birthday;
    private String idCard;
    private String address;
}
