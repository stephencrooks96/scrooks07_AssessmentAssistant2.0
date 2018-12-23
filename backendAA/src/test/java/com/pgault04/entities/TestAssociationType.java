package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.AssociationType;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAssociationType {

	private AssociationType associationTypeObj;

	private Long associationTypeID;

	private String associationType;

	@Before
	public void setUp() throws Exception {
		associationTypeObj = new AssociationType();
		associationTypeID = 1L;
		associationType = "associationType";
	}

	@Test
	public void testAssociationTypeDefaultConstructor() {
		assertNotNull(associationTypeObj);
	}

	@Test
	public void testAssociationTypeIntegerStringString() {
		associationTypeObj = null;
		associationTypeObj = new AssociationType(associationType);
		assertNotNull(associationTypeObj);
		assertEquals(associationType, associationTypeObj.getAssociationType());
	}

	@Test
	public void testGetSetAssociationTypeID() {
		associationTypeObj.setAssociationTypeID(associationTypeID);
		assertEquals(associationTypeID, associationTypeObj.getAssociationTypeID());
	}

	@Test
	public void testGetSetAssociationType() {
		associationTypeObj.setAssociationType(associationType);
		assertEquals(associationType, associationTypeObj.getAssociationType());
	}

	@Test
	public void testToString() {
		associationTypeObj = new AssociationType(associationType);
		associationTypeObj.setAssociationTypeID(associationTypeID);
		assertEquals("AssociationType{associationTypeID=1, associationType='associationType'}", associationTypeObj.toString());
	}
	

}
