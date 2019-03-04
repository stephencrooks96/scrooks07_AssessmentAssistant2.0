package com.pgault04.pojos;

import com.pgault04.entities.Answer;
import com.pgault04.entities.CorrectPoint;
import com.pgault04.entities.Inputs;
import com.pgault04.entities.OptionEntries;

import java.util.List;

public class QuestionAndAnswer {

    private QuestionAndBase64 question;
    private Answer answer;
    private List<Inputs> inputs;
    private List<OptionEntries> optionEntries;
    private List<CorrectPoint> correctPoints;

    public QuestionAndAnswer() {}

    public QuestionAndAnswer(QuestionAndBase64 question, Answer answer, List<Inputs> inputs, List<OptionEntries> optionEntries, List<CorrectPoint> correctPoints) {
        this.setQuestion(question);
        this.setAnswer(answer);
        this.setInputs(inputs);
        this.setOptionEntries(optionEntries);
        this.setCorrectPoints(correctPoints);
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

    public List<Inputs> getInputs() { return inputs; }

    public void setInputs(List<Inputs> inputs) { this.inputs = inputs; }

    public List<OptionEntries> getOptionEntries() { return optionEntries; }

    public void setOptionEntries(List<OptionEntries> optionEntries) { this.optionEntries = optionEntries; }

    public List<CorrectPoint> getCorrectPoints() { return correctPoints; }

    public void setCorrectPoints(List<CorrectPoint> correctPoints) { this.correctPoints = correctPoints; }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QuestionAndAnswer{");
        sb.append("question=").append(question);
        sb.append(", answer=").append(answer);
        sb.append(", inputs=").append(inputs);
        sb.append(", optionEntries=").append(optionEntries);
        sb.append('}');
        return sb.toString();
    }
}
