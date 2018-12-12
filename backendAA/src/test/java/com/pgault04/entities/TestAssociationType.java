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

import com.pgault04.entities.AssociationType;

/**
 * @author paulgault
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAssociationType {

	private AssociationType associationTypeObj;

	private Long associationTypeID;

	private String associationType;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		associationTypeObj = new AssociationType();

		associationTypeID = 1L;
		associationType = "associationType";

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.AssociationType#AssociationType()}.
	 */
	@Test
	public void testAssociationTypeDefaultConstructor() {
		assertNotNull(associationTypeObj);
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.AssociationType#AssociationType(java.lang.Integer, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAssociationTypeIntegerStringString() {
		associationTypeObj = null;
		associationTypeObj = new AssociationType(associationType);

		assertNotNull(associationTypeObj);
		assertEquals(associationType, associationTypeObj.getAssociationType());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.AssociationType#getAssociationTypeID()}.
	 */
	@Test
	public void testGetSetAssociationTypeID() {
		associationTypeObj.setAssociationTypeID(associationTypeID);
		assertEquals(associationTypeID, associationTypeObj.getAssociationTypeID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.AssociationType#getAssociationType()}.
	 */
	@Test
	public void testGetSetAssociationType() {
		associationTypeObj.setAssociationType(associationType);
		assertEquals(associationType, associationTypeObj.getAssociationType());
	}

	

}
