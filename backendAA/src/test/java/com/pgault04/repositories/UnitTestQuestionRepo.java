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

import com.pgault04.entities.Question;
import com.pgault04.repositories.QuestionRepo;
import com.pgault04.repositories.QuestionTypeRepo;

/**
 * @author paulgault
 *
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitTestQuestionRepo {

	private static final long USER_ID_IN_DB = 1L;

	private static final long QUESTION_TYPE_ID_IN_DB = 1L;

	@Autowired
	QuestionRepo questionRepo;

	@Autowired
	QuestionTypeRepo questionTypeRepo;

	private Question questionObj;
	private Long modelAnswerID;
	private String questionContent, questionFigure;
	private Integer maxScore;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.questionContent = "content";
		this.questionFigure = "questionFigure";
		this.maxScore = 100;
		this.modelAnswerID = null;

		questionObj = new Question(QUESTION_TYPE_ID_IN_DB, questionContent, questionFigure, maxScore,
				modelAnswerID, USER_ID_IN_DB);

	}

	/**
	 * Test method for Row Count
	 */
	@Test
	public void testRowCount() {

		int rowCountBefore = questionRepo.rowCount().intValue();
		
		// Inserts one user to table
		questionRepo.insert(questionObj);

		// Checks one value is registered as in the table
		assertTrue(questionRepo.rowCount().intValue() > rowCountBefore);

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.QuestionRepo#insert(pgault04.entities.Question)}.
	 */
	@Test
	public void testInsert() {

		// Inserts one user to table
		questionRepo.insert(questionObj);

		List<Question> questions = questionRepo.selectByQuestionID(questionObj.getQuestionID());

		assertEquals(1, questions.size());

		// Updates the user in the table
		questionObj.setQuestionContent("content2");

		// Inserts one user to table
		questionRepo.insert(questionObj);

		questions = questionRepo.selectByQuestionID(questionObj.getQuestionID());

		assertEquals("content2", questions.get(0).getQuestionContent());

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.QuestionRepo#selectByUserID(java.lang.Long)}.
	 */
	@Test
	public void testSelectByQuestionID() {
		// Inserts one user to table
		questionRepo.insert(questionObj);

		List<Question> questions = questionRepo.selectByQuestionID(questionObj.getQuestionID());

		assertEquals(1, questions.size());
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.QuestionRepo#delete(java.lang.Long)}.
	 */
	@Test
	public void testDelete() {
		// Inserts one user to table
		questionRepo.insert(questionObj);

		questionRepo.delete(questionObj.getQuestionID());

		List<Question> questions = questionRepo.selectByQuestionID(questionObj.getQuestionID());

		assertEquals(0, questions.size());
	}

}
