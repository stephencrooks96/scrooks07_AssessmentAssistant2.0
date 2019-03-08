package com.pgault04.services;

import com.pgault04.controller.MainController;
import com.pgault04.entities.UserSession;
import com.pgault04.pojos.TokenPojo;
import com.pgault04.repositories.UserSessionsRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.security.Principal;
import java.sql.Timestamp;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Paul Gault - 40126005
 * @since Jan 2019
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMainService {

    private static final String USER_IN_DB = "pgault04@qub.ac.uk";

    @Autowired
    MainService mainService;
    @Autowired
    MainController mainController;
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
        userSessionsRepo.insert(userSession);
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateTokenInvalid() {
        mainService.generateToken(null);
    }

    @Test
    @Transactional
    public void testGenerateTokenValid() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USER_IN_DB);

        TokenPojo tokenPojo = mainController.user(principal);
        assertEquals(token, tokenPojo.getToken());
    }

    @Test
    @Transactional
    public void destroyTokenValid() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USER_IN_DB);
        mainController.logout(principal);
        UserSession userSession = userSessionsRepo.selectByUsername(USER_IN_DB);
        assertTrue(userSession.getLastActive().before(new Timestamp(System.currentTimeMillis() - 1800000)));
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testDestroyTokenInvalid() {
        mainService.destroyToken(null);
    }
}