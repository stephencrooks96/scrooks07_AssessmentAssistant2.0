/**
 * 
 */
package com.pgault04.entities;

/**
 * @author paulgault
 *
 */
public class TestQuestion {

	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long testQuestionID= AUTO_INCREMENT_INITIALIZER_CONSTANT;
	
	private Long testID;

	private Long questionID;

	/**
	 * 
	 */
	public TestQuestion() {
		// TODO Auto-generated constructor stub
	}
	
	

	/**
	 * @param testID
	 * @param questionID
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

}
