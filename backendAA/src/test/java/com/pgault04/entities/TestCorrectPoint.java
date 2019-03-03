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

    private List<Alternative> alts;

    @Before
    public void setUp() throws Exception {
        correctPointObj = new CorrectPoint();
        this.correctPointID = 1L;
        this.questionID = 2L;
        this.phrase = "phrase";
        this.feedback = "feedback";
        this.marksWorth = 0.0;
        this.alts = new ArrayList<>();
    }

    @Test
    public void testCorrectPointDefaultConstructor() {
        assertNotNull(correctPointObj);
    }

    @Test
    public void testCorrectPointConstructorWithArgs() {
        correctPointObj = null;
        correctPointObj = new CorrectPoint(questionID, phrase, marksWorth, feedback, alts, 0, 0);
        assertNotNull(correctPointObj);
        assertEquals(questionID, correctPointObj.getQuestionID());
        assertEquals(phrase, correctPointObj.getPhrase());
        assertEquals(marksWorth, correctPointObj.getMarksWorth());
        assertEquals(feedback, correctPointObj.getFeedback());
        assertEquals(alts, correctPointObj.getAlternatives());
    }

    @Test
    public void testGetSetCorrectPointID() {
        correctPointObj.setCorrectPointID(correctPointID);
        assertEquals(correctPointID, correctPointObj.getCorrectPointID());
    }

    @Test
    public void testGetSetQuestionID() {
        correctPointObj.setQuestionID(questionID);
        assertEquals(questionID, correctPointObj.getQuestionID());
    }

    @Test
    public void testGetSetPhrase() {
        correctPointObj.setPhrase(phrase);
        assertEquals(phrase, correctPointObj.getPhrase());
    }

    @Test
    public void testGetSetMarksWorth() {
        correctPointObj.setMarksWorth(marksWorth);
        assertEquals(marksWorth, correctPointObj.getMarksWorth());
    }

    @Test
    public void testGetSetFeedback() {
        correctPointObj.setFeedback(feedback);
        assertEquals(feedback, correctPointObj.getFeedback());
    }

    @Test
    public void testGetSetAlternatives() {
        correctPointObj.setAlternatives(alts);
        assertEquals(alts, correctPointObj.getAlternatives());
    }

    @Test
    public void testToString() {
        correctPointObj = new CorrectPoint(questionID, phrase, marksWorth, feedback, alts, 0, 0);
        correctPointObj.setCorrectPointID(correctPointID);
        assertEquals("CorrectPoint{correctPointID=1, questionID=2, phrase='phrase', marksWorth=0.0, feedback='feedback', alternatives=[]}", correctPointObj.toString());
    }
}
