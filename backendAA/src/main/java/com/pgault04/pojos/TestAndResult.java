package com.pgault04.pojos;

import com.pgault04.entities.Answer;
import com.pgault04.entities.Question;
import com.pgault04.entities.TestResult;
import com.pgault04.entities.Tests;

import java.util.List;

public class TestAndResult {

    private Tests test;
    private TestResult testResult;
    private List<Question> questions;
    private List<Answer> answers;
    private Double percentageScore;

    public TestAndResult(Tests test, TestResult testResult, List<Question> questions, List<Answer> answers) {
        this.setTest(test);
        this.setTestResult(testResult);
        this.setQuestions(questions);
        this.setAnswers(answers);
        Double percentageScore = 0.0;
        for (Question q : questions) {
            percentageScore+=q.getMaxScore();
        }
        this.setPercentageScore(percentageScore);
    }

    public Double getPercentageScore() {
        return percentageScore;
    }

    public void setPercentageScore(Double percentageScore) {
        this.percentageScore = percentageScore;
    }

    public Tests getTest() {
        return test;
    }

    public void setTest(Tests test) {
        this.test = test;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
