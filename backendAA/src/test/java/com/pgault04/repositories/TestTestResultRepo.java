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

import com.pgault04.entities.TestResult;
import com.pgault04.repositories.TestResultRepo;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTestResultRepo {

	private static final long USER_ID_IN_DB = 1L;

	private static final long TEST_ID_IN_DB = 1L;

	@Autowired
	TestResultRepo testResultRepo;

	private TestResult testResult;

	@Before
	public void setUp() throws Exception {
		testResult = new TestResult(TEST_ID_IN_DB, USER_ID_IN_DB, 100);
	}

	@Test
	public void testRowCount() {
		int rowCountBefore = testResultRepo.rowCount();
		// Inserts one test to table
		testResultRepo.insert(testResult);
		// Checks one value is registered as in the table
		assertTrue(testResultRepo.rowCount() > rowCountBefore);
	}

	@Test
	public void testInsert() {
		// Inserts one test to table
		TestResult returnedTestResult = testResultRepo.insert(testResult);
		TestResult testResults = testResultRepo.selectByTestResultID(returnedTestResult.getTestResultID());
		assertNotNull(testResults);
		// Updates the test in the table
		returnedTestResult.setTestScore(99);
		// Inserts one test to table
		testResultRepo.insert(returnedTestResult);
		testResults = testResultRepo.selectByTestResultID(returnedTestResult.getTestResultID());
		assertEquals(99, testResults.getTestScore().intValue());
	}

	@Test
	public void testSelectByTestResultID() {
		// Inserts one test to table
		TestResult returnedTestResult = testResultRepo.insert(testResult);
		TestResult testResults = testResultRepo.selectByTestResultID(returnedTestResult.getTestResultID());
		assertNotNull(testResults);
	}

	@Test
	public void testSelectByTestResultIDNullReturn() {
		assertNull(testResultRepo.selectByTestResultID(999L));
	}

	@Test
	public void testSelectByTestID() {
		// Inserts one test to table
		testResultRepo.insert(testResult);
		List<TestResult> testResults = testResultRepo.selectByTestID(testResult.getTestID());
		assertTrue(testResults.size() > 0);
	}

	@Test
	public void testSelectByStudentID() {
		// Inserts one test to table
		testResultRepo.insert(testResult);
		List<TestResult> testResults = testResultRepo.selectByStudentID(testResult.getStudentID());
		assertTrue(testResults.size() > 0);
	}

	@Test
	public void testDelete() {
		// Inserts one test to table
		TestResult returnedTestResult = testResultRepo.insert(testResult);
		testResultRepo.delete(returnedTestResult.getTestID());
		List<TestResult> testResults = testResultRepo.selectByTestID(returnedTestResult.getTestResultID());
		assertEquals(0, testResults.size());
	}
}