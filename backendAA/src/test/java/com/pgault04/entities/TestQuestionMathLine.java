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
public class TestQuestionMathLine {

    private QuestionMathLine questionMathLine;
    private Long questionMathLineID, questionID;
    private Integer indexedAt;
    private String content;


    @Before
    public void setUp() throws Exception {
        questionMathLine = new QuestionMathLine();
        this.questionID = 1L;
        this.questionMathLineID = 2L;
        this.indexedAt = 5;
        this.content = "value";
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(questionMathLine);
    }

    @Test
    public void testConstructorWithArgs() {
        questionMathLine = null;
        questionMathLine = new QuestionMathLine(questionID, content, indexedAt);

        questionMathLine.setQuestionMathLineID(questionMathLineID);
        assertNotNull(questionMathLine);
        assertEquals(indexedAt, questionMathLine.getIndexedAt());
        assertEquals(questionID, questionMathLine.getQuestionID());
        assertEquals(content, questionMathLine.getContent());
        assertEquals(questionMathLineID, questionMathLine.getQuestionMathLineID());
    }

    @Test
    public void testToString() {
        questionMathLine.setQuestionID(questionID);
        questionMathLine.setQuestionMathLineID(questionMathLineID);
        questionMathLine.setIndexedAt(indexedAt);
        questionMathLine.setContent(content);

        assertEquals("QuestionMathLine{questionMathLineID=2, questionID=1, content='value', indexedAt=5}", questionMathLine.toString());
    }
}
