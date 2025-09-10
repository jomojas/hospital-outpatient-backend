package com.ncst.hospitaloutpatient.model.dto.billing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "需缴费项目/药品响应体")
public class ChargeItemResponse {
    @Schema(description = "类型（ITEM-医疗项目, DRUG-药品）", example = "ITEM")
    private String type;

    @Schema(description = "申请/处方ID", example = "1")
    private Integer applyId;

    @Schema(description = "患者ID", example = "1")
    private Integer patientId;

    @Schema(description = "患者姓名", example = "张三")
    private String patientName;

    @Schema(description = "挂号ID", example = "1")
    private Integer registrationId;

    @Schema(description = "项目/药品ID", example = "3")
    private Integer itemId;

    @Schema(description = "项目/药品代码", example = "A001")
    private String itemCode;

    @Schema(description = "项目/药品名称", example = "阿莫西林胶囊")
    private String itemName;

    @Schema(description = "项目类型(EXAM/LAB/DISPOSAL)或药品类别", example = "EXAM")
    private String itemType;

    @Schema(description = "药品类别ID（仅药品时有）", example = "1")
    private Integer drugCategoryId;

    @Schema(description = "价格", example = "22.50")
    private BigDecimal price;

    @Schema(description = "数量", example = "6.00")
    private BigDecimal quantity;

    @Schema(description = "总价", example = "135.00")
    private BigDecimal totalAmount;

    @Schema(description = "描述", example = "抗生素")
    private String description;

    @Schema(description = "申请/开方时间", example = "2025-09-01T10:30:00")
    private LocalDateTime createTime;
}