package com.pgault04.entities;

/**
 * 
 * @author Paul Gault - 40126005
 *
 */
public class Alternative {

	private static final long ALTERNATIVE_ID_INITIAL = -1L;

	private Long alternativeID = ALTERNATIVE_ID_INITIAL;

	private Long correctPointID;

	private String alternativePhrase;

	public Alternative() {}

	public Alternative(Long correctPointID, String alternativePhrase) {

		this.setCorrectPointID(correctPointID);
		this.setAlternativePhrase(alternativePhrase);
	}

	/**
	 * @return the alternativeID
	 */
	public Long getAlternativeID() {
		return alternativeID;
	}

	/**
	 * @param alternativeID
	 *            the alternativeID to set
	 */
	public void setAlternativeID(Long alternativeID) {
		this.alternativeID = alternativeID;
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
	 * @return the alternativePhrase
	 */
	public String getAlternativePhrase() {
		return alternativePhrase;
	}

	/**
	 * @param alternativePhrase
	 *            the alternativePhrase to set
	 */
	public void setAlternativePhrase(String alternativePhrase) {
		this.alternativePhrase = alternativePhrase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Alternative [alternativeID=" + alternativeID + ", correctPointID=" + correctPointID
				+ ", alternativePhrase=" + alternativePhrase + "]";
	}

}
