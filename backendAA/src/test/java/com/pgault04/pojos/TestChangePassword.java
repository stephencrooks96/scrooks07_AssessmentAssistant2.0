package com.pgault04.pojos;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestChangePassword {

    private ChangePassword changePassword;
    private String password;
    private String newPassword;
    private String repeatPassword;

    @Before
    public void setUp() throws Exception {
        this.changePassword = new ChangePassword();
        this.password = "password";
        this.newPassword = "newPassword";
        this.repeatPassword = "repeatPassword";
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(changePassword);
    }

    @Test
    public void testConstructorWithArgs() {
        changePassword = null;
        changePassword = new ChangePassword(password, newPassword, repeatPassword);

        assertNotNull(changePassword);
        assertEquals(password, changePassword.getPassword());
        assertEquals(newPassword, changePassword.getNewPassword());
        assertEquals(repeatPassword, changePassword.getRepeatPassword());
    }

    @Test
    public void testToString() {
        changePassword = new ChangePassword(password, newPassword, repeatPassword);
        assertEquals("ChangePassword{password='password', newPassword='newPassword', repeatPassword='repeatPassword'}", changePassword.toString());
    }
}
