package com.ncst.hospitaloutpatient.model.entity.auth;

import lombok.Data;

@Data
public class StaffEntity {
    private Integer staffId;
    private String name;
    private String phone;
    private String idCard;
    private Integer departmentId;
    private String description;
    private Integer roleId;
    private java.time.LocalDateTime createTime;
}
