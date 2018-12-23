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
 * @since December 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAlternative {

    private Alternative alternative;
    private Long alternativeID, correctPointID, alternativeIDInit;
    private String alternativePhrase;

    @Before
    public void setUp() throws Exception {
        alternative = new Alternative();
        this.alternativeIDInit = -1L;
        this.alternativeID = 1L;
        this.correctPointID = 2L;
        this.alternativePhrase = "alt";
    }

    @Test
    public void testAlternativeDefaultConstructor() {
        assertNotNull(alternative);
    }

    @Test
    public void testAlternativeConstructorWithArgs() {
        alternative = null;
        alternative = new Alternative(correctPointID, alternativePhrase);

        assertNotNull(alternative);
        assertEquals(alternativeIDInit, alternative.getAlternativeID());
        assertEquals(correctPointID, alternative.getCorrectPointID());
        assertEquals(alternativePhrase, alternative.getAlternativePhrase());
    }

    @Test
    public void testGetSetCorrectPointID() {
        alternative.setCorrectPointID(correctPointID);
        assertEquals(correctPointID, alternative.getCorrectPointID());
    }

    @Test
    public void testGetSetAlternativePhrase() {
        alternative.setAlternativePhrase(alternativePhrase);
        assertEquals(alternativePhrase, alternative.getAlternativePhrase());
    }

    @Test
    public void testGetSetAlternativeID() {
        alternative.setAlternativeID(alternativeID);
        assertEquals(alternativeID, alternative.getAlternativeID());
    }

    @Test
    public void testToString() {
        alternative.setAlternativeID(alternativeID);
        alternative.setCorrectPointID(correctPointID);
        alternative.setAlternativePhrase(alternativePhrase);
        assertEquals("Alternative{alternativeID=1, correctPointID=2, alternativePhrase='alt'}", alternative.toString());
    }

}
