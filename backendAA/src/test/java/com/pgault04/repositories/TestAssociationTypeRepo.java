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

import com.pgault04.entities.AssociationType;
import com.pgault04.repositories.AssociationTypeRepo;

/**
 * @author paulgault
 *
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAssociationTypeRepo {

	@Autowired
	AssociationTypeRepo associationTypeRepo;

	private AssociationType associationTypeObj;
	private String associationType;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.associationType = "associationType";
		associationTypeObj = new AssociationType(associationType);

	}

	/**
	 * Test method for Row Count
	 */
	@Test
	public void testRowCount() {

		int rowCountBefore = associationTypeRepo.rowCount().intValue();
		// Inserts one associationType to table
		associationTypeRepo.insert(associationTypeObj);

		// Checks one value is registered as in the table
		assertTrue(associationTypeRepo.rowCount().intValue() > rowCountBefore);

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.AssociationTypeRepo#insert(pgault04.entities.AssociationType)}.
	 */
	@Test
	public void testInsert() {

		// Inserts one associationType to table
		AssociationType returnedAssociationType = associationTypeRepo.insert(associationTypeObj);

		AssociationType associationTypes = associationTypeRepo
				.selectByAssociationTypeID(returnedAssociationType.getAssociationTypeID());

		assertNotNull(associationTypes);

		// Updates the associationType in the table
		returnedAssociationType.setAssociationType("assocType");

		// Inserts one associationType to table
		associationTypeRepo.insert(returnedAssociationType);

		associationTypes = associationTypeRepo
				.selectByAssociationTypeID(returnedAssociationType.getAssociationTypeID());

		assertEquals("assocType", associationTypes.getAssociationType());

	}

	@Test
	public void testSelectAll() {
		assertNotNull(associationTypeRepo.selectAll());

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.AssociationTypeRepo#selectByAssociationTypeID(java.lang.Long)}.
	 */
	@Test
	public void testSelectByAssociationTypeID() {
		// Inserts one associationType to table
		AssociationType returnedAssociationType = associationTypeRepo.insert(associationTypeObj);

		AssociationType associationTypes = associationTypeRepo
				.selectByAssociationTypeID(returnedAssociationType.getAssociationTypeID());

		assertNotNull(associationTypes);
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.AssociationTypeRepo#selectByEmail(java.lang.String)}.
	 */
	@Test
	public void testSelectByAssociationType() {
		// Inserts one associationType to table
		associationTypeRepo.insert(associationTypeObj);

		List<AssociationType> associationTypes = associationTypeRepo
				.selectByAssociationType(associationTypeObj.getAssociationType());

		assertTrue(associationTypes.size() > 0);
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.AssociationTypeRepo#delete(java.lang.Long)}.
	 */
	@Test
	public void testDelete() {
		// Inserts one associationType to table
		AssociationType returnedAssociationType = associationTypeRepo.insert(associationTypeObj);

		associationTypeRepo.delete(returnedAssociationType.getAssociationTypeID());

		AssociationType associationTypes = associationTypeRepo
				.selectByAssociationTypeID(returnedAssociationType.getAssociationTypeID());

		assertNull(associationTypes);
	}

}
