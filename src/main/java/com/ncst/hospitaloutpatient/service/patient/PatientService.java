package com.ncst.hospitaloutpatient.service.patient;

import com.ncst.hospitaloutpatient.mapper.patient.PatientMapper;
import com.ncst.hospitaloutpatient.model.dto.patient.PatientResponse;
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
}
