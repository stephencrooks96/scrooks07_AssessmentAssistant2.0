package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 *
 */
public class TimeModifier {

	private Long userID;
	
	private Double timeModifier;
	
	/**
	 * Default constructor
	 */
	public TimeModifier() {}

	/**
	 * Constructor with args
	 * @param userID the user
	 * @param timeModifier their time modifier
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

	/**
	 * @return the object as string
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("TimeModifier{");
		sb.append("userID=").append(userID);
		sb.append(", timeModifier=").append(timeModifier);
		sb.append('}');
		return sb.toString();
	}
}
