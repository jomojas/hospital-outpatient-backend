package com.ncst.hospitaloutpatient.controller.reference;

import com.ncst.hospitaloutpatient.common.enums.DrugUnit;
import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.mapper.reference.DictionaryMapper;
import com.ncst.hospitaloutpatient.model.dto.reference.*;
import com.ncst.hospitaloutpatient.service.reference.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/dictionaries")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @Operation(
            summary = "获取所有结算方式",
            description = "获取所有结算方式字典"
    )
    @GetMapping("/settlement-categories")
    public ApiResponse<List<SettlementCategoryResponse>> listSettlementCategories() {
        List<SettlementCategoryResponse> settlementCategories = dictionaryService.listSettlementCategories();
        return ApiResponse.ok(settlementCategories);
    }

    @Operation(
            summary = "获取午别列表",
            description = "获取午别（上午/下午）"
    )
    @GetMapping("/noon-sessions")
    public ApiResponse<List<NoonSessionResponse>> listNoonSessions() {
        List<NoonSessionResponse> sessions = dictionaryService.listNoonSessions();
        return ApiResponse.ok(sessions);
    }

    @Operation(
            summary = "获取号别列表",
            description = "获取全部挂号号别（如普通号、专家号）"
    )
    @GetMapping("/number-types")
    public ApiResponse<List<NumberTypeResponse>> listNumberTypes() {
        List<NumberTypeResponse> numberTypes = dictionaryService.listNumberTypes();
        return ApiResponse.ok(numberTypes);
    }

    @Operation(
            summary = "获取收费方式列表",
            description = "获取所有收费方式"
    )
    @GetMapping("/payment-methods")
    public ApiResponse<List<PaymentMethodResponse>> listPaymentMethods() {
        List<PaymentMethodResponse> paymentMethods = dictionaryService.listPaymentMethods();
        return ApiResponse.ok(paymentMethods);
    }

    @Operation(summary = "获取所有药品类别", description = "返回所有药品类别")
    @GetMapping("/drug-categories")
    public ApiResponse<List<DrugCategoryResponse>> listDrugCategories() {
        List<DrugCategoryResponse> categories = dictionaryService.listDrugCategories();
        return ApiResponse.ok(categories);
    }

    @Operation(summary = "获取所有项目类别", description = "返回所有项目类别")
    @GetMapping("/item-types")
    public ApiResponse<List<ProjectTypeResponse>> listItemTypes() {
        List<ProjectTypeResponse> categories = dictionaryService.listItemTypes();
        return ApiResponse.ok(categories);
    }

    @GetMapping("/drug-units")
    @Operation(summary = "列举所有药品单位", description = "获取系统中所有药品单位的列表")
    public ApiResponse<List<DrugUnit>> listDrugUnits() {
        List<DrugUnit> drugUnits = dictionaryService.listDrugUnits();
        return ApiResponse.ok(drugUnits);
    }
}
