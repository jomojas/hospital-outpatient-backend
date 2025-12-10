package com.ncst.hospitaloutpatient.model.dto.casefile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "病案项目历史记录")
public class CaseItemHistoryDTO {
    private Integer itemId;
    private String itemName;
    private String itemCode;
    private String itemType;   // EXAM / LAB / DISPOSAL
    private String status;     // PENDING_PAYMENT / UNFINISHED / FINISHED / REVOKED
    private String price;      // formatted as string
    private Integer unit;
    private String createTime; // apply_time
    private Integer applyId;   // primary key of medical_item_apply
}