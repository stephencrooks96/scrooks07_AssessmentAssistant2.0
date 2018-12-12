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

import com.pgault04.entities.User;

/**
 * @author paulgault
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUser {

	private User userObj;

	private Long userID;

	private String username;
	
	private String password;

	private String firstName;

	private String lastName;
	
	private Integer enabled;

	private Long userRoleID;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.userObj = new User();
		this.userID = 1L;
		this.username = "username";
		this.firstName = "firstName";
		this.lastName = "lastName";
		this.password = "password";
		this.enabled = 1;
		this.userRoleID = 2L;
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.User#User()}.
	 */
	@Test
	public void testUserDefaultConstructor() {
		assertNotNull(userObj);
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.User#User(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)}.
	 */
	@Test
	public void testUserConstructorWithArgs() {
		userObj = null;
		userObj = new User(username, password, firstName, lastName, enabled, userRoleID);

		assertNotNull(userObj);
		assertEquals(username, userObj.getUsername());
		assertEquals(firstName, userObj.getFirstName());
		assertEquals(lastName, userObj.getLastName());
		assertEquals(password, userObj.getPassword());
		assertEquals(enabled, userObj.getEnabled());
		assertEquals(userRoleID, userObj.getUserRoleID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.User#getUserID()}.
	 */
	@Test
	public void testGetSetUserID() {
		userObj.setUserID(userID);
		assertEquals(userID, userObj.getUserID());

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.User#getUsername()}.
	 */
	@Test
	public void testGetSetUsername() {
		userObj.setUsername(username);
		assertEquals(username, userObj.getUsername());

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.User#getFirstName()}.
	 */
	@Test
	public void testGetSetFirstName() {
		userObj.setFirstName(firstName);
		assertEquals(firstName, userObj.getFirstName());

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.User#getLastName()}.
	 */
	@Test
	public void testGetSetLastName() {
		userObj.setLastName(lastName);
		assertEquals(lastName, userObj.getLastName());

	}
	
	/**
	 * Test method for
	 * {@link pgault04.entities.User#getEnabled()}.
	 */
	@Test
	public void testGetSetEnabled() {
		userObj.setEnabled(enabled);
		assertEquals(enabled, userObj.getEnabled());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.User#getTutor()}.
	 */
	@Test
	public void testGetSetUserRole() {
		userObj.setUserRoleID(userRoleID);
		assertEquals(userRoleID, userObj.getUserRoleID());
	}

}
