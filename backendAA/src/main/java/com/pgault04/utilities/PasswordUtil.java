package com.pgault04.utilities;

import java.util.Random;

public class PasswordUtil {

    private static final String ALPHA_NUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
    private static final int STRING_LENGTH = 15;

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(STRING_LENGTH);
        Random random = new Random();
        for (int i = 0; i < STRING_LENGTH; i++) {
            sb.append(ALPHA_NUM.charAt(random. nextInt(ALPHA_NUM.length())));
        }
        return sb.toString();
    }
}
