package com.ncst.hospitaloutpatient.model.dto.pharmacy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "批量发药请求")
public class DispenseDrugsRequest {
    @Schema(description = "发药项列表")
    private List<DispenseDrugItemRequest> prescriptions;
}