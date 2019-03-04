package com.pgault04.entities;

public class QuestionMathLine {

    public static final long INITIAL = -1L;
    private Long questionMathLineID = INITIAL;
    private Long questionID;
    private String content;
    private Integer indexedAt;

    /**
     *
     */
    public QuestionMathLine() {}

    /**
     *
     * @param questionID
     * @param content
     * @param indexedAt
     */
    public QuestionMathLine(Long questionID, String content, Integer indexedAt) {
        this.setQuestionID(questionID);
        this.setContent(content);
        this.setIndexedAt(indexedAt);
    }

    /**
     *
     * @return
     */
    public Long getQuestionMathLineID() {
        return questionMathLineID;
    }

    /**
     *
     * @param questionMathLineID
     */
    public void setQuestionMathLineID(Long questionMathLineID) {
        this.questionMathLineID = questionMathLineID;
    }

    /**
     *
     * @return
     */
    public Long getQuestionID() {
        return questionID;
    }

    /**
     *
     * @param questionID
     */
    public void setQuestionID(Long questionID) {
        this.questionID = questionID;
    }

    /**
     *
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return
     */
    public Integer getIndexedAt() {
        return indexedAt;
    }

    /**
     *
     * @param indexedAt
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

