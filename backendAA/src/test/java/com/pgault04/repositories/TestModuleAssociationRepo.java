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

import com.pgault04.entities.ModuleAssociation;
import com.pgault04.repositories.ModuleAssociationRepo;

/**
 * @author paulgault
 *
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestModuleAssociationRepo {

	private static final long OTHER_ASSOCIATION_TYPE_IN_DB = 3L;

	private static final long ASSOCIATION_TYPE_ID_IN_DB = 2L;

	private static final long USER_ID_IN_DB = 1L;

	private static final long MODULE_ID_IN_DB = 1L;

	@Autowired
	ModuleAssociationRepo moduleAssociationRepo;
	
	private ModuleAssociation moduleAssociation;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		moduleAssociation = new ModuleAssociation(MODULE_ID_IN_DB, USER_ID_IN_DB,
				ASSOCIATION_TYPE_ID_IN_DB);
	}

	

	/**
	 * Test method for Row Count
	 */
	@Test
	public void testRowCount() {

		int rowCountBefore = moduleAssociationRepo.rowCount().intValue();
		
		// Inserts one moduleAssociation to table
		moduleAssociationRepo.insert(moduleAssociation);

		// Checks one value is registered as in the table
		assertTrue(moduleAssociationRepo.rowCount().intValue() > rowCountBefore);

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.ModuleAssociationRepo#insert(pgault04.entities.ModuleAssociation)}.
	 */
	@Test
	public void testInsert() {

		// Inserts one moduleAssociation to table
		ModuleAssociation returnedModuleAssociation = moduleAssociationRepo.insert(moduleAssociation);

		List<ModuleAssociation> moduleAssociations = moduleAssociationRepo
				.selectByAssociationID(returnedModuleAssociation.getAssociationID());

		assertEquals(1, moduleAssociations.size());

		// Updates the moduleAssociation in the table
		returnedModuleAssociation.setAssociationType(OTHER_ASSOCIATION_TYPE_IN_DB);

		// Inserts one moduleAssociation to table
		moduleAssociationRepo.insert(returnedModuleAssociation);

		moduleAssociations = moduleAssociationRepo.selectByAssociationID(returnedModuleAssociation.getAssociationID());

		assertEquals(OTHER_ASSOCIATION_TYPE_IN_DB,
				moduleAssociations.get(0).getAssociationType().intValue());

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.ModuleAssociationRepo#selectByModuleAssociationID(java.lang.Long)}.
	 */
	@Test
	public void testSelectByAssociationID() {
		// Inserts one moduleAssociation to table
		ModuleAssociation returnedModuleAssociation = moduleAssociationRepo.insert(moduleAssociation);

		List<ModuleAssociation> moduleAssociations = moduleAssociationRepo
				.selectByAssociationID(returnedModuleAssociation.getAssociationID());

		assertEquals(1, moduleAssociations.size());
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.ModuleAssociationRepo#selectByUsername(java.lang.String)}.
	 */
	@Test
	public void testSelectByModuleID() {
		// Inserts one moduleAssociation to table
		moduleAssociationRepo.insert(moduleAssociation);

		List<ModuleAssociation> moduleAssociations = moduleAssociationRepo
				.selectByModuleID(moduleAssociation.getModuleID());

		assertTrue(moduleAssociations.size() > 0);
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.ModuleAssociationRepo#selectByFirstName(java.lang.String)}.
	 */
	@Test
	public void testSelectByUserID() {
		// Inserts one moduleAssociation to table
		moduleAssociationRepo.insert(moduleAssociation);

		List<ModuleAssociation> moduleAssociations = moduleAssociationRepo
				.selectByUserID(moduleAssociation.getUserID());

		assertTrue(moduleAssociations.size() > 0);
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.ModuleAssociationRepo#selectByLastName(java.lang.String)}.
	 */
	@Test
	public void testSelectByAssociationType() {
		// Inserts one moduleAssociation to table
		moduleAssociationRepo.insert(moduleAssociation);

		List<ModuleAssociation> moduleAssociations = moduleAssociationRepo
				.selectByAssociationType(moduleAssociation.getAssociationType());

		assertTrue(moduleAssociations.size() > 0);
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.ModuleAssociationRepo#delete(java.lang.Long)}.
	 */
	@Test
	public void testDelete() {
		// Inserts one moduleAssociation to table
		ModuleAssociation returnedModuleAssociation = moduleAssociationRepo.insert(moduleAssociation);

		moduleAssociationRepo.delete(returnedModuleAssociation.getAssociationID());

		List<ModuleAssociation> moduleAssociations = moduleAssociationRepo
				.selectByAssociationID(returnedModuleAssociation.getAssociationID());

		assertEquals(0, moduleAssociations.size());
	}

}
