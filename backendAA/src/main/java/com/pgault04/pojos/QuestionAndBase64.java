package com.pgault04.pojos;

import com.pgault04.entities.Option;
import com.pgault04.entities.Question;
import com.pgault04.entities.QuestionMathLine;

import java.util.List;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 * Pojo to allow collection of question info to be sent to/from front end
 */
public class QuestionAndBase64 {

    private String base64;
    private List<Option> options;
    private List<QuestionMathLine> mathLines;
    private Question question;

    /**
     * Default constructor
     */
    public QuestionAndBase64() {}

    /**
     * Constructor with arguments
     *
     * @param base64    - the question image as string
     * @param options   - the options for the question
     * @param mathLines - the math lines for the question
     * @param question  - the question
     */
    public QuestionAndBase64(String base64, List<Option> options, List<QuestionMathLine> mathLines, Question question) {
        this.setBase64(base64);
        this.setOptions(options);
        this.setMathLines(mathLines);
        this.setQuestion(question);
    }

    /**
     * @return gets the image as string
     */
    public String getBase64() { return base64; }

    /**
     * @param base64 sets the image as string
     */
    public void setBase64(String base64) { this.base64 = base64; }

    /**
     * @return gets the list of options for the question
     */
    public List<Option> getOptions() { return options; }

    /**
     * @param options sets the list of options for the question
     */
    public void setOptions(List<Option> options) { this.options = options; }

    /**
     * @return math lines
     */
    public List<QuestionMathLine> getMathLines() { return mathLines; }

    /**
     * @param mathLines - The math lines for the question
     */
    public void setMathLines(List<QuestionMathLine> mathLines) { this.mathLines = mathLines; }

    /**
     * @return gets the question
     */
    public Question getQuestion() { return question; }

    /**
     * @param question sets the question
     */
    public void setQuestion(Question question) { this.question = question; }

    /*
     * the object as stirng
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QuestionAndBase64{");
        sb.append("base64='").append(base64).append('\'');
        sb.append(", options=").append(options);
        sb.append(", mathLines=").append(mathLines);
        sb.append(", question=").append(question);
        sb.append('}');
        return sb.toString();
    }
}