package com.ncst.hospitaloutpatient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncst.hospitaloutpatient.model.dto.analytics.RevenueTrendResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JacksonNamingTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void revenueTrendResponse_shouldSerializeAs_xAxis_not_xaxis() throws Exception {
        RevenueTrendResponse resp = new RevenueTrendResponse();
        resp.setXAxis(List.of("2025-01"));
        resp.setSeries(List.of(1.0));

        String json = objectMapper.writeValueAsString(resp);

        assertThat(json).contains("\"xAxis\"");
        assertThat(json).doesNotContain("\"xaxis\"");
    }
}

