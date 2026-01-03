package com.ncst.hospitaloutpatient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncst.hospitaloutpatient.model.dto.hr.StaffDetailResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StaffDetailResponseSerializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void staffDetailResponse_shouldContainStatus() throws Exception {
        StaffDetailResponse resp = new StaffDetailResponse();
        resp.setStaffId(1);
        resp.setStatus(0);

        String json = objectMapper.writeValueAsString(resp);
        assertThat(json).contains("\"status\":0");
    }
}

