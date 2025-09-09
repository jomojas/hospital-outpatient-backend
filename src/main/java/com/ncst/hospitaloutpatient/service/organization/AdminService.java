package com.ncst.hospitaloutpatient.service.organization;

import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.organization.AdminMapper;
import com.ncst.hospitaloutpatient.model.dto.organization.SetRegistrationPriceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public void setDoctorQuota(Integer doctorId, LocalDate quotaDate, Integer quota) {
        // 1. 判断是否为医生
        Boolean isDoctor = adminMapper.isDoctorRole(doctorId);
        if (isDoctor == null || !isDoctor) {
            throw new BusinessException(40004, "该员工不是医生，无法设置号额");
        }
        // 2. 先尝试更新，如果没有则插入
        int updated = adminMapper.updateDoctorQuota(doctorId, quotaDate, quota);
        if (updated == 0) {
            int inserted = adminMapper.insertDoctorQuota(doctorId, quotaDate, quota);
            if (inserted != 1) {
                throw new BusinessException(500, "插入医生号额失败！");
            }
        }
    }

    public void updateRegistrationPrices(List<SetRegistrationPriceRequest.RegistrationTypePrice> prices) {
        for (SetRegistrationPriceRequest.RegistrationTypePrice price : prices) {
            int updated = adminMapper.updateRegistrationFee(price.getNumberType(), price.getFee());
            if (updated != 1) {
                throw new BusinessException(500, "更新失败！");
            }
        }
    }
}
