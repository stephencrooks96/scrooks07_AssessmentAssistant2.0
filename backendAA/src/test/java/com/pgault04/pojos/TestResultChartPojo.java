package com.pgault04.pojos;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestResultChartPojo {

    private ResultChartPojo testResultChartPojo;
    private LinkedList<String> labels;
    private LinkedList<Integer> scores;
    private Integer classAverage;
    private LinkedList<String> colors;

    @Before
    public void setUp() throws Exception {
        this.testResultChartPojo = new ResultChartPojo();
        this.labels = new LinkedList<>();
        this.scores = new LinkedList<>();
        this.classAverage = 1;
        this.colors = new LinkedList<>();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(testResultChartPojo);
    }

    @Test
    public void testConstructorWithArgs() {
        testResultChartPojo = null;
        testResultChartPojo = new ResultChartPojo(labels, scores, classAverage, colors);

        assertNotNull(testResultChartPojo);
        assertEquals(labels, testResultChartPojo.getLabels());
        assertEquals(scores, testResultChartPojo.getScores());
        assertEquals(classAverage, testResultChartPojo.getClassAverage());
        assertEquals(colors, testResultChartPojo.getColors());
    }

    @Test
    public void testToString() {
        testResultChartPojo = new ResultChartPojo(labels, scores, classAverage, colors);
        assertEquals("ResultChartPojo{labels=[], scores=[], classAverage=1, colors=[]}", testResultChartPojo.toString());
    }
}
