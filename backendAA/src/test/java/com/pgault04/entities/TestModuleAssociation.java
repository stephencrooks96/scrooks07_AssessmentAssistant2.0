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

import com.pgault04.entities.ModuleAssociation;

/**
 * @author paulgault
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestModuleAssociation {

	private ModuleAssociation moduleAssociationObj;

	private Long associationID;

	private Long moduleID;

	private Long userID;

	private Long associationType;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		moduleAssociationObj = new ModuleAssociation();

		this.associationID = 1L;
		this.moduleID = 2L;
		this.userID = 3L;
		this.associationType = 4L;

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.ModuleAssociation#ModuleAssociation()}.
	 */
	@Test
	public void testModuleAssociationDefaultConstructor() {
		assertNotNull(moduleAssociationObj);
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.ModuleAssociation#ModuleAssociation(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	public void testModuleAssociationConstructorWithArgs() {
		moduleAssociationObj = null;
		moduleAssociationObj = new ModuleAssociation(moduleID, userID, associationType);

		assertNotNull(moduleAssociationObj);
		assertEquals(moduleID, moduleAssociationObj.getModuleID());
		assertEquals(userID, moduleAssociationObj.getUserID());
		assertEquals(associationType, moduleAssociationObj.getAssociationType());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.ModuleAssociation#getAssociationID()}.
	 */
	@Test
	public void testGetSetAssociationID() {
		moduleAssociationObj.setAssociationID(associationID);
		assertEquals(associationID, moduleAssociationObj.getAssociationID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.ModuleAssociation#getModuleID()}.
	 */
	@Test
	public void testGetSetModuleID() {
		moduleAssociationObj.setModuleID(moduleID);
		assertEquals(moduleID, moduleAssociationObj.getModuleID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.ModuleAssociation#getUserID()}.
	 */
	@Test
	public void testGetSetUserID() {
		moduleAssociationObj.setUserID(userID);
		assertEquals(userID, moduleAssociationObj.getUserID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.ModuleAssociation#getAssociationType()}.
	 */
	@Test
	public void testGetSetAssociationType() {
		moduleAssociationObj.setAssociationType(associationType);
		assertEquals(associationType, moduleAssociationObj.getAssociationType());
	}

}
