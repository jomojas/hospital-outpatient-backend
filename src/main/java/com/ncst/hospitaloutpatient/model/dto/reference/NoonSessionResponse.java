package com.ncst.hospitaloutpatient.model.dto.reference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoonSessionResponse {
    @Schema(description = "编码", example = "MORNING")
    private String code;

    @Schema(description = "午别描述", example = "上午")
    private String label;
}