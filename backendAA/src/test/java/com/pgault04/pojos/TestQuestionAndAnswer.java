package com.pgault04.pojos;

import com.pgault04.entities.*;
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
public class TestQuestionAndAnswer {

    private QuestionAndAnswer questionAndAnswer;
    private Answer answer;
    private QuestionAndBase64 questionAndBase64;
    private List<Inputs> inputs;
    private List<OptionEntries> optionEntries;
    private List<CorrectPoint> correctPoints;

    @Before
    public void setUp() throws Exception {
        this.questionAndAnswer = new QuestionAndAnswer();
        this.answer = new Answer();
        this.inputs = new ArrayList<>();
        this.questionAndBase64 = new QuestionAndBase64();
        this.optionEntries = new ArrayList<>();
        this.correctPoints = new ArrayList<>();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(questionAndAnswer);
    }

    @Test
    public void testConstructorWithArgs() {
        questionAndAnswer = null;
        questionAndAnswer = new QuestionAndAnswer(questionAndBase64, answer, inputs, optionEntries, correctPoints);

        assertNotNull(questionAndAnswer);
        assertEquals(answer, questionAndAnswer.getAnswer());
        assertEquals(inputs, questionAndAnswer.getInputs());
        assertEquals(optionEntries, questionAndAnswer.getOptionEntries());
        assertEquals(correctPoints, questionAndAnswer.getCorrectPoints());
        assertEquals(questionAndBase64, questionAndAnswer.getQuestion());
    }

    @Test
    public void testToString() {
        questionAndAnswer = new QuestionAndAnswer(questionAndBase64, answer, inputs, optionEntries, correctPoints);
        assertEquals("QuestionAndAnswer{question=QuestionAndBase64{base64='null', options=null, mathLines=null, question=null}, answer=Answer{answerID=-1, questionID=null, answererID=null, markerID=null, testID=null, content='null', score=null, feedback='null', markerApproved=null, tutorApproved=null}, inputs=[], optionEntries=[]}", questionAndAnswer.toString());
    }
}
