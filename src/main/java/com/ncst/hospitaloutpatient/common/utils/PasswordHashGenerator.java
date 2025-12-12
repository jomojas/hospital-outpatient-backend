package com.ncst.hospitaloutpatient.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        String[] rawPassword = {
                "011212", "022323", "033434", "044545", "055656", "066767", "077878", "088989", "111010",
                "100101", "110202", "130303", "140404", "150505"
        };

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("--- 请将以下输出的 Hash 值提供给我 ---");

        for (String s : rawPassword) {
            String hash = encoder.encode(s);
            System.out.println("明文: " + s + " -> Hash值: " + hash);
        }
    }
}