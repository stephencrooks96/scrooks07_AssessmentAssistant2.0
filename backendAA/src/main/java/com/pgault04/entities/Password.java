/**
 * 
 */
package com.pgault04.entities;

/**
 * @author paulgault
 *
 */
public class Password {

	private Long userID;

	private String resetString;

	/**
	 * 
	 */
	public Password() {
	}

	/**
	 * @param userID
	 * @param password
	 * @param resetString
	 */
	public Password(Long userID, String resetString) {
		this.setUserID(userID);
		this.setResetString(resetString);
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
	 * @return the resetString
	 */
	public String getResetString() {
		return resetString;
	}

	/**
	 * @param resetString
	 *            the resetString to set
	 */
	public void setResetString(String resetString) {
		this.resetString = resetString;
	}

}
