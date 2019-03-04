package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPasswordReset {

	private PasswordReset passwordResetObj;

	private Long userID;

	private String resetString;

	@Before
	public void setUp() throws Exception {
		this.passwordResetObj = new PasswordReset();
		this.userID = 1L;
		this.resetString = "resetString";
	}

	@Test
	public void testDefaultConstructor() {
		assertNotNull(passwordResetObj);
	}

	@Test
	public void testConstructorWithArgs() {
		passwordResetObj = null;
		passwordResetObj = new PasswordReset(userID, resetString);
		assertNotNull(passwordResetObj);
		assertEquals(userID, passwordResetObj.getUserID());
		assertEquals(resetString, passwordResetObj.getResetString());
	}

	@Test
	public void testToString() {
		passwordResetObj = new PasswordReset(userID, resetString);
		assertEquals("PasswordReset{userID=1, resetString='resetString'}", passwordResetObj.toString());
	}

}
