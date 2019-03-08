package com.pgault04.repositories;

import com.pgault04.entities.OptionEntries;
import com.pgault04.entities.QuestionMathLine;
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

@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQuestionMathLineRepo {

    private static final long QUESTION_ID_IN_DB = 1L;

    @Autowired
    QuestionMathLineRepo questionMathLineRepo;
    private QuestionMathLine questionMathLine;
    private String content;
    private Integer index;

    @Before
    public void setUp() throws Exception {
        this.content = "content";
        this.index = 0;
        questionMathLine = new QuestionMathLine(QUESTION_ID_IN_DB, content, index);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = questionMathLineRepo.rowCount();
        // Inserts one questionMathLine to table
        questionMathLineRepo.insert(questionMathLine);
        // Checks one value is registered as in the table
        assertTrue(questionMathLineRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        // Inserts one questionMathLine to table
        QuestionMathLine qml = questionMathLineRepo.insert(questionMathLine);
        assertNotNull(qml);
        assertNotEquals(-1L, qml.getQuestionMathLineID(), 0);
        // Updates the questionMathLine in the table
        String updateCheck = "content2";
        qml.setContent(updateCheck);
        // Inserts one questionMathLine to table
        QuestionMathLine updatedQml = questionMathLineRepo.insert(qml);
        assertEquals(qml.getQuestionMathLineID(), updatedQml.getQuestionMathLineID());
        assertEquals(updateCheck, updatedQml.getContent());
    }

    @Test
    public void testSelectByQuestionID() {
        // Inserts one questionMathLine to table
        QuestionMathLine qml = questionMathLineRepo.insert(questionMathLine);
        List<QuestionMathLine> qmls = questionMathLineRepo.selectByQuestionID(qml.getQuestionID());
        assertTrue(qmls.toString().contains(qml.toString()));
    }

    @Test
    public void testSelectByQuestionMathLineID() {
        // Inserts one questionMathLine to table
        QuestionMathLine qml = questionMathLineRepo.insert(questionMathLine);
        qml = questionMathLineRepo.selectByQuestionMathLineID(qml.getQuestionMathLineID());
        assertNotNull(qml);
    }

    @Test
    public void testDelete() {
        // Inserts one questionMathLine to table
        QuestionMathLine qml = questionMathLineRepo.insert(questionMathLine);
        questionMathLineRepo.delete(qml.getQuestionMathLineID());
        qml = questionMathLineRepo.selectByQuestionMathLineID(qml.getQuestionMathLineID());
        assertNull(qml);
    }
}
