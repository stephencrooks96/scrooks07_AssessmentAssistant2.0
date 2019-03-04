package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.QuestionType;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQuestionType {

	private QuestionType questionTypeObj;

	private Long questionTypeID;

	private String questionType;

	@Before
	public void setUp() throws Exception {

		this.questionTypeObj = new QuestionType();
		this.questionTypeID = 1L;
		this.questionType = "questionType";

	}

	@Test
	public void testDefaultConstructor() {
		assertNotNull(questionTypeObj);
	}

	@Test
	public void testConstructorWithArgs() {
		questionTypeObj = null;
		questionTypeObj = new QuestionType(questionType);

		assertNotNull(questionTypeObj);
		assertEquals(questionType, questionTypeObj.getQuestionType());
	}

	@Test
	public void testGetSetQuestionTypeID() {
		questionTypeObj.setQuestionTypeID(questionTypeID);
		assertEquals(questionTypeID, questionTypeObj.getQuestionTypeID());

	}

	@Test
	public void testToString() {
		questionTypeObj = new QuestionType(questionType);
		questionTypeObj.setQuestionTypeID(questionTypeID);
		assertEquals("QuestionType{questionTypeID=1, questionType='questionType'}", questionTypeObj.toString());
	}
}