package com.pgault04.entities;

import java.sql.Blob;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 * Represents the question table in database
 */
public class Question {

    /**
     * Used as a checker for insertions and updates
     */
    private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

    private Long questionType;

    private Long questionID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

    private String questionContent;

    private Blob questionFigure;

    private Integer maxScore;

    private Integer minScore;

    private Long creatorID;

    private Integer allThatApply;

    /**
     * Default constructor
     */
    public Question() {}

    /**
     * The constructor with args
     *
     * @param questionType    the questions type
     * @param questionContent the content of the question
     * @param questionFigure  an image to accompany the question
     * @param maxScore        the marks for this question
     */
    public Question(Long questionType, String questionContent, Blob questionFigure, Integer maxScore, Integer minScore,
                    Long creatorID, Integer allthatApply) {
        this.setQuestionType(questionType);
        this.setQuestionContent(questionContent);
        this.setQuestionFigure(questionFigure);
        this.setMaxScore(maxScore);
        this.setMinScore(minScore);
        this.setCreatorID(creatorID);
        this.setAllThatApply(allthatApply);
    }

    /**
     * @return the questionType
     */
    public Long getQuestionType() {
        return questionType;
    }

    /**
     * @param questionType the questionType to set
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
     * @param questionID the questionID to set
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
     * @param questionContent the questionContent to set
     */
    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    /**
     * @return the questionFigure
     */
    public Blob getQuestionFigure() {
        return questionFigure;
    }

    /**
     * @param questionFigure the questionFigure to set
     */
    public void setQuestionFigure(Blob questionFigure) {
        this.questionFigure = questionFigure;
    }

    /**
     * @return the maxScore
     */
    public Integer getMaxScore() {
        return maxScore;
    }

    /**
     * @param maxScore the maxScore to set
     */
    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * @return the minScore
     */
    public Integer getMinScore() {
        return minScore;
    }

    /**
     * @param minScore the minScore to set
     */
    public void setMinScore(Integer minScore) {
        this.minScore = minScore;
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
     * @return the allowed options
     */
    public Integer getAllThatApply() { return allThatApply; }

    /**
     * @param allThatApply the number of allowed options
     */
    public void setAllThatApply(Integer allThatApply) { this.allThatApply = allThatApply; }

    /*
     * the object as a string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Question{");
        sb.append("questionType=").append(questionType);
        sb.append(", questionID=").append(questionID);
        sb.append(", questionContent='").append(questionContent).append('\'');
        sb.append(", questionFigure=").append(questionFigure);
        sb.append(", maxScore=").append(maxScore);
        sb.append(", minScore=").append(minScore);
        sb.append(", creatorID=").append(creatorID);
        sb.append(", allThatApply=").append(allThatApply);
        sb.append('}');
        return sb.toString();
    }
}