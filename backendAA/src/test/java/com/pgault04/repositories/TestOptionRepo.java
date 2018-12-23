package com.pgault04.repositories;

import com.pgault04.entities.Option;
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
public class TestOptionRepo {

    private static final long QUESTION_ID_IN_DATABASE = 1L;

    @Autowired
    OptionRepo optionRepo;

    private Option optionObj;
    private String option;

    @Before
    public void setUp() throws Exception {
        this.option = "option";
        optionObj = new Option(QUESTION_ID_IN_DATABASE, option, 1);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = optionRepo.rowCount();
        optionRepo.insert(optionObj);
        // Checks one value is registered as in the table
        assertTrue(optionRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        Option returnedOpt = optionRepo.insert(optionObj);
        Option opt = optionRepo.selectByOptionID(returnedOpt.getOptionID());
        assertNotNull(opt);
        returnedOpt.setOptionContent("o 2");
        optionRepo.insert(returnedOpt);
        opt = optionRepo.selectByOptionID(returnedOpt.getOptionID());
        assertEquals(returnedOpt.getOptionContent(), opt.getOptionContent());
    }

    @Test
    public void testSelectByQuestionID() {
        Option returnedOpt = optionRepo.insert(optionObj);
        List<Option> opt = optionRepo.selectByQuestionID(returnedOpt.getQuestionID());
        assertTrue(opt.size() >= 1);
    }

    @Test
    public void testSelectByOptionID() {
        Option returnedOpt = optionRepo.insert(optionObj);
        Option opt = optionRepo.selectByOptionID(returnedOpt.getOptionID());
        assertNotNull(opt);
    }

    @Test
    public void testDelete() {
        Option returnedOpt = optionRepo.insert(optionObj);
        optionRepo.delete(returnedOpt.getOptionID());
        Option opt = optionRepo.selectByOptionID(returnedOpt.getOptionID());
        assertNull(opt);
    }
}
