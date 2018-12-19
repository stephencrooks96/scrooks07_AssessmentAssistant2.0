package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
public class TestQuestion {

	/**
	 * Used as a checker for insertions and updates
	 */
	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long testQuestionID= AUTO_INCREMENT_INITIALIZER_CONSTANT;
	
	private Long testID;

	private Long questionID;

	/**
	 * Default constructor
	 */
	public TestQuestion() {}

	/**
	 * Constructor with args
	 * @param testID the test
	 * @param questionID the question
	 */
	public TestQuestion(Long testID, Long questionID) {
		this.setTestID(testID);
		this.setQuestionID(questionID);
	}

	/**
	 * @return the testQuestionID
	 */
	public Long getTestQuestionID() {
		return testQuestionID;
	}

	/**
	 * @param testQuestionID the testQuestionID to set
	 */
	public void setTestQuestionID(Long testQuestionID) {
		this.testQuestionID = testQuestionID;
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
	 * @return the questionID
	 */
	public Long getQuestionID() {
		return questionID;
	}

	/**
	 * @param questionID
	 *            the questionID to set
	 */
	public void setQuestionID(Long questionID) {
		this.questionID = questionID;
	}

	/**
	 * @return the object as string
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("TestQuestion{");
		sb.append("testQuestionID=").append(testQuestionID);
		sb.append(", testID=").append(testID);
		sb.append(", questionID=").append(questionID);
		sb.append('}');
		return sb.toString();
	}
}
