package com.pgault04.entities;

import org.springframework.stereotype.Component;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Component
public class UserRole {

    private Long userRoleID = -1L;

    private String role;

    public UserRole() {
    }

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

    /**
     * @return the object as string
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
