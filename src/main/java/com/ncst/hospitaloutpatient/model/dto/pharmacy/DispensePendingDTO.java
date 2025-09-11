package com.ncst.hospitaloutpatient.model.dto.pharmacy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "待发药处方信息")
public class DispensePendingDTO {
    @Schema(description = "处方ID")
    private Integer prescriptionId;
    @Schema(description = "患者姓名")
    private String patientName;
    @Schema(description = "患者性别")
    private String gender;
    @Schema(description = "患者编号")
    private String patientNo;
    @Schema(description = "药品ID")
    private Integer drugId;
    @Schema(description = "药品名")
    private String drugName;
    @Schema(description = "药品类别")
    private String drugCategory;
    @Schema(description = "用法")
    private String dosage;
    @Schema(description = "数量")
    private BigDecimal quantity;
    @Schema(description = "开方时间")
    private Date prescribeTime;
    @Schema(description = "处方备注")
    private String remark;
}