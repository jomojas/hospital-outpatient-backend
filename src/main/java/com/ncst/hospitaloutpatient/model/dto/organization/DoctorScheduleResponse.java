package com.ncst.hospitaloutpatient.model.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "医生排班信息")
public class DoctorScheduleResponse {

    @Schema(description = "医生工号ID")
    private Integer staffId;

    @Schema(description = "医生姓名")
    private String staffName;

    @Schema(description = "医生职称/描述")
    private String description;

    @Schema(description = "排班列表")
    private List<DailySchedule> schedules;

    @Data
    @Schema(description = "单日排班详情")
    public static class DailySchedule {
        @Schema(description = "排班主键ID (如果是新排班则为null)")
        private Integer id;

        @Schema(description = "排班日期")
        private LocalDate date;

        @Schema(description = "总号源")
        private Integer quota;

        @Schema(description = "已用号源")
        private Integer used;
    }
}