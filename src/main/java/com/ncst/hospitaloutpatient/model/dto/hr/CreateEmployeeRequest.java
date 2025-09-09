package com.ncst.hospitaloutpatient.model.dto.hr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateEmployeeRequest {
    @Schema(description = "员工姓名", required = true, example = "张三")
    @NotBlank(message = "员工姓名不能为空")
    private String name;

    @Schema(description = "手机号", required = true, example = "13812345678")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Schema(description = "身份证号", required = true, example = "110101199003071234")
    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    @Schema(description = "部门ID", required = true, example = "1")
    @NotNull(message = "部门ID不能为空")
    private Integer departmentId;

    @Schema(description = "角色ID", required = true, example = "2")
    @NotNull(message = "角色ID不能为空")
    private Integer roleId;

    @Schema(description = "描述", required = false, example = "新入职员工")
    private String description;

    @Schema(description = "账号名（前端传入）", required = true, example = "zhangsan")
    @NotBlank(message = "账号名不能为空")
    private String accountName;

    @Schema(description = "密码", required = true, example = "password123")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "是否为专家，仅门诊医生可选", required = false, example = "true")
    private Boolean isExpert;
}