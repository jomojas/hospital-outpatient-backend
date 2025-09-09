package com.ncst.hospitaloutpatient.model.dto.reference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DrugCategoryResponse {
    @Schema(description = "类别ID", example = "1")
    private Integer categoryId;

    @Schema(description = "类别名称", example = "抗生素")
    private String categoryName;

    @Schema(description = "类别描述", example = "抗生素类药物")
    private String description;
}