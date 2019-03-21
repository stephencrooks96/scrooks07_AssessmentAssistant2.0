package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 * Represents the PasswordReset table in database
 */
public class PasswordReset {

    private Long userID;
    private String resetString;

    /**
     * Default constructor
     */
    public PasswordReset() {}

    /**
     * Constructor with args
     *
     * @param userID      the user
     * @param resetString their corresponding reset string
     */
    public PasswordReset(Long userID, String resetString) {
        this.setUserID(userID);
        this.setResetString(resetString);
    }

    /**
     * @return the userID
     */
    public Long getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(Long userID) {
        this.userID = userID;
    }

    /**
     * @return the resetString
     */
    public String getResetString() {
        return resetString;
    }

    /**
     * @param resetString the resetString to set
     */
    public void setResetString(String resetString) {
        this.resetString = resetString;
    }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PasswordReset{");
        sb.append("userID=").append(userID);
        sb.append(", resetString='").append(resetString).append('\'');
        sb.append('}');
        return sb.toString();
    }
}