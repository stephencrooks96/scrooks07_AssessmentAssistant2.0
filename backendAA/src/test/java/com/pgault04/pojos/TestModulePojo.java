package com.pgault04.pojos;

import com.pgault04.entities.Module;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestModulePojo {

    private ModulePojo modulePojo;
    private Module module;
    private List<Associate> associations;

    @Before
    public void setUp() throws Exception {
        this.modulePojo = new ModulePojo();
        this.module = new Module();
        this.associations = new ArrayList<>();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(modulePojo);
    }

    @Test
    public void testConstructorWithArgs() {
        modulePojo = null;
        modulePojo = new ModulePojo(module, associations);

        assertNotNull(modulePojo);
        assertEquals(module, modulePojo.getModule());
        assertEquals(associations, modulePojo.getAssociations());
    }

    @Test
    public void testToString() {
        modulePojo = new ModulePojo(module, associations);
        assertEquals("ModulePojo{module=Module{moduleID=-1, moduleName='null', moduleDescription='null', tutorUserID=null, commencementDate='null', endDate='null', approved=null}, associations=[]}", modulePojo.toString());
    }
}