package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DepartmentTypeResponse {
    @Schema(description = "科室类型英文编码", example = "OUTPATIENT")
    private String type;

    @Schema(description = "科室类型中文名称", example = "门诊")
    private String typeName;
}