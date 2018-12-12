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

import com.pgault04.entities.Answer;

/**
 * @author paulgault
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAnswer {

	private Answer answer;
	private Long answerID, questionID, answererID, markerID, testID;
	Integer score;
	private String content, feedback, marksGainedFor;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		answer = new Answer();

		this.answerID = 1L;
		this.questionID = 2L;
		this.answererID = 3L;
		this.markerID = 4L;
		this.testID = 5L;
		this.score = 5;
		this.content = "content";
		this.feedback = "feedback";
		this.marksGainedFor = "marksGainedFor";

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Answer#Answer()}.
	 */
	@Test
	public void testAnswerDefaultConstructor() {
		assertNotNull(answer);
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Answer#Answer(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAnswerConstructorWithArgs() {
		answer = null;
		answer = new Answer(questionID, answererID, markerID, testID, content, score);

		assertNotNull(answer);
		assertEquals(questionID, answer.getQuestionID());
		assertEquals(answererID, answer.getAnswererID());
		assertEquals(markerID, answer.getMarkerID());
		assertEquals(testID, answer.getTestID());
		assertEquals(content, answer.getContent());
		assertEquals(score, answer.getScore());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Answer#getAnswerID()}.
	 */
	@Test
	public void testGetSetAnswerID() {
		answer.setAnswerID(answerID);
		assertEquals(answerID, answer.getAnswerID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Answer#getQuestionID()}.
	 */
	@Test
	public void testGetSetQuestionID() {
		answer.setQuestionID(questionID);
		assertEquals(questionID, answer.getQuestionID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Answer#getAnswererID()}.
	 */
	@Test
	public void testGetSetAnswererID() {
		answer.setAnswererID(answererID);
		assertEquals(answererID, answer.getAnswererID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Answer#getMarkerID()}.
	 */
	@Test
	public void testGetSetMarkerID() {
		answer.setMarkerID(markerID);
		assertEquals(markerID, answer.getMarkerID());
	}
	
	/**
	 * Test method for
	 * {@link pgault04.entities.Answer#getTestID()}.
	 */
	@Test
	public void testGetSetTestID() {
		answer.setTestID(testID);
		assertEquals(testID, answer.getTestID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Answer#getContent()}.
	 */
	@Test
	public void testGetSetContent() {
		answer.setContent(content);
		assertEquals(content, answer.getContent());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Answer#getScore()}.
	 */
	@Test
	public void testGetSetScore() {
		answer.setScore(score);
		assertEquals(score, answer.getScore());
	}

}
