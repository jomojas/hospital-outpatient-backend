package com.ncst.hospitaloutpatient.model.entity.auth;

import lombok.Data;

@Data
public class StaffRoleEntity {
    private Integer roleId;
    private String roleName;
    private String description;
}
