package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 * Class corresponding to the Answer table in database
 */
public class Answer {

    /**
     * Used as a checker for insertions and updates
     */
    private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

    private Long answerID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

    private Long questionID;

    private Long answererID;

    private Long markerID;

    private Long testID;

    private String content;

    private Integer score;

    private String feedback;

    private Integer markerApproved;

    private Integer tutorApproved;

    /**
     * The default constructor
     */
    public Answer() {}

    /**
     * Constructor with args
     *
     * @param questionID the question
     * @param answererID the answerer
     * @param markerID   the marker
     * @param content    the answers content
     * @param score      the score given
     */
    public Answer(Long questionID, Long answererID, Long markerID, Long testID, String content, Integer score, String feedback, Integer markerApproved, Integer tutorApproved) {
        this.setQuestionID(questionID);
        this.setAnswererID(answererID);
        this.setMarkerID(markerID);
        this.setTestID(testID);
        this.setContent(content);
        this.setScore(score);
        this.setFeedback(feedback);
        this.setMarkerApproved(markerApproved);
        this.setTutorApproved(tutorApproved);
    }

    /**
     * @return returns the feedback attribute for the object
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     * @param feedback sets the feedback attribute for the object
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     * @return the answerID
     */
    public Long getAnswerID() {
        return answerID;
    }

    /**
     * @param answerID the answerID to set
     */
    public void setAnswerID(Long answerID) {
        this.answerID = answerID;
    }

    /**
     * @return the questionID
     */
    public Long getQuestionID() {
        return questionID;
    }

    /**
     * @param questionID the questionID to set
     */
    public void setQuestionID(Long questionID) {
        this.questionID = questionID;
    }

    /**
     * @return the answererID
     */
    public Long getAnswererID() {
        return answererID;
    }

    /**
     * @param answererID the answererID to set
     */
    public void setAnswererID(Long answererID) {
        this.answererID = answererID;
    }

    /**
     * @return the markerID
     */
    public Long getMarkerID() {
        return markerID;
    }

    /**
     * @param markerID the markerID to set
     */
    public void setMarkerID(Long markerID) {
        this.markerID = markerID;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * @return the testID
     */
    public Long getTestID() {
        return testID;
    }

    /**
     * @param testID the testID to set
     */
    public void setTestID(Long testID) {
        this.testID = testID;
    }

    /**
     * @return whether the answer is approved by the marker
     */
    public Integer getMarkerApproved() {
        return markerApproved;
    }

    /**
     * @param markerApproved sets whether the answer is removed by the marker
     */
    public void setMarkerApproved(Integer markerApproved) { this.markerApproved = markerApproved; }

    /**
     * @return whether the answer is approved by the tutor or not
     */
    public Integer getTutorApproved() { return tutorApproved; }

    /**
     * @param tutorApproved sets whether the answer is removed by the marker
     */
    public void setTutorApproved(Integer tutorApproved) { this.tutorApproved = tutorApproved; }

    /**
     * @return the object as a string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Answer{");
        sb.append("answerID=").append(answerID);
        sb.append(", questionID=").append(questionID);
        sb.append(", answererID=").append(answererID);
        sb.append(", markerID=").append(markerID);
        sb.append(", testID=").append(testID);
        sb.append(", content='").append(content).append('\'');
        sb.append(", score=").append(score);
        sb.append(", feedback='").append(feedback).append('\'');
        sb.append(", markerApproved=").append(markerApproved);
        sb.append(", tutorApproved=").append(tutorApproved);
        sb.append('}');
        return sb.toString();
    }
}