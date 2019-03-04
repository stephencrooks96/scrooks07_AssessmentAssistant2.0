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
	private Long userID, userRoleID;
	private String username, password, firstName, lastName;
	private Integer enabled, tutor;

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
		this.tutor = 1;
	}

	@Test
	public void testDefaultConstructor() {
		assertNotNull(userObj);
	}

	@Test
	public void testConstructorWithArgs() {
		userObj = null;
		userObj = new User(username, password, firstName, lastName, enabled, userRoleID, tutor);

		assertNotNull(userObj);
		assertEquals(username, userObj.getUsername());
		assertEquals(firstName, userObj.getFirstName());
		assertEquals(lastName, userObj.getLastName());
		assertEquals(password, userObj.getPassword());
		assertEquals(enabled, userObj.getEnabled());
		assertEquals(tutor, userObj.getTutor());
		assertEquals(userRoleID, userObj.getUserRoleID());
	}

	@Test
	public void testGetSetUserID() {
		userObj.setUserID(userID);
		assertEquals(userID, userObj.getUserID());
	}

	@Test
	public void testToString() {
		userObj = new User(username, password, firstName, lastName, enabled, userRoleID, tutor);
		userObj.setUserID(userID);
		assertEquals("User{userID=1, username='username', password='password', firstName='firstName', lastName='lastName', enabled=1, userRoleID=2, tutor=1}", userObj.toString());
	}
}
