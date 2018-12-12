/**
 * 
 */
package com.pgault04.repositories;

import static org.junit.Assert.*;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.Module;
import com.pgault04.repositories.ModuleRepo;

/**
 * @author paulgault
 *
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


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.moduleName = "moduleName";
		this.moduleDescription = "moduleDescription";
		this.year = 2018;

		module = new Module(moduleName, moduleDescription, TUTOR_ID_IN_DB, year);
	}

	

	/**
	 * Test method for Row Count
	 */
	@Test
	public void testRowCount() {

		int rowCountBefore = moduleRepo.rowCount().intValue();
		
		// Inserts one module to table
		moduleRepo.insert(module);

		// Checks one value is registered as in the table
		assertTrue(moduleRepo.rowCount().intValue() > rowCountBefore);

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.ModuleRepo#insert(pgault04.entities.Module)}.
	 */
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

	/**
	 * Test method for
	 * {@link pgault04.repositories.ModuleRepo#selectByModuleID(java.lang.Long)}.
	 */
	@Test
	public void testSelectByModuleID() {
		// Inserts one module to table
		Module returnedModule = moduleRepo.insert(module);

		returnedModule = moduleRepo.selectByModuleID(returnedModule.getModuleID());

		assertNotNull(returnedModule);
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.ModuleRepo#selectByUsername(java.lang.String)}.
	 */
	@Test
	public void testSelectByModuleName() {
		// Inserts one module to table
		moduleRepo.insert(module);

		List<Module> modules = moduleRepo.selectByModuleName(module.getModuleName());

		assertTrue(modules.size() > 0);
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.ModuleRepo#selectByFirstName(java.lang.String)}.
	 */
	@Test
	public void testSelectByTutorID() {
		// Inserts one module to table
		moduleRepo.insert(module);

		List<Module> modules = moduleRepo.selectByTutorID(TUTOR_ID_IN_DB);

		assertTrue(modules.size() > 1);
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.ModuleRepo#selectByLastName(java.lang.String)}.
	 */
	@Test
	public void testSelectByYear() {
		// Inserts one module to table
		moduleRepo.insert(module);

		List<Module> modules = moduleRepo.selectByYear(module.getYear());

		assertTrue(modules.size() > 1);
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.ModuleRepo#delete(java.lang.Long)}.
	 */
	@Test
	public void testDelete() {
		// Inserts one module to table
		Module returnedModule = moduleRepo.insert(module);

		moduleRepo.delete(returnedModule.getModuleID());

		returnedModule = moduleRepo.selectByModuleID(returnedModule.getModuleID());

		assertNull(returnedModule);
	}

}
