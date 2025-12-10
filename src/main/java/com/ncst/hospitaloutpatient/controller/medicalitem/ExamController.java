package com.ncst.hospitaloutpatient.controller.medicalitem;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.medicalitem.*;
import com.ncst.hospitaloutpatient.service.medicalitem.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/exam")
public class ExamController {
    @Autowired
    private ExamService examService;

    @GetMapping("/applies")
    @Operation(summary = "分页查询检查申请")
    public ApiResponse<List<ExamApplyDTO>> listExamApplies(
            @Parameter(description = "关键字(患者姓名或项目名称)") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "排序字段(patientName, applyTime)", example = "applyTime") @RequestParam(defaultValue = "applyTime") String sortBy,
            @Parameter(description = "排序顺序(desc/asc)", example = "desc") @RequestParam(defaultValue = "desc") String order
    ) {
        List<ExamApplyDTO> applies = examService.listExamApplies(keyword, page, pageSize, sortBy, order);
        long total = examService.countExamApplies(keyword);
        return ApiResponse.pageOk(applies, page, pageSize, total);
    }

//    @PostMapping("/applies/{applyId}/cancel")
//    @Operation(
//            summary = "取消医疗项目申请",
//            description = "只有在patient_visit.current_status为WAITING_FOR_CHECKUP时才能取消，取消后项目状态变为CANCELLED"
//    )
//    public ApiResponse<?> cancelExamApply(
//            @Parameter(description = "医疗项目申请ID", required = true)
//            @PathVariable Integer applyId) {
//        examService.cancelExamApply(applyId);
//        return ApiResponse.ok();
//    }

    @PostMapping("/applies/{applyId}/execute")
    @Operation(
            summary = "执行医疗项目",
            description = "根据applyId执行医疗项目，若patient_visit.current_status不是CHECKING则自动切换为CHECKING，并插入医疗项目操作日志。"
    )
    public ApiResponse<?> executeExam(
            @Parameter(description = "医疗项目申请ID", required = true) @PathVariable Integer applyId
    ) {
        examService.executeExam(applyId);
        return ApiResponse.ok();
    }

    @PostMapping("/applies/{applyId}/result")
    @Operation(summary = "提交检查项目结果", description = "仅UNFINISHED状态可提交，写结果、写操作日志、项目设为FINISHED，所有项目完成后将就诊状态设为WAITING_FOR_REVISIT")
    public ApiResponse<?> submitExamResult(
            @Parameter(description = "检查申请ID", required = true) @PathVariable Integer applyId,
            @RequestBody ItemApplyResultDTO resultDTO
    ) {
        examService.submitExamResult(applyId, resultDTO);
        return ApiResponse.ok();
    }

    @GetMapping("/departments")
    @Operation(summary = "获取所有检查科室", description = "返回所有type为EXAM的科室列表")
    public ApiResponse<List<DepartmentSimpleDTO>> listExamDepartments() {
        List<DepartmentSimpleDTO> list = examService.listExamDepartments();
        return ApiResponse.ok(list);
    }

    @GetMapping("/staffs/{departmentId}")
    @Operation(summary = "获取检查科室员工列表", description = "根据科室ID获取该科室下的所有员工信息")
    public ApiResponse<List<StaffSimpleDTO>> listExamStaffs(
            @Parameter(description = "科室ID", required = true) @PathVariable Integer departmentId) {
        List<StaffSimpleDTO> list = examService.listStaffsByDepartmentId(departmentId);
        return ApiResponse.ok(list);
    }


    @GetMapping("/records")
    @Operation(summary = "分页获取当前员工经手的检查/检验/处置项目")
    public ApiResponse<List<MedicalItemOperateLogDTO>> listExamRecords(
            @Parameter(description = "关键字(患者姓名或项目名称)") @RequestParam(required = false) String keyword,
            @Parameter(description = "操作类型(EXECUTE/INPUT_RESULT)") @RequestParam(required = false) String operateType,
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "排序字段(patientName, operateTime)", example = "operateTime") @RequestParam(defaultValue = "operateTime") String sortBy,
            @Parameter(description = "排序顺序(desc/asc)", example = "desc") @RequestParam(defaultValue = "desc") String order
    ) {
        List<MedicalItemOperateLogDTO> records = examService.listOperateLogs(keyword, operateType, page, pageSize, sortBy, order);
        long total = examService.countOperateLogs(keyword, operateType);
        return ApiResponse.pageOk(records, page, pageSize, total);
    }
}
