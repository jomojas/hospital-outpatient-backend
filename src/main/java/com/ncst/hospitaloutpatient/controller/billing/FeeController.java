package com.ncst.hospitaloutpatient.controller.billing;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.billing.FeeTransactionDTO;
import com.ncst.hospitaloutpatient.service.billing.FeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/fees")
public class FeeController {
    @Autowired
    private FeeService feeService;

    @GetMapping
    @Operation(summary = "分页查询费用交易记录", description = "按患者姓名、交易状态、时间区间等条件分页查询费用支付/退费记录")
    public ApiResponse<List<FeeTransactionDTO>> queryFees(
            @Parameter(description = "患者姓名，模糊查询", example = "张三") @RequestParam(required = false) String name,
            @Parameter(description = "交易状态，PAID=已支付，REFUND=已退费", example = "PAID") @RequestParam String status,
            @Parameter(description = "起始时间", example = "2025-09-01 00:00:00") @RequestParam(required = false) String startTime,
            @Parameter(description = "结束时间", example = "2025-09-10 23:59:59") @RequestParam(required = false) String endTime,
            @Parameter(description = "页码，从1开始", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "排序字段(name, transactionTime)", example = "transactionTime") @RequestParam(defaultValue = "transactionTime") String sortBy,
            @Parameter(description = "排序方式(asc, desc)", example = "desc") @RequestParam(defaultValue = "desc") String order
    ) {
        List<FeeTransactionDTO> items = feeService.queryFees(name, status, startTime, endTime, page, pageSize, sortBy, order);
        long total = feeService.countFees(name, status, startTime, endTime);
        return ApiResponse.pageOk(items, page, pageSize, total);
    }
}
