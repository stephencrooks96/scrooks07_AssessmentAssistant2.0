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

import com.pgault04.entities.User;
import com.pgault04.repositories.UserRepo;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserRepo {

	private static final long USER_ROLE_IN_DB = 1L;

	@Autowired
	UserRepo userRepo;

	private User user;
	private String username, firstName, lastName, password;
	private Integer enabled;

	@Before
	public void setUp() throws Exception {
		this.username = "username";
		this.firstName = "firstName";
		this.lastName = "lastName";
		this.password = "password";
		this.enabled = 0;
		user = new User(username, password, firstName, lastName, enabled, USER_ROLE_IN_DB);
	}

	@Test
	public void testRowCount() {
		int rowCountBefore = userRepo.rowCount();
		// Inserts one user to table
		userRepo.insert(user);
		// Checks one value is registered as in the table
		assertTrue(userRepo.rowCount() > rowCountBefore);
	}

	@Test
	public void testInsert() {
		// Inserts one user to table
		User returnedUser = userRepo.insert(user);
		User user = userRepo.selectByUserID(returnedUser.getUserID());
		assertNotNull(user);
		// Updates the user in the table
		returnedUser.setEnabled(1);
		// Inserts one user to table
		userRepo.insert(returnedUser);
		user = userRepo.selectByUserID(returnedUser.getUserID());
		assertEquals(1, user.getEnabled().intValue());
	}
	
	@Test
	public void testSelectAll() {
		assertNotNull(userRepo.selectAll());
	}

	@Test
	public void testSelectByUserID() {
		// Inserts one user to table
		User returnedUser = userRepo.insert(user);
		User user = userRepo.selectByUserID(returnedUser.getUserID());
		assertEquals(returnedUser.getUserID(), user.getUserID());
	}

	@Test
	public void testSelectByUserIDNullReturn() {
		assertNull(userRepo.selectByUserID(9999L));
	}

	@Test
	public void testSelectByUserNameNullReturn() {
		assertNull(userRepo.selectByUsername(null));
	}

	@Test
	public void testSelectByUsername() {
		// Inserts one user to table
		userRepo.insert(user);
		User user2 = userRepo.selectByUsername(user.getUsername());
		assertEquals(user2.getUsername(), user.getUsername());
	}

	@Test
	public void testSelectByFirstName() {
		// Inserts one user to table
		userRepo.insert(user);
		List<User> users = userRepo.selectByFirstName(user.getFirstName());
		assertTrue(users.size() > 0);
	}

	@Test
	public void testSelectByLastName() {
		// Inserts one user to table
		userRepo.insert(user);
		List<User> users = userRepo.selectByLastName(user.getLastName());
		assertTrue(users.size() > 0);
	}

	@Test
	public void testDelete() {
		// Inserts one user to table
		User returnedUser = userRepo.insert(user);
		userRepo.delete(returnedUser.getUserID());
		List <User> users = userRepo.selectAll();
		assertFalse(users.contains(returnedUser));
	}
}