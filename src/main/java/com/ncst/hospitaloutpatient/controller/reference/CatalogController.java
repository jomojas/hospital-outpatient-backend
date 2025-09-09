package com.ncst.hospitaloutpatient.controller.reference;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.reference.DrugDetailResponse;
import com.ncst.hospitaloutpatient.model.dto.reference.MedicalItemResponse;
import com.ncst.hospitaloutpatient.service.reference.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/catalog")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    @Operation(summary = "获取所有检查项目", description = "分页获取所有检查项目")
    @GetMapping("/exam-items")
    public ApiResponse<List<MedicalItemResponse>> listExamItems(
            @Parameter(description = "页码，从1开始", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "关键字搜索（项目名称/编码）")
            @RequestParam(required = false) String keyword
    ) {
        List<MedicalItemResponse> items = catalogService.listExamItems(page, pageSize, keyword);
        int total = catalogService.countExamItems(keyword);
        return ApiResponse.pageOk(items, page, pageSize, total);
    }

    @Operation(summary = "获取所有检验项目", description = "分页获取所有检验项目")
    @GetMapping("/lab-items")
    public ApiResponse<List<MedicalItemResponse>> listLabItems(
            @Parameter(description = "页码，从1开始", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "关键字搜索（项目名称/编码）")
            @RequestParam(required = false) String keyword
    ) {
        List<MedicalItemResponse> items = catalogService.listLabItems(page, pageSize, keyword);
        int total = catalogService.countLabItems(keyword);
        return ApiResponse.pageOk(items, page, pageSize, total);
    }

    @Operation(summary = "获取所有处置项目", description = "分页获取所有处置项目")
    @GetMapping("/disposal-items")
    public ApiResponse<List<MedicalItemResponse>> listDisposalItems(
            @Parameter(description = "页码，从1开始", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "关键字搜索（项目名称/编码）")
            @RequestParam(required = false) String keyword
    ) {
        List<MedicalItemResponse> items = catalogService.listDisposalItems(page, pageSize, keyword);
        int total = catalogService.countDisposalItems(keyword);
        return ApiResponse.pageOk(items, page, pageSize, total);
    }

    @Operation(summary = "获取所有药品信息", description = "分页获取所有药品信息")
    @GetMapping("/drugs")
    public ApiResponse<List<DrugDetailResponse>> listDrugs(
            @Parameter(description = "页码，从1开始", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "关键字搜索（药品名称/编码）")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "药品类别ID")
            @RequestParam(required = false) Integer categoryId
    ) {
        List<DrugDetailResponse> items = catalogService.listDrugCatalog(page, pageSize, keyword, categoryId);
        int total = catalogService.countDrugCatalog(keyword, categoryId);
        return ApiResponse.pageOk(items, page, pageSize, total);
    }
}
