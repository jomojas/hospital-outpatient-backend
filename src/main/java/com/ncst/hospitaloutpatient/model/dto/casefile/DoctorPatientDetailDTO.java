package com.ncst.hospitaloutpatient.model.dto.casefile;

import lombok.Data;

@Data
public class DoctorPatientDetailDTO {
    private Integer patientId;
    private String patientNo;
    private String name;
    private String gender;
    private String birthday;
    private String idCard;
    private String address;
    private String medicalNo;
    private String status;
    private String registrationDate;
    private String complaint;
}