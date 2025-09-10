package com.ncst.hospitaloutpatient.controller.billing;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.billing.ChargeRefundableItemResponse;
import com.ncst.hospitaloutpatient.model.dto.billing.ChargeSettleItemDTO;
import com.ncst.hospitaloutpatient.service.billing.RefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/refunds")
public class RefundController {

    @Autowired
    private RefundService refundService;

    @Operation(summary = "获取所有可退费项目及药品")
    @GetMapping("/items")
    public ApiResponse<List<ChargeRefundableItemResponse>> listRefundableItems(
            @Parameter(description = "类型（ITEM-医疗项目, DRUG-药品）") @RequestParam(required = false) String type,
            @Parameter(description = "关键字（患者名/项目名/药品名）") @RequestParam(required = false) String keyword,
            @Parameter(description = "项目类型(EXAM/LAB/DISPOSAL)") @RequestParam(required = false) String itemType,
            @Parameter(description = "药品类别ID") @RequestParam(required = false) Integer drugCategory,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "排序字段(totalAmount/chargeTime)") @RequestParam(required = false) String sortBy,
            @Parameter(description = "排序方式(asc/desc)") @RequestParam(required = false) String order
    ) {
        List<ChargeRefundableItemResponse> items = refundService.listRefundableItems(
                type, keyword, itemType, drugCategory, page, pageSize, sortBy, order
        );
        long total = refundService.countRefundableItems(type, keyword, itemType, drugCategory);
        return ApiResponse.pageOk(items, page, pageSize, total);
    }

    @Operation(summary = "退费操作", description = "根据请求中的项目列表对每一个项目进行退费")
    @PostMapping
    public ApiResponse<?> refund(
            @RequestBody @Valid List<ChargeSettleItemDTO> refundItems) {
        refundService.refund(refundItems);
        return ApiResponse.ok();
    }
}
