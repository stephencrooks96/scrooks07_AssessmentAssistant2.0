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
public class User {

	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long userID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

	private String username;
	
	private String password;

	private String firstName;

	private String lastName;

	private Integer enabled;

	private Long userRoleID;

	/**
	 * Default constructor
	 */
	public User() {
	}

	/**
	 * @param userID
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param userType
	 */
	public User(String username, String password, String firstName, String lastName, Integer enabled, Long userRoleID) {
		this.setUsername(username);
		this.setPassword(password);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setEnabled(enabled);
		this.setUserRoleID(userRoleID);
	}

	/**
	 * @return the userID
	 */
	public Long getUserID() {
		return userID;
	}

	/**
	 * @param userID
	 *            the userID to set
	 */
	public void setUserID(Long userID) {
		this.userID = userID;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the enabled
	 */
	public Integer getEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the userType
	 */
	public Long getUserRoleID() {
		return userRoleID;
	}

	/**
	 * @param userType
	 *            the userType to set
	 */
	public void setUserRoleID(Long userRoleID) {
		this.userRoleID = userRoleID;
	}

}
