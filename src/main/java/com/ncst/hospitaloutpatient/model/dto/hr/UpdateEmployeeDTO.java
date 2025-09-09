package com.ncst.hospitaloutpatient.model.dto.hr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Data
public class UpdateEmployeeDTO {
    @Schema(description = "部门ID", required = true, example = "1")
    @NotNull(message = "部门ID不能为空")
    private Integer departmentId;

    @Schema(description = "角色ID", required = true, example = "2")
    @NotNull(message = "角色ID不能为空")
    private Integer roleId;

    @Schema(description = "员工姓名", required = true, example = "李四")
    @NotBlank(message = "员工姓名不能为空")
    private String name;

    @Schema(description = "手机号", required = true, example = "13912345678")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Schema(description = "身份证号", required = true, example = "110101199001011234")
    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    @Schema(description = "是否为专家，仅门诊医生可选", required = false, example = "true")
    private Boolean isExpert;
}