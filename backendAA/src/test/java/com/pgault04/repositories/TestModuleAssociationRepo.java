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
 * @author Paul Gault 40126005
 * @since November 2018
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

	@Before
	public void setUp() throws Exception {
		moduleAssociation = new ModuleAssociation(MODULE_ID_IN_DB, USER_ID_IN_DB,
				ASSOCIATION_TYPE_ID_IN_DB);
	}

	@Test
	public void testRowCount() {
		int rowCountBefore = moduleAssociationRepo.rowCount();
		// Inserts one moduleAssociation to table
		moduleAssociationRepo.insert(moduleAssociation);
		// Checks one value is registered as in the table
		assertTrue(moduleAssociationRepo.rowCount() > rowCountBefore);
	}

	@Test
	public void testInsert() {
		// Inserts one moduleAssociation to table
		ModuleAssociation returnedModuleAssociation = moduleAssociationRepo.insert(moduleAssociation);
		ModuleAssociation moduleAssociations = moduleAssociationRepo
				.selectByAssociationID(returnedModuleAssociation.getAssociationID());
		assertNotNull(moduleAssociations);
		// Updates the moduleAssociation in the table
		returnedModuleAssociation.setAssociationType(OTHER_ASSOCIATION_TYPE_IN_DB);
		// Inserts one moduleAssociation to table
		moduleAssociationRepo.insert(returnedModuleAssociation);
		moduleAssociations = moduleAssociationRepo.selectByAssociationID(returnedModuleAssociation.getAssociationID());
		assertEquals(OTHER_ASSOCIATION_TYPE_IN_DB,
				moduleAssociations.getAssociationType().intValue());
	}

	@Test
	public void testSelectByAssociationID() {
		// Inserts one moduleAssociation to table
		ModuleAssociation returnedModuleAssociation = moduleAssociationRepo.insert(moduleAssociation);
		ModuleAssociation moduleAssociations = moduleAssociationRepo
				.selectByAssociationID(returnedModuleAssociation.getAssociationID());
		assertNotNull(moduleAssociations);
	}

	@Test
	public void testSelectByModuleID() {
		// Inserts one moduleAssociation to table
		moduleAssociationRepo.insert(moduleAssociation);
		List<ModuleAssociation> moduleAssociations = moduleAssociationRepo
				.selectByModuleID(moduleAssociation.getModuleID());
		assertTrue(moduleAssociations.size() > 0);
	}

	@Test
	public void testSelectByUserID() {
		// Inserts one moduleAssociation to table
		moduleAssociationRepo.insert(moduleAssociation);
		List<ModuleAssociation> moduleAssociations = moduleAssociationRepo
				.selectByUserID(moduleAssociation.getUserID());
		assertTrue(moduleAssociations.size() > 0);
	}

	@Test
	public void testSelectByAssociationType() {
		// Inserts one moduleAssociation to table
		moduleAssociationRepo.insert(moduleAssociation);
		List<ModuleAssociation> moduleAssociations = moduleAssociationRepo
				.selectByAssociationType(moduleAssociation.getAssociationType());
		assertTrue(moduleAssociations.size() > 0);
	}

	@Test
	public void testDelete() {
		// Inserts one moduleAssociation to table
		ModuleAssociation returnedModuleAssociation = moduleAssociationRepo.insert(moduleAssociation);
		moduleAssociationRepo.delete(returnedModuleAssociation.getAssociationID());
		ModuleAssociation moduleAssociations = moduleAssociationRepo
				.selectByAssociationID(returnedModuleAssociation.getAssociationID());
		assertNull(moduleAssociations);
	}
}