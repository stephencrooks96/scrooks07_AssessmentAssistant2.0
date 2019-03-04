package com.pgault04.entities;

import org.junit.Before;
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
public class TestCorrectPoint {

    private CorrectPoint correctPointObj;

    private Long correctPointID, questionID;

    private String phrase, feedback;

    private Double marksWorth;

    private Integer indexedAt, math;

    private List<Alternative> alts;

    @Before
    public void setUp() throws Exception {
        correctPointObj = new CorrectPoint();
        this.correctPointID = 1L;
        this.questionID = 2L;
        this.phrase = "phrase";
        this.feedback = "feedback";
        this.marksWorth = 0.0;
        this.indexedAt = 1;
        this.math = 1;
        this.alts = new ArrayList<>();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(correctPointObj);
    }

    @Test
    public void testConstructorWithArgs() {
        correctPointObj = null;
        correctPointObj = new CorrectPoint(questionID, phrase, marksWorth, feedback, alts, indexedAt, math);
        assertNotNull(correctPointObj);
        assertEquals(questionID, correctPointObj.getQuestionID());
        assertEquals(phrase, correctPointObj.getPhrase());
        assertEquals(marksWorth, correctPointObj.getMarksWorth());
        assertEquals(feedback, correctPointObj.getFeedback());
        assertEquals(alts, correctPointObj.getAlternatives());
        assertEquals(indexedAt, correctPointObj.getIndexedAt());
        assertEquals(math, correctPointObj.getMath());
    }

    @Test
    public void testGetSetCorrectPointID() {
        correctPointObj.setCorrectPointID(correctPointID);
        assertEquals(correctPointID, correctPointObj.getCorrectPointID());
    }

    @Test
    public void testToString() {
        correctPointObj = new CorrectPoint(questionID, phrase, marksWorth, feedback, alts, 0, 0);
        correctPointObj.setCorrectPointID(correctPointID);
        assertEquals("CorrectPoint{correctPointID=1, questionID=2, phrase='phrase', marksWorth=0.0, feedback='feedback', alternatives=[], indexedAt=0, math=0}", correctPointObj.toString());
    }
}
