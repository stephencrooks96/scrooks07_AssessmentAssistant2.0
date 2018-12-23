package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.Question;

/**
 * @author Paul Gault 40126005
 * @since November 2018
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

	@Test
	public void testQuestionDefaultConstructor() {
		assertNotNull(questionObj);
	}

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

	@Test
	public void testGetSetQuestionType() {
		questionObj.setQuestionType(questionType);
		assertEquals(questionType, questionObj.getQuestionType());
	}

	@Test
	public void testGetSetQuestionID() {
		questionObj.setQuestionID(questionID);
		assertEquals(questionID, questionObj.getQuestionID());
	}

	@Test
	public void testGetSetQuestionContent() {
		questionObj.setQuestionContent(questionContent);
		assertEquals(questionContent, questionObj.getQuestionContent());
	}

	@Test
	public void testGetSetQuestionFigure() {
		questionObj.setQuestionFigure(questionFigure);
		assertEquals(questionFigure, questionObj.getQuestionFigure());
	}

	@Test
	public void testGetSetMaxScore() {
		questionObj.setMaxScore(maxScore);
		assertEquals(maxScore, questionObj.getMaxScore());
	}

	@Test
	public void testGetSetCreatorID() {
		questionObj.setCreatorID(creatorID);
		assertEquals(creatorID, questionObj.getCreatorID());
	}

	@Test
	public void testToString() {
		questionObj = new Question(questionType, questionContent, questionFigure, maxScore, creatorID);
		questionObj.setQuestionID(questionID);
		assertEquals("Question{questionType=1, questionID=2, questionContent='questionContent', questionFigure='questionFigure', maxScore=3, creatorID=1}", questionObj.toString());
	}
}
