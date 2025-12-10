package com.ncst.hospitaloutpatient.model.dto.casefile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "费用查询响应")
public class CaseFeeDTO {
    @Schema(description = "挂号费（字符串）", example = "10.00")
    private String registrationFee;

    @Schema(description = "医疗项目费用列表")
    private List<ItemFeeDTO> medicalItemFees;

    @Schema(description = "处方费用列表")
    private List<DrugFeeDTO> prescriptionFees;

    @Schema(description = "总金额（字符串）", example = "120.00")
    private String totalAmount;

    @Schema(description = "待缴金额（字符串）", example = "30.00")
    private String unpaidAmount;
}