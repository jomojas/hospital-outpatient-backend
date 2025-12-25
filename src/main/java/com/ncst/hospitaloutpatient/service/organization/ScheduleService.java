package com.ncst.hospitaloutpatient.service.organization;

import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.organization.ScheduleMapper;
import com.ncst.hospitaloutpatient.model.dto.organization.BatchSetQuotaRequest;
import com.ncst.hospitaloutpatient.model.dto.organization.CopyScheduleRequest;
import com.ncst.hospitaloutpatient.model.dto.organization.DoctorScheduleResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.ScheduleQueryDTO;
import com.ncst.hospitaloutpatient.model.dto.organization.SetQuotaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    /**
     * 查询排班矩阵
     */
    public List<DoctorScheduleResponse> listSchedules(ScheduleQueryDTO query) {
        // 1. 校验时间跨度 (防止查太多数据)
        long days = ChronoUnit.DAYS.between(query.getStartDate(), query.getEndDate());
        if (days > 31) {
            throw new BusinessException(400, "查询时间跨度不能超过31天");
        }

        // 2. 先查该科室下的所有医生
        List<DoctorScheduleResponse> doctors = scheduleMapper.selectDoctorsByDept(query.getDepartmentId());
        if (doctors.isEmpty()) {
            return new ArrayList<>();
        }

        // 3. 提取所有医生ID
        List<Integer> staffIds = doctors.stream()
                .map(DoctorScheduleResponse::getStaffId)
                .collect(Collectors.toList());

        // 4. 查询这些医生在指定时间段内的排班记录
        // 这里的 RawSchedule 建议单独定义一个简单的类，包含 staffId, date, quota, used
        List<Map<String, Object>> rawSchedules = scheduleMapper.selectSchedulesMap(staffIds, query.getStartDate(), query.getEndDate());

        // 5. 内存组装 (Grouping)
        // 将扁平的排班记录按 staffId 分组
        Map<Integer, List<DoctorScheduleResponse.DailySchedule>> scheduleMap = rawSchedules.stream()
                .collect(Collectors.groupingBy(
                        m -> (Integer) m.get("staff_id"),
                        Collectors.mapping(m -> {
                            DoctorScheduleResponse.DailySchedule ds = new DoctorScheduleResponse.DailySchedule();
                            ds.setId((Integer) m.get("id"));
                            ds.setDate(((java.sql.Date) m.get("quota_date")).toLocalDate());
                            ds.setQuota((Integer) m.get("quota"));
                            ds.setUsed((Integer) m.get("used"));
                            return ds;
                        }, Collectors.toList())
                ));

        // 6. 填充回医生列表
        for (DoctorScheduleResponse doctor : doctors) {
            doctor.setSchedules(scheduleMap.getOrDefault(doctor.getStaffId(), new ArrayList<>()));
        }

        return doctors;
    }

    /**
     * 设置号源
     */
    @Transactional(rollbackFor = Exception.class)
    public void setQuota(SetQuotaRequest request) {
        // 1. 检查是否存在
        Integer existId = scheduleMapper.selectQuotaId(request.getStaffId(), request.getDate());

        if (existId != null) {
            // 更新
            scheduleMapper.updateQuota(existId, request.getQuota());
        } else {
            // 插入 (忽略 quota=0 的情况，0表示不排班，没必要插一条0的记录)
            if (request.getQuota() > 0) {
                scheduleMapper.insertQuota(request.getStaffId(), request.getDate(), request.getQuota());
            }
        }
    }

    /**
     * 批量设置号源。
     * 要求：复用 setQuota，不新增 SQL。
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchSetQuota(BatchSetQuotaRequest req) {
        if (req.getStartDate().isAfter(req.getEndDate())) {
            throw new BusinessException(400, "startDate不能晚于endDate");
        }
        if (req.getQuota() < 0) {
            throw new BusinessException(400, "quota不能为负数");
        }

        Set<Integer> weekFilter = null;
        if (req.getWeekDays() != null && !req.getWeekDays().isEmpty()) {
            // 简单合法性校验：允许 1..7
            for (Integer d : req.getWeekDays()) {
                if (d == null || d < 1 || d > 7) {
                    throw new BusinessException(400, "weekDays取值范围应为1-7");
                }
            }
            weekFilter = Set.copyOf(req.getWeekDays());
        }

        LocalDate current = req.getStartDate();
        while (!current.isAfter(req.getEndDate())) {
            int dayOfWeek = current.getDayOfWeek().getValue(); // 1=Mon..7=Sun
            if (weekFilter == null || weekFilter.contains(dayOfWeek)) {
                SetQuotaRequest one = new SetQuotaRequest();
                one.setStaffId(req.getStaffId());
                one.setDate(current);
                one.setQuota(req.getQuota());
                this.setQuota(one);
            }
            current = current.plusDays(1);
        }
    }

    /**
     * 复制排班。
     * 规则：sourceStartDate/targetStartDate 都是周一。
     * 数据来源：复用 mapper.selectSchedulesMap。
     * 落库：复用 this.setQuota。
     */
    @Transactional(rollbackFor = Exception.class)
    public void copySchedule(CopyScheduleRequest req) {
        LocalDate sourceStart = req.getSourceStartDate();
        LocalDate sourceEnd = sourceStart.plusDays(6);

        // 1) 查询科室下所有医生
        List<DoctorScheduleResponse> doctors = scheduleMapper.selectDoctorsByDept(req.getDepartmentId());
        if (doctors.isEmpty()) {
            return;
        }
        List<Integer> staffIds = doctors.stream().map(DoctorScheduleResponse::getStaffId).toList();

        // 2) 取源周排班数据
        List<Map<String, Object>> raw = scheduleMapper.selectSchedulesMap(staffIds, sourceStart, sourceEnd);
        if (raw.isEmpty()) {
            return;
        }

        long daysDiff = ChronoUnit.DAYS.between(sourceStart, req.getTargetStartDate());

        // 3) 应用到目标周
        for (Map<String, Object> row : raw) {
            Integer staffId = (Integer) row.get("staff_id");
            if (staffId == null) continue;

            Object quotaObj = row.get("quota");
            Integer quota = null;
            if (quotaObj instanceof Integer i) {
                quota = i;
            } else if (quotaObj instanceof Number n) {
                quota = n.intValue();
            }
            if (quota == null) continue;

            LocalDate srcDate;
            Object dateObj = row.get("quota_date");
            if (dateObj instanceof java.sql.Date d) {
                srcDate = d.toLocalDate();
            } else if (dateObj instanceof java.util.Date d) {
                srcDate = new java.sql.Date(d.getTime()).toLocalDate();
            } else {
                continue;
            }

            LocalDate targetDate = srcDate.plusDays(daysDiff);

            SetQuotaRequest one = new SetQuotaRequest();
            one.setStaffId(staffId);
            one.setDate(targetDate);
            one.setQuota(quota);
            this.setQuota(one);
        }
    }
}