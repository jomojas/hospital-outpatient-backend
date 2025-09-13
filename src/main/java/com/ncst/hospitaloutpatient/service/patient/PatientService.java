package com.ncst.hospitaloutpatient.service.patient;

import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.patient.PatientMapper;
import com.ncst.hospitaloutpatient.model.dto.patient.PatientResponse;
import com.ncst.hospitaloutpatient.model.dto.patient.PatientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    @Autowired
    private PatientMapper patientMapper;

    public List<PatientResponse> searchPatient(String name, String idCard) {
        return patientMapper.searchPatient(name, idCard);
    }

    public long createPatient(PatientRequest request) {
        int rows = patientMapper.insertPatient(request);
        if (rows <= 0) {
            throw new BusinessException(500, "创建患者失败");
        }
        // 插入成功后，patientId 会自动回填到 request 对象
        return request.getPatientId();
    }
}
