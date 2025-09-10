package com.ncst.hospitaloutpatient.model.dto.medicalitem;

import lombok.Data;

/**
 * 检查项目结果提交 DTO
 */
@Data
public class ItemApplyResultDTO {
    private String result;       // 检查结果内容
    private String remark;       // 可选，操作备注
}