package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.User;

/**
 * @author Paul Gault 40126005
 * @since November 2018
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

	@Test
	public void testUserDefaultConstructor() {
		assertNotNull(userObj);
	}

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

	@Test
	public void testGetSetUserID() {
		userObj.setUserID(userID);
		assertEquals(userID, userObj.getUserID());
	}

	@Test
	public void testGetSetUsername() {
		userObj.setUsername(username);
		assertEquals(username, userObj.getUsername());
	}

	@Test
	public void testGetSetFirstName() {
		userObj.setFirstName(firstName);
		assertEquals(firstName, userObj.getFirstName());
	}

	@Test
	public void testGetSetLastName() {
		userObj.setLastName(lastName);
		assertEquals(lastName, userObj.getLastName());
	}

	@Test
	public void testGetSetEnabled() {
		userObj.setEnabled(enabled);
		assertEquals(enabled, userObj.getEnabled());
	}

	@Test
	public void testGetSetUserRole() {
		userObj.setUserRoleID(userRoleID);
		assertEquals(userRoleID, userObj.getUserRoleID());
	}

	@Test
	public void testToString() {
		userObj = new User(username, password, firstName, lastName, enabled, userRoleID);
		userObj.setUserID(userID);
		assertEquals("User{userID=1, username='username', password='password', firstName='firstName', lastName='lastName', enabled=1, userRoleID=2}", userObj.toString());
	}
}
