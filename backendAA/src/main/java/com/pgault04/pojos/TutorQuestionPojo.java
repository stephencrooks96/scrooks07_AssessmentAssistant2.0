package com.pgault04.pojos;

import java.util.List;

import com.pgault04.entities.Alternative;
import com.pgault04.entities.CorrectPoint;
import com.pgault04.entities.Option;
import com.pgault04.entities.Question;

/**
 * Inner class for facilitating the retrieval of form data from the front-end
 * 
 * @author Paul Gault - 40126005
 */
public class TutorQuestionPojo {

	private static final long LONG_INITIAL = -1L;

	private Long testID;

	private Question question;

	private List<Option> options;

	private List<CorrectPoint> correctPoints;

	private List<Alternative> alternatives;

	public TutorQuestionPojo() {
	}

	public TutorQuestionPojo(Long testID, Question question, List<Option> options, List<CorrectPoint> correctPoints,
							 List<Alternative> alternatives) {
		this.setTestID(testID);
		this.setQuestion(question);
		this.setOptions(options);
		this.setCorrectPoints(correctPoints);
		this.setAlternatives(alternatives);
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
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
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * @return the correctPoints
	 */
	public List<CorrectPoint> getCorrectPoints() {
		return correctPoints;
	}

	/**
	 * @param correctPoints
	 *            the correctPoints to set
	 */
	public void setCorrectPoints(List<CorrectPoint> correctPoints) {
		this.correctPoints = correctPoints;
	}

	/**
	 * @return the alternatives
	 */
	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	/**
	 * @param alternatives
	 *            the alternatives to set
	 */
	public void setAlternatives(List<Alternative> alternatives) {
		this.alternatives = alternatives;
	}

}