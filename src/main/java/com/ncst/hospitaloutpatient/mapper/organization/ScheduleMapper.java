package com.ncst.hospitaloutpatient.mapper.organization;

import com.ncst.hospitaloutpatient.model.dto.organization.DoctorScheduleResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface ScheduleMapper {

    /**
     * 查询某科室下所有医生的基础信息
     */
    List<DoctorScheduleResponse> selectDoctorsByDept(@Param("deptId") Integer deptId);

    /**
     * 查询指定医生列表在指定日期范围内的所有排班记录
     */
    List<DoctorScheduleResponse.DailySchedule> selectSchedulesByDoctorIds(
            @Param("staffIds") List<Integer> staffIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 查询单条排班记录 (用于判断是否存在)
     */
    Integer selectQuotaId(@Param("staffId") Integer staffId, @Param("date") LocalDate date);

    /**
     * 更新号源
     */
    int updateQuota(@Param("id") Integer id, @Param("quota") Integer quota);

    /**
     * 插入号源
     */
    int insertQuota(@Param("staffId") Integer staffId, @Param("date") LocalDate date, @Param("quota") Integer quota);

    /**
     * ✅ 新增：查询具体的排班数据，返回 Map 列表
     * 对应 XML 中的 <select id="selectSchedulesMap">
     */
    List<Map<String, Object>> selectSchedulesMap(
            @Param("staffIds") List<Integer> staffIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}