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

import com.pgault04.entities.UserRole;

/**
 * @author paulgault
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserRole {

	private UserRole userRoleObj;

	private Long userRoleID;

	private String userRole;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		userRoleObj = new UserRole();

		userRoleID = 1L;
		userRole = "userRole";

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.UserRole#UserRole()}.
	 */
	@Test
	public void testUserRoleDefaultConstructor() {
		assertNotNull(userRoleObj);
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.UserRole#UserRole(java.lang.Integer, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUserRoleIntegerStringString() {
		userRoleObj = null;
		userRoleObj = new UserRole(userRole);

		assertNotNull(userRoleObj);
		assertEquals(userRole, userRoleObj.getRole());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.UserRole#getUserRoleID()}.
	 */
	@Test
	public void testGetSetUserRoleID() {
		userRoleObj.setUserRoleID(userRoleID);
		assertEquals(userRoleID, userRoleObj.getUserRoleID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.UserRole#getRole()}.
	 */
	@Test
	public void testGetSetUserRole() {
		userRoleObj.setRole(userRole);
		assertEquals(userRole, userRoleObj.getRole());
	}

}
