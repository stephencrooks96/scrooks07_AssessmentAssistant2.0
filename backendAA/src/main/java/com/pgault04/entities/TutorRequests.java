package com.pgault04.entities;

public class TutorRequests {

    private Long tutorRequestID = -1L;
    private Long userID;
    private String reason;
    private Integer approved;

    /**
     *
     */
    public TutorRequests() {}

    /**
     *
     * @param userID
     * @param reason
     * @param approved
     */
    public TutorRequests(Long userID, String reason, Integer approved) {
        this.setUserID(userID);
        this.setReason(reason);
        this.setApproved(approved);
    }

    /**
     *
     * @return
     */
    public Long getTutorRequestID() { return tutorRequestID; }

    /**
     *
     * @param tutorRequestID
     */
    public void setTutorRequestID(Long tutorRequestID) { this.tutorRequestID = tutorRequestID; }

    /**
     *
     * @return
     */
    public Long getUserID() { return userID; }

    /**
     *
     * @param userID
     */
    public void setUserID(Long userID) { this.userID = userID; }

    /**
     *
     * @return
     */
    public String getReason() { return reason; }

    /**
     *
     * @param reason
     */
    public void setReason(String reason) { this.reason = reason; }

    /**
     *
     * @return
     */
    public Integer getApproved() { return approved; }

    /**
     *
     * @param approved
     */
    public void setApproved(Integer approved) { this.approved = approved; }

    /*
     *
     */
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
