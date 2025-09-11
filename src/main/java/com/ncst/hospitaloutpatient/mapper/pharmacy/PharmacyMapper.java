package com.ncst.hospitaloutpatient.mapper.pharmacy;

import com.ncst.hospitaloutpatient.model.dto.pharmacy.DispensePendingDTO;
import com.ncst.hospitaloutpatient.model.dto.pharmacy.PharmacyRecordDTO;
import com.ncst.hospitaloutpatient.model.dto.pharmacy.PrescriptionInfo;
import com.ncst.hospitaloutpatient.model.entity.pharmacy.PharmacyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PharmacyMapper {
    List<DispensePendingDTO> listDispensePending(
            @Param("keyword") String keyword,
            @Param("sortBy") String sortBy,
            @Param("order") String order,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );
    long countDispensePending(@Param("keyword") String keyword);

    int updatePrescriptionStatusToFinished(@Param("prescriptionId") Integer prescriptionId);

    Integer selectRegistrationIdByPrescriptionId(@Param("prescriptionId") Integer prescriptionId);

    int countUnfinishedPrescriptions(@Param("registrationId") Integer registrationId);

    int updatePatientVisitStatusByRegistrationId(@Param("registrationId") Integer registrationId,
                                                 @Param("status") String status);

    String selectPrescriptionStatus(@Param("prescriptionId") Integer prescriptionId);

    int updatePrescriptionStatusToReturned(@Param("prescriptionId") Integer prescriptionId);

    // 新增：插入pharmacy_record
    int insertPharmacyRecord(PharmacyRecord record);

    // 新增：查询处方详情用于pharmacy_record
    PrescriptionInfo selectPrescriptionInfo(@Param("prescriptionId") Integer prescriptionId);

    int decreaseDrugStock(@Param("drugId") Integer drugId, @Param("quantity") BigDecimal quantity);

    // 回补药品库存
    int increaseDrugStock(@Param("drugId") Integer drugId, @Param("quantity") BigDecimal quantity);

    List<PharmacyRecordDTO> listPharmacyRecords(
            @Param("keyword") String keyword,
            @Param("type") String type,
            @Param("sortBy") String sortBy,
            @Param("order") String order,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    long countPharmacyRecords(
            @Param("keyword") String keyword,
            @Param("type") String type
    );
}
