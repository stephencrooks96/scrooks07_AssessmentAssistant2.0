package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 * Represents the QuestionType table
 */
public class QuestionType {

    /**
     * Represents where text based is held in question types table
     */
    public static final long TEXT_BASED = 1L;

    /**
     * Represents where multiple choice is held in question types table
     */
    public static final long MULTIPLE_CHOICE = 2L;

    /**
     * Represents where insert the word is held in question types table
     */
    public static final long INSERT_THE_WORD = 3L;

    /**
     * Represents where math type is held in question types table
     */
    public static final long TEXT_MATH = 4L;

    /**
     * Used as a checker for insertions and updates
     */
    private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

    private Long questionTypeID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

    private String questionType;

    /**
     * Default constructor
     */
    public QuestionType() {}

    /**
     * Constructor with args
     *
     * @param questionType the question type
     */
    public QuestionType(String questionType) {
        this.setQuestionType(questionType);
    }

    /**
     * @return the questionTypeID
     */
    public Long getQuestionTypeID() {
        return questionTypeID;
    }

    /**
     * @param questionTypeID the questionTypeID to set
     */
    public void setQuestionTypeID(Long questionTypeID) {
        this.questionTypeID = questionTypeID;
    }

    /**
     * @return the questionType
     */
    public String getQuestionType() {
        return questionType;
    }

    /**
     * @param questionType the questionType to set
     */
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    /*
     * the object as a string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QuestionType{");
        sb.append("questionTypeID=").append(questionTypeID);
        sb.append(", questionType='").append(questionType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}