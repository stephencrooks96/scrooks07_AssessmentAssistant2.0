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

import com.pgault04.entities.TestResult;

/**
 * @author paulgault
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTestResult {

	private TestResult testResultObj;

	private Long testResultID;

	private Long testID;

	private Long studentID;

	private Integer testScore;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.testResultObj = new TestResult();
		this.testResultID = 1L;
		this.testID = 2L;
		this.studentID = 3L;
		this.testScore = 4;
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.TestResult#TestResult()}.
	 */
	@Test
	public void testTestResultDefaultConstructor() {
		assertNotNull(testResultObj);
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.TestResult#TestResult(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	public void testTestResultConstructorWithArgs() {
		testResultObj = null;
		testResultObj = new TestResult(testID, studentID, testScore);

		assertNotNull(testResultObj);
		assertEquals(testID, testResultObj.getTestID());
		assertEquals(studentID, testResultObj.getStudentID());
		assertEquals(testScore, testResultObj.getTestScore());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.TestResult#getTestResultID()}.
	 */
	@Test
	public void testGetSetTestResultID() {
		testResultObj.setTestResultID(testResultID);
		assertEquals(testResultID, testResultObj.getTestResultID());

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.TestResult#getTestID()}.
	 */
	@Test
	public void testGetSetTestID() {
		testResultObj.setTestID(testID);
		assertEquals(testID, testResultObj.getTestID());

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.TestResult#getStudentID()}.
	 */
	@Test
	public void testGetSetStudentID() {
		testResultObj.setStudentID(studentID);
		assertEquals(studentID, testResultObj.getStudentID());

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.TestResult#getTestScore()}.
	 */
	@Test
	public void testGetSetTestScore() {
		testResultObj.setTestScore(testScore);
		assertEquals(testScore, testResultObj.getTestScore());
	}

}
