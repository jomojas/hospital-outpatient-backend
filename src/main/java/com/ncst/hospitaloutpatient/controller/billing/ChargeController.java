package com.ncst.hospitaloutpatient.controller.billing;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.billing.ChargeItemResponse;
import com.ncst.hospitaloutpatient.model.dto.billing.ChargeSettleRequestDTO;
import com.ncst.hospitaloutpatient.service.billing.ChargeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/charges")
public class ChargeController {
    @Autowired
    private ChargeService chargeService;

    @Operation(summary = "获取所有需缴费项目及药品")
    @GetMapping("/items")
    public ApiResponse<List<ChargeItemResponse>> listChargeItems(
            @Parameter(description = "类型（ITEM-医疗项目, DRUG-药品）") @RequestParam(required = false) String type,
            @Parameter(description = "关键字（患者名/项目名/药品名）") @RequestParam(required = false) String keyword,
            @Parameter(description = "项目类型(EXAM/LAB/DISPOSAL)") @RequestParam(required = false) String itemType,
            @Parameter(description = "药品类别ID") @RequestParam(required = false) Integer drugCategory,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "排序字段(totalAmount/createTime)") @RequestParam(required = false) String sortBy,
            @Parameter(description = "排序方式(asc/desc)") @RequestParam(required = false) String order
    ) {
        List<ChargeItemResponse> items = chargeService.listChargeItems(
                type, keyword, itemType, drugCategory, page, pageSize, sortBy, order
        );
        long total = chargeService.countChargeItems(type, keyword, itemType, drugCategory);
        return ApiResponse.pageOk(items, page, pageSize, total);
    }

    @Operation(summary = "结算缴费项目", description = "批量结算选中的待缴费项目/药品")
    @PostMapping("/settle")
    public ApiResponse<Void> settleCharges(
            @RequestBody ChargeSettleRequestDTO request
    ) {
        chargeService.settleCharges(request);
        return ApiResponse.ok();
    }
}
