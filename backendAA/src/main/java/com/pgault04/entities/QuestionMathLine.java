package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 * Represents the question table in database
 */
public class QuestionMathLine {

    private static final long INITIAL = -1L;
    private Long questionMathLineID = INITIAL;
    private Long questionID;
    private String content;
    private Integer indexedAt;

    /**
     * Default constructor
     */
    public QuestionMathLine() {}

    /**
     * Constructor with arguments
     *
     * @param questionID - the question the math line belongs to
     * @param content    - the content of the math line
     * @param indexedAt  - what position it is held at
     */
    public QuestionMathLine(Long questionID, String content, Integer indexedAt) {
        this.setQuestionID(questionID);
        this.setContent(content);
        this.setIndexedAt(indexedAt);
    }

    /**
     * @return the unique identifier for the math line
     */
    public Long getQuestionMathLineID() {
        return questionMathLineID;
    }

    /**
     * @param questionMathLineID sets the unique identifier for the math line
     */
    public void setQuestionMathLineID(Long questionMathLineID) {
        this.questionMathLineID = questionMathLineID;
    }

    /**
     * @return gets the unique identifier for question the math line belongs to
     */
    public Long getQuestionID() {
        return questionID;
    }

    /**
     * @param questionID sets the unique identifier for question the math line belongs to
     */
    public void setQuestionID(Long questionID) {
        this.questionID = questionID;
    }

    /**
     * @return gets the content of the math line
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content sets the content of the math line
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return gets where the math line is held at
     */
    public Integer getIndexedAt() {
        return indexedAt;
    }

    /**
     * @param indexedAt sets where the math line is held at
     */
    public void setIndexedAt(Integer indexedAt) {
        this.indexedAt = indexedAt;
    }

    /*
     * object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QuestionMathLine{");
        sb.append("questionMathLineID=").append(questionMathLineID);
        sb.append(", questionID=").append(questionID);
        sb.append(", content='").append(content).append('\'');
        sb.append(", indexedAt=").append(indexedAt);
        sb.append('}');
        return sb.toString();
    }
}