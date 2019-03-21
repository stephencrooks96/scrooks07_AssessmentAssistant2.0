package com.pgault04.pojos;

import java.util.List;

/**
 * @author Paul Gault - 40126005
 * @since Jan 2019
 * Pojo to allow collection of marker with chart info to be sent to/from front end
 */
public class MarkerWithChart {

    private List<Marker> markers;
    private List<String> labels;
    private List<Integer> data;
    private List<String> colours;

    /**
     * Default constructor
     */
    public MarkerWithChart() {}

    /**
     * Constructor with arguments
     *
     * @param markers - the list of markers
     * @param labels  - the list of labels for the charts data points
     * @param data    - the data for the chart
     * @param colours - the colors of each section of the chart
     */
    public MarkerWithChart(List<Marker> markers, List<String> labels, List<Integer> data, List<String> colours) {
        this.setMarkers(markers);
        this.setData(data);
        this.setLabels(labels);
        this.setColours(colours);
    }

    /**
     * @return gets the list of colors for the data points
     */
    public List<String> getColours() { return colours; }

    /**
     * @param colours sets the list of colors for the data points
     */
    public void setColours(List<String> colours) { this.colours = colours; }

    /**
     * @return gets the list of markers
     */
    public List<Marker> getMarkers() { return markers; }

    /**
     * @param markers sets the list of markers
     */
    public void setMarkers(List<Marker> markers) { this.markers = markers; }

    /**
     * @return gets the labels for the data points
     */
    public List<String> getLabels() { return labels; }

    /**
     * @param labels sets the list of labels for the data points
     */
    public void setLabels(List<String> labels) { this.labels = labels; }

    /**
     * @return gets the chart data
     */
    public List<Integer> getData() { return data; }

    /**
     * @param data sets the chart data
     */
    public void setData(List<Integer> data) { this.data = data; }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MarkerWithChart{");
        sb.append("markers=").append(markers);
        sb.append(", labels=").append(labels);
        sb.append(", data=").append(data);
        sb.append(", colours=").append(colours);
        sb.append('}');
        return sb.toString();
    }
}