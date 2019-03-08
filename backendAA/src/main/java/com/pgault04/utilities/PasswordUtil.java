package com.pgault04.utilities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;

public class PasswordUtil {

    private static final String ALPHA_NUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
    private static final int STRING_LENGTH = 15;

    public PasswordUtil() {}

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(STRING_LENGTH);
        Random random = new Random();
        for (int i = 0; i < STRING_LENGTH; i++) {
            sb.append(ALPHA_NUM.charAt(random. nextInt(ALPHA_NUM.length())));
        }
        return sb.toString();
    }

    /**
     * Password encrypt method
     *
     * @param password the password
     * @return the encrypted password
     */
    public static String encrypt(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
