package com.pgault04.entities;

import java.sql.Timestamp;

public class UserSession {

    private String username;
    private String token;
    private Timestamp lastActive;

    /**
     *
     */
    public UserSession() {
    }

    /**
     *
     * @param username
     * @param token
     * @param lastActive
     */
    public UserSession(String username, String token, Timestamp lastActive) {
        this.setUsername(username);
        this.setToken(token);
        this.setLastActive(lastActive);
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     *
     * @return
     */
    public Timestamp getLastActive() {
        return lastActive;
    }

    /**
     *
     * @param lastActive
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
