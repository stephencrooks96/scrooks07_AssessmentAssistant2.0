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

import com.pgault04.entities.QuestionType;
import com.pgault04.repositories.QuestionTypeRepo;

/**
 * @author paulgault
 *
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQuestionTypeRepo {

	@Autowired
	QuestionTypeRepo questionTypeRepo;

	private QuestionType questionTypeObj;
	private String questionType;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.questionType = "questionType";
		questionTypeObj = new QuestionType(questionType);

	}

	/**
	 * Test method for Row Count
	 */
	@Test
	public void testRowCount() {

		int rowCountBefore = questionTypeRepo.rowCount().intValue();
		
		// Inserts one questionType to table
		questionTypeRepo.insert(questionTypeObj);

		// Checks one value is registered as in the table
		assertTrue(questionTypeRepo.rowCount().intValue() > rowCountBefore);

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.QuestionTypeRepo#insert(pgault04.entities.QuestionType)}.
	 */
	@Test
	public void testInsert() {

		// Inserts one questionType to table
		QuestionType returnedQuestionType = questionTypeRepo.insert(questionTypeObj);

		List<QuestionType> questionTypes = questionTypeRepo
				.selectByQuestionTypeID(returnedQuestionType.getQuestionTypeID());

		assertEquals(1, questionTypes.size());

		// Updates the questionType in the table
		returnedQuestionType.setQuestionType("quesType");

		// Inserts one questionType to table
		questionTypeRepo.insert(returnedQuestionType);

		questionTypes = questionTypeRepo.selectByQuestionTypeID(returnedQuestionType.getQuestionTypeID());

		assertEquals("quesType", questionTypes.get(0).getQuestionType());

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.QuestionTypeRepo#selectByQuestionTypeID(java.lang.Long)}.
	 */
	@Test
	public void testSelectByQuestionTypeID() {
		// Inserts one questionType to table
		QuestionType returnedQuestionType = questionTypeRepo.insert(questionTypeObj);

		List<QuestionType> questionTypes = questionTypeRepo
				.selectByQuestionTypeID(returnedQuestionType.getQuestionTypeID());

		assertEquals(1, questionTypes.size());
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.QuestionTypeRepo#selectByEmail(java.lang.String)}.
	 */
	@Test
	public void testSelectByQuestionType() {
		// Inserts one questionType to table
		questionTypeRepo.insert(questionTypeObj);

		List<QuestionType> questionTypes = questionTypeRepo.selectByQuestionType(questionTypeObj.getQuestionType());

		assertEquals(1, questionTypes.size());
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.QuestionTypeRepo#delete(java.lang.Long)}.
	 */
	@Test
	public void testDelete() {
		// Inserts one questionType to table
		QuestionType returnedQuestionType = questionTypeRepo.insert(questionTypeObj);

		questionTypeRepo.delete(returnedQuestionType.getQuestionTypeID());

		List<QuestionType> questionTypes = questionTypeRepo
				.selectByQuestionTypeID(returnedQuestionType.getQuestionTypeID());

		assertEquals(0, questionTypes.size());
	}

}
