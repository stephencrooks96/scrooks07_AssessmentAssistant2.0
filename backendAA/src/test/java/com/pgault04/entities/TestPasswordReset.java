/**
 * 
 */
package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author paulgault
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPasswordReset {

	private PasswordReset passwordResetObj;

	private Long userID;

	private String resetString;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.passwordResetObj = new PasswordReset();

		this.userID = 1L;

		this.resetString = "resetString";
	}

	/**
	 * Test method for {@link pgault04.entities.Password#Password()}.
	 */
	@Test
	public void testPasswordDefaultConstructor() {
		assertNotNull(passwordResetObj);
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Password#Password(java.lang.Integer, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testPasswordConstructorWithArgs() {
		passwordResetObj = null;
		passwordResetObj = new PasswordReset(userID, resetString);

		assertNotNull(passwordResetObj);
		assertEquals(userID, passwordResetObj.getUserID());
		assertEquals(resetString, passwordResetObj.getResetString());

	}

	/**
	 * Test method for {@link pgault04.entities.Password#getUserID()}.
	 */
	@Test
	public void testGetSetUserID() {
		passwordResetObj.setUserID(userID);
		assertEquals(userID, passwordResetObj.getUserID());

	}

	/**
	 * Test method for {@link pgault04.entities.Password#getResetString()}.
	 */
	@Test
	public void testGetSetResetString() {
		passwordResetObj.setResetString(resetString);
		assertEquals(resetString, passwordResetObj.getResetString());
	}

}
