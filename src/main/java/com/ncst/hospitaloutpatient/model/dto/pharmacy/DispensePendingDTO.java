package com.ncst.hospitaloutpatient.model.dto.pharmacy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DispensePendingDTO {

    @Schema(description = "处方ID")
    private Integer prescriptionId;

    @Schema(description = "挂号ID")
    private Integer registrationId;

    @Schema(description = "患者姓名")
    private String patientName;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "患者编号")
    private String patientNo;

    @Schema(description = "药品ID")
    private Integer drugId;

    @Schema(description = "药品名称")
    private String drugName;

    @Schema(description = "药品分类")
    private String drugCategory;

    @Schema(description = "规格")
    private String specification;

    @Schema(description = "单位（英文原样，服务层转换）")
    private String unit;

    @Schema(description = "剂量")
    private String dosage;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "开药时间")
    private java.time.LocalDateTime prescribeTime;

    @Schema(description = "备注")
    private String remark;
}