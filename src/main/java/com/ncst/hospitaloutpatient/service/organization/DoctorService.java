package com.ncst.hospitaloutpatient.service.organization;


import com.ncst.hospitaloutpatient.mapper.organization.DoctorMapper;
import com.ncst.hospitaloutpatient.model.dto.organization.DoctorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorMapper doctorMapper;

    public List<DoctorResponse> listDoctorsByDepartment(Integer departmentId) {
        return doctorMapper.selectDoctorsByDepartmentId(departmentId);
    }
}
