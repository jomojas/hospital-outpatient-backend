package com.ncst.hospitaloutpatient;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ncst.hospitaloutpatient.mapper")
public class HospitalOutpatientApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalOutpatientApplication.class, args);
    }

}
