package com.ncst.hospitaloutpatient.mapper.analytics;

import com.ncst.hospitaloutpatient.model.dto.analytics.RegistrationsByDepartmentDTO;
import com.ncst.hospitaloutpatient.model.dto.analytics.RegistrationsByDoctorDTO;
import com.ncst.hospitaloutpatient.model.dto.analytics.RegistrationsTypeBreakdownDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatsMapper {
    List<Map<String, Object>> selectRegistrationTrend(Map<String, Object> params);

    // 新增：查询数据库最早挂号时间（yyyy-MM-dd字符串，或null）
    String selectEarliestRegistrationDate();

    List<RegistrationsTypeBreakdownDTO> selectRegistrationTypeBreakdown(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    List<RegistrationsByDepartmentDTO> selectRegistrationsByDepartment(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    List<RegistrationsByDoctorDTO> selectRegistrationsByDoctor(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    // 收入趋势
    List<Map<String, Object>> selectRevenueTrend(Map<String, Object> params);

    // 查询最早收入日期（如无收入表可沿用挂号表最早日期）
    String selectEarliestRevenueDate();

    // 按类型统计收入
    List<Map<String, Object>> selectRevenueByType(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<Map<String, Object>> selectRevenueByDepartment(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<Map<String, Object>> selectRefundTrend(Map<String, Object> params);
}
