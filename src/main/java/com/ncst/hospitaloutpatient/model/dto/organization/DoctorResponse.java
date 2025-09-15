package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
public class DoctorResponse {
    @Schema(description = "员工ID", example = "1")
    private Integer staffId;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "手机号", example = "13800000001")
    private String phone;

    @Schema(description = "身份证号", example = "110101199001010011")
    private String idCard;

    @Schema(description = "科室ID", example = "1")
    private Integer departmentId;

    @Schema(description = "简介/职位描述", example = "内科主治医生")
    private String description;

    @Schema(description = "角色ID", example = "1")
    private Integer roleId;

    @Schema(description = "创建时间", example = "2025-08-20 08:30:00")
    private Date createTime;

    @Schema(description = "是否为专家", example = "true")
    private Boolean isExpert;

    @Schema(description = "Quota Number That Day", example = "true")
    private Integer initQuota;

    @Schema(description = "Being Used Quota Number", example = "true")
    private Integer usedQuota;
}