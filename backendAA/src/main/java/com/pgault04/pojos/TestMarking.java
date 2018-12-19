package com.pgault04.pojos;

import com.pgault04.entities.Tests;

/**
 * Class to accumulate the test and all marking data needed before output to front end
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
public class TestMarking {

    private Tests test;
    private Integer toBeMarkedByYou;
    private Integer toBeMarkedByTAs;
    private Integer marked;
    private Integer totalForYou;
    private Integer totalForTAs;

    /**
     * The default constructor
     */
    public TestMarking() {}

    /**
     * The constructor with args
     *
     * @param test            the test
     * @param toBeMarkedByYou the answers to be marked by the principal user
     * @param toBeMarkedByTAs the answers to be marked by assistants
     * @param marked          the already marked answers
     * @param totalForYou     total scripts assigned to principal user
     * @param totalForTAs     total scripts assigned to assistants
     */
    public TestMarking(Tests test, Integer toBeMarkedByYou, Integer toBeMarkedByTAs, Integer marked, Integer totalForYou,
                       Integer totalForTAs) {
        this.setTest(test);
        this.setToBeMarkedByYou(toBeMarkedByYou);
        this.setToBeMarkedByTAs(toBeMarkedByTAs);
        this.setMarked(marked);
        this.setTotalForYou(totalForYou);
        this.setTotalForTAs(totalForTAs);
    }

    /**
     * @return the total for the principal user
     */
    public Integer getTotalForYou() {
        return totalForYou;
    }

    /**
     * @param totalForYou the total for the principal user to set
     */
    public void setTotalForYou(Integer totalForYou) {
        this.totalForYou = totalForYou;
    }

    /**
     * @return the total for the assistants
     */
    public Integer getTotalForTAs() {
        return totalForTAs;
    }

    /**
     * @param totalForTAs the total for the assistants to set
     */
    public void setTotalForTAs(Integer totalForTAs) {
        this.totalForTAs = totalForTAs;
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
     * @return the scripts to be marked by principal user
     */
    public Integer getToBeMarkedByYou() {
        return toBeMarkedByYou;
    }

    /**
     * @param toBeMarkedByYou the scripts to be marked by principal user to set
     */
    public void setToBeMarkedByYou(Integer toBeMarkedByYou) {
        this.toBeMarkedByYou = toBeMarkedByYou;
    }

    /**
     * @return the scripts to be marked by assistants
     */
    public Integer getToBeMarkedByTAs() {
        return toBeMarkedByTAs;
    }

    /**
     * @param toBeMarkedByTAs the scripts to be marked by assistants to set
     */
    public void setToBeMarkedByTAs(Integer toBeMarkedByTAs) {
        this.toBeMarkedByTAs = toBeMarkedByTAs;
    }

    /**
     * @return the number of marked scripts
     */
    public Integer getMarked() {
        return marked;
    }

    /**
     * @param marked the number of marked scripts to set
     */
    public void setMarked(Integer marked) {
        this.marked = marked;
    }

    /**
     * @return the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TestMarking{");
        sb.append("test=").append(test);
        sb.append(", toBeMarkedByYou=").append(toBeMarkedByYou);
        sb.append(", toBeMarkedByTAs=").append(toBeMarkedByTAs);
        sb.append(", marked=").append(marked);
        sb.append(", totalForYou=").append(totalForYou);
        sb.append(", totalForTAs=").append(totalForTAs);
        sb.append('}');
        return sb.toString();
    }
}
