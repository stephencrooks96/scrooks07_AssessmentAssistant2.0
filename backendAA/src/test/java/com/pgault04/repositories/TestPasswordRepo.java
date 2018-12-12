/**
 * 
 */
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

import com.pgault04.entities.Password;
import com.pgault04.repositories.PasswordRepo;

/**
 * @author paulgault
 *
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPasswordRepo {

	private static final long USER_ID_IN_DB = 1L;

	@Autowired
	PasswordRepo passwordRepo;
	
	private Password passwordObj;
	private String resetString;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		passwordObj = new Password(USER_ID_IN_DB, resetString);
	}

	/**
	 * Test method for Row Count
	 */
	@Test
	public void testRowCount() {

		int rowCountBefore = passwordRepo.rowCount().intValue();
				
		// Inserts one user to table
		passwordRepo.insert(passwordObj);

		// Checks one value is registered as in the table
		assertTrue(passwordRepo.rowCount().intValue() > rowCountBefore);

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.PasswordRepo#insert(pgault04.entities.Password)}.
	 */
	@Test
	public void testInsert() {

		// Inserts one user to table
		passwordRepo.insert(passwordObj);

		List<Password> passwords = passwordRepo.selectByUserID(passwordObj.getUserID());

		assertEquals(1, passwords.size());

		// Updates the user in the table
		passwordObj.setResetString("newPass");

		// Inserts one user to table
		passwordRepo.insert(passwordObj);

		passwords = passwordRepo.selectByUserID(passwordObj.getUserID());

		assertEquals("newPass", passwords.get(0).getResetString());

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.PasswordRepo#selectByUserID(java.lang.Long)}.
	 */
	@Test
	public void testSelectByUserID() {
		// Inserts one user to table
		passwordRepo.insert(passwordObj);

		List<Password> passwords = passwordRepo.selectByUserID(passwordObj.getUserID());

		assertEquals(1, passwords.size());
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.PasswordRepo#delete(java.lang.Long)}.
	 */
	@Test
	public void testDelete() {
		// Inserts one user to table
		passwordRepo.insert(passwordObj);

		passwordRepo.delete(passwordObj.getUserID());

		List<Password> passwords = passwordRepo.selectByUserID(passwordObj.getUserID());

		assertEquals(0, passwords.size());
	}

}
