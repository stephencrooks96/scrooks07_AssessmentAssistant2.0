package com.pgault04.pojos;

import java.util.List;

import com.pgault04.entities.Alternative;
import com.pgault04.entities.CorrectPoint;
import com.pgault04.entities.Option;
import com.pgault04.entities.Question;

/**
 * Class for facilitating the retrieval of form data from the front-end
 * 
 * @author Paul Gault - 40126005
 * @since November 2018
 */
public class TutorQuestionPojo {

	private Long testID;

	private Question question;

	private List<Option> options;

	private List<CorrectPoint> correctPoints;

	/**
	 * Default constructor
	 */
	public TutorQuestionPojo() {}

	/**
	 * Constructor with args
	 * @param testID the test
	 * @param question the question
	 * @param options the options
	 * @param correctPoints the correct points
	 */
	public TutorQuestionPojo(Long testID, Question question, List<Option> options, List<CorrectPoint> correctPoints) {
		this.setTestID(testID);
		this.setQuestion(question);
		this.setOptions(options);
		this.setCorrectPoints(correctPoints);
	}

	/**
	 * @return the options
	 */
	public List<Option> getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
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
	 * @return the object as string
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("TutorQuestionPojo{");
		sb.append("testID=").append(testID);
		sb.append(", question=").append(question.toString());
		sb.append(", options=").append(options);
		sb.append(", correctPoints=").append(correctPoints);
		sb.append('}');
		return sb.toString();
	}
}