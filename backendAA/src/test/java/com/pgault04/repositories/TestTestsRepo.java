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

import com.pgault04.entities.Tests;
import com.pgault04.repositories.TestsRepo;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTestsRepo {

	private static final long MODULE_ID_IN_DB = 1L;

	@Autowired
	TestsRepo testRepo;

	// Tests vars
	private Tests test;
	private String testTitle, startDateTime, endDateTime;
	private Integer publishResults, scheduled, publishGrades;

	@Before
	public void setUp() throws Exception {
		this.testTitle = "testTitle";
		this.startDateTime = "2000-01-01 00:00:00";
		this.endDateTime = "2000-01-01 00:00:00";
		this.publishResults = 1;
		this.scheduled = 1;
		this.publishGrades = 1;
		test = new Tests(MODULE_ID_IN_DB, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades, 0);
	}

	@Test
	public void testRowCount() {
		int rowCountBefore = testRepo.rowCount();
		// Inserts one test to table
		testRepo.insert(test);
		// Checks one value is registered as in the table
		assertTrue(testRepo.rowCount() > rowCountBefore);
	}

	@Test
	public void testInsert() {
		// Inserts one test to table
		Tests returnedTest = testRepo.insert(test);
		Tests tests = testRepo.selectByTestID(returnedTest.getTestID());
		assertNotNull(tests);
		// Updates the test in the table
		returnedTest.setStartDateTime("2000-01-01 00:00:00");
		// Inserts one test to table
		testRepo.insert(returnedTest);
		tests = testRepo.selectByTestID(returnedTest.getTestID());
		assertEquals("2000-01-01 00:00:00", tests.getStartDateTime());
	}

	@Test
	public void testSelectByTestID() {
		// Inserts one test to table
		Tests returnedTest = testRepo.insert(test);
		Tests tests = testRepo.selectByTestID(returnedTest.getTestID());
		assertNotNull(tests);
	}

	@Test
	public void testSelectByModuleID() {
		// Inserts one test to table
		testRepo.insert(test);
		List<Tests> tests = testRepo.selectByModuleID(test.getModuleID());
		assertTrue(tests.size() > 0);
	}

	@Test
	public void testSelectByStartDateTime() {
		// Inserts one test to table
		testRepo.insert(test);
		List<Tests> tests = testRepo.selectByStartDateTime(test.getStartDateTime());
		assertTrue(tests.size() > 0);
	}

	@Test
	public void testSelectByEndDateTime() {
		// Inserts one test to table
		testRepo.insert(test);
		List<Tests> tests = testRepo.selectByEndDateTime(test.getEndDateTime());
		assertTrue(tests.size() > 0);
	}

	@Test
	public void testDelete() {
		// Inserts one test to table
		Tests returnedTests = testRepo.insert(test);
		testRepo.delete(returnedTests.getTestID());
		Tests tests = testRepo.selectByTestID(returnedTests.getTestID());
		assertNull(tests);
	}
}