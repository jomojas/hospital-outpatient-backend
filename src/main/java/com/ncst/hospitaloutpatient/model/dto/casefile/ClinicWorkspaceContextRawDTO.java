package com.ncst.hospitaloutpatient.model.dto.casefile;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClinicWorkspaceContextRawDTO {
    private Integer registrationId;
    private String medicalNo;
    private String patientName;
    private String patientGender;
    private LocalDate birthDate;
    private String visitStatus;
    private Integer caseId;
}