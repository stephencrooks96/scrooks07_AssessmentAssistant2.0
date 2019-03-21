package com.pgault04.entities;

/**
 * @author Paul Gault
 * @since Jan 19
 * Class represents the option entries table in the database
 */
public class OptionEntries {

    /**
     * Constant for new option entries
     */
    private static final long ENTRY_CONST = -1L;
    private Long optionEntryID = ENTRY_CONST;
    private Long optionID;
    private Long answerID;

    /**
     * Default constructor
     */
    public OptionEntries() {}

    /**
     * The constructor with args
     *
     * @param optionID the option id
     * @param answerID the answer id
     */
    public OptionEntries(Long optionID, Long answerID) {
        this.setOptionID(optionID);
        this.setAnswerID(answerID);
    }

    /**
     * @return the option entry id
     */
    public Long getOptionEntryID() { return optionEntryID; }

    /**
     * @param optionEntryID the option entry id
     */
    public void setOptionEntryID(Long optionEntryID) { this.optionEntryID = optionEntryID; }

    /**
     * @return the option id
     */
    public Long getOptionID() { return optionID; }

    /**
     * @param optionID the option id
     */
    public void setOptionID(Long optionID) { this.optionID = optionID; }

    /**
     * @return the answer id
     */
    public Long getAnswerID() { return answerID; }

    /**
     * @param answerID the answer id
     */
    public void setAnswerID(Long answerID) { this.answerID = answerID; }

    /*
     * the object as a string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OptionEntries{");
        sb.append("optionEntryID=").append(optionEntryID);
        sb.append(", optionID=").append(optionID);
        sb.append(", answerID=").append(answerID);
        sb.append('}');
        return sb.toString();
    }
}