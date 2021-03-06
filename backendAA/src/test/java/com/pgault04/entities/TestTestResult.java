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
public class TestTestResult {

    private TestResult testResultObj;

    private Long testResultID;

    private Long testID;

    private Long studentID;

    private Integer testScore;

    @Before
    public void setUp() throws Exception {
        this.testResultObj = new TestResult();
        this.testResultID = 1L;
        this.testID = 2L;
        this.studentID = 3L;
        this.testScore = 4;
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(testResultObj);
    }

    @Test
    public void testConstructorWithArgs() {
        testResultObj = null;
        testResultObj = new TestResult(testID, studentID, testScore);

        assertNotNull(testResultObj);
        assertEquals(testID, testResultObj.getTestID());
        assertEquals(studentID, testResultObj.getStudentID());
        assertEquals(testScore, testResultObj.getTestScore());
    }

    @Test
    public void testGetSetTestResultID() {
        testResultObj.setTestResultID(testResultID);
        assertEquals(testResultID, testResultObj.getTestResultID());
    }

    @Test
    public void testToString() {
        testResultObj = new TestResult(testID, studentID, testScore);
        testResultObj.setTestResultID(testResultID);
        assertEquals("TestResult{testResultID=1, testID=2, studentID=3, testScore=4}", testResultObj.toString());
    }
}