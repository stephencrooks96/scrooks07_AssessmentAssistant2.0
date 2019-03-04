package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.ModuleAssociation;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestModuleAssociation {

	private ModuleAssociation moduleAssociationObj;

	private Long associationID;

	private Long moduleID;

	private Long userID;

	private Long associationType;

	@Before
	public void setUp() throws Exception {
		moduleAssociationObj = new ModuleAssociation();
		this.associationID = 1L;
		this.moduleID = 2L;
		this.userID = 3L;
		this.associationType = 4L;
	}

	@Test
	public void testDefaultConstructor() {
		assertNotNull(moduleAssociationObj);
	}

	@Test
	public void testConstructorWithArgs() {
		moduleAssociationObj = null;
		moduleAssociationObj = new ModuleAssociation(moduleID, userID, associationType);
		assertNotNull(moduleAssociationObj);
		assertEquals(moduleID, moduleAssociationObj.getModuleID());
		assertEquals(userID, moduleAssociationObj.getUserID());
		assertEquals(associationType, moduleAssociationObj.getAssociationType());
	}

	@Test
	public void testGetSetAssociationID() {
		moduleAssociationObj.setAssociationID(associationID);
		assertEquals(associationID, moduleAssociationObj.getAssociationID());
	}

	@Test
	public void testToString() {
		moduleAssociationObj = new ModuleAssociation(moduleID, userID, associationType);
		moduleAssociationObj.setAssociationID(associationID);
		assertEquals("ModuleAssociation{associationID=1, moduleID=2, userID=3, associationType=4}", moduleAssociationObj.toString());
	}
}