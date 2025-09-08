package com.ncst.hospitaloutpatient.controller.auth;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.auth.ChangePasswordRequest;
import com.ncst.hospitaloutpatient.model.dto.auth.LoginRequest;
import com.ncst.hospitaloutpatient.model.dto.auth.UserDto;
import com.ncst.hospitaloutpatient.service.auth.AuthService;
import com.ncst.hospitaloutpatient.common.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @Operation(
            summary = "用户登录",
            description = "通过账号和密码进行登录"
    )
    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        UserDto user = authService.login(request.getUsername(), request.getPassword());
        Map<String, Object> claims = Map.of(
                "userId", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole()
        );
        String token = jwtUtil.generateToken(claims, String.valueOf(user.getId()));
        Map<String, String> data = Map.of("token", token);
        return ApiResponse.ok(data);
    }

    @Operation(
            summary = "用户登出",
            description = "登出当前用户"
    )
    @PostMapping("/logout")
    public ApiResponse<?> logout() {
        // 在无状态的JWT认证中，通常不需要在服务器端处理登出逻辑
        return ApiResponse.ok();
    }

    @Operation(
            summary = "更改密码",
            description = "更改当前用户的密码"
    )
    @PostMapping("/password/change")
    public ApiResponse<?> changePassword(@RequestBody ChangePasswordRequest request) {
        String staffId = SecurityContextHolder.getContext().getAuthentication().getName();
        authService.changePassword(staffId, request.getOldPassword(), request.getNewPassword());
        return ApiResponse.ok("密码修改成功");
    }
}
