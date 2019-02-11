package com.pgault04.pojos;

import com.pgault04.entities.CorrectPoint;
import com.pgault04.entities.User;

import java.util.List;

public class AnswerData {

    private QuestionAndAnswer questionAndAnswer;
    private User student;
    private List<CorrectPoint> correctPoints;

    /**
     * Default constructor
     */
    public AnswerData() {}

    /**
     * Constructor with args
     *
     * @param questionAndAnswer question and answer data
     * @param student the student
     * @param correctPoints the mark scheme
     */
    public AnswerData(QuestionAndAnswer questionAndAnswer, User student, List<CorrectPoint> correctPoints) {
        this.setQuestionAndAnswer(questionAndAnswer);
        this.setStudent(student);
        this.setCorrectPoints(correctPoints);
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
     * @return the correct points
     */
    public List<CorrectPoint> getCorrectPoints() {
        return correctPoints;
    }

    /**
     * @param correctPoints the correct points
     */
    public void setCorrectPoints(List<CorrectPoint> correctPoints) {
        this.correctPoints = correctPoints;
    }

    /**
     * @return the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AnswerData{");
        sb.append("questionAndAnswer=").append(questionAndAnswer);
        sb.append(", student=").append(student);
        sb.append(", correctPoints=").append(correctPoints);
        sb.append('}');
        return sb.toString();
    }
}
