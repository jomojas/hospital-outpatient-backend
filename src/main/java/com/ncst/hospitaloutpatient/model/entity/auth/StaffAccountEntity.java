package com.ncst.hospitaloutpatient.model.entity.auth;

import lombok.Data;

@Data
public class StaffAccountEntity {
    private Integer accountId;
    private Integer staffId;
    private String accountName;
    private String password;
    private Integer status;
    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime lastLogin;
}
