package com.pgault04.entities;

import java.sql.Timestamp;

/**
 * @author Paul Gault 40126005
 * @since Feb 2019
 * Represents the UserSession table in the database
 */
public class UserSession {

    private String username;
    private String token;
    private Timestamp lastActive;

    /**
     * Default constructor
     */
    public UserSession() {}

    /**
     * Constructor with arguments
     *
     * @param username   - the user
     * @param token      - their session token
     * @param lastActive - when they were last active
     */
    public UserSession(String username, String token, Timestamp lastActive) {
        this.setUsername(username);
        this.setToken(token);
        this.setLastActive(lastActive);
    }

    /**
     * @return the username corresponding to the session
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username sets the username corresponding to the session
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the token corresponding to the session
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token sets the token corresponding to the session
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return gets when the user was last active
     */
    public Timestamp getLastActive() {
        return lastActive;
    }

    /**
     * @param lastActive - sets when the user was last active
     */
    public void setLastActive(Timestamp lastActive) {
        this.lastActive = lastActive;
    }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserSession{");
        sb.append("username='").append(username).append('\'');
        sb.append(", token='").append(token).append('\'');
        sb.append(", lastActive=").append(lastActive);
        sb.append('}');
        return sb.toString();
    }
}