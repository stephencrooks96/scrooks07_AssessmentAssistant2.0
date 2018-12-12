/**
 * 
 */
package com.pgault04.utilities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author paulgault
 *
 */
public class PasswordEncrypt {
	
	public PasswordEncrypt() {}

	public static String encrypt(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}
	
}
