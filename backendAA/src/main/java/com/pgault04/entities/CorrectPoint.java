/**
 * 
 */
package com.pgault04.entities;

import java.util.List;

/**
 * @author Paul Gault - 40126005
 * @since November 2018
 */
public class CorrectPoint {

	/**
	 * Used as a checker for insertions and updates
	 */
	private static final long CORRECT_POINT_INITIAL_ID = -1L;

	private Long correctPointID = CORRECT_POINT_INITIAL_ID;

	private Long questionID;

	private String phrase;

	private Double marksWorth;

	private String feedback;
	
	private List<Alternative> alternatives;

	/**
	 * The default constructor
	 */
	public CorrectPoint() {}

	/**
	 * The constructor with args
	 * @param questionID the question
	 * @param phrase the phrase that is worth marks
	 * @param marksWorth the marks the phrase is worth
	 * @param feedback the feedback given for this phrase appearing
	 * @param alternatives the list of phrases that are worthy of gaining the exact same marks
	 */
	public CorrectPoint(Long questionID, String phrase, Double marksWorth, String feedback, List<Alternative> alternatives) {
		
		this.setQuestionID(questionID);
		this.setPhrase(phrase);
		this.setMarksWorth(marksWorth);
		this.setFeedback(feedback);
		this.setAlternatives(alternatives);
	}

	/**
	 * @return the list of alternatives
	 */
	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	/**
	 * @param alternatives the list of alternatives to set
	 */
	public void setAlternatives(List<Alternative> alternatives) {
		this.alternatives = alternatives;
	}

	/**
	 * @return the correctPointID
	 */
	public Long getCorrectPointID() {
		return correctPointID;
	}

	/**
	 * @param correctPointID
	 *            the correctPointID to set
	 */
	public void setCorrectPointID(Long correctPointID) {
		this.correctPointID = correctPointID;
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
	 * @return the phrase
	 */
	public String getPhrase() {
		return phrase;
	}

	/**
	 * @param phrase
	 *            the phrase to set
	 */
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	/**
	 * @return the marksWorth
	 */
	public Double getMarksWorth() {
		return marksWorth;
	}

	/**
	 * @param marksWorth
	 *            the marksWorth to set
	 */
	public void setMarksWorth(Double marksWorth) {
		this.marksWorth = marksWorth;
	}

	/**
	 * @return the feedback
	 */
	public String getFeedback() {
		return feedback;
	}

	/**
	 * @param feedback
	 *            the feedback to set
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	/**
	 * @return the object as a string
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("CorrectPoint{");
		sb.append("correctPointID=").append(correctPointID);
		sb.append(", questionID=").append(questionID);
		sb.append(", phrase='").append(phrase).append('\'');
		sb.append(", marksWorth=").append(marksWorth);
		sb.append(", feedback='").append(feedback).append('\'');
		sb.append(", alternatives=").append(alternatives);
		sb.append('}');
		return sb.toString();
	}
}
