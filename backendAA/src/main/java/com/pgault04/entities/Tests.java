/**
 * 
 */
package com.pgault04.entities;

/**
 * @author paulgault
 *
 */
public class Tests {

	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long testID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

	private Long moduleID;

	private String testTitle;

	private String startDateTime;

	private String endDateTime;

	private Integer publishResults;

	private Integer scheduled;

	private Integer publishGrades;

	/**
	 * 
	 */
	public Tests() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param testID
	 * @param moduleID
	 * @param startDateTime
	 * @param endDateTime
	 * @param maxScore
	 */
	public Tests(Long moduleID, String testTitle, String startDateTime, String endDateTime, Integer publishResults,
			Integer scheduled, Integer publishGrades) {
		this.setModuleID(moduleID);
		this.setTestTitle(testTitle);
		this.setStartDateTime(startDateTime);
		this.setEndDateTime(endDateTime);
		this.setPublishResults(publishResults);
		this.setScheduled(scheduled);
		this.setPublishGrades(publishGrades);
	}

	public Integer getPublishGrades() {
		return publishGrades;
	}

	public void setPublishGrades(Integer publishGrades) {
		this.publishGrades = publishGrades;
	}

	/**
	 * @return the testID
	 */
	public Long getTestID() {
		return testID;
	}

	/**
	 * @param testID
	 *            the testID to set
	 */
	public void setTestID(Long testID) {
		this.testID = testID;
	}

	/**
	 * @return the moduleID
	 */
	public Long getModuleID() {
		return moduleID;
	}

	/**
	 * @param moduleID
	 *            the moduleID to set
	 */
	public void setModuleID(Long moduleID) {
		this.moduleID = moduleID;
	}

	public String getTestTitle() {
		return testTitle;
	}

	public void setTestTitle(String testTitle) {
		this.testTitle = testTitle;
	}

	/**
	 * @return the startDateTime
	 */
	public String getStartDateTime() {
		return startDateTime;
	}

	/**
	 * @param startDateTime
	 *            the startDateTime to set
	 */
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	/**
	 * @return the endDateTime
	 */
	public String getEndDateTime() {
		return endDateTime;
	}

	/**
	 * @param endDateTime
	 *            the endDateTime to set
	 */
	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	/**
	 * @return the publishResults
	 */
	public Integer getPublishResults() {
		return publishResults;
	}

	/**
	 * @param publishResults
	 *            the publishResults to set
	 */
	public void setPublishResults(Integer publishResults) {
		this.publishResults = publishResults;
	}

	/**
	 * @return the scheduled
	 */
	public Integer getScheduled() {
		return scheduled;
	}

	/**
	 * @param scheduled
	 *            the scheduled to set
	 */
	public void setScheduled(Integer scheduled) {
		this.scheduled = scheduled;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tests [testID=" + testID + ", moduleID=" + moduleID + ", testTitle=" + testTitle + ", startDateTime="
				+ startDateTime + ", endDateTime=" + endDateTime + ", publishResults=" + publishResults + ", scheduled="
				+ scheduled + "]";
	}
	
	

}
