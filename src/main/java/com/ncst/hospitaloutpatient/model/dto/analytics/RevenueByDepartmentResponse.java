package com.ncst.hospitaloutpatient.model.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "按科室统计的收入响应")
public class RevenueByDepartmentResponse {
    @Schema(description = "各科室收入明细")
    private List<DepartmentRevenueItem> items;

    @Data
    @Schema(description = "单科室收入")
    public static class DepartmentRevenueItem {
        @Schema(description = "科室ID")
        private Integer departmentId;
        @Schema(description = "科室名称")
        private String departmentName;
        @Schema(description = "该科室的收入总额")
        private Double amount;
    }
}
