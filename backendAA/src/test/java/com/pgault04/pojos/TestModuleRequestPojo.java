package com.pgault04.pojos;

import com.pgault04.entities.Module;
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
public class TestModuleRequestPojo {

    private ModuleRequestPojo moduleRequestPojo;
    private User tutor;
    private Module module;

    @Before
    public void setUp() throws Exception {
        this.moduleRequestPojo = new ModuleRequestPojo();
        this.module = new Module();
        this.tutor = new User();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(moduleRequestPojo);
    }

    @Test
    public void testConstructorWithArgs() {
        moduleRequestPojo = null;
        moduleRequestPojo = new ModuleRequestPojo(tutor, module);

        assertNotNull(moduleRequestPojo);
        assertEquals(module, moduleRequestPojo.getModule());
        assertEquals(tutor, moduleRequestPojo.getTutor());
    }

    @Test
    public void testToString() {
        moduleRequestPojo = new ModuleRequestPojo(tutor, module);
        assertEquals("ModuleRequestPojo{tutor=User{userID=-1, username='null', password='null', firstName='null', lastName='null', enabled=null, userRoleID=null, tutor=null}, module=Module{moduleID=-1, moduleName='null', moduleDescription='null', tutorUserID=null, commencementDate='null', endDate='null', approved=null}}", moduleRequestPojo.toString());
    }
}
