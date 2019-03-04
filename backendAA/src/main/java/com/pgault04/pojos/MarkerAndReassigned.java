package com.pgault04.pojos;

public class MarkerAndReassigned {

    private Long markerID;
    private Long previousMarkerID;
    private Long specifyQuestion;
    private Long numberToReassign;

    public MarkerAndReassigned() {}

    public MarkerAndReassigned(Long markerID, Long previousMarkerID, Long specifyQuestion, Long numberToReassign) {
        this.setMarkerID(markerID);
        this.setPreviousMarkerID(previousMarkerID);
        this.setSpecifyQuestion(specifyQuestion);
        this.setNumberToReassign(numberToReassign);
    }

    public Long getMarkerID() { return markerID; }

    public void setMarkerID(Long markerID) { this.markerID = markerID; }

    public Long getPreviousMarkerID() { return previousMarkerID; }

    public void setPreviousMarkerID(Long previousMarkerID) { this.previousMarkerID = previousMarkerID; }

    public Long getSpecifyQuestion() { return specifyQuestion; }

    public void setSpecifyQuestion(Long specifyQuestion) { this.specifyQuestion = specifyQuestion; }

    public Long getNumberToReassign() { return numberToReassign; }

    public void setNumberToReassign(Long numberToReassign) { this.numberToReassign = numberToReassign; }

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
