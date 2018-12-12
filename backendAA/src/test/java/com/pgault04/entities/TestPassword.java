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

import com.pgault04.entities.Password;

/**
 * @author paulgault
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPassword {

	private Password passwordObj;

	private Long userID;

	private String resetString;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.passwordObj = new Password();

		this.userID = 1L;

		this.resetString = "resetString";
	}

	/**
	 * Test method for {@link pgault04.entities.Password#Password()}.
	 */
	@Test
	public void testPasswordDefaultConstructor() {
		assertNotNull(passwordObj);
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Password#Password(java.lang.Integer, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testPasswordConstructorWithArgs() {
		passwordObj = null;
		passwordObj = new Password(userID, resetString);

		assertNotNull(passwordObj);
		assertEquals(userID, passwordObj.getUserID());
		assertEquals(resetString, passwordObj.getResetString());

	}

	/**
	 * Test method for {@link pgault04.entities.Password#getUserID()}.
	 */
	@Test
	public void testGetSetUserID() {
		passwordObj.setUserID(userID);
		assertEquals(userID, passwordObj.getUserID());

	}

	/**
	 * Test method for {@link pgault04.entities.Password#getResetString()}.
	 */
	@Test
	public void testGetSetResetString() {
		passwordObj.setResetString(resetString);
		assertEquals(resetString, passwordObj.getResetString());
	}

}
