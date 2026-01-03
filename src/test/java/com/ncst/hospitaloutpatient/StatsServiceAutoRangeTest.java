package com.ncst.hospitaloutpatient;

import com.ncst.hospitaloutpatient.service.analytics.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class StatsServiceAutoRangeTest {

    @Autowired
    private StatsService statsService;

    @Test
    void registrationsTrend_auto_withoutDates_shouldNotThrow() {
        assertThatCode(() -> statsService.registrationsTrend("auto", null, null))
                .doesNotThrowAnyException();
    }

    @Test
    void revenueTrend_auto_withoutDates_shouldNotThrow() {
        assertThatCode(() -> statsService.revenueTrend("auto", null, null))
                .doesNotThrowAnyException();
    }

    @Test
    void refundTrend_auto_withoutDates_shouldNotThrow() {
        assertThatCode(() -> statsService.refundTrend("auto", null, null))
                .doesNotThrowAnyException();
    }
}

