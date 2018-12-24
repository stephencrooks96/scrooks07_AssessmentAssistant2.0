package com.pgault04.pojos;

import com.pgault04.entities.CorrectPoint;
import com.pgault04.entities.Option;
import com.pgault04.entities.Question;
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
public class TestTutorQuestionPojo {

    private TutorQuestionPojo tutorQuestionPojoObj;
    private Long testID;
    private Question question;
    private Option option;
    private CorrectPoint correctPoint;
    private List<Option> options;
    private List<CorrectPoint> correctPoints;

    @Before
    public void setUp() throws Exception {
        this.tutorQuestionPojoObj = new TutorQuestionPojo();
        this.testID = 1L;
        this.question = new Question();
        this.option = new Option();
        this.correctPoint = new CorrectPoint();
        this.options = new ArrayList<>();
        this.correctPoints = new ArrayList<>();
        options.add(option);
        correctPoints.add(correctPoint);
    }

    @Test
    public void testTutorQuestionPojoDefaultConstructor() {
        assertNotNull(tutorQuestionPojoObj);
    }

    @Test
    public void testTutorQuestionPojoConstructorWithArgs() {
        tutorQuestionPojoObj = null;
        tutorQuestionPojoObj = new TutorQuestionPojo(testID, question, options, correctPoints);

        assertNotNull(tutorQuestionPojoObj);
        assertEquals(testID, tutorQuestionPojoObj.getTestID());
        assertEquals(question, tutorQuestionPojoObj.getQuestion());
        assertEquals(options, tutorQuestionPojoObj.getOptions());
        assertEquals(correctPoints, tutorQuestionPojoObj.getCorrectPoints());
    }

    @Test
    public void testGetSetTestID() {
        tutorQuestionPojoObj.setTestID(testID);
        assertEquals(testID, tutorQuestionPojoObj.getTestID());
    }

    @Test
    public void testGetSetQuestion() {
        tutorQuestionPojoObj.setQuestion(question);
        assertEquals(question, tutorQuestionPojoObj.getQuestion());
    }

    @Test
    public void testGetSetOptions() {
        tutorQuestionPojoObj.setOptions(options);
        assertEquals(options, tutorQuestionPojoObj.getOptions());
    }

    @Test
    public void testGetSetCorrectPoints() {
        tutorQuestionPojoObj.setCorrectPoints(correctPoints);
        assertEquals(correctPoints, tutorQuestionPojoObj.getCorrectPoints());
    }

    @Test
    public void testToString() {
        tutorQuestionPojoObj = new TutorQuestionPojo(testID, question, options, correctPoints);
        assertEquals("TutorQuestionPojo{testID=1, question=Question{questionType=null, questionID=-1, questionContent='null', questionFigure='null', maxScore=null, creatorID=null}, options=[Option{optionID=-1, questionID=null, optionContent='null', correct=null}], correctPoints=[CorrectPoint{correctPointID=-1, questionID=null, phrase='null', marksWorth=null, feedback='null', alternatives=null}]}", tutorQuestionPojoObj.toString());
    }
}
