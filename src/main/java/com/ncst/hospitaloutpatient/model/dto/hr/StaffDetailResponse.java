package com.ncst.hospitaloutpatient.model.dto.hr;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class StaffDetailResponse {
    private Integer staffId;
    private String name;
    private String phone;
    private String idCard;
    private Integer departmentId;
    private String departmentName; // 新增
    private String departmentType; // 新增
    private Integer roleId;
    private String roleName;       // 新增
    private String description;
    private LocalDateTime createTime;
}
