package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.UserRole;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserRole {

	private UserRole userRoleObj;

	private Long userRoleID;

	private String userRole;

	@Before
	public void setUp() throws Exception {
		userRoleObj = new UserRole();
		userRoleID = 1L;
		userRole = "userRole";
	}

	@Test
	public void testDefaultConstructor() {
		assertNotNull(userRoleObj);
	}

	@Test
	public void testUserRoleIntegerStringString() {
		userRoleObj = null;
		userRoleObj = new UserRole(userRole);

		assertNotNull(userRoleObj);
		assertEquals(userRole, userRoleObj.getRole());
	}

	@Test
	public void testGetSetUserRoleID() {
		userRoleObj.setUserRoleID(userRoleID);
		assertEquals(userRoleID, userRoleObj.getUserRoleID());
	}

	@Test
	public void testToString() {
		userRoleObj = new UserRole(userRole);
		userRoleObj.setUserRoleID(userRoleID);
		assertEquals("UserRole{userRoleID=1, role='userRole'}", userRoleObj.toString());
	}
}