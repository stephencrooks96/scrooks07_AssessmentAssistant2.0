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

import com.pgault04.entities.TimeModifier;
import com.pgault04.repositories.TimeModifierRepo;

/**
 * @author paulgault
 *
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTimeModifierRepo {

	private static final long USER_ID_IN_DB = 1L;

	@Autowired
	TimeModifierRepo timeModifierRepo;
	
	private TimeModifier timeModifierObj;
	private Double timeModifier;


	@Before
	public void setUp() throws Exception {

		this.timeModifier = 5.0;

		timeModifierObj = new TimeModifier(USER_ID_IN_DB, timeModifier);
	}
	@Test
	public void testRowCount() {

		int rowCountBefore = timeModifierRepo.rowCount();
		
		// Inserts one user to table
		timeModifierRepo.insert(timeModifierObj);

		// Checks one value is registered as in the table
		assertTrue
		(timeModifierRepo.rowCount() > rowCountBefore);

	}

	@Test
	public void testInsert() {

		// Inserts one user to table
		timeModifierRepo.insert(timeModifierObj);

		TimeModifier timeModifiers = timeModifierRepo.selectByUserID(timeModifierObj.getUserID());

		assertNotNull(timeModifiers);

		// Updates the user in the table
		timeModifierObj.setTimeModifier(4.0);

		// Inserts one user to table
		timeModifierRepo.insert(timeModifierObj);

		timeModifiers = timeModifierRepo.selectByUserID(timeModifierObj.getUserID());

		assertEquals(4.0, timeModifiers.getTimeModifier(), 0);

	}

	@Test
	public void testSelectByUserID() {
		// Inserts one user to table
		timeModifierRepo.insert(timeModifierObj);

		TimeModifier timeModifiers = timeModifierRepo.selectByUserID(timeModifierObj.getUserID());

		assertNotNull(timeModifiers);
	}

	@Test
	public void testDelete() {
		// Inserts one user to table
		timeModifierRepo.insert(timeModifierObj);

		timeModifierRepo.delete(timeModifierObj.getUserID());

		TimeModifier timeModifiers = timeModifierRepo.selectByUserID(timeModifierObj.getUserID());

		assertNull(timeModifiers);
	}

}
