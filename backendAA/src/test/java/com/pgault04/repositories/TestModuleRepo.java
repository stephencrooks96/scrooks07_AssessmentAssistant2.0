package com.pgault04.repositories;

import com.pgault04.entities.Module;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestModuleRepo {

    private static final long TUTOR_ID_IN_DB = 2L;

    @Autowired
    ModuleRepo moduleRepo;

    // Module vars
    private Module module;
    private String moduleName, moduleDescription;
    private Integer year;

    @Before
    public void setUp() throws Exception {
        this.moduleName = "moduleName";
        this.moduleDescription = "moduleDescription";
        this.year = 2018;
        module = new Module(moduleName, moduleDescription, TUTOR_ID_IN_DB, "dateC", "dateE", 1);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = moduleRepo.rowCount();
        // Inserts one module to table
        moduleRepo.insert(module);
        // Checks one value is registered as in the table
        assertTrue(moduleRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        // Inserts one module to table
        Module returnedModule = moduleRepo.insert(module);
        returnedModule = moduleRepo.selectByModuleID(returnedModule.getModuleID());
        assertNotNull(returnedModule);
        // Updates the module in the table
        returnedModule.setModuleName("moduleName2");
        // Inserts one module to table
        moduleRepo.insert(returnedModule);
        returnedModule = moduleRepo.selectByModuleID(returnedModule.getModuleID());
        assertEquals("moduleName2", returnedModule.getModuleName());
    }

    @Test
    public void testSelectByModuleID() {
        // Inserts one module to table
        Module returnedModule = moduleRepo.insert(module);
        returnedModule = moduleRepo.selectByModuleID(returnedModule.getModuleID());
        assertNotNull(returnedModule);
    }

    @Test
    public void testSelectByModuleName() {
        // Inserts one module to table
        moduleRepo.insert(module);
        List<Module> modules = moduleRepo.selectByModuleName(module.getModuleName());
        assertTrue(modules.size() > 0);
    }

    @Test
    public void testSelectByTutorID() {
        // Inserts one module to table
        moduleRepo.insert(module);
        List<Module> modules = moduleRepo.selectByTutorID(TUTOR_ID_IN_DB);
        assertTrue(modules.size() > 1);
    }

    @Test
    public void testDelete() {
        // Inserts one module to table
        Module returnedModule = moduleRepo.insert(module);
        moduleRepo.delete(returnedModule.getModuleID());
        returnedModule = moduleRepo.selectByModuleID(returnedModule.getModuleID());
        assertNull(returnedModule);
    }
}