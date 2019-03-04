package com.pgault04.pojos;

import com.pgault04.entities.Module;
import com.pgault04.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestModuleWithTutor {

    private ModuleWithTutor moduleWithTutor;

    private User userObj;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private Integer enabled;

    private Long userRoleID;

    private Module moduleObj;

    private String moduleName;

    private String moduleDescription;

    private Long tutorUserID;

    private String commencementDate;

    private String endDate;

    @Before
    public void setUp() throws Exception {
        moduleWithTutor = new ModuleWithTutor();
        this.username = "username";
        this.firstName = "firstName";
        this.lastName = "lastName";
        this.password = "password";
        this.enabled = 1;
        this.userRoleID = 2L;
        userObj = new User(username, password, firstName, lastName, enabled, userRoleID, 0);
        moduleName = "moduleName";
        moduleDescription = "moduleDescription";
        tutorUserID = 2L;
        commencementDate = "dateC";
        endDate = "dateE";
        moduleObj = new Module(moduleName, moduleDescription, tutorUserID, commencementDate, endDate, 1);
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(moduleWithTutor);
    }

    @Test
    public void testConstructorWithArgs() {
        moduleWithTutor = null;
        moduleWithTutor = new ModuleWithTutor(userObj, moduleObj);

        assertNotNull(moduleWithTutor);
        assertEquals(userObj, moduleWithTutor.getTutor());
        assertEquals(moduleObj, moduleWithTutor.getModule());
    }

    @Test
    public void testToString() {
        moduleWithTutor = new ModuleWithTutor(userObj, moduleObj);
        assertEquals("ModuleWithTutor{tutor=User{userID=-1, username='username', password='password', firstName='firstName', lastName='lastName', enabled=1, userRoleID=2, tutor=0}, module=Module{moduleID=-1, moduleName='moduleName', moduleDescription='moduleDescription', tutorUserID=2, commencementDate='dateC', endDate='dateE', approved=1}}", moduleWithTutor.toString());
    }
}
