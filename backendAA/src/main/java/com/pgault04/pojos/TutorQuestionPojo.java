package com.pgault04.pojos;

import com.pgault04.entities.CorrectPoint;
import com.pgault04.entities.Option;
import com.pgault04.entities.Question;
import com.pgault04.entities.QuestionMathLine;

import java.util.List;

/**
 * Class for facilitating the retrieval of form data from the front-end
 *
 * @author Paul Gault - 40126005
 * @since November 2018
 */
public class TutorQuestionPojo {

    private Long testID;

    private Question question;

    private List<Option> options;

    private List<CorrectPoint> correctPoints;

    private List<QuestionMathLine> mathLines;

    private String base64;

    /**
     * Default constructor
     */
    public TutorQuestionPojo() {}

    /**
     * Constructor with args
     *
     * @param testID        the test
     * @param question      the question
     * @param options       the options
     * @param mathLines     question math lines
     * @param correctPoints the correct points
     */
    public TutorQuestionPojo(Long testID, String base64, Question question, List<Option> options, List<QuestionMathLine> mathLines, List<CorrectPoint> correctPoints) {
        this.setTestID(testID);
        this.setQuestion(question);
        this.setBase64(base64);
        this.setOptions(options);
        this.setMathLines(mathLines);
        this.setCorrectPoints(correctPoints);
    }

    /**
     * @return the base64 encoded string for the question
     */
    public String getBase64() { return base64; }

    /**
     * @param base64 sets the base64 encoded string for the question image
     */
    public void setBase64(String base64) { this.base64 = base64; }

    /**
     * @return the options
     */
    public List<Option> getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(List<Option> options) {
        this.options = options;
    }

    /**
     * @return the testID
     */
    public Long getTestID() {
        return testID;
    }

    /**
     * @param testID the testID to set
     */
    public void setTestID(Long testID) {
        this.testID = testID;
    }

    /**
     * @return the question
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(Question question) {
        this.question = question;
    }

    /**
     * @return the list of math lines
     */
    public List<QuestionMathLine> getMathLines() { return mathLines; }

    /**
     * @param mathLines the list of math lines
     */
    public void setMathLines(List<QuestionMathLine> mathLines) { this.mathLines = mathLines; }

    /**
     * @return the correctPoints
     */
    public List<CorrectPoint> getCorrectPoints() {
        return correctPoints;
    }

    /**
     * @param correctPoints the correctPoints to set
     */
    public void setCorrectPoints(List<CorrectPoint> correctPoints) {
        this.correctPoints = correctPoints;
    }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TutorQuestionPojo{");
        sb.append("testID=").append(testID);
        sb.append(", question=").append(question);
        sb.append(", options=").append(options);
        sb.append(", correctPoints=").append(correctPoints);
        sb.append(", mathLines=").append(mathLines);
        sb.append(", base64='").append(base64).append('\'');
        sb.append('}');
        return sb.toString();
    }
}