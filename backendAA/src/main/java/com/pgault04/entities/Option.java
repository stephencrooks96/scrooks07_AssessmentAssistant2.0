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

    private  Integer worthMarks;

    private String feedback;

    /**
     * The default constructor
     */
    public Option() {}

    /**
     * Constructor with args
     * @param questionID the question
     * @param optionContent the optionContent text
     */
    public Option(Long questionID, String optionContent, Integer worthMarks, String feedback) {
        this.setQuestionID(questionID);
        this.setOptionContent(optionContent);
        this.setWorthMarks(worthMarks);
        this.setFeedback(feedback);
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     * @return the marks it is worth
     */
    public Integer getWorthMarks() { return worthMarks; }

    /**
     * @param worthMarks the marks worth to set
     */
    public void setWorthMarks(Integer worthMarks) { this.worthMarks = worthMarks; }

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
        sb.append(", worthMarks=").append(worthMarks);
        sb.append(", feedback='").append(feedback).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
