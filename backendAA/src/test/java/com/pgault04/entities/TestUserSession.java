package com.pgault04.entities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserSession {

    private UserSession userSession;
    private String username;
    private String token;
    private Timestamp lastActive;

    @Before
    public void setUp() throws Exception {
        this.userSession = new UserSession();
        this.username = "username";
        this.token = "token";
        this.lastActive = Timestamp.valueOf("2019-03-04 10:10:10.0");
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(userSession);
    }

    @Test
    public void testConstructorWithArgs() {
        userSession = null;
        userSession = new UserSession(username, token, lastActive);
        assertNotNull(userSession);
        assertEquals(username, userSession.getUsername());
        assertEquals(token, userSession.getToken());
        assertEquals(lastActive, userSession.getLastActive());
    }

    @Test
    public void testToString() {
        userSession = new UserSession(username, token, lastActive);
        assertEquals("UserSession{username='username', token='token', lastActive=2019-03-04 10:10:10.0}", userSession.toString());
    }
}
