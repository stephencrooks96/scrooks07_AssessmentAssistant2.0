/**
 * 
 */
package com.pgault04.entities;

/**
 * @author paulgault
 *
 */
public class QuestionType {

	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long questionTypeID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

	private String questionType;

	/**
	 * 
	 */
	public QuestionType() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param questionTypeID
	 * @param questionType
	 */
	public QuestionType(String questionType) {

		this.setQuestionType(questionType);
	}

	/**
	 * @return the questionTypeID
	 */
	public Long getQuestionTypeID() {
		return questionTypeID;
	}

	/**
	 * @param questionTypeID
	 *            the questionTypeID to set
	 */
	public void setQuestionTypeID(Long questionTypeID) {
		this.questionTypeID = questionTypeID;
	}

	/**
	 * @return the questionType
	 */
	public String getQuestionType() {
		return questionType;
	}

	/**
	 * @param questionType
	 *            the questionType to set
	 */
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

}
