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

import com.pgault04.entities.TestQuestion;
import com.pgault04.repositories.TestQuestionRepo;


/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTestQuestionRepo {

	private static final long SECOND_QUESTION_ID_IN_DB = 2L;

	private static final long TEST_ID_IN_DB = 1L;

	private static final long QUESTION_ID_IN_DB = 1L;

	@Autowired
	TestQuestionRepo testQuestionRepo;

	private TestQuestion testQuestion;

	@Before
	public void setUp() throws Exception {
		testQuestion = new TestQuestion(TEST_ID_IN_DB, QUESTION_ID_IN_DB);
	}

	@Test
	public void testRowCount() {
		int rowCountBefore = testQuestionRepo.rowCount();
		// Inserts one test to table
		testQuestionRepo.insert(testQuestion);
		// Checks one value is registered as in the table
		assertTrue(testQuestionRepo.rowCount() > rowCountBefore);
	}

	@Test
	public void testTestQuestionInsert() {
		// Inserts one testQuestion to table
		TestQuestion returnedTestQuestion = testQuestionRepo.insert(testQuestion);
		List<TestQuestion> testQuestions = testQuestionRepo
				.selectByTestQuestionID(returnedTestQuestion.getTestQuestionID());
		assertEquals(1, testQuestions.size());
		// Updates the testQuestion in the table
		returnedTestQuestion.setQuestionID(SECOND_QUESTION_ID_IN_DB);
		// Inserts one testQuestion to table
		testQuestionRepo.insert(returnedTestQuestion);
		testQuestions = testQuestionRepo.selectByTestQuestionID(returnedTestQuestion.getTestQuestionID());
		assertEquals(SECOND_QUESTION_ID_IN_DB, testQuestions.get(0).getQuestionID().intValue());
	}

	@Test
	public void testTestQuestionSelectByTestQuestionID() {
		// Inserts one testQuestion to table
		TestQuestion returnedTestQuestion = testQuestionRepo.insert(testQuestion);
		List<TestQuestion> testQuestions = testQuestionRepo
				.selectByTestQuestionID(returnedTestQuestion.getTestQuestionID());
		assertEquals(1, testQuestions.size());
	}

	@Test
	public void testTestQuestionSelectByTestID() {
		// Inserts one testQuestion to table
		testQuestionRepo.insert(testQuestion);
		List<TestQuestion> testQuestions = testQuestionRepo.selectByTestID(testQuestion.getTestID());
		assertTrue(testQuestions.size() > 0);
	}

	@Test
	public void testTestQuestionSelectByQuestionID() {
		// Inserts one testQuestion to table
		testQuestionRepo.insert(testQuestion);
		List<TestQuestion> testQuestions = testQuestionRepo.selectByQuestionID(testQuestion.getQuestionID());
		assertTrue(testQuestions.size() > 0);
	}

	@Test
	public void testTestQuestionDelete() {
		// Inserts one testQuestion to table
		TestQuestion returnedTestQuestions = testQuestionRepo.insert(testQuestion);
		testQuestionRepo.delete(returnedTestQuestions.getTestQuestionID());
		List<TestQuestion> testQuestions = testQuestionRepo
				.selectByTestQuestionID(returnedTestQuestions.getTestQuestionID());
		assertEquals(0, testQuestions.size());
	}
}