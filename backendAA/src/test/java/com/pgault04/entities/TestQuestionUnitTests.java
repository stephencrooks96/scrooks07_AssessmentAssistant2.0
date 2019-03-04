package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.Question;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQuestionUnitTests {

	private Question questionObj;

	private Long questionType;

	private Long questionID;

	private String questionContent;

	private SerialBlob questionFigure;

	private Integer maxScore;

	private Integer minScore;

	private Integer allThatApply;
	
	private Long creatorID;

	@Before
	public void setUp() throws Exception {
		this.questionObj = new Question();
		this.questionType = 1L;
		this.questionID = 2L;
		this.questionContent = "questionContent";
		this.questionFigure = new SerialBlob("questionFigure".getBytes());
		this.maxScore = 3;
		this.minScore = 0;
		this.allThatApply = 1;
		this.creatorID = 1L;
	}

	@Test
	public void testDefaultConstructor() {
		assertNotNull(questionObj);
	}

	@Test
	public void testConstructorWithArgs() {
		questionObj = null;
		questionObj = new Question(questionType, questionContent, questionFigure, maxScore, minScore, creatorID, allThatApply);

		assertNotNull(questionObj);
		assertEquals(questionType, questionObj.getQuestionType());
		assertEquals(questionContent, questionObj.getQuestionContent());
		assertEquals(questionFigure, questionObj.getQuestionFigure());
		assertEquals(maxScore, questionObj.getMaxScore());
		assertEquals(minScore, questionObj.getMinScore());
		assertEquals(creatorID, questionObj.getCreatorID());
		assertEquals(allThatApply, questionObj.getAllThatApply());
	}

	@Test
	public void testGetSetQuestionID() {
		questionObj.setQuestionID(questionID);
		assertEquals(questionID, questionObj.getQuestionID());
	}

	@Test
	public void testToString() {
		questionObj = new Question(questionType, questionContent, questionFigure, maxScore, 0, creatorID, 0);
		questionObj.setQuestionID(questionID);
		assertEquals("Question{questionType=1, questionID=2, questionContent='questionContent', questionFigure=javax.sql.rowset.serial.SerialBlob@34a7100a, maxScore=3, minScore=0, creatorID=1, allThatApply=0}", questionObj.toString());
	}
}
