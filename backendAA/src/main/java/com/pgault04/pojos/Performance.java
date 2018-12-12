package com.pgault04.pojos;

public class Performance {

    private TestAndResult testAndResult;
    private Double classAverage;

    public Performance(TestAndResult testAndResult, Double classAverage) {
        this.setTestAndResult(testAndResult);
        this.setClassAverage(classAverage);
    }

    public TestAndResult getTestAndResult() { return testAndResult; }

    public void setTestAndResult(TestAndResult testAndResult) { this.testAndResult = testAndResult; }

    public Double getClassAverage() { return classAverage; }

    public void setClassAverage(Double classAverage) { this.classAverage = classAverage; }

}
