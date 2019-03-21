package com.pgault04.pojos;

import com.pgault04.entities.Tests;

/**
 * Class for accumulating information on test and grade info before outputting it to front end
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
public class TestAndGrade {

    private Tests test;
    private String grade;

    /**
     * The default constructor
     */
    public TestAndGrade() {}

    /**
     * The constructor with args
     *
     * @param test   the test
     * @param grades the grades
     */
    public TestAndGrade(Tests test, String grades) {
        this.setTest(test);
        this.setGrade(grades);
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
     * @return the grade
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @param grade the grade to set
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TestAndGrade{");
        sb.append("test=").append(test.toString());
        sb.append(", grade='").append(grade).append('\'');
        sb.append('}');
        return sb.toString();
    }
}