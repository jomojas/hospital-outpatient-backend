package com.ncst.hospitaloutpatient.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        String[] rawPassword = {"000000", "111111", "222222", "333333", "444444", "555555", "666666", "777777"};
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (String s : rawPassword) {
            String hash = encoder.encode(s);
            System.out.println("加密后的hash值：" + hash);
        }
    }
}