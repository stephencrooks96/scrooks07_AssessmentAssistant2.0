package com.pgault04.utilities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;

/**
 * @author Paul Gault - 40126005
 * @since Jan 2019
 * Allows for password to be changed along with corresponding reset strings
 */
public class PasswordUtil {

    /*
     * All all letters (both cases) and numbers
     */
    private static final String ALPHA_NUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

    /*
     * Static length of the randomly generated reset string
     */
    private static final int STRING_LENGTH = 15;

    /**
     * The default constructor
     */
    public PasswordUtil() {}

    /**
     * Generates the password reset string for a user
     *
     * @return the randomly generated string
     */
    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(STRING_LENGTH);
        Random random = new Random();
        // Creates the random string with length 15 chars
        for (int i = 0; i < STRING_LENGTH; i++) {
            sb.append(ALPHA_NUM.charAt(random.nextInt(ALPHA_NUM.length())));
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