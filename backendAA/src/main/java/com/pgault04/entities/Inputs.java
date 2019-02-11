package com.pgault04.entities;

/**
 * @author Paul Gault
 * @since Jan 19
 */
public class Inputs {

    private static final long ENTRY_CONST = -1L;
    private Long inputID = ENTRY_CONST;
    private String inputValue;
    private Integer inputIndex;
    private Long answerID;

    public Inputs() {}

    /**
     * Constructor with args
     * @param inputValue the input value
     * @param inputIndex the position entered at
     * @param answerID the answer id the input is associated to
     */
    public Inputs(String inputValue, Integer inputIndex, Long answerID) {
        this.setInputValue(inputValue);
        this.setInputIndex(inputIndex);
        this.setAnswerID(answerID);
    }

    /**
     * Gets the input id
     * @return the input id
     */
    public Long getInputID() {
        return inputID;
    }

    /**
     * Sets the input id
     * @param inputID the input id
     */
    public void setInputID(Long inputID) {
        this.inputID = inputID;
    }

    /**
     * Gets the input value
     * @return the input value
     */
    public String getInputValue() {
        return inputValue;
    }

    /**
     * Sets the input value
     * @param inputValue the input value
     */
    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    /**
     * Gets the input index
     * @return the input index
     */
    public Integer getInputIndex() {
        return inputIndex;
    }

    /**
     * Sets the input index
     * @param inputIndex the input index
     */
    public void setInputIndex(Integer inputIndex) {
        this.inputIndex = inputIndex;
    }

    /**
     * Gets the answer id
     * @return the answer id
     */
    public Long getAnswerID() {
        return answerID;
    }

    /**
     * Sets the answer id
     * @param answerID the answer id
     */
    public void setAnswerID(Long answerID) {
        this.answerID = answerID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Inputs{");
        sb.append("inputID=").append(inputID);
        sb.append(", inputValue='").append(inputValue).append('\'');
        sb.append(", inputIndex=").append(inputIndex);
        sb.append(", answerID=").append(answerID);
        sb.append('}');
        return sb.toString();
    }
}
