package com.ncst.hospitaloutpatient.controller.inventory;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.inventory.CreateDrugRequest;
import com.ncst.hospitaloutpatient.model.dto.inventory.UpdateDrugRequest;
import com.ncst.hospitaloutpatient.service.inventory.DrugService;
import io.swagger.v3.oas.annotations.Operation;
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
}
