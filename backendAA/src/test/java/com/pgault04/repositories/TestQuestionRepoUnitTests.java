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

import com.pgault04.entities.Question;
import com.pgault04.repositories.QuestionRepo;
import com.pgault04.repositories.QuestionTypeRepo;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQuestionRepoUnitTests {

	private static final long USER_ID_IN_DB = 1L;

	private static final long QUESTION_TYPE_ID_IN_DB = 1L;

	@Autowired
	QuestionRepo questionRepo;

	@Autowired
	QuestionTypeRepo questionTypeRepo;

	private Question questionObj;
	private String questionContent, questionFigure;
	private Integer maxScore;

	@Before
	public void setUp() throws Exception {
		this.questionContent = "content";
		this.questionFigure = "questionFigure";
		this.maxScore = 100;
		questionObj = new Question(QUESTION_TYPE_ID_IN_DB, questionContent, questionFigure, maxScore,
				USER_ID_IN_DB);
	}

	@Test
	public void testRowCount() {
		int rowCountBefore = questionRepo.rowCount();
		// Inserts one user to table
		questionRepo.insert(questionObj);
		// Checks one value is registered as in the table
		assertTrue(questionRepo.rowCount() > rowCountBefore);
	}

	@Test
	public void testInsert() {
		// Inserts one user to table
		questionRepo.insert(questionObj);
		Question questions = questionRepo.selectByQuestionID(questionObj.getQuestionID());
		assertNotNull(questions);
		// Updates the user in the table
		questionObj.setQuestionContent("content2");
		// Inserts one user to table
		questionRepo.insert(questionObj);
		questions = questionRepo.selectByQuestionID(questionObj.getQuestionID());
		assertEquals("content2", questions.getQuestionContent());
	}

	@Test
	public void testSelectByQuestionID() {
		// Inserts one user to table
		questionRepo.insert(questionObj);
		Question questions = questionRepo.selectByQuestionID(questionObj.getQuestionID());
		assertNotNull(questions);
	}

	@Test
	public void testSelectByCreatorID() {
		// Inserts one user to table
		questionRepo.insert(questionObj);
		List<Question> questions = questionRepo.selectByCreatorID(questionObj.getCreatorID());
		for (Question q : questions) {
			assertEquals(USER_ID_IN_DB, q.getCreatorID(), 0.0);
		}
	}

	@Test
	public void testDelete() {
		// Inserts one user to table
		questionRepo.insert(questionObj);
		questionRepo.delete(questionObj.getQuestionID());
		Question questions = questionRepo.selectByQuestionID(questionObj.getQuestionID());
		assertNull(questions);
	}
}