package com.pgault04.pojos;
import com.pgault04.entities.Tests;

public class TestMarking {

    private Tests test;
    private Integer toBeMarkedByYou;
    private Integer toBeMarkedByTAs;
    private Integer marked;
    private Integer totalForYou;
    private Integer totalForTAs;

    public TestMarking(Tests test, Integer toBeMarkedByYou, Integer toBeMarkedByTAs, Integer marked, Integer totalForYou,
            Integer totalForTAs) {
        this.setTest(test);
        this.setToBeMarkedByYou(toBeMarkedByYou);
        this.setToBeMarkedByTAs(toBeMarkedByTAs);
        this.setMarked(marked);
        this.setTotalForYou(totalForYou);
        this.setTotalForTAs(totalForTAs);
    }

    public Integer getTotalForYou() { return totalForYou; }

    public void setTotalForYou(Integer totalForYou) { this.totalForYou = totalForYou; }

    public Integer getTotalForTAs() { return totalForTAs; }

    public void setTotalForTAs(Integer totalForTAs) { this.totalForTAs = totalForTAs; }

    public Tests getTest() { return test; }

    public void setTest(Tests test) {
        this.test = test;
    }

    public Integer getToBeMarkedByYou() {
        return toBeMarkedByYou;
    }

    public void setToBeMarkedByYou(Integer toBeMarkedByYou) {
        this.toBeMarkedByYou = toBeMarkedByYou;
    }

    public Integer getToBeMarkedByTAs() {
        return toBeMarkedByTAs;
    }

    public void setToBeMarkedByTAs(Integer toBeMarkedByTAs) {
        this.toBeMarkedByTAs = toBeMarkedByTAs;
    }

    public Integer getMarked() { return marked; }

    public void setMarked(Integer marked) { this.marked = marked; }
}
