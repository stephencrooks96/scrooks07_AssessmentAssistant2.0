/**
 * 
 */
package com.pgault04.entities;

/**
 * @author paulgault
 *
 */
public class TestResult {

	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long testResultID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

	private Long testID;

	private Long studentID;

	private Integer testScore;

	/**
	 * 
	 */
	public TestResult() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param testResultID
	 * @param testID
	 * @param studentID
	 * @param testScore
	 */
	public TestResult(Long testID, Long studentID, Integer testScore) {
		this.setTestID(testID);
		this.setStudentID(studentID);
		this.setTestScore(testScore);
	}

	/**
	 * @return the testResultID
	 */
	public Long getTestResultID() {
		return testResultID;
	}

	/**
	 * @param testResultID
	 *            the testResultID to set
	 */
	public void setTestResultID(Long testResultID) {
		this.testResultID = testResultID;
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
	 * @return the studentID
	 */
	public Long getStudentID() {
		return studentID;
	}

	/**
	 * @param studentID
	 *            the studentID to set
	 */
	public void setStudentID(Long studentID) {
		this.studentID = studentID;
	}

	/**
	 * @return the testScore
	 */
	public Integer getTestScore() {
		return testScore;
	}

	/**
	 * @param testScore
	 *            the testScore to set
	 */
	public void setTestScore(Integer testScore) {
		this.testScore = testScore;
	}

}
