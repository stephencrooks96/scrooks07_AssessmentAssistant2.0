package com.pgault04.pojos;

import java.util.List;

public class MarkerWithChart {

    private List<Marker> markers;

    private List<String> labels;
    private List<Integer> data;
    private List<String> colours;

    public MarkerWithChart(List<Marker> markers, List<String> labels, List<Integer> data, List<String> colours) {
        this.setMarkers(markers);
        this.setData(data);
        this.setLabels(labels);
        this.setColours(colours);
    }

    public List<String> getColours() {
        return colours;
    }

    public void setColours(List<String> colours) {
        this.colours = colours;
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(List<Marker> markers) {
        this.markers = markers;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
