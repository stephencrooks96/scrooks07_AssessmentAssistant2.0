package com.pgault04.pojos;

import com.pgault04.entities.Answer;
import com.pgault04.entities.Question;
import com.pgault04.entities.TestResult;
import com.pgault04.entities.Tests;

import java.util.List;

/**
 * Class for accumulating test and result info before outputting it to front end
 * @author Paul Gault 40126005
 * @since November 2018
 */
public class TestAndResult {

    private Tests test;
    private TestResult testResult;
    private List<Question> questions;
    private List<Answer> answers;
    private Double percentageScore;

    /**
     * Default constructor
     */
    public TestAndResult() { }

    /**
     * Constructor with args
     * @param test the test
     * @param testResult the result
     * @param questions the questions
     * @param answers the answers
     */
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

    /**
     * @return the percentage score
     */
    public Double getPercentageScore() {
        return percentageScore;
    }

    /**
     * @param percentageScore the percentage score to set
     */
    public void setPercentageScore(Double percentageScore) {
        this.percentageScore = percentageScore;
    }

    /**
     * @return the test
     */
    public Tests getTest() {
        return test;
    }

    /**
     * @param test the test to set
     */
    public void setTest(Tests test) {
        this.test = test;
    }

    /**
     * @return the test result
     */
    public TestResult getTestResult() {
        return testResult;
    }

    /**
     * @param testResult the test result to set
     */
    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    /**
     * @return the questions
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * @param questions the questions to set
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    /**
     * @return the answers
     */
    public List<Answer> getAnswers() {
        return answers;
    }

    /**
     * @param answers the answers to set
     */
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    /**
     * @return the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TestAndResult{");
        sb.append("test=").append(test);
        sb.append(", testResult=").append(testResult);
        sb.append(", questions=").append(questions);
        sb.append(", answers=").append(answers);
        sb.append(", percentageScore=").append(percentageScore);
        sb.append('}');
        return sb.toString();
    }
}
