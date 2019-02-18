package com.pgault04.pojos;

import com.pgault04.entities.Answer;
import com.pgault04.entities.Question;
import com.pgault04.entities.TestResult;
import com.pgault04.entities.Tests;
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
    private List<Answer> answers;
    private List<Question> questions;

    private Tests testsObj;
    private Question questionObj, questionWithScore;
    private Answer answer;
    private TestResult testResultObj;

    @Before
    public void setUp() throws Exception {
        testAndResultObj = new TestAndResult();
        this.perentageScore = 100.0;
        answers = new ArrayList<>();
        questions = new ArrayList<>();
        testsObj = new Tests();
        questionObj = new Question();
        answer = new Answer();
        questionWithScore = new Question();
        questionWithScore.setMaxScore(100);
        testResultObj = new TestResult();
        answers.add(answer);
        questions.add(questionWithScore);
        questions.add(questionObj);
    }

    @Test
    public void testTestAndResultDefaultConstructor() {
        assertNotNull(testAndResultObj);
    }

    @Test @Ignore
    public void testTestAndResultConstructorWithArgs() {
        testAndResultObj = null;
       // testAndResultObj = new TestAndResult(testsObj, testResultObj, questions);

        assertNotNull(testAndResultObj);
        assertEquals(testsObj, testAndResultObj.getTest());
        assertEquals(testResultObj, testAndResultObj.getTestResult());
        assertEquals(questions, testAndResultObj.getQuestions());
       // assertEquals(answers, testAndResultObj.getAnswers());
        assertEquals(100.0, testAndResultObj.getPercentageScore(), 0.0);
    }

    @Test
    public void testGetSetTest() {
        testAndResultObj.setTest(testsObj);
        assertEquals(testsObj, testAndResultObj.getTest());
    }

    @Test
    public void testGetSetTestResult() {
        testAndResultObj.setTestResult(testResultObj);
        assertEquals(testResultObj, testAndResultObj.getTestResult());
    }

    @Test @Ignore
    public void testGetSetQuestions() {
       // testAndResultObj.setQuestions(questions);
        assertEquals(questions, testAndResultObj.getQuestions());
    }

    @Test @Ignore
    public void testGetSetAnswers() {
      //  testAndResultObj.setAnswers(answers);
      //  assertEquals(answers, testAndResultObj.getAnswers());
    }

    @Test
    public void testGetSetPercentageScore() {
        testAndResultObj.setPercentageScore(perentageScore);
        assertEquals(perentageScore, testAndResultObj.getPercentageScore());
    }

    @Test @Ignore
    public void testToString() {
       // testAndResultObj = new TestAndResult(testsObj, testResultObj, questions, answers);
        assertEquals("TestAndResult{test=Tests{testID=-1, moduleID=null, testTitle='null', startDateTime='null', endDateTime='null', publishResults=null, scheduled=null, publishGrades=null}, testResult=TestResult{testResultID=-1, testID=null, studentID=null, testScore=null}, questions=[Question{questionType=null, questionID=-1, questionContent='null', questionFigure='null', maxScore=100, creatorID=null}, Question{questionType=null, questionID=-1, questionContent='null', questionFigure='null', maxScore=null, creatorID=null}], answers=[Answer{answerID=-1, questionID=null, answererID=null, markerID=null, testID=null, content='null', score=null}], percentageScore=100.0}", testAndResultObj.toString());
    }
}
