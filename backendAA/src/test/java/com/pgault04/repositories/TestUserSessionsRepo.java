package com.pgault04.repositories;

import com.pgault04.entities.UserSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.sql.Timestamp;

import static org.junit.Assert.*;

@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserSessionsRepo {

    private static final String USER_IN_DB = "pgault04@qub.ac.uk";

    @Autowired
    private UserSessionsRepo userSessionsRepo;
    private UserSession userSession;
    private String token;
    private Timestamp lastActive;

    @Before
    public void setUp() throws Exception {
        this.token = "token";
        this.lastActive = new Timestamp(1569285049284L);
        userSession = new UserSession(USER_IN_DB, token, lastActive);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = userSessionsRepo.rowCount();
        // Inserts one userSession to table
        userSessionsRepo.insert(userSession);
        // Checks one value is registered as in the table
        assertTrue(userSessionsRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        // Inserts one userSession to table
        UserSession us = userSessionsRepo.insert(userSession);
        assertNotNull(us);
        // Updates the userSession in the table
        String updateCheck = "token2";
        us.setToken(updateCheck);
        // Inserts one userSession to table
        UserSession updatedUs = userSessionsRepo.insert(us);
        assertEquals(us.getUsername(), updatedUs.getUsername());
        assertEquals(updateCheck, updatedUs.getToken());
    }

    @Test
    public void testSelectByUsername() {
        // Inserts one userSession to table
        UserSession us = userSessionsRepo.insert(userSession);
        us = userSessionsRepo.selectByUsername(us.getUsername());
        assertNotNull(us);
    }
}
