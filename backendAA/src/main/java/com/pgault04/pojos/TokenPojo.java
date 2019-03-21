package com.pgault04.pojos;

import com.pgault04.entities.User;

/**
 * @author Paul Gault 40126005
 * @since Jan 2019
 * Pojo to allow collection of token info to be sent to/from front end
 */
public class TokenPojo {

    private User user;
    private String token;

    /**
     * Default constructor
     */
    public TokenPojo() {}

    /**
     * Constructor with arguments
     *
     * @param user  the user who owns the token
     * @param token the user's token
     */
    public TokenPojo(User user, String token) {
        this.setUser(user);
        this.setToken(token);
    }

    /**
     * @return gets the user object
     */
    public User getUser() { return user; }

    /**
     * @param user sets the user object
     */
    public void setUser(User user) { this.user = user; }

    /**
     * @return gets the token string
     */
    public String getToken() { return token; }

    /**
     * @param token sets the token string
     */
    public void setToken(String token) { this.token = token; }

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