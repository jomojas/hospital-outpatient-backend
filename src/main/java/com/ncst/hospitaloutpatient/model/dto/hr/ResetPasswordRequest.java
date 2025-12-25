package com.ncst.hospitaloutpatient.model.dto.hr;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @Schema(description = "新密码", example = "NewP@ssw0rd")
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度必须在6-50之间")
    private String newPassword;
}

