package com.ncst.hospitaloutpatient.controller.outpatient;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.outpatient.CreateRegistrationRequest;
import com.ncst.hospitaloutpatient.model.dto.outpatient.CreateRegistrationResponse;
import com.ncst.hospitaloutpatient.service.outpatient.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/registrations")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @Operation(summary = "创建挂号记录", description = "创建一条新的挂号记录")
    @PostMapping
    public ApiResponse<CreateRegistrationResponse> createRegistration(@RequestBody CreateRegistrationRequest request) {
        CreateRegistrationResponse response = registrationService.createRegistration(request);
        return ApiResponse.ok(response);
    }
}
