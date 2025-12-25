package com.ncst.hospitaloutpatient.controller.medicalitem;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.medicalitem.CreateMedicalItemRequest;
import com.ncst.hospitaloutpatient.model.dto.medicalitem.UpdateMedicalItemRequest;
import com.ncst.hospitaloutpatient.model.dto.reference.MedicalItemResponse;
import com.ncst.hospitaloutpatient.service.medicalitem.MedicalItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/items") // 或者建议改为 "api/medical-items" 更清晰
public class MedicalItemController {

    @Autowired
    private MedicalItemService medicalItemService;

    @Operation(summary = "查询医疗项目列表", description = "支持按关键字、状态、科室筛选")
    @GetMapping
    public ApiResponse<List<MedicalItemResponse>> listItems(
            @Parameter(description = "关键字(编码/名称)") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态(1启用/0停用)") @RequestParam(required = false) Integer status,
            @Parameter(description = "科室ID") @RequestParam(required = false) Integer departmentId,
            @Parameter(description = "页码(从1开始)") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        List<MedicalItemResponse> list = medicalItemService.listItems(keyword, status, departmentId, page, pageSize);
        long total = medicalItemService.countItems(keyword, status, departmentId);
        return ApiResponse.pageOk(list, page, pageSize, total);
    }

    @Operation(summary = "新增医疗项目")
    @PostMapping
    public ApiResponse<Void> createItem(
            @Valid @RequestBody CreateMedicalItemRequest request
    ) {
        medicalItemService.createItem(request);
        return ApiResponse.ok();
    }

    @Operation(summary = "更新医疗项目信息")
    @PutMapping("/{itemId}")
    public ApiResponse<Void> updateItem(
            @Parameter(description = "项目ID") @PathVariable Integer itemId,
            @Valid @RequestBody UpdateMedicalItemRequest request
    ) {
        medicalItemService.updateItem(itemId, request);
        return ApiResponse.ok();
    }

    @Operation(summary = "切换项目状态", description = "启用/停用项目")
    @PatchMapping("/{itemId}/toggle")
    public ApiResponse<Void> toggleItemStatus(
            @Parameter(description = "项目ID") @PathVariable Integer itemId
    ) {
        medicalItemService.toggleStatus(itemId);
        return ApiResponse.ok();
    }
}