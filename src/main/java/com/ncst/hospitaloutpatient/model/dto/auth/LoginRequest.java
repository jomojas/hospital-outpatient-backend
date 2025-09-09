package com.ncst.hospitaloutpatient.model.dto.auth;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @Schema(description = "用户名", example = "INF250001")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "Pass123")
    @NotBlank(message = "密码不能为空")
    private String password;
}