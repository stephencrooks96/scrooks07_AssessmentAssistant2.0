package com.pgault04.pojos;

import com.pgault04.entities.Option;
import com.pgault04.entities.Question;
import com.pgault04.entities.QuestionMathLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQuestionAndBase64 {

    private QuestionAndBase64 questionAndBase64;
    private String base64;
    private List<Option> options;
    private List<QuestionMathLine> mathLines;
    private Question question;

    @Before
    public void setUp() throws Exception {
        this.questionAndBase64 = new QuestionAndBase64();
        this.base64 = "base64";
        this.options = new ArrayList<>();
        this.mathLines = new ArrayList<>();
        this.question = new Question();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(questionAndBase64);
    }

    @Test
    public void testConstructorWithArgs() {
        questionAndBase64 = null;
        questionAndBase64 = new QuestionAndBase64(base64, options, mathLines, question);

        assertNotNull(questionAndBase64);
        assertEquals(base64, questionAndBase64.getBase64());
        assertEquals(options, questionAndBase64.getOptions());
        assertEquals(mathLines, questionAndBase64.getMathLines());
        assertEquals(question, questionAndBase64.getQuestion());
    }

    @Test
    public void testToString() {
        questionAndBase64 = new QuestionAndBase64(base64, options, mathLines, question);
        assertEquals("QuestionAndBase64{base64='base64', options=[], mathLines=[], question=Question{questionType=null, questionID=-1, questionContent='null', questionFigure=null, maxScore=null, minScore=null, creatorID=null, allThatApply=null}}", questionAndBase64.toString());
    }
}
