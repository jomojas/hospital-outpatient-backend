package com.ncst.hospitaloutpatient.model.dto.outpatient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateRegistrationResponse {
    @Schema(description = "挂号记录ID")
    private Integer registrationId;

    @Schema(description = "就诊ID")
    private Integer visitId;
}