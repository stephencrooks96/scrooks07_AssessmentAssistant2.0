/**
 * 
 */
package com.pgault04.entities;

import org.springframework.stereotype.Component;

/**
 * @author paulgault
 *
 */
@Component
public class UserRole {

	private Long userRoleID = -1L;
	
	private String role;

	public UserRole() {}

	/**
	 * 
	 * @param userRoleID
	 * @param userRole
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
	 * @param userRole the userRole to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	
	
	
}
