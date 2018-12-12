/**
 * 
 */
package com.pgault04.entities;

import java.util.List;

/**
 * @author Paul Gault - 40126005
 *
 */
public class CorrectPoint {

	private static final long CORRECT_POINT_INITIAL_ID = -1L;

	private Long correctPointID = CORRECT_POINT_INITIAL_ID;

	private Long questionID;

	private String phrase;

	private Double marksWorth;

	private String feedback;
	
	private List<Alternative> alternatives;

	public CorrectPoint() {}

	public CorrectPoint(Long correctPointID, Long questionID, String phrase, Double marksWorth, String feedback, List<Alternative> alternatives) {
		
		this.setQuestionID(questionID);
		this.setPhrase(phrase);
		this.setMarksWorth(marksWorth);
		this.setFeedback(feedback);
		this.setAlternatives(alternatives);
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CorrectPoint [phrase=" + phrase + ", marksWorth=" + marksWorth + ", feedback=" + feedback + "]";
	}

}
