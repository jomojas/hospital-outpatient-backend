package com.ncst.hospitaloutpatient.service.billing;

import com.ncst.hospitaloutpatient.mapper.billing.FeeMapper;
import com.ncst.hospitaloutpatient.model.dto.billing.FeeTransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeeService {
    @Autowired
    private FeeMapper feeMapper;

    public List<FeeTransactionDTO> queryFees(String name, String status, String startTime, String endTime, Integer page, Integer pageSize, String sortBy, String order) {
        int offset = (page - 1) * pageSize;
        return feeMapper.selectFees(name, status, startTime, endTime, sortBy, order, offset, pageSize);
    }

    public long countFees(String name, String status, String startTime, String endTime) {
        return feeMapper.countFees(name, status, startTime, endTime);
    }
}
