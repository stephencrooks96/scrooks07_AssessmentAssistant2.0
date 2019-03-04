package com.pgault04.pojos;

import com.pgault04.entities.CorrectPoint;
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
    private QuestionMathLine mathLine;
    private CorrectPoint correctPoint;
    private List<Option> options;
    private List<CorrectPoint> correctPoints;
    private String base64;
    private List<QuestionMathLine> mathLines;

    @Before
    public void setUp() throws Exception {
        this.tutorQuestionPojoObj = new TutorQuestionPojo();
        this.testID = 1L;
        this.question = new Question();
        this.option = new Option();
        this.correctPoint = new CorrectPoint();
        this.options = new ArrayList<>();
        this.correctPoints = new ArrayList<>();
        this.mathLines = new ArrayList<>();
        this.base64 = "base64";
        mathLines.add(mathLine);
        options.add(option);
        correctPoints.add(correctPoint);
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(tutorQuestionPojoObj);
    }

    @Test
    public void testConstructorWithArgs() {
        tutorQuestionPojoObj = null;
        tutorQuestionPojoObj = new TutorQuestionPojo(testID, base64, question, options, mathLines, correctPoints);

        assertNotNull(tutorQuestionPojoObj);
        assertEquals(testID, tutorQuestionPojoObj.getTestID());
        assertEquals(base64, tutorQuestionPojoObj.getBase64());
        assertEquals(mathLines, tutorQuestionPojoObj.getMathLines());
        assertEquals(question, tutorQuestionPojoObj.getQuestion());
        assertEquals(options, tutorQuestionPojoObj.getOptions());
        assertEquals(correctPoints, tutorQuestionPojoObj.getCorrectPoints());
    }

    @Test
    public void testToString() {
        tutorQuestionPojoObj = new TutorQuestionPojo(testID, base64, question, options, mathLines, correctPoints);
        assertEquals("TutorQuestionPojo{testID=1, question=Question{questionType=null, questionID=-1, questionContent='null', questionFigure=null, maxScore=null, minScore=null, creatorID=null, allThatApply=null}, options=[Option{optionID=-1, questionID=null, optionContent='null', worthMarks=null, feedback='null'}], correctPoints=[CorrectPoint{correctPointID=-1, questionID=null, phrase='null', marksWorth=null, feedback='null', alternatives=null, indexedAt=null, math=null}], mathLines=[null], base64='base64'}", tutorQuestionPojoObj.toString());
    }
}
