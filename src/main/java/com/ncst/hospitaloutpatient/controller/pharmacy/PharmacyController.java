package com.ncst.hospitaloutpatient.controller.pharmacy;

import com.ncst.hospitaloutpatient.common.response.ApiResponse;
import com.ncst.hospitaloutpatient.model.dto.pharmacy.DispenseDrugsRequest;
import com.ncst.hospitaloutpatient.model.dto.pharmacy.DispensePendingDTO;
import com.ncst.hospitaloutpatient.model.dto.pharmacy.PharmacyRecordDTO;
import com.ncst.hospitaloutpatient.service.pharmacy.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pharmacy")
public class PharmacyController {
    @Autowired
    private PharmacyService pharmacyService;

    @Operation(summary = "获取所有待发药处方")
    @GetMapping("/dispense/pending")
    public ApiResponse<?> listDispensePending(
            @Parameter(description = "关键字（患者姓名/药品名）") @RequestParam(required = false) String keyword,
            @Parameter(description = "排序字段", example = "patientName, prescribeTime") @RequestParam(defaultValue = "prescribeTime") String sortBy,
            @Parameter(description = "排序顺序", example = "desc, asc") @RequestParam(defaultValue = "desc") String order,
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") int pageSize) {

        List<DispensePendingDTO> list = pharmacyService.listDispensePending(keyword, sortBy, order, page, pageSize);
        long total = pharmacyService.countDispensePending(keyword);
        return ApiResponse.pageOk(list, page, pageSize, total);
    }


    @Operation(summary = "发药（支持批量）")
    @PostMapping("/dispense")
    public ApiResponse<?> dispenseDrugs(@RequestBody DispenseDrugsRequest request) {
        pharmacyService.dispenseDrugs(request);
        return ApiResponse.ok();
    }

//    @Operation(
//            summary = "退药（支持批量）",
//            description = "批量退药，将处方状态置为RETURNED。"
//    )
//    @PostMapping("/return")
//    public ApiResponse<?> returnDrugs(
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "批量退药请求体，包含所有要退药的处方ID和患者编号",
//                    required = true
//            )
//            @RequestBody DispenseDrugsRequest request
//    ) {
//        pharmacyService.returnDrugs(request);
//        return ApiResponse.ok();
//    }

    @Operation(summary = "获取所有药房操作记录")
    @GetMapping("/records")
    public ApiResponse<?> listPharmacyRecords(
            @Parameter(description = "关键字（患者姓名/药品名）") @RequestParam(required = false) String keyword,
            @Parameter(description = "操作类型", example = "DISPENSE, RETURN") @RequestParam(required = false) String type,
            @Parameter(description = "排序字段", example = "patientName, amount, operateTime") @RequestParam(defaultValue = "operate_time") String sortBy,
            @Parameter(description = "排序顺序", example = "desc, asc") @RequestParam(defaultValue = "desc") String order,
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") int pageSize) {

        List<PharmacyRecordDTO> list = pharmacyService.listPharmacyRecords(keyword, type, sortBy, order, page, pageSize);
        long total = pharmacyService.countPharmacyRecords(keyword, type);
        return ApiResponse.pageOk(list, page, pageSize, total);
    }
}
