package com.ncst.hospitaloutpatient.model.dto.hr;

import lombok.Data;

@Data
public class CreateEmployeeRequest {
    private String name;
    private String phone;
    private String idCard;
    private Integer departmentId;
    private Integer roleId;
    private String description;
    private String accountName; // 前端传入
    private String password;
    private Boolean isExpert; // 可选，只有门诊医生用
}
