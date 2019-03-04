package com.pgault04.pojos;

import com.pgault04.entities.Inputs;
import com.pgault04.entities.Option;
import com.pgault04.entities.Question;
import com.pgault04.entities.QuestionMathLine;

import java.util.List;

public class QuestionAndBase64 {

    private String base64;
    private List<Option> options;
    private List<QuestionMathLine> mathLines;
    private Question question;

    public QuestionAndBase64() {}

    public QuestionAndBase64(String base64, List<Option> options, List<QuestionMathLine> mathLines, Question question) {
        this.setBase64(base64);
        this.setOptions(options);
        this.setMathLines(mathLines);
        this.setQuestion(question);
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public List<Option> getOptions() { return options; }

    public void setOptions(List<Option> options) { this.options = options; }

    /**
     * @return math lines
     */
    public List<QuestionMathLine> getMathLines() { return mathLines; }

    /**
     * @param mathLines - The math lines for the question
     */
    public void setMathLines(List<QuestionMathLine> mathLines) { this.mathLines = mathLines; }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QuestionAndBase64{");
        sb.append("base64='").append(base64).append('\'');
        sb.append(", options=").append(options);
        sb.append(", mathLines=").append(mathLines);
        sb.append(", question=").append(question);
        sb.append('}');
        return sb.toString();
    }
}
