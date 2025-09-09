package com.ncst.hospitaloutpatient.model.entity.outpatient;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PatientVisit {
    private Integer visitId;
    private Integer patientId;
    private Integer registrationId;
    private Integer departmentId;
    private Integer doctorId;
    private LocalDate visitDate;
    private String currentStatus;
    private LocalDateTime statusChangedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}