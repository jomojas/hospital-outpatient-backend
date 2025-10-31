package com.ncst.hospitaloutpatient.controller.casefile;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.casefile.*;
import com.ncst.hospitaloutpatient.model.entity.casefile.MedicalRecord;
import com.ncst.hospitaloutpatient.service.casefile.CaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cases")
public class CaseController {
    @Autowired
    private CaseService caseService;

    @PostMapping
    @Operation(summary = "创建病案", description = "新增一条病案记录")
    public ApiResponse<?> createCase(@RequestBody MedicalRecordCreateRequest request) {
        caseService.createMedicalRecord(request);
        return ApiResponse.ok();
    }


    @PostMapping("/{caseId}/applies")
    @Operation(summary = "为指定病案申请医疗项目", description = "提交一组医疗项目申请")
    public ApiResponse<?> submitApplies(
            @Parameter(description = "病案号", example = "1") @PathVariable("caseId") Integer caseId,
            @RequestBody MedicalItemApplyRequest request
    ) {
        caseService.submitApplies(caseId, request);
        return ApiResponse.ok();
    }

    @GetMapping("/{caseId}/results")
    @Operation(summary = "获取病案医疗项目结果", description = "根据病案ID获取其所有医疗项目申请的结果")
    public ApiResponse<List<CaseApplyResultDTO>> listCaseResults(
            @Parameter(description = "病案ID", example = "1") @PathVariable("caseId") Integer caseId) {
        List<CaseApplyResultDTO> results = caseService.listCaseResults(caseId);
        return ApiResponse.ok(results);
    }

    @PostMapping("/{caseId}/prescriptions")
    @Operation(summary = "为病案开具处方", description = "对指定病案批量开具药品处方")
    public ApiResponse<?> createPrescriptions(
            @Parameter(description = "病案ID", example = "1") @PathVariable("caseId") Integer caseId,
            @RequestBody PrescriptionCreateRequest request
    ) {
        caseService.createPrescriptions(caseId, request);
        return ApiResponse.ok();
    }

    @GetMapping("/{caseId}/fees")
    @Operation(summary = "获取病案费用明细", description = "根据病案id获取挂号费、医疗项目费、处方药品费")
    public ApiResponse<CaseFeeDTO> listCaseFees(
            @Parameter(description = "病案ID", example = "1") @PathVariable("caseId") Integer caseId) {
        CaseFeeDTO dto = caseService.listCaseFees(caseId);
        return ApiResponse.ok(dto);
    }

    @GetMapping("/registrations/patients")
    @Operation(summary = "获取当前登录医生的挂号患者列表", description = "分页、模糊查询患者姓名或病历号")
    public ApiResponse<?> listDoctorRegisteredPatients(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword
    ) {
        List<DoctorPatientDTO> data = caseService.getRegisteredPatientsByDoctor(page, pageSize, keyword);
        long total = caseService.countRegisteredPatientsByDoctor(keyword);
        return ApiResponse.pageOk(data, page, pageSize, total);
    }
}
