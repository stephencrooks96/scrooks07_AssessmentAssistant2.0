package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.Module;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestModule {

	private Module moduleObj;

	private Long moduleID;

	private String moduleName;

	private String moduleDescription;

	private Long tutorUserID;

	private Integer year;

	@Before
	public void setUp() throws Exception {
		moduleObj = new Module();
		moduleID = 1L;
		moduleName = "moduleName";
		moduleDescription = "moduleDescription";
		tutorUserID = 2L;
		year = 2018;
	}

	@Test
	public void testModuleDefaultConstructor() {
		assertNotNull(moduleObj);
	}

	@Test
	public void testModuleConstructorWithArgs() {
		moduleObj = null;
		moduleObj = new Module(moduleName, moduleDescription, tutorUserID, year, 1);

		assertNotNull(moduleObj);
		assertEquals(moduleName, moduleObj.getModuleName());
		assertEquals(moduleDescription, moduleObj.getModuleDescription());
		assertEquals(tutorUserID, moduleObj.getTutorUserID());
		assertEquals(year, moduleObj.getYear());
	}

	@Test
	public void testGetSetModuleID() {
		moduleObj.setModuleID(moduleID);
		assertEquals(moduleID, moduleObj.getModuleID());
	}

	@Test
	public void testGetSetModuleName() {
		moduleObj.setModuleName(moduleName);
		assertEquals(moduleName, moduleObj.getModuleName());
	}

	@Test
	public void testGetSetModuleDescription() {
		moduleObj.setModuleDescription(moduleDescription);
		assertEquals(moduleDescription, moduleObj.getModuleDescription());
	}

	@Test
	public void testGetSetTutorUserID() {
		moduleObj.setTutorUserID(tutorUserID);
		assertEquals(tutorUserID, moduleObj.getTutorUserID());
	}

	@Test
	public void testGetSetYear() {
		moduleObj.setYear(year);
		assertEquals(year, moduleObj.getYear());
	}

	@Test
	public void testToString() {
		moduleObj = new Module(moduleName, moduleDescription, tutorUserID, year, 1);
		moduleObj.setModuleID(moduleID);
		assertEquals("Module{moduleID=1, moduleName='moduleName', moduleDescription='moduleDescription', tutorUserID=2, year=2018}", moduleObj.toString());
	}
}