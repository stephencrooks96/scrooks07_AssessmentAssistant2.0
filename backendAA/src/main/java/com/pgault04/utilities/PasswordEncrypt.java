package com.pgault04.utilities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Class to encrypt passwords entered
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
public class PasswordEncrypt {

    /**
     * Default constructor
     */
    public PasswordEncrypt() {}

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
