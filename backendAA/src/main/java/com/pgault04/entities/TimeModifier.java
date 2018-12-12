/**
 * 
 */
package com.pgault04.entities;

/**
 * @author paulgault
 *
 */
public class TimeModifier {

	private Long userID;
	
	private Double timeModifier;
	
	/**
	 * 
	 */
	public TimeModifier() {
		// TODO Auto-generated constructor stub
	}
	
	

	/**
	 * @param userID
	 * @param timeModifier
	 */
	public TimeModifier(Long userID, Double timeModifier) {
		this.setUserID(userID);
		this.setTimeModifier(timeModifier);
	}



	/**
	 * @return the userID
	 */
	public Long getUserID() {
		return userID;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(Long userID) {
		this.userID = userID;
	}

	/**
	 * @return the timeModifier
	 */
	public Double getTimeModifier() {
		return timeModifier;
	}

	/**
	 * @param timeModifier the timeModifier to set
	 */
	public void setTimeModifier(Double timeModifier) {
		this.timeModifier = timeModifier;
	}
	
	

}
