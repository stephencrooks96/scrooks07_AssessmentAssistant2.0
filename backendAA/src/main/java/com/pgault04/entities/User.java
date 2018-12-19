package com.pgault04.entities;

import org.springframework.stereotype.Component;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Component
public class User {

	/**
	 * Used as a checker for insertions and updates
	 */
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
	public User() {}

	/**
	 * Constructor with args
	 * @param username the users username
	 * @param password the users password
	 * @param firstName the users first name
	 * @param lastName the users last name
	 * @param enabled whether the user is enabled or not
	 * @param userRoleID the users user role
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
	 * @return the userRoleID
	 */
	public Long getUserRoleID() {
		return userRoleID;
	}

	/**
	 * @param userRoleID
	 *            the userRoleID to set
	 */
	public void setUserRoleID(Long userRoleID) {
		this.userRoleID = userRoleID;
	}

	/**
	 * @return the object as string
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("User{");
		sb.append("userID=").append(userID);
		sb.append(", username='").append(username).append('\'');
		sb.append(", password='").append(password).append('\'');
		sb.append(", firstName='").append(firstName).append('\'');
		sb.append(", lastName='").append(lastName).append('\'');
		sb.append(", enabled=").append(enabled);
		sb.append(", userRoleID=").append(userRoleID);
		sb.append('}');
		return sb.toString();
	}
}
