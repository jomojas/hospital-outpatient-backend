package com.ncst.hospitaloutpatient.controller.patient;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.patient.PatientResponse;
import com.ncst.hospitaloutpatient.service.patient.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Operation(summary = "查询患者信息", description = "根据姓名和身份证号查询患者信息")
    @GetMapping("/search")
    public ApiResponse<List<PatientResponse>> searchPatient(
            @Parameter(description = "姓名") @RequestParam(required = false) String name,
            @Parameter(description = "身份证号") @RequestParam(required = false) String idCard
    ) {
        List<PatientResponse> patients = patientService.searchPatient(name, idCard);
        return ApiResponse.ok(patients);
    }
}
