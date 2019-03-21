package com.pgault04.entities;

/**
 * @author Paul Gault - 40126005
 * @since November 2018
 * Class corresponding to alternative table
 */
public class Alternative {

    /**
     * Used as a checker for insertions and updates
     */
    private static final long ALTERNATIVE_ID_INITIAL = -1L;

    private Long alternativeID = ALTERNATIVE_ID_INITIAL;

    private Long correctPointID;

    private String alternativePhrase;

    private Integer math;

    /**
     * Default constructor
     */
    public Alternative() {}

    /**
     * Constructor with args
     *
     * @param correctPointID    the correct point id
     * @param alternativePhrase the alternative phrase
     */
    public Alternative(Long correctPointID, String alternativePhrase, Integer math) {
        this.setCorrectPointID(correctPointID);
        this.setAlternativePhrase(alternativePhrase);
        this.setMath(math);
    }

    /**
     * @return the alternativeID
     */
    public Long getAlternativeID() {
        return alternativeID;
    }

    /**
     * @param alternativeID the alternativeID to set
     */
    public void setAlternativeID(Long alternativeID) {
        this.alternativeID = alternativeID;
    }

    /**
     * @return the correctPointID
     */
    public Long getCorrectPointID() {
        return correctPointID;
    }

    /**
     * @param correctPointID the correctPointID to set
     */
    public void setCorrectPointID(Long correctPointID) {
        this.correctPointID = correctPointID;
    }

    /**
     * @return the alternativePhrase
     */
    public String getAlternativePhrase() {
        return alternativePhrase;
    }

    /**
     * @param alternativePhrase the alternativePhrase to set
     */
    public void setAlternativePhrase(String alternativePhrase) {
        this.alternativePhrase = alternativePhrase;
    }

    /**
     * @return whether it is math or not
     */
    public Integer getMath() { return math; }

    /**
     * @param math whether it is math or not
     */
    public void setMath(Integer math) { this.math = math; }

    /**
     * @return the object as a string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Alternative{");
        sb.append("alternativeID=").append(alternativeID);
        sb.append(", correctPointID=").append(correctPointID);
        sb.append(", alternativePhrase='").append(alternativePhrase).append('\'');
        sb.append(", math=").append(math);
        sb.append('}');
        return sb.toString();
    }
}
