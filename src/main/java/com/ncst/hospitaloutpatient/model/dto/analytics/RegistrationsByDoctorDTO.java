package com.ncst.hospitaloutpatient.model.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "医生挂号分布数据")
public class RegistrationsByDoctorDTO {
    @Schema(description = "医生姓名", example = "张三")
    private String name;

    @Schema(description = "该医生挂号数量", example = "20")
    private Integer value;
}