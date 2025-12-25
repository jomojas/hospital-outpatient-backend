package com.ncst.hospitaloutpatient.model.dto.reference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DrugDetailResponse {
    @Schema(description = "药品Id", example = "1001s")
    private String drugId;

    @Schema(description = "药品编码", example = "A001")
    private String drugCode;

    @Schema(description = "药品名称", example = "阿莫西林胶囊")
    private String drugName;

    @Schema(description = "类别ID", example = "1")
    private Integer categoryId;

    @Schema(description = "类别名称", example = "抗生素")
    private String categoryName;

    @Schema(description = "类别描述", example = "抗生素类药物")
    private String categoryDescription;

    @Schema(description = "生产日期", example = "2025-01-10")
    private Date productionDate;

    @Schema(description = "保质期", example = "24个月")
    private String shelfLife;

    @Schema(description = "库存数量", example = "200.00")
    private BigDecimal stockQuantity;

    @Schema(description = "规格", example = "0.25g*24粒")
    private String specification;

    @Schema(description = "单位", example = "BOX")
    private String unit;

    @Schema(description = "零售价", example = "22.50")
    private BigDecimal retailPrice;

    @Schema(description = "药品描述", example = "抗生素")
    private String description;

    @Schema(description = "生产厂家", example = "某某制药有限公司")
    private String manufacturer;
}