package com.pgault04.pojos;

import com.pgault04.entities.Answer;

public class QuestionAndAnswer {

    private QuestionAndBase64 question;
    private Answer answer;

    public QuestionAndAnswer(QuestionAndBase64 question, Answer answer) {
        this.setQuestion(question);
        this.setAnswer(answer);
    }

    public QuestionAndBase64 getQuestion() {
        return question;
    }

    public void setQuestion(QuestionAndBase64 question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QuestionAndAnswer{");
        sb.append("question=").append(question);
        sb.append(", answer=").append(answer);
        sb.append('}');
        return sb.toString();
    }
}
