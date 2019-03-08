package com.pgault04.utilities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Paul Gault - 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPasswordUtil {

    private String passwordToEncode;

    @Before
    public void setUp() throws Exception {
        this.passwordToEncode = "123";
    }

    @Test
    public void testDefaultConstructor() {
        PasswordUtil pE = new PasswordUtil();
        assertNotNull(pE);
    }

    @Test
    public void testGenerateRandomString() {
        assertEquals(15, PasswordUtil.generateRandomString().length());
    }

    @Test
    public void testEncrypt() {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        assertTrue(bcrypt.matches(passwordToEncode, PasswordUtil.encrypt(passwordToEncode)));
    }

}
