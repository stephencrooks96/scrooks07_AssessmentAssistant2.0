package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.Answer;

/**
 * @author Paul Gault 40126005
 * @since December 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAnswer {

	private Answer answer;
	private Long answerID, questionID, answererID, markerID, testID;
	private Integer score;
	private String content, feedback;


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
	}

	@Test
	public void testAnswerDefaultConstructor() {
		assertNotNull(answer);
	}

	@Test
	public void testAnswerConstructorWithArgs() {
		answer = null;
		answer = new Answer(questionID, answererID, markerID, testID, content, score, feedback, 0, 0);

		assertNotNull(answer);
		assertEquals(questionID, answer.getQuestionID());
		assertEquals(answererID, answer.getAnswererID());
		assertEquals(markerID, answer.getMarkerID());
		assertEquals(testID, answer.getTestID());
		assertEquals(content, answer.getContent());
		assertEquals(score, answer.getScore());
		assertEquals(feedback, answer.getFeedback());
	}

	@Test
	public void testGetSetAnswerID() {
		answer.setAnswerID(answerID);
		assertEquals(answerID, answer.getAnswerID());
	}

	@Test
	public void testGetSetQuestionID() {
		answer.setQuestionID(questionID);
		assertEquals(questionID, answer.getQuestionID());
	}

	@Test
	public void testGetSetAnswererID() {
		answer.setAnswererID(answererID);
		assertEquals(answererID, answer.getAnswererID());
	}

	@Test
	public void testGetSetMarkerID() {
		answer.setMarkerID(markerID);
		assertEquals(markerID, answer.getMarkerID());
	}

	@Test
	public void testGetSetTestID() {
		answer.setTestID(testID);
		assertEquals(testID, answer.getTestID());
	}

	@Test
	public void testGetSetContent() {
		answer.setContent(content);
		assertEquals(content, answer.getContent());
	}

	@Test
	public void testGetSetScore() {
		answer.setScore(score);
		assertEquals(score, answer.getScore());
	}

	@Test
	public void testGetSetFeedback() {
		answer.setFeedback(feedback);
		assertEquals(feedback, answer.getFeedback());
	}

	@Test
	public void testToString() {
		answer.setAnswerID(answerID);
		answer.setQuestionID(questionID);
		answer.setAnswererID(answererID);
		answer.setMarkerID(markerID);
		answer.setTestID(testID);
		answer.setContent(content);
		answer.setScore(score);

		assertEquals("Answer{answerID=1, questionID=2, answererID=3, markerID=4, testID=5, content='content', score=5, feedback='feedback'}", answer.toString());
	}

}
