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

import com.pgault04.entities.Question;

/**
 * @author paulgault
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitTestQuestion {

	private Question questionObj;

	private Long questionType;

	private Long questionID;

	private String questionContent;

	private String questionFigure;

	private Integer maxScore;
	
	private Long creatorID;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.questionObj = new Question();
		this.questionType = 1L;
		this.questionID = 2L;
		this.questionContent = "questionContent";
		this.questionFigure = "questionFigure";
		this.maxScore = 3;
		this.creatorID = 1L;
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Question#Question()}.
	 */
	@Test
	public void testQuestionDefaultConstructor() {
		assertNotNull(questionObj);
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Question#Question(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	public void testQuestionConstructorWithArgs() {
		questionObj = null;
		questionObj = new Question(questionType, questionContent, questionFigure, maxScore, creatorID);

		assertNotNull(questionObj);
		assertEquals(questionType, questionObj.getQuestionType());
		assertEquals(questionContent, questionObj.getQuestionContent());
		assertEquals(questionFigure, questionObj.getQuestionFigure());
		assertEquals(maxScore, questionObj.getMaxScore());
		assertEquals(creatorID, questionObj.getCreatorID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Question#getQuestionType()}.
	 */
	@Test
	public void testGetSetQuestionType() {
		questionObj.setQuestionType(questionType);
		assertEquals(questionType, questionObj.getQuestionType());

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Question#getQuestionID()}.
	 */
	@Test
	public void testGetSetQuestionID() {
		questionObj.setQuestionID(questionID);
		assertEquals(questionID, questionObj.getQuestionID());

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Question#getQuestionContent()}.
	 */
	@Test
	public void testGetSetQuestionContent() {
		questionObj.setQuestionContent(questionContent);
		assertEquals(questionContent, questionObj.getQuestionContent());

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Question#getQuestionFigure()}.
	 */
	@Test
	public void testGetSetQuestionFigure() {
		questionObj.setQuestionFigure(questionFigure);
		assertEquals(questionFigure, questionObj.getQuestionFigure());

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Question#getMaxScore()}.
	 */
	@Test
	public void testGetSetMaxScore() {
		questionObj.setMaxScore(maxScore);
		assertEquals(maxScore, questionObj.getMaxScore());

	}
	
	/**
	 * Test method for
	 * {@link pgault04.entities.Question#getCreatorID()}.
	 */
	@Test
	public void testGetSetCreatorID() {
		questionObj.setCreatorID(creatorID);
		assertEquals(creatorID, questionObj.getCreatorID());
	}

}
