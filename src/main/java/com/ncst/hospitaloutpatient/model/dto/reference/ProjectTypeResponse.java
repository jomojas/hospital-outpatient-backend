package com.ncst.hospitaloutpatient.model.dto.reference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectTypeResponse {
    @Schema(description = "项目类型编码", example = "EXAM")
    private String code;

    @Schema(description = "项目类型名称", example = "检查")
    private String name;
}