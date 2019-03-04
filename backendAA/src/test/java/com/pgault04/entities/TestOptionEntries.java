package com.pgault04.entities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOptionEntries {

    private OptionEntries oe;
    private Long optionEntryID, optionID, answerID;


    @Before
    public void setUp() throws Exception {
        oe = new OptionEntries();
        this.answerID = 1L;
        this.optionEntryID = 2L;
        this.optionID = 5L;
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(oe);
    }

    @Test
    public void testConstructorWithArgs() {
        oe = null;
        oe = new OptionEntries(optionID, answerID);
        oe.setOptionEntryID(optionEntryID);
        assertNotNull(oe);
        assertEquals(optionEntryID, oe.getOptionEntryID());
        assertEquals(optionID, oe.getOptionID());
        assertEquals(answerID, oe.getAnswerID());
    }

    @Test
    public void testToString() {
        oe.setAnswerID(answerID);
        oe.setOptionEntryID(optionEntryID);
        oe.setOptionID(optionID);

        assertEquals("OptionEntries{optionEntryID=2, optionID=5, answerID=1}", oe.toString());
    }
}
