package com.ncst.hospitaloutpatient.model.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "科室挂号分布数据")
public class RegistrationsByDepartmentDTO {
    @Schema(description = "科室名称", example = "内科")
    private String name;
    @Schema(description = "挂号数量", example = "10")
    private Integer value;
}