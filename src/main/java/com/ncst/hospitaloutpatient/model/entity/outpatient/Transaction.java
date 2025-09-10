package com.ncst.hospitaloutpatient.model.entity.outpatient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Transaction {
    private Integer transactionId;
    private String businessType;
    private Integer registrationId;
    private Integer patientId;
    private BigDecimal amount;
    private String transactionType;
    private Integer paymentMethodId;
    private LocalDateTime transactionTime;
    private Integer cashierId;
    private String remark;
    private Integer settlementTypeId;
}