package com.pgault04.repositories;

import static org.junit.Assert.*;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.UserRole;
import com.pgault04.repositories.UserRoleRepo;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserRoleRepo {

	@Autowired
	UserRoleRepo userRoleRepo;

	private UserRole userRoleObj;
	private String userRole;

	@Before
	public void setUp() throws Exception {
		this.userRole = "userRole";
		userRoleObj = new UserRole(userRole);
	}

	@Test
	public void testRowCount() {
		int rowCountBefore = userRoleRepo.rowCount();
		// Inserts one userRole to table
		userRoleRepo.insert(userRoleObj);
		// Checks one value is registered as in the table
		assertTrue(userRoleRepo.rowCount() > rowCountBefore);
	}

	@Test
	public void testInsert() {
		// Inserts one userRole to table
		UserRole returnedUserRole = userRoleRepo.insert(userRoleObj);
		UserRole userRole = userRoleRepo.selectByUserRoleID(returnedUserRole.getUserRoleID());
		assertEquals(returnedUserRole.getUserRoleID(), userRole.getUserRoleID());
		// Updates the userRole in the table
		returnedUserRole.setRole("usType");
		// Inserts one userRole to table
		userRoleRepo.insert(returnedUserRole);
		userRole = userRoleRepo.selectByUserRoleID(returnedUserRole.getUserRoleID());
		assertEquals("usType", userRole.getRole());
	}

	@Test
	public void testSelectAll() {
		assertNotNull(userRoleRepo.selectAll());
	}

	@Test
	public void testSelectByUserRoleID() {
		// Inserts one userRole to table
		UserRole returnedUserRole = userRoleRepo.insert(userRoleObj);
		UserRole userRole = userRoleRepo.selectByUserRoleID(returnedUserRole.getUserRoleID());
		assertEquals("userRole", userRole.getRole());
	}

	@Test
	public void testSelectByUserRoleIDNullReturn() {
		assertNull(userRoleRepo.selectByUserRoleID(9999L));
	}

	@Test
	public void testSelectByUserRole() {
		// Inserts one userRole to table
		userRoleRepo.insert(userRoleObj);
		List<UserRole> userRoles = userRoleRepo.selectByRole(userRoleObj.getRole());
		assertTrue(userRoles.size() > 0);
	}

	@Test
	public void testDelete() {
		// Inserts one userRole to table
		UserRole returnedUserRole = userRoleRepo.insert(userRoleObj);
		userRoleRepo.delete(returnedUserRole.getUserRoleID());
		List<UserRole> userRoles = userRoleRepo.selectAll();
		assertFalse(userRoles.contains(returnedUserRole));
	}
}