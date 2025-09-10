package com.ncst.hospitaloutpatient.model.dto.medicalitem;

import lombok.Data;

@Data
public class DepartmentSimpleDTO {
    private Integer departmentId;
    private String departmentName;
    private String type;
}