package com.pgault04.pojos;

import java.util.LinkedList;

/**
 * @author Paul Gault 40126005
 * @since Jan 2019
 * Pojo to allow collection of result chart info to be sent to/from front end
 */
public class ResultChartPojo {

    private LinkedList<String> labels;
    private LinkedList<Integer> scores;
    private Integer classAverage;
    private LinkedList<String> colors;

    /**
     * Default constructor
     */
    public ResultChartPojo() {}

    /**
     * Constructor with arguments
     *
     * @param labels       the labels for the chart data points
     * @param scores       the scores i.e the data for the chart
     * @param classAverage the class average
     * @param colors       the colors to be shown in the chart
     */
    public ResultChartPojo(LinkedList<String> labels, LinkedList<Integer> scores, Integer classAverage, LinkedList<String> colors) {
        this.setLabels(labels);
        this.setScores(scores);
        this.setClassAverage(classAverage);
        this.setColors(colors);
    }

    /**
     * @return the list of labels
     */
    public LinkedList<String> getLabels() { return labels; }

    /**
     * @param labels sets the list of labels
     */
    public void setLabels(LinkedList<String> labels) { this.labels = labels; }

    /**
     * @return gets the list of scores
     */
    public LinkedList<Integer> getScores() { return scores; }

    /**
     * @param scores sets the list of scores for the chart data
     */
    public void setScores(LinkedList<Integer> scores) { this.scores = scores; }

    /**
     * @return gets the class average
     */
    public Integer getClassAverage() { return classAverage; }

    /**
     * @param classAverage sets the class average
     */
    public void setClassAverage(Integer classAverage) { this.classAverage = classAverage; }

    /**
     * @return gets the list of colors for the chart
     */
    public LinkedList<String> getColors() { return colors; }

    /**
     * @param colors sets the list of colors for the charts
     */
    public void setColors(LinkedList<String> colors) { this.colors = colors; }

    /*
     * the object as string
     */
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