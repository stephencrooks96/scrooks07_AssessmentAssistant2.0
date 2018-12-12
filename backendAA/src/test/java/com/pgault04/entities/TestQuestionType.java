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

import com.pgault04.entities.QuestionType;

/**
 * @author paulgault
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQuestionType {

	private QuestionType questionTypeObj;

	private Long questionTypeID;

	private String questionType;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.questionTypeObj = new QuestionType();
		this.questionType = "questionType";

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.QuestionType#QuestionType()}.
	 */
	@Test
	public void testQuestionTypeDefaultConstructor() {
		assertNotNull(questionTypeObj);
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.QuestionType#QuestionType(java.lang.Integer, java.lang.String)}.
	 */
	@Test
	public void testQuestionTypeConstructorWithArgs() {
		questionTypeObj = null;
		questionTypeObj = new QuestionType(questionType);

		assertNotNull(questionTypeObj);
		assertEquals(questionType, questionTypeObj.getQuestionType());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.QuestionType#getQuestionTypeID()}.
	 */
	@Test
	public void testGetSetQuestionTypeID() {
		questionTypeObj.setQuestionTypeID(questionTypeID);
		assertEquals(questionTypeID, questionTypeObj.getQuestionTypeID());

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.QuestionType#getQuestionType()}.
	 */
	@Test
	public void testGetSetQuestionType() {
		questionTypeObj.setQuestionType(questionType);
		assertEquals(questionType, questionTypeObj.getQuestionType());
	}

}
