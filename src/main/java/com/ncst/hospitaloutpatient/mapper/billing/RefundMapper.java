package com.ncst.hospitaloutpatient.mapper.billing;

import com.ncst.hospitaloutpatient.model.dto.billing.ChargeRefundableItemResponse;
import com.ncst.hospitaloutpatient.model.dto.billing.ChargeSettleItemDTO;
import com.ncst.hospitaloutpatient.model.entity.billing.Registration;
import com.ncst.hospitaloutpatient.model.entity.outpatient.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RefundMapper {
    List<ChargeRefundableItemResponse> selectRefundableItems(
            @Param("type") String type,
            @Param("keyword") String keyword,
            @Param("itemType") String itemType,
            @Param("drugCategory") Integer drugCategory,
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("sortBy") String sortBy,
            @Param("order") String order);

    long countRefundableItems(
            @Param("type") String type,
            @Param("keyword") String keyword,
            @Param("itemType") String itemType,
            @Param("drugCategory") Integer drugCategory);

    String selectPatientVisitStatus(@Param("registrationId") Integer registrationId);

    boolean hasPayTransactionForMedicalItem(@Param("item") ChargeSettleItemDTO item);

    boolean hasRefundTransactionForMedicalItem(@Param("item") ChargeSettleItemDTO item);

    void updateMedicalItemApplyStatus(@Param("applyId") Integer applyId, @Param("status") String status);

    boolean hasPayTransactionForDrug(@Param("item") ChargeSettleItemDTO item);

    boolean hasRefundTransactionForDrug(@Param("item") ChargeSettleItemDTO item);

    void updatePrescriptionStatus(@Param("applyId") Integer applyId, @Param("status") String status);

    boolean allItemsAndDrugsFinishedOrCancelled(@Param("registrationId") Integer registrationId);

    int insertTransaction(Transaction tx);

    void updateStatus(@Param("registrationId") Integer registrationId, @Param("status") String status);

    Registration selectRegistrationById(@Param("registrationId") Integer registrationId);
}
