package com.pgault04.pojos;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMarkerWithChart {

    private MarkerWithChart markerWithChart;
    private List<Marker> markers;
    private List<String> labels;
    private List<Integer> data;
    private List<String> colours;

    @Before
    public void setUp() throws Exception {
        this.markerWithChart = new MarkerWithChart();
        this.markers = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.data = new ArrayList<>();
        this.colours = new ArrayList<>();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(markerWithChart);
    }

    @Test
    public void testConstructorWithArgs() {
        markerWithChart = null;
        markerWithChart = new MarkerWithChart(markers, labels, data, colours);

        assertNotNull(markerWithChart);
        assertEquals(markers, markerWithChart.getMarkers());
        assertEquals(labels, markerWithChart.getLabels());
        assertEquals(data, markerWithChart.getData());
        assertEquals(colours, markerWithChart.getColours());
    }

    @Test
    public void testToString() {
        markerWithChart = new MarkerWithChart(markers, labels, data, colours);
        assertEquals("MarkerWithChart{markers=[], labels=[], data=[], colours=[]}", markerWithChart.toString());
    }
}
