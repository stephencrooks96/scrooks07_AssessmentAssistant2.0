package com.pgault04.repositories;

import com.pgault04.entities.AssociationType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Paul Gault 40126005
 * @since November 2018
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

    @Before
    public void setUp() throws Exception {
        this.associationType = "associationType";
        associationTypeObj = new AssociationType(associationType);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = associationTypeRepo.rowCount();
        // Inserts one associationType to table
        associationTypeRepo.insert(associationTypeObj);
        // Checks one value is registered as in the table
        assertTrue(associationTypeRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        // Inserts one associationType to table
        AssociationType returnedAssociationType = associationTypeRepo.insert(associationTypeObj);
        AssociationType associationTypes = associationTypeRepo
                .selectByID(returnedAssociationType.getAssociationTypeID());
        assertNotNull(associationTypes);
        // Updates the associationType in the table
        returnedAssociationType.setAssociationType("assocType");
        // Inserts one associationType to table
        associationTypeRepo.insert(returnedAssociationType);
        associationTypes = associationTypeRepo
                .selectByID(returnedAssociationType.getAssociationTypeID());
        assertEquals("assocType", associationTypes.getAssociationType());
    }

    @Test
    public void testSelectAll() {
        assertNotNull(associationTypeRepo.selectAll());
    }

    @Test
    public void testSelectByAssociationTypeID() {
        // Inserts one associationType to table
        AssociationType returnedAssociationType = associationTypeRepo.insert(associationTypeObj);
        AssociationType associationTypes = associationTypeRepo
                .selectByID(returnedAssociationType.getAssociationTypeID());
        assertNotNull(associationTypes);
    }

    @Test
    public void testSelectByAssociationType() {
        // Inserts one associationType to table
        associationTypeRepo.insert(associationTypeObj);
        List<AssociationType> associationTypes = associationTypeRepo
                .selectByAssociationType(associationTypeObj.getAssociationType());
        assertTrue(associationTypes.size() > 0);
    }

    @Test
    public void testDelete() {
        // Inserts one associationType to table
        AssociationType returnedAssociationType = associationTypeRepo.insert(associationTypeObj);
        associationTypeRepo.delete(returnedAssociationType.getAssociationTypeID());
        AssociationType associationTypes = associationTypeRepo
                .selectByID(returnedAssociationType.getAssociationTypeID());
        assertNull(associationTypes);
    }
}