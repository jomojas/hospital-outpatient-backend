package com.ncst.hospitaloutpatient.model.entity.casefile;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicalItemApply {
    private Integer applyId;
    private Integer recordId;
    private Integer registrationId;
    private Integer itemId;
    private String applyType;
    private String applyPurpose;
    private String applySite;
    private LocalDateTime applyTime;
    private Integer performerId;
    private Integer resultRecorderId;
    private LocalDateTime performTime;
    private String result;
    private String status;
    private Integer unit;
    private String remark;
}