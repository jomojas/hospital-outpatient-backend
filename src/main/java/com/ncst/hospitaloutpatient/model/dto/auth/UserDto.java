package com.ncst.hospitaloutpatient.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "user1")
    private String username;

    @Schema(description = "角色", example = "admin")
    private String role;

    @Schema(description = "科室类型", example = "内科")
    private String departmentType;

    @Schema(description = "科室类型", example = "内科")
    private String patientName;

    @Schema(description = "科室类型", example = "内科")
    private String lastLoginTime;
}