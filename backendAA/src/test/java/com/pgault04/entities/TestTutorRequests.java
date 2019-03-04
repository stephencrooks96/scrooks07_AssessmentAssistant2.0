package com.pgault04.entities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTutorRequests {

    private TutorRequests tutorRequests;
    private Long tutorRequestID;
    private Long userID;
    private String reason;
    private Integer approved;

    @Before
    public void setUp() throws Exception {
        this.tutorRequests = new TutorRequests();
        this.tutorRequestID = 1L;
        this.userID = 2L;
        this.reason = "reason";
        this.approved = 1;
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(tutorRequests);
    }

    @Test
    public void testConstructorWithArgs() {
        tutorRequests = null;
        tutorRequests = new TutorRequests(userID, reason, approved);
        tutorRequests.setTutorRequestID(tutorRequestID);
        assertNotNull(tutorRequests);
        assertEquals(tutorRequestID, tutorRequests.getTutorRequestID());
        assertEquals(userID, tutorRequests.getUserID());
        assertEquals(reason, tutorRequests.getReason());
        assertEquals(approved, tutorRequests.getApproved());
    }

    @Test
    public void testToString() {
        tutorRequests = new TutorRequests(userID, reason, approved);
        tutorRequests.setTutorRequestID(tutorRequestID);
        assertEquals("TutorRequests{tutorRequestID=1, userID=2, reason='reason', approved=1}", tutorRequests.toString());
    }
}
