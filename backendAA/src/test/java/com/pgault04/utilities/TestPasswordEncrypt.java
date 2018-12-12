package com.pgault04.utilities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 
 * @author Paul Gault - 40126005
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPasswordEncrypt {

	private String passwordToEncode, encodedPassword;

	@Before
	public void setUp() throws Exception {

		this.passwordToEncode = "123";
		this.encodedPassword = "$2a$10$Z43GXkPCCYfzNE5SXg.RQOqOMQWUgP3Xf.RoHit5ae0Tyx2xAHSrS";
	}
	
	@Test
	public void testDefaultConstructor() {
		PasswordEncrypt pE = new PasswordEncrypt();
		assertNotNull(pE);
	}

	@Test
	public void testEncrypt() {

		assertThat(encodedPassword.matches(PasswordEncrypt.encrypt(passwordToEncode)));
	}

}
