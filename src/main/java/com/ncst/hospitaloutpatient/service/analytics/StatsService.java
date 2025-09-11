package com.ncst.hospitaloutpatient.service.analytics;

import com.ncst.hospitaloutpatient.common.enums.Granularity;
import com.ncst.hospitaloutpatient.common.enums.StatisticsPeriod;
import com.ncst.hospitaloutpatient.mapper.analytics.StatsMapper;
import com.ncst.hospitaloutpatient.model.dto.analytics.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class StatsService {
    @Autowired
    private StatsMapper statsMapper;

    private static final Map<StatisticsPeriod, Granularity> PERIOD_GRANULARITY_MAP = new EnumMap<>(StatisticsPeriod.class);
    static {
        PERIOD_GRANULARITY_MAP.put(StatisticsPeriod.MONTH, Granularity.DAY);
        PERIOD_GRANULARITY_MAP.put(StatisticsPeriod.SEASON, Granularity.DAY);
        PERIOD_GRANULARITY_MAP.put(StatisticsPeriod.YEAR, Granularity.MONTH);
        PERIOD_GRANULARITY_MAP.put(StatisticsPeriod.ALL, Granularity.YEAR);
    }

    public Granularity resolveGranularity(StatisticsPeriod period, LocalDate startDate, LocalDate endDate) {
        if (period == StatisticsPeriod.AUTO) {
            long days = ChronoUnit.DAYS.between(startDate, endDate);
            if (days <= 90) {
                return Granularity.DAY;
            } else if (days <= 730) {
                return Granularity.MONTH;
            } else {
                return Granularity.YEAR;
            }
        }
        return PERIOD_GRANULARITY_MAP.get(period);
    }

    public static LocalDate[] getStartAndEndByPeriod(String period) {
        LocalDate today = LocalDate.now();
        LocalDate start;
        LocalDate end = today;
        switch (period) {
            case "month" -> start = today.minusMonths(1).plusDays(1);
            case "season" -> start = today.minusMonths(3).plusDays(1);
            case "year" -> start = today.minusYears(1).plusDays(1);
            case "all" -> start = null;
            default -> start = today;
        }
        return new LocalDate[]{start, end};
    }

    public static <T extends Number> Map<String, List<?>> fillDateGaps(
            LocalDate startDate, LocalDate endDate,
            List<String> xAxis, List<T> series,
            String granularity, Class<T> type) {
        Map<String, T> dateValueMap = new HashMap<>();
        for (int i = 0; i < xAxis.size(); i++) {
            dateValueMap.put(xAxis.get(i), series.get(i));
        }

        List<String> fullXAxis = new ArrayList<>();
        List<T> fullSeries = new ArrayList<>();
        T zeroValue = type == Double.class ? type.cast(0.0) : type.cast(0);

        if ("day".equalsIgnoreCase(granularity)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = startDate;
            while (!date.isAfter(endDate)) {
                String ds = date.format(formatter);
                fullXAxis.add(ds);
                fullSeries.add(dateValueMap.getOrDefault(ds, zeroValue));
                date = date.plusDays(1);
            }
        } else if ("month".equalsIgnoreCase(granularity)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            LocalDate date = LocalDate.of(startDate.getYear(), startDate.getMonth(), 1);
            LocalDate endMonth = LocalDate.of(endDate.getYear(), endDate.getMonth(), 1);
            while (!date.isAfter(endMonth)) {
                String ds = date.format(formatter);
                fullXAxis.add(ds);
                fullSeries.add(dateValueMap.getOrDefault(ds, zeroValue));
                date = date.plusMonths(1);
            }
        } else if ("year".equalsIgnoreCase(granularity)) {
            int startYear = startDate.getYear();
            int endYear = endDate.getYear();
            for (int y = startYear; y <= endYear; y++) {
                String ds = String.valueOf(y);
                fullXAxis.add(ds);
                fullSeries.add(dateValueMap.getOrDefault(ds, zeroValue));
            }
        } else {
            // 默认按天处理
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = startDate;
            while (!date.isAfter(endDate)) {
                String ds = date.format(formatter);
                fullXAxis.add(ds);
                fullSeries.add(dateValueMap.getOrDefault(ds, zeroValue));
                date = date.plusDays(1);
            }
        }

        Map<String, List<?>> result = new HashMap<>();
        result.put("xaxis", fullXAxis);
        result.put("series", fullSeries);
        return result;
    }

//    public static Map<String, List<?>> fillDateGaps(LocalDate startDate, LocalDate endDate,
//                                                    List<String> xAxis, List<Integer> series,
//                                                    String granularity) {
//        Map<String, Integer> dateValueMap = new HashMap<>();
//        for (int i = 0; i < xAxis.size(); i++) {
//            dateValueMap.put(xAxis.get(i), series.get(i));
//        }
//
//        List<String> fullXAxis = new ArrayList<>();
//        List<Integer> fullSeries = new ArrayList<>();
//
//        if ("day".equalsIgnoreCase(granularity)) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate date = startDate;
//            while (!date.isAfter(endDate)) {
//                String ds = date.format(formatter);
//                fullXAxis.add(ds);
//                fullSeries.add(dateValueMap.getOrDefault(ds, 0));
//                date = date.plusDays(1);
//            }
//        } else if ("month".equalsIgnoreCase(granularity)) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
//            LocalDate date = LocalDate.of(startDate.getYear(), startDate.getMonth(), 1);
//            LocalDate endMonth = LocalDate.of(endDate.getYear(), endDate.getMonth(), 1);
//            while (!date.isAfter(endMonth)) {
//                String ds = date.format(formatter);
//                fullXAxis.add(ds);
//                fullSeries.add(dateValueMap.getOrDefault(ds, 0));
//                date = date.plusMonths(1);
//            }
//        } else if ("year".equalsIgnoreCase(granularity)) {
//            int startYear = startDate.getYear();
//            int endYear = endDate.getYear();
//            for (int y = startYear; y <= endYear; y++) {
//                String ds = String.valueOf(y);
//                fullXAxis.add(ds);
//                fullSeries.add(dateValueMap.getOrDefault(ds, 0));
//            }
//        } else {
//            // 默认按天处理
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate date = startDate;
//            while (!date.isAfter(endDate)) {
//                String ds = date.format(formatter);
//                fullXAxis.add(ds);
//                fullSeries.add(dateValueMap.getOrDefault(ds, 0));
//                date = date.plusDays(1);
//            }
//        }
//
//        Map<String, List<?>> result = new HashMap<>();
//        result.put("xaxis", fullXAxis);
//        result.put("series", fullSeries);
//        return result;
//    }



    public RegistrationsTrendResponse registrationsTrend(String periodStr, String startDateStr, String endDateStr) {
        StatisticsPeriod period = StatisticsPeriod.valueOf(periodStr.toUpperCase());
        LocalDate startDate = (startDateStr != null && !startDateStr.isEmpty()) ? LocalDate.parse(startDateStr) : null;
        LocalDate endDate = (endDateStr != null && !endDateStr.isEmpty()) ? LocalDate.parse(endDateStr) : null;

        // period为all时，自动查最早挂号时间
        if (period == StatisticsPeriod.ALL) {
            if (startDate == null) {
                String earliestDateStr = statsMapper.selectEarliestRegistrationDate();
                if (earliestDateStr != null) {
                    startDate = LocalDate.parse(earliestDateStr);
                } else {
                    // 若无数据，默认用今天
                    startDate = LocalDate.now();
                }
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }
        } else {
            // 其它period用工具方法补齐
            if (startDate == null || endDate == null) {
                LocalDate[] range = getStartAndEndByPeriod(periodStr.toLowerCase());
                if (startDate == null) startDate = range[0];
                if (endDate == null) endDate = range[1];
            }
        }

        Granularity granularity = resolveGranularity(period, startDate, endDate);

        // 查询数据库
        Map<String, Object> params = new HashMap<>();
        params.put("granularity", granularity.name());
        params.put("startDate", startDate != null ? startDate.toString() : null);
        params.put("endDate", endDate != null ? endDate.toString() : null);
        List<Map<String, Object>> dbResult = statsMapper.selectRegistrationTrend(params);

        // 解析结果
        List<String> xAxis = new ArrayList<>();
        List<Integer> series = new ArrayList<>();
        for (Map<String, Object> row : dbResult) {
            xAxis.add((String) row.get("time"));
            series.add(((Number) row.get("num")).intValue());
        }

        // 补全缺失日期
        Map<String, List<?>> filled = fillDateGaps(
                startDate, endDate, xAxis, series, granularity.name().toLowerCase(), Integer.class
        );

        RegistrationsTrendResponse resp = new RegistrationsTrendResponse();
        resp.setXAxis((List<String>) filled.get("xaxis"));
        resp.setSeries((List<Integer>) filled.get("series"));
        return resp;
    }

    public List<RegistrationsTypeBreakdownDTO> registrationsTypeBreakdown(
            String period,
            String startDateStr,
            String endDateStr) {
        LocalDate startDate = null;
        LocalDate endDate = null;

        if ("auto".equalsIgnoreCase(period)) {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = LocalDate.parse(startDateStr);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = LocalDate.parse(endDateStr);
            }
        } else if ("all".equalsIgnoreCase(period)) {
            // 查最早挂号日期
            String earliestDateStr = statsMapper.selectEarliestRegistrationDate();
            startDate = (earliestDateStr != null) ? LocalDate.parse(earliestDateStr) : LocalDate.now();
            endDate = LocalDate.now();
        } else {
            LocalDate[] range = getStartAndEndByPeriod(period.toLowerCase());
            startDate = range[0];
            endDate = range[1];
        }

        String start = (startDate != null) ? startDate.toString() : null;
        String end = (endDate != null) ? endDate.toString() : null;

        return statsMapper.selectRegistrationTypeBreakdown(start, end);
    }

    public List<RegistrationsByDepartmentDTO> registrationsByDepartment(
            String period,
            String startDateStr,
            String endDateStr) {
        LocalDate startDate = null;
        LocalDate endDate = null;

        if ("auto".equalsIgnoreCase(period)) {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = LocalDate.parse(startDateStr);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = LocalDate.parse(endDateStr);
            }
        } else if ("all".equalsIgnoreCase(period)) {
            String earliestDateStr = statsMapper.selectEarliestRegistrationDate();
            startDate = (earliestDateStr != null) ? LocalDate.parse(earliestDateStr) : LocalDate.now();
            endDate = LocalDate.now();
        } else {
            LocalDate[] range = getStartAndEndByPeriod(period.toLowerCase());
            startDate = range[0];
            endDate = range[1];
        }

        String start = (startDate != null) ? startDate.toString() : null;
        String end = (endDate != null) ? endDate.toString() : null;

        return statsMapper.selectRegistrationsByDepartment(start, end);
    }

    public List<RegistrationsByDoctorDTO> registrationsByDoctor(
            String period,
            String startDateStr,
            String endDateStr) {
        LocalDate startDate = null;
        LocalDate endDate = null;

        if ("auto".equalsIgnoreCase(period)) {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = LocalDate.parse(startDateStr);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = LocalDate.parse(endDateStr);
            }
        } else if ("all".equalsIgnoreCase(period)) {
            String earliestDateStr = statsMapper.selectEarliestRegistrationDate();
            startDate = (earliestDateStr != null) ? LocalDate.parse(earliestDateStr) : LocalDate.now();
            endDate = LocalDate.now();
        } else {
            LocalDate[] range = getStartAndEndByPeriod(period.toLowerCase());
            startDate = range[0];
            endDate = range[1];
        }

        String start = (startDate != null) ? startDate.toString() : null;
        String end = (endDate != null) ? endDate.toString() : null;

        return statsMapper.selectRegistrationsByDoctor(start, end);
    }

    public RevenueTrendResponse revenueTrend(String periodStr, String startDateStr, String endDateStr) {
        StatisticsPeriod period = StatisticsPeriod.valueOf(periodStr.toUpperCase());
        LocalDate startDate = (startDateStr != null && !startDateStr.isEmpty()) ? LocalDate.parse(startDateStr) : null;
        LocalDate endDate = (endDateStr != null && !endDateStr.isEmpty()) ? LocalDate.parse(endDateStr) : null;

        // period为all时，自动查最早收入时间
        if (period == StatisticsPeriod.ALL) {
            if (startDate == null) {
                String earliestDateStr = statsMapper.selectEarliestRevenueDate();
                if (earliestDateStr != null) {
                    startDate = LocalDate.parse(earliestDateStr);
                } else {
                    startDate = LocalDate.now();
                }
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }
        } else if (!"AUTO".equalsIgnoreCase(periodStr)) {
            // 其它period用工具方法补齐
            if (startDate == null || endDate == null) {
                LocalDate[] range = getStartAndEndByPeriod(periodStr.toLowerCase());
                if (startDate == null) startDate = range[0];
                if (endDate == null) endDate = range[1];
            }
        }

        Granularity granularity = resolveGranularity(period, startDate, endDate);

        // 查询数据库
        Map<String, Object> params = new HashMap<>();
        params.put("granularity", granularity.name());
        params.put("startDate", startDate != null ? startDate.toString() : null);
        params.put("endDate", endDate != null ? endDate.toString() : null);
        List<Map<String, Object>> dbResult = statsMapper.selectRevenueTrend(params);

        // 解析结果
        List<String> xAxis = new ArrayList<>();
        List<Double> series = new ArrayList<>();
        for (Map<String, Object> row : dbResult) {
            xAxis.add((String) row.get("time"));
            series.add(((Number) row.get("num")).doubleValue());
        }

        // 补全缺失日期
        Map<String, List<?>> filled = fillDateGaps(
                startDate, endDate, xAxis, series, granularity.name().toLowerCase(), Double.class
        );

        RevenueTrendResponse resp = new RevenueTrendResponse();
        resp.setXAxis((List<String>) filled.get("xaxis"));
        resp.setSeries((List<Double>) filled.get("series"));
        return resp;
    }

    public RevenueByTypeResponse revenueByType(String periodStr, String startDateStr, String endDateStr) {
        StatisticsPeriod period = StatisticsPeriod.valueOf(periodStr.toUpperCase());
        LocalDate startDate = (startDateStr != null && !startDateStr.isEmpty()) ? LocalDate.parse(startDateStr) : null;
        LocalDate endDate = (endDateStr != null && !endDateStr.isEmpty()) ? LocalDate.parse(endDateStr) : null;

        // period为all时，自动查最早收入时间
        if (period == StatisticsPeriod.ALL) {
            if (startDate == null) {
                String earliestDateStr = statsMapper.selectEarliestRevenueDate();
                if (earliestDateStr != null) {
                    startDate = LocalDate.parse(earliestDateStr);
                } else {
                    startDate = LocalDate.now();
                }
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }
        } else if (!"AUTO".equalsIgnoreCase(periodStr)) {
            // 其它period用工具方法补齐
            if (startDate == null || endDate == null) {
                LocalDate[] range = getStartAndEndByPeriod(periodStr.toLowerCase());
                if (startDate == null) startDate = range[0];
                if (endDate == null) endDate = range[1];
            }
        }

        List<Map<String, Object>> dbResult = statsMapper.selectRevenueByType(startDate.toString(), endDate.toString());

        // 补全三种类型
        List<String> allTypes = Arrays.asList("REGISTRATION", "MEDICAL_ITEM", "DRUG");
        Map<String, Double> typeToAmount = new HashMap<>();
        for (Map<String, Object> row : dbResult) {
            String type = (String) row.get("type");
            Double amount = row.get("amount") == null ? 0.0 : ((Number) row.get("amount")).doubleValue();
            typeToAmount.put(type, amount);
        }

        List<RevenueByTypeResponse.RevenueTypeItem> items = new ArrayList<>();
        for (String type : allTypes) {
            RevenueByTypeResponse.RevenueTypeItem item = new RevenueByTypeResponse.RevenueTypeItem();
            item.setType(type);
            item.setAmount(typeToAmount.getOrDefault(type, 0.0));
            items.add(item);
        }

        RevenueByTypeResponse resp = new RevenueByTypeResponse();
        resp.setItems(items);
        return resp;
    }

    public RevenueByDepartmentResponse revenueByDepartment(String periodStr, String startDateStr, String endDateStr) {
        StatisticsPeriod period = StatisticsPeriod.valueOf(periodStr.toUpperCase());
        LocalDate startDate = (startDateStr != null && !startDateStr.isEmpty()) ? LocalDate.parse(startDateStr) : null;
        LocalDate endDate = (endDateStr != null && !endDateStr.isEmpty()) ? LocalDate.parse(endDateStr) : null;

        // period为all时，自动查最早收入时间
        if (period == StatisticsPeriod.ALL) {
            if (startDate == null) {
                String earliestDateStr = statsMapper.selectEarliestRevenueDate();
                if (earliestDateStr != null) {
                    startDate = LocalDate.parse(earliestDateStr);
                } else {
                    startDate = LocalDate.now();
                }
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }
        } else if (!"AUTO".equalsIgnoreCase(periodStr)) {
            // 其它period用工具方法补齐
            if (startDate == null || endDate == null) {
                LocalDate[] range = getStartAndEndByPeriod(periodStr.toLowerCase());
                if (startDate == null) startDate = range[0];
                if (endDate == null) endDate = range[1];
            }
        }

        List<Map<String, Object>> dbResult = statsMapper.selectRevenueByDepartment(startDate.toString(), endDate.toString());

        List<RevenueByDepartmentResponse.DepartmentRevenueItem> items = new ArrayList<>();
        for (Map<String, Object> row : dbResult) {
            RevenueByDepartmentResponse.DepartmentRevenueItem item = new RevenueByDepartmentResponse.DepartmentRevenueItem();
            item.setDepartmentId((Integer) row.get("department_id"));
            item.setDepartmentName((String) row.get("department_name"));
            item.setAmount(row.get("amount") == null ? 0.0 : ((Number) row.get("amount")).doubleValue());
            items.add(item);
        }

        RevenueByDepartmentResponse resp = new RevenueByDepartmentResponse();
        resp.setItems(items);
        return resp;
    }

    public RefundTrendResponse refundTrend(String periodStr, String startDateStr, String endDateStr) {
        StatisticsPeriod period = StatisticsPeriod.valueOf(periodStr.toUpperCase());
        LocalDate startDate = (startDateStr != null && !startDateStr.isEmpty()) ? LocalDate.parse(startDateStr) : null;
        LocalDate endDate = (endDateStr != null && !endDateStr.isEmpty()) ? LocalDate.parse(endDateStr) : null;

        // period为all时，自动查最早收入时间
        if (period == StatisticsPeriod.ALL) {
            if (startDate == null) {
                String earliestDateStr = statsMapper.selectEarliestRevenueDate();
                if (earliestDateStr != null) {
                    startDate = LocalDate.parse(earliestDateStr);
                } else {
                    startDate = LocalDate.now();
                }
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }
        } else {
            // 其它period用工具方法补齐
            if (startDate == null || endDate == null) {
                LocalDate[] range = getStartAndEndByPeriod(periodStr.toLowerCase());
                if (startDate == null) startDate = range[0];
                if (endDate == null) endDate = range[1];
            }
        }

        // 1. 计算粒度（日、月、年等）
        Granularity granularity = resolveGranularity(period, startDate, endDate);

        // 2. 组装参数并查询
        Map<String, Object> params = new HashMap<>();
        params.put("granularity", granularity.name());
        params.put("startDate", startDate != null ? startDate.toString() : null);
        params.put("endDate", endDate != null ? endDate.toString() : null);
        List<Map<String, Object>> dbResult = statsMapper.selectRefundTrend(params);

        // 3. 解析结果
        List<String> xAxis = new ArrayList<>();
        List<Double> series = new ArrayList<>();
        for (Map<String, Object> row : dbResult) {
            // 注意数据库返回的date列类型，保险起见用toString
            xAxis.add(row.get("date").toString());
            series.add(row.get("amount") == null ? 0.0 : ((Number) row.get("amount")).doubleValue());
        }

        // 4. 补全缺失日期
        Map<String, List<?>> filled = fillDateGaps(
                startDate, endDate, xAxis, series, granularity.name().toLowerCase(), Double.class
        );

        // 5. 封装返回
        RefundTrendResponse resp = new RefundTrendResponse();
        resp.setXAxis((List<String>) filled.get("xaxis"));
        resp.setSeries((List<Double>) filled.get("series"));
        return resp;
    }
}
