package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UpdateDepartmentRequest {
    @Schema(description = "科室名称", example = "口腔科")
    private String departmentName;

    @Schema(description = "科室类型代码（OUTPATIENT/EXAM/LAB/DISPOSAL/PHARMACY/REGISTRATION/INFORMATION）", example = "OUTPATIENT")
    private String type;

    @Schema(description = "角色ID列表", example = "[1, 2, 3]")
    private List<Integer> roleIds;
}