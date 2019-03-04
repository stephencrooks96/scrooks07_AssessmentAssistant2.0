package com.pgault04.pojos;

import com.pgault04.entities.User;

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

    /*
     * object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TokenPojo{");
        sb.append("user=").append(user);
        sb.append(", token='").append(token).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
