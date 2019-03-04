package com.pgault04.pojos;

import com.pgault04.entities.Question;
import com.pgault04.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTokenPojo {

    private TokenPojo tokenPojo;
    private User user;
    private String token;

    @Before
    public void setUp() throws Exception {
        this.tokenPojo = new TokenPojo();
        this.user = new User();
        this.token = "token";
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(tokenPojo);
    }

    @Test
    public void testConstructorWithArgs() {
        tokenPojo = null;
        tokenPojo = new TokenPojo(user, token);

        assertNotNull(tokenPojo);
        assertEquals(user, tokenPojo.getUser());
        assertEquals(token, tokenPojo.getToken());
    }

    @Test
    public void testToString() {
        tokenPojo = new TokenPojo(user, token);
        assertEquals("TokenPojo{user=User{userID=-1, username='null', password='null', firstName='null', lastName='null', enabled=null, userRoleID=null, tutor=null}, token='token'}", tokenPojo.toString());
    }
}
