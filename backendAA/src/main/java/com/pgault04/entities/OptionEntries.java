package com.pgault04.entities;

/**
 * @author Paul Gault
 * @since Jan 19
 */
public class OptionEntries {

    private static final long ENTRY_CONST = -1L;
    private Long optionEntryID = ENTRY_CONST;
    private Long optionID;
    private Long answerID;

    public OptionEntries() {}

    /**
     * The constructor with args
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

    /**
     * @return the object as a string
     */
    @Override
    public String toString() {
        return new StringBuilder("OptionEntries{")
                .append("optionEntryID=").append(optionEntryID)
                .append(", optionID=").append(optionID)
                .append(", answerID=").append(answerID)
                .append('}').toString();
    }
}
