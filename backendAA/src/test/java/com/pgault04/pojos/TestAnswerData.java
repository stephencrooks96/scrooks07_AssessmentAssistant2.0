package com.pgault04.pojos;

import com.pgault04.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAnswerData {

    private AnswerData answerData;
    private QuestionAndAnswer questionAndAnswer;
    private User student;

    @Before
    public void setUp() throws Exception {
        this.answerData = new AnswerData();
        this.questionAndAnswer = new QuestionAndAnswer();
        this.student = new User();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(answerData);
    }

    @Test
    public void testConstructorWithArgs() {
        answerData = null;
        answerData = new AnswerData(questionAndAnswer, student);

        assertNotNull(answerData);
        assertEquals(questionAndAnswer, answerData.getQuestionAndAnswer());
        assertEquals(student, answerData.getStudent());
    }

    @Test
    public void testToString() {
        answerData = new AnswerData(questionAndAnswer, student);
        assertEquals("AnswerData{questionAndAnswer=QuestionAndAnswer{question=null, answer=null, inputs=null, optionEntries=null}, student=User{userID=-1, username='null', password='null', firstName='null', lastName='null', enabled=null, userRoleID=null, tutor=null}}", answerData.toString());
    }
}