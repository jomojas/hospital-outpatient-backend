package com.ncst.hospitaloutpatient.model.dto.pharmacy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "发药项请求")
public class DispenseDrugItemRequest {
    @Schema(description = "处方ID")
    private Integer prescriptionId;
    @Schema(description = "患者编号")
    private String patientNo;
}