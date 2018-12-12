package com.pgault04.pojos;

import com.pgault04.entities.Tests;

public class TestAndGrade {

    private Tests test;
    private String grade;

    public TestAndGrade(Tests test, String grades) {
        this.setTest(test);
        this.setGrade(grades);
    }

    public Tests getTest() {
        return test;
    }

    public void setTest(Tests test) {
        this.test = test;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
