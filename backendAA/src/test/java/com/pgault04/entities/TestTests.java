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
public class TestTests {

    private Tests testsObj;

    private Long testID;

    private Long moduleID;

    private String testTitle;

    private String startDateTime;

    private String endDateTime;

    private Integer publishResults;

    private Integer scheduled;

    private Integer publishGrades;

    private Integer practice;

    @Before
    public void setUp() throws Exception {
        this.testsObj = new Tests();
        this.testID = 1L;
        this.moduleID = 2L;
        this.testTitle = "testTitle";
        this.startDateTime = "startDateTime";
        this.endDateTime = "endDateTime";
        this.publishResults = 1;
        this.scheduled = 2;
        this.publishGrades = 1;
        this.practice = 1;
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(testsObj);
    }

    @Test
    public void testConstructorWithArgs() {
        testsObj = null;
        testsObj = new Tests(moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades, practice);
        testsObj.setTestID(testID);
        assertNotNull(testsObj);
        assertEquals(testID, testsObj.getTestID());
        assertEquals(moduleID, testsObj.getModuleID());
        assertEquals(testTitle, testsObj.getTestTitle());
        assertEquals(startDateTime, testsObj.getStartDateTime());
        assertEquals(endDateTime, testsObj.getEndDateTime());
        assertEquals(publishResults, testsObj.getPublishResults());
        assertEquals(scheduled, testsObj.getScheduled());
        assertEquals(publishGrades, testsObj.getPublishGrades());
        assertEquals(practice, testsObj.getPractice());
    }

    @Test
    public void testToString() {
        testsObj = new Tests(moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades, practice);
        testsObj.setTestID(testID);
        assertEquals("Tests{testID=1, moduleID=2, testTitle='testTitle', startDateTime='startDateTime', endDateTime='endDateTime', publishResults=1, scheduled=2, publishGrades=1, practice=1}", testsObj.toString());
    }
}