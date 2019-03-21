package com.pgault04.entities;

import org.springframework.stereotype.Component;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 * Represents the UserRole table in the database
 */
@Component
public class UserRole {

    /**
     * Constant represents where the admin role is held in the table
     */
    public static final Long ROLE_ADMIN = 1L;

    /**
     * Constant represents where the user role is held in the table
     */
    public static final Long ROLE_USER = 2L;

    private Long userRoleID = -1L;

    private String role;

    /**
     * Default constructor
     */
    public UserRole() {}

    /**
     * Constructor with args
     *
     * @param userRole the users role
     */
    public UserRole(String userRole) {
        this.setRole(userRole);
    }

    /**
     * @return the userRoleID
     */
    public Long getUserRoleID() {
        return userRoleID;
    }

    /**
     * @param userRoleID the userRoleID to set
     */
    public void setUserRoleID(Long userRoleID) {
        this.userRoleID = userRoleID;
    }

    /**
     * @return the userRole
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserRole{");
        sb.append("userRoleID=").append(userRoleID);
        sb.append(", role='").append(role).append('\'');
        sb.append('}');
        return sb.toString();
    }
}