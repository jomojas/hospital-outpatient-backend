package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DepartmentRoleResponse {
    @Schema(description = "角色ID", example = "1")
    private Integer roleId;

    @Schema(description = "角色名称", example = "doctor")
    private String roleName;

    @Schema(description = "角色描述", example = "医生")
    private String description;
}