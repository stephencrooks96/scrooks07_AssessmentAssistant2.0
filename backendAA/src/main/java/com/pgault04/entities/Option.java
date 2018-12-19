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

    private String option;

    /**
     * The default constructor
     */
    public Option() {}

    /**
     * Constructor with args
     * @param optionID the option's id
     * @param questionID the question
     * @param option the option text
     */
    public Option(Long optionID, Long questionID, String option) {
        this.setOptionID(optionID);
        this.setQuestionID(questionID);
        this.setOption(option);
    }

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
     * @return the option
     */
    public String getOption() {
        return option;
    }

    /**
     * @param option the option to set
     */
    public void setOption(String option) {
        this.option = option;
    }

    /**
     * @return the object as a string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Option{");
        sb.append("optionID=").append(optionID);
        sb.append(", questionID=").append(questionID);
        sb.append(", option='").append(option).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
