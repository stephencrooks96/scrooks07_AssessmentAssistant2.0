package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
public class TestResult {

	/**
	 * Used as a checker for insertions and updates
	 */
	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long testResultID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

	private Long testID;

	private Long studentID;

	private Integer testScore;

	/**
	 * Default constructor
	 */
	public TestResult() {}

	/**
	 * Constructor with args
	 * @param testID the test
	 * @param studentID the student
	 * @param testScore their score
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

	/**
	 * @return the object as string
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("TestResult{");
		sb.append("testResultID=").append(testResultID);
		sb.append(", testID=").append(testID);
		sb.append(", studentID=").append(studentID);
		sb.append(", testScore=").append(testScore);
		sb.append('}');
		return sb.toString();
	}
}
