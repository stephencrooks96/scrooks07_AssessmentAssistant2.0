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
public class TestOption {

    private Option optionObj;

    private Long questionID;

    private Long optionID;

    private String option;

    private Integer worthMarks;

    private String feedback;

    @Before
    public void setUp() throws Exception {
        optionObj = new Option();
        this.optionID = 1L;
        this.questionID = 2L;
        this.option = "option";
        this.worthMarks = 1;
        this.feedback = "feedback";
    }

    @Test
    public void testCorrectPointDefaultConstructor() {
        assertNotNull(optionObj);
    }

    @Test
    public void testCorrectPointConstructorWithArgs() {
        optionObj = null;
        optionObj = new Option(questionID, option, worthMarks, feedback);
        assertNotNull(optionObj);
        assertEquals(questionID, optionObj.getQuestionID());
        assertEquals(option, optionObj.getOptionContent());
        assertEquals(worthMarks, optionObj.getWorthMarks());
        assertEquals(feedback, optionObj.getFeedback());
    }

    @Test
    public void testGetSetOptionID() {
        optionObj.setOptionID(optionID);
        assertEquals(optionID, optionObj.getOptionID());
    }

    @Test
    public void testGetSetQuestionID() {
        optionObj.setQuestionID(questionID);
        assertEquals(questionID, optionObj.getQuestionID());
    }

    @Test
    public void testGetSetOptionContent() {
        optionObj.setOptionContent(option);
        assertEquals(option, optionObj.getOptionContent());
    }

    @Test
    public void testGetSetWorthMarks() {
        optionObj.setWorthMarks(worthMarks);
        assertEquals(worthMarks, optionObj.getWorthMarks());
    }

    @Test
    public void testGetSetFeedback() {
        optionObj.setFeedback(feedback);
        assertEquals(feedback, optionObj.getFeedback());
    }

    @Test
    public void testToString() {
        optionObj = new Option(questionID, option, worthMarks, feedback);
        optionObj.setOptionID(optionID);
        assertEquals("Option{optionID=1, questionID=2, optionContent='option', worthMarks=1, feedback='feedback'}", optionObj.toString());
    }
}
