package com.ncst.hospitaloutpatient.mapper.billing;

import com.ncst.hospitaloutpatient.model.dto.billing.ChargeItemResponse;
import com.ncst.hospitaloutpatient.model.dto.billing.ChargeSettleItemDTO;
import com.ncst.hospitaloutpatient.model.entity.outpatient.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChargeMapper {
    List<ChargeItemResponse> selectChargeItems(
            @Param("type") String type,
            @Param("keyword") String keyword,
            @Param("itemType") String itemType,
            @Param("drugCategory") Integer drugCategory,
            @Param("size") Integer size,
            @Param("offset") Integer offset,
            @Param("sortBy") String sortBy,
            @Param("order") String order
    );

    long countChargeItems(
            @Param("type") String type,
            @Param("keyword") String keyword,
            @Param("itemType") String itemType,
            @Param("drugCategory") Integer drugCategory
    );

    ChargeSettleItemDTO selectMedicalItemForSettle(@Param("applyId") Integer applyId);

    ChargeSettleItemDTO selectPrescriptionForSettle(@Param("applyId") Integer applyId);

    int insertTransaction(Transaction transaction);

    int updateMedicalItemApplyStatus(@Param("applyId") Integer applyId, @Param("status") String status);

    int updatePrescriptionStatus(@Param("prescriptionId") Integer prescriptionId, @Param("status") String status);

    int countUnpaidMedicalItem(@Param("registrationId") Integer registrationId);

    int countUnpaidPrescription(@Param("registrationId") Integer registrationId);

    int updatePatientVisitStatus(@Param("registrationId") Integer registrationId, @Param("status") String status);
}
