package com.ncst.hospitaloutpatient.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @Schema(description = "旧密码", example = "oldPass123")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @Schema(description = "新密码", example = "newPass456")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
