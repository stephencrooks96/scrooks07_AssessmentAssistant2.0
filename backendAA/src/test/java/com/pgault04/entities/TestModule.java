/**
 * 
 */
package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.Module;

/**
 * @author paulgault
 *
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

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		moduleObj = new Module();

		moduleID = 1L;

		moduleName = "moduleName";

		moduleDescription = "moduleDescription";

		tutorUserID = 2L;

		year = 2018;
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Module#Module()}.
	 */
	@Test
	public void testModuleDefaultConstructor() {
		assertNotNull(moduleObj);
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Module#Module(java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	public void testModuleConstructorWithArgs() {
		moduleObj = null;
		moduleObj = new Module(moduleName, moduleDescription, tutorUserID, year);

		assertNotNull(moduleObj);
		assertEquals(moduleName, moduleObj.getModuleName());
		assertEquals(moduleDescription, moduleObj.getModuleDescription());
		assertEquals(tutorUserID, moduleObj.getTutorUserID());
		assertEquals(year, moduleObj.getYear());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Module#getModuleID()}.
	 */
	@Test
	public void testGetSetModuleID() {
		moduleObj.setModuleID(moduleID);
		assertEquals(moduleID, moduleObj.getModuleID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Module#getModuleName()}.
	 */
	@Test
	public void testGetSetModuleName() {
		moduleObj.setModuleName(moduleName);
		assertEquals(moduleName, moduleObj.getModuleName());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Module#getModuleDescription()}.
	 */
	@Test
	public void testGetSetModuleDescription() {
		moduleObj.setModuleDescription(moduleDescription);
		assertEquals(moduleDescription, moduleObj.getModuleDescription());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Module#getTutorUserID()}.
	 */
	@Test
	public void testGetSetTutorUserID() {
		moduleObj.setTutorUserID(tutorUserID);
		assertEquals(tutorUserID, moduleObj.getTutorUserID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Module#getYear()}.
	 */
	@Test
	public void testGetSetYear() {
		moduleObj.setYear(year);
		assertEquals(year, moduleObj.getYear());
	}

}
