package com.pgault04.pojos;

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
public class TestPerformance {

    private Performance performance;
    private TestAndResult testAndResultObj;
    private Double classAverage;

    @Before
    public void setUp() throws Exception {
        this.testAndResultObj = new TestAndResult();
        this.classAverage = 100.0;
        performance = new Performance();
    }

    @Test
    public void testPerformanceDefaultConstructor() {
        assertNotNull(performance);
    }

    @Test
    public void testPerformanceConstructorWithArgs() {
        performance = null;
        performance = new Performance(testAndResultObj, classAverage);

        assertNotNull(performance);
        assertEquals(testAndResultObj, performance.getTestAndResult());
        assertEquals(classAverage, performance.getClassAverage(), 0.0);
    }

    @Test
    public void testGetSetTestAndResult() {
        performance.setTestAndResult(testAndResultObj);
        assertEquals(testAndResultObj, performance.getTestAndResult());
    }

    @Test
    public void testGetSetClassAverage() {
        performance.setClassAverage(classAverage);
        assertEquals(classAverage, performance.getClassAverage());
    }

    @Test
    public void testToString() {
        performance = new Performance(testAndResultObj, classAverage);
        assertEquals("Performance{testAndResult=TestAndResult{test=null, testResult=null, questions=null, answers=null, percentageScore=null}, classAverage=100.0}", performance.toString());
    }
}
