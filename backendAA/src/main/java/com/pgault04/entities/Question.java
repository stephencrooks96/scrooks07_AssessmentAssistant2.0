/**
 * 
 */
package com.pgault04.entities;

/**
 * @author paulgault
 *
 */
public class Question {

	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long questionType;

	private Long questionID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

	private String questionContent;

	private String questionFigure;

	private Integer maxScore;
	
	private Long creatorID;

	/**
	 * 
	 */
	public Question() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param questionType
	 * @param questionID
	 * @param questionContent
	 * @param questionFigure
	 * @param maxScore
	 * @param modelAnswerID
	 */
	public Question(Long questionType, String questionContent, String questionFigure, Integer maxScore,
			Long creatorID) {
		this.setQuestionType(questionType);
		this.setQuestionContent(questionContent);
		this.setQuestionFigure(questionFigure);
		this.setMaxScore(maxScore);
		this.setCreatorID(creatorID);
	}

	/**
	 * @return the questionType
	 */
	public Long getQuestionType() {
		return questionType;
	}

	/**
	 * @param questionType
	 *            the questionType to set
	 */
	public void setQuestionType(Long questionType) {
		this.questionType = questionType;
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
	 * @return the questionContent
	 */
	public String getQuestionContent() {
		return questionContent;
	}

	/**
	 * @param questionContent
	 *            the questionContent to set
	 */
	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	/**
	 * @return the questionFigure
	 */
	public String getQuestionFigure() {
		return questionFigure;
	}

	/**
	 * @param questionFigure
	 *            the questionFigure to set
	 */
	public void setQuestionFigure(String questionFigure) {
		this.questionFigure = questionFigure;
	}

	/**
	 * @return the maxScore
	 */
	public Integer getMaxScore() {
		return maxScore;
	}

	/**
	 * @param maxScore
	 *            the maxScore to set
	 */
	public void setMaxScore(Integer maxScore) {
		this.maxScore = maxScore;
	}

	/**
	 * @return the creatorID
	 */
	public Long getCreatorID() {
		return creatorID;
	}

	/**
	 * @param creatorID the creatorID to set
	 */
	public void setCreatorID(Long creatorID) {
		this.creatorID = creatorID;
	}

	@Override
	public String toString() {
		return "Question{" +
				"questionType=" + questionType +
				", questionID=" + questionID +
				", questionContent='" + questionContent + '\'' +
				", questionFigure='" + questionFigure + '\'' +
				", maxScore=" + maxScore +
				", creatorID=" + creatorID +
				'}';
	}
}
