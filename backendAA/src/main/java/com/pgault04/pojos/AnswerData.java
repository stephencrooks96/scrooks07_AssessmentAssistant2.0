package com.pgault04.pojos;

import com.pgault04.entities.CorrectPoint;
import com.pgault04.entities.User;

import java.util.List;

public class AnswerData {

    private QuestionAndAnswer questionAndAnswer;
    private User student;

    /**
     * Default constructor
     */
    public AnswerData() {}

    /**
     * Constructor with args
     *
     * @param questionAndAnswer question and answer data
     * @param student the student
     */
    public AnswerData(QuestionAndAnswer questionAndAnswer, User student) {
        this.setQuestionAndAnswer(questionAndAnswer);
        this.setStudent(student);
    }

    /**
     * @return the question and answer obj
     */
    public QuestionAndAnswer getQuestionAndAnswer() {
        return questionAndAnswer;
    }

    /**
     * @param questionAndAnswer the question and answer obj
     */
    public void setQuestionAndAnswer(QuestionAndAnswer questionAndAnswer) {
        this.questionAndAnswer = questionAndAnswer;
    }

    /**
     * @return the student
     */
    public User getStudent() {
        return student;
    }

    /**
     * @param student the student
     */
    public void setStudent(User student) {
        this.student = student;
    }

    /**
     * @return the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AnswerData{");
        sb.append("questionAndAnswer=").append(questionAndAnswer);
        sb.append(", student=").append(student);
        sb.append('}');
        return sb.toString();
    }
}
