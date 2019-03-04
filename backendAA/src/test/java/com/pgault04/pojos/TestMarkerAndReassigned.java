package com.pgault04.pojos;

import com.pgault04.entities.Answer;
import com.pgault04.entities.Tests;
import com.pgault04.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMarkerAndReassigned {

    private MarkerAndReassigned markerAndReassigned;
    private Long markerID;
    private Long previousMarkerID;
    private Long specifyQuestion;
    private Long numberToReassign;

    @Before
    public void setUp() throws Exception {
        this.markerAndReassigned = new MarkerAndReassigned();
        this.markerID = 1L;
        this.previousMarkerID = 2L;
        this.specifyQuestion = 3L;
        this.numberToReassign = 4L;
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(markerAndReassigned);
    }

    @Test
    public void testConstructorWithArgs() {
        markerAndReassigned = null;
        markerAndReassigned = new MarkerAndReassigned(markerID, previousMarkerID, specifyQuestion, numberToReassign);

        assertNotNull(markerAndReassigned);
        assertEquals(markerID, markerAndReassigned.getMarkerID());
        assertEquals(previousMarkerID, markerAndReassigned.getPreviousMarkerID());
        assertEquals(specifyQuestion, markerAndReassigned.getSpecifyQuestion());
        assertEquals(numberToReassign, markerAndReassigned.getNumberToReassign());
    }

    @Test
    public void testToString() {
        markerAndReassigned = new MarkerAndReassigned(markerID, previousMarkerID, specifyQuestion, numberToReassign);
        assertEquals("MarkerAndReassigned{markerID=1, previousMarkerID=2, specifyQuestion=3, numberToReassign=4}", markerAndReassigned.toString());
    }


}
