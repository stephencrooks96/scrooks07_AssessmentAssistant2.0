package com.pgault04.entities;

public class QuestionMathLine {

    private Long questionMathLineID;
    private Long questionID;
    private String content;
    private Integer indexedAt;

    public QuestionMathLine() {}

    public QuestionMathLine(Long questionMathLineID, Long questionID, String content, Integer indexedAt) {
        this.setQuestionMathLineID(questionMathLineID);
        this.setQuestionID(questionID);
        this.setContent(content);
        this.setIndexedAt(indexedAt);
    }

    public Long getQuestionMathLineID() {
        return questionMathLineID;
    }

    public void setQuestionMathLineID(Long questionMathLineID) {
        this.questionMathLineID = questionMathLineID;
    }

    public Long getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Long questionID) {
        this.questionID = questionID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIndexedAt() {
        return indexedAt;
    }

    public void setIndexedAt(Integer indexedAt) {
        this.indexedAt = indexedAt;
    }
}

