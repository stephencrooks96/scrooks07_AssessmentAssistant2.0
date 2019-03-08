package com.pgault04.repositories;

import com.pgault04.entities.Answer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Paul Gault 40126005
 * @since November 2018
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
        answer = new Answer(QUESTION_ID_IN_DATABASE, ANSWERER_ID_IN_DB, MARKER_ID_IN_DB, TEST_ID_IN_DB, "content", 100, "feedback", 0, 0);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = answerRepo.rowCount();
        // Inserts one answer to table
        answerRepo.insert(answer);
        // Checks one value is registered as in the table
        assertTrue(answerRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        // Inserts one answer to table
        Answer returnedAnswer = answerRepo.insert(answer);
        Answer answer = answerRepo.selectByAnswerID(returnedAnswer.getAnswerID());
        assertNotNull(answer);
        // Updates the answer in the table
        returnedAnswer.setContent("content 2");
        // Inserts one answer to table
        answerRepo.insert(returnedAnswer);
        answer = answerRepo.selectByAnswerID(returnedAnswer.getAnswerID());
        assertEquals(returnedAnswer.getContent(), answer.getContent());
    }

    @Test
    public void testSelectByQuestionID() {
        // Inserts one answer to table
        Answer returnedAnswer = answerRepo.insert(answer);
        List<Answer> answers = answerRepo.selectByQuestionID(returnedAnswer.getQuestionID());
        assertTrue(answers.size() >= 1);
    }

    @Test
    public void testSelectByQuestionIDAndAnswererIDAndTestID() {
        // Inserts one answer to table
        answerRepo.insert(answer);
        Answer answer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(QUESTION_ID_IN_DATABASE, ANSWERER_ID_IN_DB, TEST_ID_IN_DB);
        assertEquals(QUESTION_ID_IN_DATABASE, answer.getQuestionID(), 0);
        assertEquals(ANSWERER_ID_IN_DB, answer.getAnswererID(), 0);
        assertNull(answerRepo.selectByQuestionIDAndAnswererIDAndTestID(null, null, null));
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
        Answer answer = answerRepo.selectByAnswerID(returnedAnswer.getAnswerID());
        assertNotNull(answer);
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
        Answer answers = answerRepo.selectByAnswerID(returnedAnswer.getAnswerID());
        assertNull(answers);
    }
}