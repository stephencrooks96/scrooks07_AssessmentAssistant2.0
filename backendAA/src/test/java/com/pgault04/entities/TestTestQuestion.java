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

import com.pgault04.entities.TestQuestion;

/**
 * @author paulgault
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTestQuestion {

	private TestQuestion testQuestionObj;

	private Long testQuestionID;

	private Long testID;

	private Long questionID;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.testQuestionObj = new TestQuestion();
		this.testQuestionID = 0L;
		this.testID = 1L;
		this.questionID = 2L;
	}

	/**
	 * Test method for
	 * {@link com.pgault04.entities.UnitTestQuestion#TestQuestion()}.
	 */
	@Test
	public void testTestQuestionDefaultConsturctor() {
		assertNotNull(testQuestionObj);
	}

	/**
	 * Test method for
	 * {@link com.pgault04.entities.UnitTestQuestion#TestQuestion(java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	public void testTestQuestionConstructorWithArgs() {
		testQuestionObj = null;
		testQuestionObj = new TestQuestion(testID, questionID);

		assertNotNull(testQuestionObj);
		assertEquals(testID, testQuestionObj.getTestID());
		assertEquals(questionID, testQuestionObj.getQuestionID());
	}

	/**
	 * Test method for
	 * {@link com.pgault04.entities.UnitTestQuestion#getTestID()}.
	 */
	@Test
	public void testGetSetTestQuestionID() {
		testQuestionObj.setTestQuestionID(testQuestionID);
		assertEquals(testQuestionID, testQuestionObj.getTestQuestionID());

	}

	/**
	 * Test method for
	 * {@link com.pgault04.entities.UnitTestQuestion#getTestID()}.
	 */
	@Test
	public void testGetSetTestID() {
		testQuestionObj.setTestID(testID);
		assertEquals(testID, testQuestionObj.getTestID());

	}

	/**
	 * Test method for
	 * {@link com.pgault04.entities.UnitTestQuestion#getQuestionID()}.
	 */
	@Test
	public void testGetSetQuestionID() {
		testQuestionObj.setQuestionID(questionID);
		assertEquals(questionID, testQuestionObj.getQuestionID());
	}

}
