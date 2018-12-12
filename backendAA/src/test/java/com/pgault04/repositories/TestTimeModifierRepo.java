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

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.timeModifier = 5.0;

		timeModifierObj = new TimeModifier(USER_ID_IN_DB, timeModifier);
	}

	

	/**
	 * Test method for Row Count
	 */
	@Test
	public void testRowCount() {

		int rowCountBefore = timeModifierRepo.rowCount().intValue();
		
		// Inserts one user to table
		timeModifierRepo.insert(timeModifierObj);

		// Checks one value is registered as in the table
		assertTrue
		(timeModifierRepo.rowCount().intValue()  > rowCountBefore);

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.TimeModifierRepo#insert(pgault04.entities.TimeModifier)}.
	 */
	@Test
	public void testInsert() {

		// Inserts one user to table
		timeModifierRepo.insert(timeModifierObj);

		List<TimeModifier> timeModifiers = timeModifierRepo.selectByUserID(timeModifierObj.getUserID());

		assertEquals(1, timeModifiers.size());

		// Updates the user in the table
		timeModifierObj.setTimeModifier(4.0);

		// Inserts one user to table
		timeModifierRepo.insert(timeModifierObj);

		timeModifiers = timeModifierRepo.selectByUserID(timeModifierObj.getUserID());

		assertEquals(4.0, timeModifiers.get(0).getTimeModifier(), 0);

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.TimeModifierRepo#selectByUserID(java.lang.Long)}.
	 */
	@Test
	public void testSelectByUserID() {
		// Inserts one user to table
		timeModifierRepo.insert(timeModifierObj);

		List<TimeModifier> timeModifiers = timeModifierRepo.selectByUserID(timeModifierObj.getUserID());

		assertEquals(1, timeModifiers.size());
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.TimeModifierRepo#delete(java.lang.Long)}.
	 */
	@Test
	public void testDelete() {
		// Inserts one user to table
		timeModifierRepo.insert(timeModifierObj);

		timeModifierRepo.delete(timeModifierObj.getUserID());

		List<TimeModifier> timeModifiers = timeModifierRepo.selectByUserID(timeModifierObj.getUserID());

		assertEquals(0, timeModifiers.size());
	}

}
