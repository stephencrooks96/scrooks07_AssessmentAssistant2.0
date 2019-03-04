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
public class TestInputs {

    private Inputs input;
    private Long inputID, answerID;
    private Integer inputIndex, math;
    private String inputValue;


    @Before
    public void setUp() throws Exception {
        input = new Inputs();
        this.answerID = 1L;
        this.inputID = 2L;
        this.inputIndex = 5;
        this.math = 1;
        this.inputValue = "value";
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(input);
    }

    @Test
    public void testConstructorWithArgs() {
        input = null;
        input = new Inputs(inputValue, inputIndex, answerID, math);

        assertNotNull(input);
        assertEquals(inputValue, input.getInputValue());
        assertEquals(inputIndex, input.getInputIndex());
        assertEquals(answerID, input.getAnswerID());
        assertEquals(math, input.getMath());
    }

    @Test
    public void testGetSetInputID() {
        input.setInputID(inputID);
        assertEquals(inputID, input.getInputID());
    }

    @Test
    public void testToString() {
        input.setAnswerID(answerID);
        input.setInputID(inputID);
        input.setMath(math);
        input.setInputIndex(inputIndex);
        input.setInputValue(inputValue);

        assertEquals("Inputs{inputID=2, inputValue='value', inputIndex=5, answerID=1, math=1}", input.toString());
    }
}
