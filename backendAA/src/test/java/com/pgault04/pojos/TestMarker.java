package com.pgault04.pojos;

import com.pgault04.entities.Answer;
import com.pgault04.entities.Tests;
import com.pgault04.entities.User;
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
public class TestMarker {

    private Marker markerObj;
    private Tests test;
    private User marker;
    private String markerType;
    private List<Answer> scripts;
    private Integer marked;
    private Integer unmarked;

    @Before
    public void setUp() throws Exception {
        this.markerObj = new Marker();
        this.test = new Tests();
        this.marker = new User();
        this.markerType = "type";
        this.scripts = new ArrayList<>();
        this.marked = 1;
        this.unmarked = 2;
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(markerObj);
    }

    @Test
    public void testConstructorWithArgs() {
        markerObj = null;
        markerObj = new Marker(test, marker, markerType, scripts, marked, unmarked);

        assertNotNull(markerObj);
        assertEquals(test, markerObj.getTest());
        assertEquals(marker, markerObj.getMarker());
        assertEquals(markerType, markerObj.getMarkerType());
        assertEquals(scripts, markerObj.getScripts());
        assertEquals(marked, markerObj.getMarked());
        assertEquals(unmarked, markerObj.getUnmarked());
    }

    @Test
    public void testToString() {
        markerObj = new Marker(test, marker, markerType, scripts, marked, unmarked);
        assertEquals("Marker{test=Tests{testID=-1, moduleID=null, testTitle='null', startDateTime='null', endDateTime='null', publishResults=null, scheduled=null, publishGrades=null, practice=null}, marker=User{userID=-1, username='null', password='null', firstName='null', lastName='null', enabled=null, userRoleID=null, tutor=null}, markerType='type', scripts=[], marked=1, unmarked=2}", markerObj.toString());
    }
}
