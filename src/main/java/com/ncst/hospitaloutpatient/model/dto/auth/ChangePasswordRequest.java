package com.ncst.hospitaloutpatient.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @Schema(description = "旧密码", example = "oldPass123")
    private String oldPassword;

    @Schema(description = "新密码", example = "newPass456")
    private String newPassword;
}
