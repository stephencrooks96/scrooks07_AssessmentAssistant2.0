package com.pgault04.repositories;

import com.pgault04.entities.OptionEntries;
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

@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOptionEntriesRepo {

    private static final long ANSWER_ID_IN_DB = 1L;
    private static final long SECOND_ANSWER_ID_IN_DB = 2L;
    private static final long OPTION_ID_IN_DB = 1L;

    @Autowired
    OptionEntriesRepo optionEntriesRepo;
    private OptionEntries optionEntries;

    @Before
    public void setUp() throws Exception {
        optionEntries = new OptionEntries(OPTION_ID_IN_DB, ANSWER_ID_IN_DB);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = optionEntriesRepo.rowCount();
        // Inserts one optionEntries to table
        optionEntriesRepo.insert(optionEntries);
        // Checks one value is registered as in the table
        assertTrue(optionEntriesRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        // Inserts one optionEntries to table
        OptionEntries oe = optionEntriesRepo.insert(optionEntries);
        assertNotNull(oe);
        assertNotEquals(-1L, oe.getOptionEntryID(), 0);
        // Updates the optionEntries in the table
        oe.setAnswerID(SECOND_ANSWER_ID_IN_DB);
        // Inserts one optionEntries to table
        OptionEntries updatedOe = optionEntriesRepo.insert(oe);
        assertEquals(oe.getOptionEntryID(), updatedOe.getOptionEntryID());
        assertEquals(SECOND_ANSWER_ID_IN_DB, updatedOe.getAnswerID(), 0);
    }

    @Test
    public void testSelectByAnswerID() {
        // Inserts one optionEntries to table
        OptionEntries oe = optionEntriesRepo.insert(optionEntries);
        List<OptionEntries> oes = optionEntriesRepo.selectByAnswerID(oe.getAnswerID());
        assertTrue(oes.toString().contains(oe.toString()));
    }

    @Test
    public void testSelectByOptionEntryID() {
        // Inserts one optionEntries to table
        OptionEntries oe = optionEntriesRepo.insert(optionEntries);
        oe = optionEntriesRepo.selectByID(oe.getOptionEntryID());
        assertNotNull(oe);
    }

    @Test
    public void testDelete() {
        // Inserts one optionEntries to table
        OptionEntries oe = optionEntriesRepo.insert(optionEntries);
        optionEntriesRepo.delete(oe.getOptionEntryID());
        oe = optionEntriesRepo.selectByID(oe.getOptionEntryID());
        assertNull(oe);
    }
}
