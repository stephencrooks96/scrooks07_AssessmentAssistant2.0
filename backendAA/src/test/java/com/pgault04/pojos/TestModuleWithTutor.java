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

    private Integer year;

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
        year = 2018;
        moduleObj = new Module(moduleName, moduleDescription, tutorUserID, year, 1);
    }

    @Test
    public void testModuleWithTutorDefaultConstructor() {
        assertNotNull(moduleWithTutor);
    }

    @Test
    public void testModuleWithTutorConstructorWithArgs() {
        moduleWithTutor = null;
        moduleWithTutor = new ModuleWithTutor(userObj, moduleObj);

        assertNotNull(moduleWithTutor);
        assertEquals(userObj, moduleWithTutor.getTutor());
        assertEquals(moduleObj, moduleWithTutor.getModule());
    }

    @Test
    public void testGetSetTutor() {
        moduleWithTutor.setTutor(userObj);
        assertEquals(userObj, moduleWithTutor.getTutor());
    }

    @Test
    public void testGetSetModule() {
        moduleWithTutor.setModule(moduleObj);
        assertEquals(moduleObj, moduleWithTutor.getModule());
    }

    @Test
    public void testToString() {
        moduleWithTutor = new ModuleWithTutor(userObj, moduleObj);
        assertEquals("ModuleWithTutor{tutor=User{userID=-1, username='username', password='password', firstName='firstName', lastName='lastName', enabled=1, userRoleID=2}, module=Module{moduleID=-1, moduleName='moduleName', moduleDescription='moduleDescription', tutorUserID=2, year=2018}}", moduleWithTutor.toString());
    }
}
