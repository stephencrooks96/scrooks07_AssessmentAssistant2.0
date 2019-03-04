package com.pgault04.pojos;

import com.pgault04.entities.TutorRequests;
import com.pgault04.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTutorRequestPojo {

    private TutorRequestPojo tutorRequestPojo;
    private User tutor;
    private TutorRequests request;

    @Before
    public void setUp() throws Exception {
        this.tutorRequestPojo = new TutorRequestPojo();
        this.tutor = new User();
        this.request = new TutorRequests();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(tutorRequestPojo);
    }

    @Test
    public void testConstructorWithArgs() {
        tutorRequestPojo = null;
        tutorRequestPojo = new TutorRequestPojo(tutor, request);

        assertNotNull(tutorRequestPojo);
        assertEquals(tutor, tutorRequestPojo.getTutor());
        assertEquals(request, tutorRequestPojo.getRequest());
    }

    @Test
    public void testToString() {
        tutorRequestPojo = new TutorRequestPojo(tutor, request);
        assertEquals("TutorRequestPojo{tutor=User{userID=-1, username='null', password='null', firstName='null', lastName='null', enabled=null, userRoleID=null, tutor=null}, request=TutorRequests{tutorRequestID=-1, userID=null, reason='null', approved=null}}", tutorRequestPojo.toString());
    }
}
