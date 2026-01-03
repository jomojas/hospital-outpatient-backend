package com.ncst.hospitaloutpatient;

import com.ncst.hospitaloutpatient.service.analytics.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class StatsServiceAutoRangeRevenueExtraTest {

    @Autowired
    private StatsService statsService;

    @Test
    void revenueByType_auto_withoutDates_shouldNotThrow() {
        assertThatCode(() -> statsService.revenueByType("auto", null, null))
                .doesNotThrowAnyException();
    }

    @Test
    void revenueByDepartment_auto_withoutDates_shouldNotThrow() {
        assertThatCode(() -> statsService.revenueByDepartment("auto", null, null))
                .doesNotThrowAnyException();
    }
}

