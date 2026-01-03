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
    private Boolean isExpert; // 新增

    /**
     * 员工在职状态：来源 staff_account.status
     * - 0: 在职/启用
     * - 1: 停用/离职
     */
    private Integer status;

    private LocalDateTime createTime;
}
