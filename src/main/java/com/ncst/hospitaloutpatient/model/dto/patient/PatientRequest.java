package com.ncst.hospitaloutpatient.model.dto.patient;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientRequest {
    private Long patientId;
    private String patientNo;
    private String name;
    private String gender;
    private String birthday;
    private String idCard;
    private String address;
}
