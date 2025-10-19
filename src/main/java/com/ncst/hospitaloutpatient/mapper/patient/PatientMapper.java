package com.ncst.hospitaloutpatient.mapper.patient;

import com.ncst.hospitaloutpatient.model.dto.patient.PatientResponse;
import com.ncst.hospitaloutpatient.model.dto.patient.PatientRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PatientMapper {

    List<PatientResponse> searchPatient(@Param("name") String name, @Param("idCard") String idCard);

    int existsByIdCard(String idCard);

    int insertPatient(PatientRequest request);
}
