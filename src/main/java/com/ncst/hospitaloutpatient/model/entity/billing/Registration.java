package com.ncst.hospitaloutpatient.model.entity.billing;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Registration {
    private Integer registrationId;       // 挂号记录ID
    private Integer patientId;            // 患者ID
    private Integer departmentId;         // 科室ID
    private Integer doctorId;             // 医生ID
    private LocalDate visitDate;          // 看诊日期
    private String period;                // 午别（上午/下午）
    private String numberType;            // 号别（普通号/专家号）
    private Integer initQuota;            // 初始号额
    private Integer usedQuota;            // 已用号额
    private Integer settlementTypeId;     // 结算类别ID
    private Integer paymentMethodId;      // 收费方式ID
    private BigDecimal payableAmount;     // 应收金额
    private Byte medicalRecordBook;       // 是否购买病历本（0=否，1=是）
}