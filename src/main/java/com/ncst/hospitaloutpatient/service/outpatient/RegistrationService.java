package com.ncst.hospitaloutpatient.service.outpatient;

import com.ncst.hospitaloutpatient.common.enums.VisitStatus;
import com.ncst.hospitaloutpatient.mapper.outpatient.RegistrationMapper;
import com.ncst.hospitaloutpatient.model.dto.outpatient.CreateRegistrationRequest;
import com.ncst.hospitaloutpatient.model.dto.outpatient.CreateRegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistrationService {
    @Autowired
    private RegistrationMapper registrationMapper;

    @Transactional
    public CreateRegistrationResponse createRegistration(CreateRegistrationRequest request) {
        // 1. 插入 registration 表
        registrationMapper.insertRegistration(request);

        Integer registrationId = request.getRegistrationId();

        // 2. 插入 patient_visit 表
        LocalDateTime now = LocalDateTime.now();
        registrationMapper.insertPatientVisit(
                request.getPatientId(),
                registrationId,
                request.getDepartmentId(),
                request.getDoctorId(),
                request.getVisitDate(),
                VisitStatus.WAITING_FOR_CONSULTATION.name(),
                now, now, now
        );

        Integer visitId = request.getVisitId();

        // 3. 返回响应
        CreateRegistrationResponse response = new CreateRegistrationResponse();
        response.setRegistrationId(registrationId);
        response.setVisitId(visitId);
        return response;
    }
}
