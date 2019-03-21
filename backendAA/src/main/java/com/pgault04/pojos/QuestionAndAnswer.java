package com.pgault04.pojos;

import com.pgault04.entities.Answer;
import com.pgault04.entities.CorrectPoint;
import com.pgault04.entities.Inputs;
import com.pgault04.entities.OptionEntries;

import java.util.List;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 * Pojo to allow collection of question and answer info to be sent to/from front end
 */
public class QuestionAndAnswer {

    private QuestionAndBase64 question;
    private Answer answer;
    private List<Inputs> inputs;
    private List<OptionEntries> optionEntries;
    private List<CorrectPoint> correctPoints;

    /**
     * Default constructor
     */
    public QuestionAndAnswer() {}

    /**
     * Constructor with arguments
     *
     * @param question      - the question
     * @param answer        - the answer
     * @param inputs        - the inputs entered
     * @param optionEntries - options chosen
     * @param correctPoints - correct points for the question
     */
    public QuestionAndAnswer(QuestionAndBase64 question, Answer answer, List<Inputs> inputs, List<OptionEntries> optionEntries, List<CorrectPoint> correctPoints) {
        this.setQuestion(question);
        this.setAnswer(answer);
        this.setInputs(inputs);
        this.setOptionEntries(optionEntries);
        this.setCorrectPoints(correctPoints);
    }

    /**
     * @return gets the question
     */
    public QuestionAndBase64 getQuestion() { return question; }

    /**
     * @param question sets the question
     */
    public void setQuestion(QuestionAndBase64 question) { this.question = question; }

    /**
     * @return gets the answer
     */
    public Answer getAnswer() { return answer; }

    /**
     * @param answer sets the answer
     */
    public void setAnswer(Answer answer) { this.answer = answer; }

    /**
     * @return gets the list of inputs
     */
    public List<Inputs> getInputs() { return inputs; }

    /**
     * @param inputs sets the list of inputs
     */
    public void setInputs(List<Inputs> inputs) { this.inputs = inputs; }

    /**
     * @return returns the list of selected options
     */
    public List<OptionEntries> getOptionEntries() { return optionEntries; }

    /**
     * @param optionEntries sets the list of selected options
     */
    public void setOptionEntries(List<OptionEntries> optionEntries) { this.optionEntries = optionEntries; }

    /**
     * @return gets the list of correct points
     */
    public List<CorrectPoint> getCorrectPoints() { return correctPoints; }

    /**
     * @param correctPoints sets the list of correct points
     */
    public void setCorrectPoints(List<CorrectPoint> correctPoints) { this.correctPoints = correctPoints; }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QuestionAndAnswer{");
        sb.append("question=").append(question);
        sb.append(", answer=").append(answer);
        sb.append(", inputs=").append(inputs);
        sb.append(", optionEntries=").append(optionEntries);
        sb.append('}');
        return sb.toString();
    }
}