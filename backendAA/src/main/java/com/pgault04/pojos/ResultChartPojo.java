package com.pgault04.pojos;

import java.util.LinkedList;

public class ResultChartPojo {

    private LinkedList<String> labels;
    private LinkedList<Integer> scores;
    private Integer classAverage;
    private LinkedList<String> colors;

    public ResultChartPojo() {}

    public ResultChartPojo(LinkedList<String> labels, LinkedList<Integer> scores, Integer classAverage, LinkedList<String> colors) {
        this.setLabels(labels);
        this.setScores(scores);
        this.setClassAverage(classAverage);
        this.setColors(colors);
    }

    public LinkedList<String> getLabels() {
        return labels;
    }

    public void setLabels(LinkedList<String> labels) {
        this.labels = labels;
    }

    public LinkedList<Integer> getScores() {
        return scores;
    }

    public void setScores(LinkedList<Integer> scores) {
        this.scores = scores;
    }

    public Integer getClassAverage() {
        return classAverage;
    }

    public void setClassAverage(Integer classAverage) {
        this.classAverage = classAverage;
    }

    public LinkedList<String> getColors() {
        return colors;
    }

    public void setColors(LinkedList<String> colors) {
        this.colors = colors;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResultChartPojo{");
        sb.append("labels=").append(labels);
        sb.append(", scores=").append(scores);
        sb.append(", classAverage=").append(classAverage);
        sb.append(", colors=").append(colors);
        sb.append('}');
        return sb.toString();
    }
}
