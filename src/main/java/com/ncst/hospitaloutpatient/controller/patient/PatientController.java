package com.ncst.hospitaloutpatient.controller.patient;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.patient.PatientRequest;
import com.ncst.hospitaloutpatient.model.dto.patient.PatientResponse;
import com.ncst.hospitaloutpatient.service.patient.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

    @Operation(summary = "添加患者", description = "创建新的患者信息")
    @PostMapping
    public ApiResponse<Map<String, Long>> createPatient(@RequestBody PatientRequest request) {
        long patientdId = patientService.createPatient(request);
        Map<String, Long> response = new HashMap<>();
        response.put("patientId", patientdId);
        return ApiResponse.ok(response);
    }
}
