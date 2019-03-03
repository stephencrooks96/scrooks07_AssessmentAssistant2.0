package com.pgault04.pojos;

import com.pgault04.entities.User;

import java.security.Principal;

public class TokenPojo {

    private User user;
    private String token;


    public TokenPojo() {
    }

    public TokenPojo(User user, String token) {
        this.setUser(user);
        this.setToken(token);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
