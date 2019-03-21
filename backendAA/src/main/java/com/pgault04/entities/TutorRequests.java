package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since Feb 2019
 * Represents the TutorRequest table
 */
public class TutorRequests {

    private Long tutorRequestID = -1L;
    private Long userID;
    private String reason;
    private Integer approved;

    /**
     * Default constructor
     */
    public TutorRequests() {}

    /**
     * Constructor with arguments
     *
     * @param userID   - the user
     * @param reason   - the reason for wanting to become a tutor
     * @param approved - whether the request has been approved
     */
    public TutorRequests(Long userID, String reason, Integer approved) {
        this.setUserID(userID);
        this.setReason(reason);
        this.setApproved(approved);
    }

    /**
     * @return gets the unique identifier for the tutor request
     */
    public Long getTutorRequestID() { return tutorRequestID; }

    /**
     * @param tutorRequestID - sets the unique identifier for the tutor request
     */
    public void setTutorRequestID(Long tutorRequestID) { this.tutorRequestID = tutorRequestID; }

    /**
     * @return gets the unique identifier for the user who the request belongs to
     */
    public Long getUserID() { return userID; }

    /**
     * @param userID - sets the unique identifier for the user who the request belongs to
     */
    public void setUserID(Long userID) { this.userID = userID; }

    /**
     * @return gets the reason the user is making the request
     */
    public String getReason() { return reason; }

    /**
     * @param reason - sets the reason the user is making the request
     */
    public void setReason(String reason) { this.reason = reason; }

    /**
     * @return - gets whether the request has been approved
     */
    public Integer getApproved() { return approved; }

    /**
     * @param approved - sets whether the request has been approved or not
     */
    public void setApproved(Integer approved) { this.approved = approved; }

    /*
     * the object as string
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