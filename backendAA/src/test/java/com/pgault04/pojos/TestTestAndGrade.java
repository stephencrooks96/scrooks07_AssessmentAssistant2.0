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
public class TestTestAndGrade {

    private TestAndGrade testAndGradeObj;
    private Tests testObj;
    private String grade;

    @Before
    public void setUp() throws Exception {
        this.testAndGradeObj = new TestAndGrade();
        this.testObj = new Tests();
        this.grade = "A";
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(testAndGradeObj);
    }

    @Test
    public void testConstructorWithArgs() {
        testAndGradeObj = null;
        testAndGradeObj = new TestAndGrade(testObj, grade);

        assertNotNull(testAndGradeObj);
        assertEquals(testObj, testAndGradeObj.getTest());
        assertEquals(grade, testAndGradeObj.getGrade());
    }

    @Test
    public void testToString() {
        testAndGradeObj = new TestAndGrade(testObj, grade);
        assertEquals("TestAndGrade{test=Tests{testID=-1, moduleID=null, testTitle='null', startDateTime='null', endDateTime='null', publishResults=null, scheduled=null, publishGrades=null, practice=null}, grade='A'}", testAndGradeObj.toString());
    }
}
