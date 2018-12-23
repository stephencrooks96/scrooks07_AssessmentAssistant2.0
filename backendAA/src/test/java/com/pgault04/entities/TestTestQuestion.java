package com.pgault04.entities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTestQuestion {

    private TestQuestion testQuestionObj;

    private Long testQuestionID;

    private Long testID;

    private Long questionID;

    @Before
    public void setUp() throws Exception {
        this.testQuestionObj = new TestQuestion();
        this.testQuestionID = 0L;
        this.testID = 1L;
        this.questionID = 2L;
    }

    @Test
    public void testTestQuestionDefaultConstructor() {
        assertNotNull(testQuestionObj);
    }

    @Test
    public void testTestQuestionConstructorWithArgs() {
        testQuestionObj = null;
        testQuestionObj = new TestQuestion(testID, questionID);

        assertNotNull(testQuestionObj);
        assertEquals(testID, testQuestionObj.getTestID());
        assertEquals(questionID, testQuestionObj.getQuestionID());
    }

    @Test
    public void testGetSetTestQuestionID() {
        testQuestionObj.setTestQuestionID(testQuestionID);
        assertEquals(testQuestionID, testQuestionObj.getTestQuestionID());

    }

    @Test
    public void testGetSetTestID() {
        testQuestionObj.setTestID(testID);
        assertEquals(testID, testQuestionObj.getTestID());
    }

    @Test
    public void testGetSetQuestionID() {
        testQuestionObj.setQuestionID(questionID);
        assertEquals(questionID, testQuestionObj.getQuestionID());
    }

    @Test
    public void testToString() {
        testQuestionObj = new TestQuestion(testID, questionID);
        testQuestionObj.setTestQuestionID(testQuestionID);
        assertEquals("TestQuestion{testQuestionID=0, testID=1, questionID=2}", testQuestionObj.toString());
    }
}