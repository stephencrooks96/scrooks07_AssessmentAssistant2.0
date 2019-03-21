package com.pgault04.pojos;

import com.pgault04.entities.TestResult;
import com.pgault04.entities.Tests;
import com.pgault04.entities.User;

import java.util.List;

/**
 * Class for accumulating test and result info before outputting it to front end
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
public class TestAndResult {

    private Tests test;
    private TestResult testResult;
    private List<QuestionAndAnswer> questions;
    private Double percentageScore;
    private User user;

    /**
     * Default constructor
     */
    public TestAndResult() {
    }

    /**
     * Constructor with args
     *
     * @param test       the test
     * @param testResult the result
     * @param questions  the questions
     */
    public TestAndResult(Tests test, TestResult testResult, List<QuestionAndAnswer> questions, User user) {
        this.setTest(test);
        this.setTestResult(testResult);
        this.setQuestions(questions);
        this.setUser(user);
        Double percentageScore = 0.0;
        for (QuestionAndAnswer q : questions) {
            if (q.getQuestion().getQuestion().getMaxScore() != null)
                percentageScore += q.getQuestion().getQuestion().getMaxScore();
        }
        this.setPercentageScore(percentageScore);
    }

    /**
     * @return gets the user whose result it is
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user sets the user whose result it is
     */
    public void setUser(User user) {
        this.user = user;
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
    public List<QuestionAndAnswer> getQuestions() {
        return questions;
    }

    /**
     * @param questions the questions to set
     */
    public void setQuestions(List<QuestionAndAnswer> questions) {
        this.questions = questions;
    }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TestAndResult{");
        sb.append("test=").append(test);
        sb.append(", testResult=").append(testResult);
        sb.append(", questions=").append(questions);
        sb.append(", percentageScore=").append(percentageScore);
        sb.append('}');
        return sb.toString();
    }
}