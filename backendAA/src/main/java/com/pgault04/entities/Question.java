package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
public class Question {

	/**
	 * Used as a checker for insertions and updates
	 */
	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long questionType;

	private Long questionID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

	private String questionContent;

	private String questionFigure;

	private Integer maxScore;
	
	private Long creatorID;

	/**
	 * Default constructor
	 */
	public Question() {}

	/**
	 * The constructor with args
	 * @param questionType the questions type
	 * @param questionContent the content of the question
	 * @param questionFigure an image to accompany the question
	 * @param maxScore the marks for this question
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

	/**
	 * @return the object as a string
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Question{");
		sb.append("questionType=").append(questionType);
		sb.append(", questionID=").append(questionID);
		sb.append(", questionContent='").append(questionContent).append('\'');
		sb.append(", questionFigure='").append(questionFigure).append('\'');
		sb.append(", maxScore=").append(maxScore);
		sb.append(", creatorID=").append(creatorID);
		sb.append('}');
		return sb.toString();
	}
}
