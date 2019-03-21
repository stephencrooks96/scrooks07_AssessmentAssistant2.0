package com.pgault04.pojos;

/**
 * Class to accumulate information needed for performance metrics and output them to front end
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
public class Performance {

    private TestAndResult testAndResult;
    private Double classAverage;

    /**
     * Default constructor
     */
    public Performance() {}

    /**
     * Constructor with args
     *
     * @param testAndResult the test with students result
     * @param classAverage  the class average for comparison
     */
    public Performance(TestAndResult testAndResult, Double classAverage) {
        this.setTestAndResult(testAndResult);
        this.setClassAverage(classAverage);
    }

    /**
     * @return the test and result
     */
    public TestAndResult getTestAndResult() { return testAndResult; }

    /**
     * @param testAndResult the test and result to set
     */
    public void setTestAndResult(TestAndResult testAndResult) { this.testAndResult = testAndResult; }

    /**
     * @return the class average
     */
    public Double getClassAverage() { return classAverage; }

    /**
     * @param classAverage the class average to set
     */
    public void setClassAverage(Double classAverage) { this.classAverage = classAverage; }

    /**
     * @return the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Performance{");
        sb.append("testAndResult=").append(testAndResult.toString());
        sb.append(", classAverage=").append(classAverage);
        sb.append('}');
        return sb.toString();
    }
}