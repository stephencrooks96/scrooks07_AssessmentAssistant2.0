package com.pgault04.entities;

import java.sql.Timestamp;

public class UserSession {

    private String username;
    private String token;
    private Timestamp lastActive;

    public UserSession() {
    }

    public UserSession(String username, String token, Timestamp lastActive) {
        this.setUsername(username);
        this.setToken(token);
        this.setLastActive(lastActive);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getLastActive() {
        return lastActive;
    }

    public void setLastActive(Timestamp lastActive) {
        this.lastActive = lastActive;
    }
}
