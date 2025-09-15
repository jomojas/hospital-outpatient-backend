package com.ncst.hospitaloutpatient.mapper.outpatient;

import com.ncst.hospitaloutpatient.model.dto.outpatient.CreateRegistrationRequest;
import com.ncst.hospitaloutpatient.model.dto.outpatient.RegistrationDetailResponse;
import com.ncst.hospitaloutpatient.model.entity.outpatient.PatientVisit;
import com.ncst.hospitaloutpatient.model.entity.outpatient.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RegistrationMapper {

    int insertRegistration(CreateRegistrationRequest request);

    int insertPatientVisit(PatientVisit patientVisit);

    int incrementDoctorQuotaUsed(@Param("staffId") Integer staffId, @Param("quotaDate") LocalDate quotaDate);

    int insertTransaction(Transaction transaction);

    RegistrationDetailResponse selectRegistrationById(@Param("registrationId") Integer registrationId);

    List<RegistrationDetailResponse> selectRegistrations(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("date") LocalDate date,
            @Param("deptId") Integer deptId,
            @Param("doctorId") Integer doctorId,
            @Param("status") String status,
            @Param("keyword") String keyword,
            @Param("sortBy") String sortBy,
            @Param("order") String order
    );


    long countRegistrations(
            @Param("date") LocalDate date,
            @Param("deptId") Integer deptId,
            @Param("doctorId") Integer doctorId,
            @Param("status") String status,
            @Param("keyword") String keyword
    );

    int updatePatientVisitStatusToFinished(@Param("registrationId") Integer registrationId);

    String selectMaxPatientNo();

    String getDepartmentNameById(Integer departmentId);
    String getDoctorNameById(Integer doctorId);
    String getSettlementTypeNameById(Integer settlementTypeId);
    String getPaymentMethodNameById(Integer paymentMethodId);
}
