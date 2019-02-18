package com.pgault04.entities;

public class TutorRequests {

    private Long tutorRequestID;
    private Long userID;
    private String reason;
    private Integer approved;

    public TutorRequests() {}

    public TutorRequests(Long tutorRequestID, Long userID, String reason, Integer approved) {
        this.setTutorRequestID(tutorRequestID);
        this.setUserID(userID);
        this.setReason(reason);
        this.setApproved(approved);
    }

    public Long getTutorRequestID() { return tutorRequestID; }

    public void setTutorRequestID(Long tutorRequestID) { this.tutorRequestID = tutorRequestID; }

    public Long getUserID() { return userID; }

    public void setUserID(Long userID) { this.userID = userID; }

    public String getReason() { return reason; }

    public void setReason(String reason) { this.reason = reason; }

    public Integer getApproved() { return approved; }

    public void setApproved(Integer approved) { this.approved = approved; }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TutorRequests{");
        sb.append("tutorRequestID=").append(tutorRequestID);
        sb.append(", userID=").append(userID);
        sb.append(", reason='").append(reason).append('\'');
        sb.append(", approved=").append(approved);
        sb.append('}');
        return sb.toString();
    }
}
