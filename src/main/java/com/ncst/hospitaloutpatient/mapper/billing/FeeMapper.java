package com.ncst.hospitaloutpatient.mapper.billing;

import com.ncst.hospitaloutpatient.model.dto.billing.FeeTransactionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeeMapper {
    List<FeeTransactionDTO> selectFees(
            @Param("name") String name,
            @Param("status") String status,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("sortBy") String sortBy,
            @Param("order") String order,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );

    long countFees(
            @Param("name") String name,
            @Param("status") String status,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );
}
