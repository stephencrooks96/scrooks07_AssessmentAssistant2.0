package com.pgault04.pojos;

import com.pgault04.entities.Tests;
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
public class TestTestMarking {

    private TestMarking testMarkingObj;
    private Tests testObj;
    private Integer toBeMarkedByYou;
    private Integer toBeMarkedByTAs;
    private Integer marked;
    private Integer totalForYou;
    private Integer totalForTAs;

    @Before
    public void setUp() throws Exception {
        this.testMarkingObj = new TestMarking();
        this.testObj = new Tests();
        this.toBeMarkedByYou = 100;
        this.toBeMarkedByTAs = 50;
        this.marked = 30;
        this.totalForYou = 100;
        this.totalForTAs = 100;
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(testMarkingObj);
    }

    @Test
    public void testConstructorWithArgs() {
        testMarkingObj = null;
        testMarkingObj = new TestMarking(testObj, toBeMarkedByYou, toBeMarkedByTAs, marked, totalForYou, totalForTAs);

        assertNotNull(testMarkingObj);
        assertEquals(testObj, testMarkingObj.getTest());
        assertEquals(toBeMarkedByYou, testMarkingObj.getToBeMarkedByYou());
        assertEquals(toBeMarkedByTAs, testMarkingObj.getToBeMarkedByTAs());
        assertEquals(marked, testMarkingObj.getMarked());
        assertEquals(totalForYou, testMarkingObj.getTotalForYou());
        assertEquals(totalForTAs, testMarkingObj.getTotalForTAs());
    }

    @Test
    public void testToString() {
        testMarkingObj = new TestMarking(testObj, toBeMarkedByYou, toBeMarkedByTAs, marked, totalForYou, totalForTAs);
        assertEquals("TestMarking{test=Tests{testID=-1, moduleID=null, testTitle='null', startDateTime='null', endDateTime='null', publishResults=null, scheduled=null, publishGrades=null, practice=null}, toBeMarkedByYou=100, toBeMarkedByTAs=50, marked=30, totalForYou=100, totalForTAs=100}", testMarkingObj.toString());
    }
}
