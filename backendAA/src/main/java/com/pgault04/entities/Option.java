/**
 *
 */
package com.pgault04.entities;

/**
 * @author Paul Gault - 40126005
 * @since November 2018
 *
 */
public class Option {

    /**
     * Used as a checker for insertions and updates
     */
    private static final long OPTION_INITIAL_VALUE = -1L;

    private Long optionID = OPTION_INITIAL_VALUE;

    private Long questionID;

    private String optionContent;

    private  Integer correct;

    /**
     * The default constructor
     */
    public Option() {}

    /**
     * Constructor with args
     * @param questionID the question
     * @param optionContent the optionContent text
     */
    public Option(Long questionID, String optionContent, Integer correct) {
        this.setQuestionID(questionID);
        this.setOptionContent(optionContent);
        this.setCorrect(correct);
    }

    /**
     * @return the correct flag
     */
    public Integer getCorrect() { return correct; }

    /**
     * @param correct the correct flag to set
     */
    public void setCorrect(Integer correct) { this.correct = correct; }

    /**
     * @return the optionID
     */
    public Long getOptionID() {
        return optionID;
    }

    /**
     * @param optionID the optionID to set
     */
    public void setOptionID(Long optionID) {
        this.optionID = optionID;
    }

    /**
     * @return the questionID
     */
    public Long getQuestionID() {
        return questionID;
    }

    /**
     * @param questionID the questionID to set
     */
    public void setQuestionID(Long questionID) {
        this.questionID = questionID;
    }

    /**
     * @return the optionContent
     */
    public String getOptionContent() {
        return optionContent;
    }

    /**
     * @param optionContent the optionContent to set
     */
    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }

    /**
     * @return the object as a string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Option{");
        sb.append("optionID=").append(optionID);
        sb.append(", questionID=").append(questionID);
        sb.append(", optionContent='").append(optionContent).append('\'');
        sb.append(", correct=").append(correct);
        sb.append('}');
        return sb.toString();
    }
}
