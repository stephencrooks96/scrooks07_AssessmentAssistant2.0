package com.pgault04.repositories;

import com.pgault04.entities.PasswordReset;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPasswordResetRepo {

    private static final long USER_ID_IN_DB = 1L;

    @Autowired
    PasswordResetRepo passwordResetRepo;

    private PasswordReset passwordResetObj;
    private String resetString = "xyz";

    @Before
    public void setUp() throws Exception {
        passwordResetObj = new PasswordReset(USER_ID_IN_DB, resetString);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = passwordResetRepo.rowCount();
        // Inserts one user to table
        passwordResetRepo.insert(passwordResetObj);
        // Checks one value is registered as in the table
        assertTrue(passwordResetRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        // Inserts one user to table
        passwordResetRepo.insert(passwordResetObj);
        PasswordReset passwordResets = passwordResetRepo.selectByUserID(passwordResetObj.getUserID());
        assertNotNull(passwordResets);
        // Updates the user in the table
        passwordResetObj.setResetString("newPass");
        // Inserts one user to table
        passwordResetRepo.insert(passwordResetObj);
        passwordResets = passwordResetRepo.selectByUserID(passwordResetObj.getUserID());
        assertEquals("newPass", passwordResets.getResetString());
    }

    @Test
    public void testSelectByUserID() {
        // Inserts one user to table
        passwordResetRepo.insert(passwordResetObj);
        PasswordReset passwordResets = passwordResetRepo.selectByUserID(passwordResetObj.getUserID());
        assertNotNull(passwordResets);
    }

    @Test
    public void testDelete() {
        // Inserts one user to table
        passwordResetRepo.insert(passwordResetObj);
        passwordResetRepo.delete(passwordResetObj.getUserID());
        PasswordReset passwordResets = passwordResetRepo.selectByUserID(passwordResetObj.getUserID());
        assertNull(passwordResets);
    }
}