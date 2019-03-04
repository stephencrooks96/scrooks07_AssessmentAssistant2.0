package com.pgault04.pojos;

import com.pgault04.entities.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTestAndResult {

    private TestAndResult testAndResultObj;
    private Double perentageScore;
    private List<QuestionAndAnswer> questions;
    private User user;
    private Tests testsObj;
    private QuestionAndAnswer questionAndAnswerObj;
    private QuestionAndBase64 questionAndBaseObj;
    private Question question;
    private TestResult testResultObj;

    @Before
    public void setUp() throws Exception {
        testAndResultObj = new TestAndResult();
        perentageScore = 100.0;
        questions = new ArrayList<>();
        user = new User();
        testsObj = new Tests();
        questionAndAnswerObj = new QuestionAndAnswer();
        question = new Question();
        question.setMaxScore(100);
        questionAndBaseObj = new QuestionAndBase64();
        questionAndBaseObj.setQuestion(question);
        questionAndAnswerObj.setQuestion(questionAndBaseObj);
        testResultObj = new TestResult();
        questions.add(questionAndAnswerObj);
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(testAndResultObj);
    }

    @Test
    public void testConstructorWithArgs() {
        testAndResultObj = null;
        testAndResultObj = new TestAndResult(testsObj, testResultObj, questions, user);

        assertNotNull(testAndResultObj);
        assertEquals(testsObj, testAndResultObj.getTest());
        assertEquals(testResultObj, testAndResultObj.getTestResult());
        assertEquals(questions, testAndResultObj.getQuestions());
        assertEquals(user, testAndResultObj.getUser());
        assertEquals(100.0, testAndResultObj.getPercentageScore(), 0.0);
    }

    @Test
    public void testToString() {
        testAndResultObj = new TestAndResult(testsObj, testResultObj, questions, user);
        assertEquals("TestAndResult{test=Tests{testID=-1, moduleID=null, testTitle='null', startDateTime='null', endDateTime='null', publishResults=null, scheduled=null, publishGrades=null, practice=null}, testResult=TestResult{testResultID=-1, testID=null, studentID=null, testScore=null}, questions=[QuestionAndAnswer{question=QuestionAndBase64{base64='null', options=null, mathLines=null, question=Question{questionType=null, questionID=-1, questionContent='null', questionFigure=null, maxScore=100, minScore=null, creatorID=null, allThatApply=null}}, answer=null, inputs=null, optionEntries=null}], percentageScore=100.0}", testAndResultObj.toString());
    }
}
