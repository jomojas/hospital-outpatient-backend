package com.ncst.hospitaloutpatient.model.dto.casefile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "病案费用详情DTO")
public class CaseFeeDTO {
    @Schema(description = "挂号费")
    private BigDecimal registrationFee;

    @Schema(description = "医疗项目费用列表")
    private List<ItemFeeDTO> medicalItemFees;

    @Schema(description = "处方药品费用列表")
    private List<DrugFeeDTO> prescriptionFees;

    @Schema(description = "总费用")
    private BigDecimal total;
}