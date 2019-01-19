package com.pgault04.pojos;

import com.pgault04.entities.Answer;
import com.pgault04.entities.Tests;
import com.pgault04.entities.User;

import java.util.List;

public class Marker {

    private Tests test;
    private User marker;
    private String markerType;
    private List<Answer> scripts;
    private Integer marked;
    private Integer unmarked;

    public Marker(Tests test, User marker, String markerType, List<Answer> scripts, Integer marked, Integer unmarked) {
        this.setTest(test);
        this.setMarker(marker);
        this.setMarkerType(markerType);
        this.setScripts(scripts);
        this.setMarked(marked);
        this.setUnmarked(unmarked);
    }

    public Tests getTest() {
        return test;
    }

    public void setTest(Tests test) {
        this.test = test;
    }

    public User getMarker() {
        return marker;
    }

    public void setMarker(User marker) {
        this.marker = marker;
    }

    public String getMarkerType() {
        return markerType;
    }

    public void setMarkerType(String markerType) {
        this.markerType = markerType;
    }

    public List<Answer> getScripts() {
        return scripts;
    }

    public void setScripts(List<Answer> scripts) {
        this.scripts = scripts;
    }

    public Integer getMarked() { return marked; }

    public void setMarked(Integer marked) { this.marked = marked; }

    public Integer getUnmarked() { return unmarked; }

    public void setUnmarked(Integer unmarked) { this.unmarked = unmarked; }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Marker{");
        sb.append("test=").append(test);
        sb.append(", marker=").append(marker);
        sb.append(", markerType='").append(markerType).append('\'');
        sb.append(", scripts=").append(scripts);
        sb.append(", marked=").append(marked);
        sb.append(", unmarked=").append(unmarked);
        sb.append('}');
        return sb.toString();
    }
}
