/**
 * 
 */
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

import com.pgault04.entities.Answer;
import com.pgault04.repositories.AnswerRepo;

/**
 * @author paulgault
 *
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAnswerRepo {

	private static final long TEST_ID_IN_DB = 1L;

	private static final long MARKER_ID_IN_DB = 1L;

	private static final long ANSWERER_ID_IN_DB = 1L;

	private static final long QUESTION_ID_IN_DATABASE = 1L;

	@Autowired
	AnswerRepo answerRepo;

	private Answer answer;

	@Before
	public void setUp() throws Exception {
		
		answer = new Answer(QUESTION_ID_IN_DATABASE, ANSWERER_ID_IN_DB, MARKER_ID_IN_DB, TEST_ID_IN_DB, "content", 100);
		
	}

	@Test
	public void testRowCount() {

		int rowCountBefore = answerRepo.rowCount().intValue();
		
		// Inserts one answer to table
		answerRepo.insert(answer);

		// Checks one value is registered as in the table
		assertTrue(answerRepo.rowCount().intValue() > rowCountBefore);

	}

	@Test
	public void testInsert() {

		// Inserts one answer to table
		Answer returnedAnswer = answerRepo.insert(answer);

		List<Answer> answers = answerRepo.selectByAnswerID(returnedAnswer.getAnswerID());

		assertEquals(1, answers.size());

		// Updates the answer in the table
		returnedAnswer.setContent("content 2");

		// Inserts one answer to table
		answerRepo.insert(returnedAnswer);

		answers = answerRepo.selectByAnswerID(returnedAnswer.getAnswerID());

		assertEquals(returnedAnswer.getContent(), answers.get(0).getContent());

	}

	@Test
	public void testSelectByQuestionID() {
		// Inserts one answer to table
		Answer returnedAnswer = answerRepo.insert(answer);

		List<Answer> answers = answerRepo.selectByQuestionID(returnedAnswer.getQuestionID());

		assertTrue(answers.size() >= 1);
	}

	@Test
	public void testSelectByTestID() {
		// Inserts one answer to table
		Answer returnedAnswer = answerRepo.insert(answer);

		List<Answer> answers = answerRepo.selectByTestID(returnedAnswer.getTestID());

		assertTrue(answers.size() >= 1);
	}

	@Test
	public void testSelectByAnswerID() {
		// Inserts one answer to table
		Answer returnedAnswer = answerRepo.insert(answer);

		List<Answer> answers = answerRepo.selectByAnswerID(returnedAnswer.getAnswerID());

		assertEquals(1, answers.size());
	}

	@Test
	public void testSelectByAnswererID() {
		// Inserts one answer to table
		answerRepo.insert(answer);

		List<Answer> answers = answerRepo.selectByAnswererID(answer.getAnswererID());

		assertTrue(answers.size() >= 1);
	}


	@Test
	public void testSelectByMarkerID() {
		// Inserts one answer to table
		answerRepo.insert(answer);

		List<Answer> answers = answerRepo.selectByMarkerID(answer.getMarkerID());

		assertTrue(answers.size() >= 1);
	}

	@Test
	public void testDelete() {
		// Inserts one answer to table
		Answer returnedAnswer = answerRepo.insert(answer);

		answerRepo.delete(returnedAnswer.getAnswerID());

		List<Answer> answers = answerRepo.selectByAnswerID(returnedAnswer.getAnswerID());

		assertEquals(0, answers.size());
	}

}
