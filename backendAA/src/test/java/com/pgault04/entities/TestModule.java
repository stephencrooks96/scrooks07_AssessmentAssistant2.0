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

	private String commencementDate;

	private String endDate;

	private Integer approved;

	@Before
	public void setUp() throws Exception {
		moduleObj = new Module();
		moduleID = 1L;
		moduleName = "moduleName";
		moduleDescription = "moduleDescription";
		tutorUserID = 2L;
		commencementDate = "dateC";
		endDate = "dateE";
		approved = 1;
	}

	@Test
	public void testDefaultConstructor() {
		assertNotNull(moduleObj);
	}

	@Test
	public void testConstructorWithArgs() {
		moduleObj = null;
		moduleObj = new Module(moduleName, moduleDescription, tutorUserID, commencementDate, endDate, approved);

		assertNotNull(moduleObj);
		assertEquals(moduleName, moduleObj.getModuleName());
		assertEquals(moduleDescription, moduleObj.getModuleDescription());
		assertEquals(tutorUserID, moduleObj.getTutorUserID());
		assertEquals(commencementDate, moduleObj.getCommencementDate());
		assertEquals(endDate, moduleObj.getEndDate());
		assertEquals(approved, moduleObj.getApproved());
	}

	@Test
	public void testGetSetModuleID() {
		moduleObj.setModuleID(moduleID);
		assertEquals(moduleID, moduleObj.getModuleID());
	}

	@Test
	public void testToString() {
		moduleObj = new Module(moduleName, moduleDescription, tutorUserID, commencementDate, endDate, approved);
		moduleObj.setModuleID(moduleID);
		assertEquals("Module{moduleID=1, moduleName='moduleName', moduleDescription='moduleDescription', tutorUserID=2, commencementDate='dateC', endDate='dateE', approved=1}", moduleObj.toString());
	}
}