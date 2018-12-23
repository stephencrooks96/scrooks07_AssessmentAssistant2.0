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

    @Before
    public void setUp() throws Exception {
        this.testsObj = new Tests();
        this.moduleID = 2L;
        this.testTitle = "testTitle";
        this.startDateTime = "startDateTime";
        this.endDateTime = "endDateTime";
        this.publishResults = 1;
        this.scheduled = 2;
        this.publishGrades = 1;
    }

    @Test
    public void testTestDefaultConstructor() {
        assertNotNull(testsObj);
    }

    @Test
    public void testTestConstructorWithArgs() {
        testsObj = null;
        testsObj = new Tests(moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades);

        assertNotNull(testsObj);
        assertEquals(moduleID, testsObj.getModuleID());
        assertEquals(testTitle, testsObj.getTestTitle());
        assertEquals(startDateTime, testsObj.getStartDateTime());
        assertEquals(endDateTime, testsObj.getEndDateTime());
        assertEquals(publishResults, testsObj.getPublishResults());
        assertEquals(scheduled, testsObj.getScheduled());
        assertEquals(publishGrades, testsObj.getPublishGrades());
    }

    @Test
    public void testGetSetTestID() {
        testsObj.setTestID(testID);
        assertEquals(testID, testsObj.getTestID());
    }

    @Test
    public void testGetSetModuleID() {
        testsObj.setModuleID(moduleID);
        assertEquals(moduleID, testsObj.getModuleID());
    }

    @Test
    public void testGetSetTestTitle() {
        testsObj.setTestTitle(testTitle);
        assertEquals(testTitle, testsObj.getTestTitle());
    }

    @Test
    public void testGetSetStartDateTime() {
        testsObj.setStartDateTime(startDateTime);
        assertEquals(startDateTime, testsObj.getStartDateTime());
    }

    @Test
    public void testGetSetEndDateTime() {
        testsObj.setEndDateTime(endDateTime);
        assertEquals(endDateTime, testsObj.getEndDateTime());
    }

    @Test
    public void testGetSetPublishResults() {
        testsObj.setPublishResults(publishResults);
        assertEquals(publishResults, testsObj.getPublishResults());
    }

    @Test
    public void testGetSetScheduled() {
        testsObj.setScheduled(scheduled);
        assertEquals(scheduled, testsObj.getScheduled());
    }

    @Test
    public void testToString() {
        testsObj = new Tests(moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades);
        testsObj.setTestID(testID);
        assertEquals("Tests{testID=null, moduleID=2, testTitle='testTitle', startDateTime='startDateTime', endDateTime='endDateTime', publishResults=1, scheduled=2, publishGrades=1}", testsObj.toString());
    }
}