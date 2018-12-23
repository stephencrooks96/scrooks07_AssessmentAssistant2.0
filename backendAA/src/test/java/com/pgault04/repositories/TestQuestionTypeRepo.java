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
 * @author Paul Gault 40126005
 * @since November 2018
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

	@Before
	public void setUp() throws Exception {
		this.questionType = "questionType";
		questionTypeObj = new QuestionType(questionType);
	}

	@Test
	public void testRowCount() {
		int rowCountBefore = questionTypeRepo.rowCount();
		// Inserts one questionType to table
		questionTypeRepo.insert(questionTypeObj);
		// Checks one value is registered as in the table
		assertTrue(questionTypeRepo.rowCount() > rowCountBefore);
	}

	@Test
	public void testInsert() {
		// Inserts one questionType to table
		QuestionType returnedQuestionType = questionTypeRepo.insert(questionTypeObj);
		QuestionType questionTypes = questionTypeRepo
				.selectByQuestionTypeID(returnedQuestionType.getQuestionTypeID());
		assertNotNull(questionTypes);
		// Updates the questionType in the table
		returnedQuestionType.setQuestionType("quesType");
		// Inserts one questionType to table
		questionTypeRepo.insert(returnedQuestionType);
		questionTypes = questionTypeRepo.selectByQuestionTypeID(returnedQuestionType.getQuestionTypeID());
		assertEquals("quesType", questionTypes.getQuestionType());
	}

	@Test
	public void testSelectAll() {
		assertNotNull(questionTypeRepo.selectAll());
	}

	@Test
	public void testSelectByQuestionTypeID() {
		// Inserts one questionType to table
		QuestionType returnedQuestionType = questionTypeRepo.insert(questionTypeObj);
		QuestionType questionTypes = questionTypeRepo
				.selectByQuestionTypeID(returnedQuestionType.getQuestionTypeID());
		assertNotNull(questionTypes);
	}

	@Test
	public void testSelectByQuestionType() {
		// Inserts one questionType to table
		questionTypeRepo.insert(questionTypeObj);
		List<QuestionType> questionTypes = questionTypeRepo.selectByQuestionType(questionTypeObj.getQuestionType());
		assertEquals(1, questionTypes.size());
	}

	@Test
	public void testDelete() {
		// Inserts one questionType to table
		QuestionType returnedQuestionType = questionTypeRepo.insert(questionTypeObj);
		questionTypeRepo.delete(returnedQuestionType.getQuestionTypeID());
		QuestionType questionTypes = questionTypeRepo
				.selectByQuestionTypeID(returnedQuestionType.getQuestionTypeID());
		assertNull(questionTypes);
	}
}