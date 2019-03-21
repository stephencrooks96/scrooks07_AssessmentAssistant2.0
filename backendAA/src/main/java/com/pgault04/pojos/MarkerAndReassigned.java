package com.pgault04.pojos;

/**
 * @author Paul Gault - 40126005
 * @since Jan 2019
 * Pojo to allow collection of reassignment info to be sent to/from front end
 */
public class MarkerAndReassigned {

    private Long markerID;
    private Long previousMarkerID;
    private Long specifyQuestion;
    private Long numberToReassign;

    /**
     * Default constructor
     */
    public MarkerAndReassigned() {}

    /**
     * Constructor with arguments
     *
     * @param markerID         - the new marker
     * @param previousMarkerID - the previous marker
     * @param specifyQuestion  - whether a specific question should be allocated from
     * @param numberToReassign - the number of answers to reassign
     */
    public MarkerAndReassigned(Long markerID, Long previousMarkerID, Long specifyQuestion, Long numberToReassign) {
        this.setMarkerID(markerID);
        this.setPreviousMarkerID(previousMarkerID);
        this.setSpecifyQuestion(specifyQuestion);
        this.setNumberToReassign(numberToReassign);
    }

    /**
     * @return gets the new marker
     */
    public Long getMarkerID() { return markerID; }

    /**
     * @param markerID sets the new marker
     */
    public void setMarkerID(Long markerID) { this.markerID = markerID; }

    /**
     * @return gets the previous markers id
     */
    public Long getPreviousMarkerID() { return previousMarkerID; }

    /**
     * @param previousMarkerID sets the previous markers id
     */
    public void setPreviousMarkerID(Long previousMarkerID) { this.previousMarkerID = previousMarkerID; }

    /**
     * @return gets the specific question to allocate from if any
     */
    public Long getSpecifyQuestion() { return specifyQuestion; }

    /**
     * @param specifyQuestion sets the specific question to allocate from if any
     */
    public void setSpecifyQuestion(Long specifyQuestion) { this.specifyQuestion = specifyQuestion; }

    /**
     * @return gets the number of scripts to reassign
     */
    public Long getNumberToReassign() { return numberToReassign; }

    /**
     * @param numberToReassign sets the number of scripts to reassign
     */
    public void setNumberToReassign(Long numberToReassign) { this.numberToReassign = numberToReassign; }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MarkerAndReassigned{");
        sb.append("markerID=").append(markerID);
        sb.append(", previousMarkerID=").append(previousMarkerID);
        sb.append(", specifyQuestion=").append(specifyQuestion);
        sb.append(", numberToReassign=").append(numberToReassign);
        sb.append('}');
        return sb.toString();
    }
}