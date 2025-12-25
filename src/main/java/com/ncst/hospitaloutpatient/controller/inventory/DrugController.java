package com.ncst.hospitaloutpatient.controller.inventory;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.inventory.CreateDrugRequest;
import com.ncst.hospitaloutpatient.model.dto.inventory.UpdateDrugRequest;
import com.ncst.hospitaloutpatient.service.inventory.DrugService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/drugs")
public class DrugController {
    @Autowired
    private DrugService drugService;

    @Operation(summary = "添加药品")
    @PostMapping
    public ApiResponse<?> createDrug(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "药品信息", required = true)
            @RequestBody CreateDrugRequest request) {
        drugService.createDrug(request);
        return ApiResponse.ok();
    }

    @Operation(summary = "编辑药品信息")
    @PutMapping("/{id}")
    public ApiResponse<?> updateDrug(
            @PathVariable("id") Integer drugId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "药品信息", required = true)
            @RequestBody UpdateDrugRequest request) {
        drugService.updateDrug(drugId, request);
        return ApiResponse.ok();
    }

    @Operation(summary = "切换药品上下架状态", description = "status=1 时下架为 0；status=0 时上架为 1")
    @PatchMapping("/{id}/toggle")
    public ApiResponse<Void> toggleDrugStatus(
            @Parameter(description = "药品ID") @PathVariable("id") Integer drugId
    ) {
        drugService.toggleStatus(drugId);
        return ApiResponse.ok();
    }
}
