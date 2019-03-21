package com.pgault04.pojos;

import com.pgault04.entities.Answer;
import com.pgault04.entities.Tests;
import com.pgault04.entities.User;

import java.util.List;

/**
 * @author Paul Gault - 40126005
 * @since Jan 2019
 * Pojo to allow collection of marker info to be sent to/from front end
 */
public class Marker {

    private Tests test;
    private User marker;
    private String markerType;
    private List<Answer> scripts;
    private Integer marked;
    private Integer unmarked;

    /**
     * Default constructor
     */
    public Marker() {}

    /**
     * Constructor with arguments
     *
     * @param test       - the test
     * @param marker     - the marker
     * @param markerType - the type of marker they are
     * @param scripts    - the scripts they are marking / have marked
     * @param marked     - how many are marked
     * @param unmarked   - how many aren't marked
     */
    public Marker(Tests test, User marker, String markerType, List<Answer> scripts, Integer marked, Integer unmarked) {
        this.setTest(test);
        this.setMarker(marker);
        this.setMarkerType(markerType);
        this.setScripts(scripts);
        this.setMarked(marked);
        this.setUnmarked(unmarked);
    }

    /**
     * @return gets the test
     */
    public Tests getTest() { return test; }

    /**
     * @param test - sets the test
     */
    public void setTest(Tests test) { this.test = test; }

    /**
     * @return gets the marker
     */
    public User getMarker() { return marker; }

    /**
     * @param marker sets the marker
     */
    public void setMarker(User marker) { this.marker = marker; }

    /**
     * @return gets the type of marker that they are
     */
    public String getMarkerType() { return markerType; }

    /**
     * @param markerType sets the marker type
     */
    public void setMarkerType(String markerType) { this.markerType = markerType; }

    /**
     * @return gets the scripts the marker has or is marking
     */
    public List<Answer> getScripts() { return scripts; }

    /**
     * @param scripts - sets the scripts the marker has or is marking
     */
    public void setScripts(List<Answer> scripts) { this.scripts = scripts; }

    /**
     * @return gets the amount of scripts that have been marked
     */
    public Integer getMarked() { return marked; }

    /**
     * @param marked sets the amount of answers that have been marked
     */
    public void setMarked(Integer marked) { this.marked = marked; }

    /**
     * @return gets the amount of answers that haven't been marked
     */
    public Integer getUnmarked() { return unmarked; }

    /**
     * @param unmarked sets the amount of answers that haven't been marked
     */
    public void setUnmarked(Integer unmarked) { this.unmarked = unmarked; }

    /*
     * the object as string
     */
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