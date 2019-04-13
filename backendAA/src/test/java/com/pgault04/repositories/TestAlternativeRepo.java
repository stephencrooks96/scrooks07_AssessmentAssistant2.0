package com.pgault04.repositories;

import com.pgault04.entities.Alternative;
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
 * @since December 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAlternativeRepo {

    private static final long CORRECTPOINT_IN_DB = 1L;

    @Autowired
    AlternativeRepo alternativeRepo;

    private Alternative alternative;
    private String phrase;

    @Before
    public void setUp() throws Exception {
        this.phrase = "phrase";
        alternative = new Alternative(CORRECTPOINT_IN_DB, phrase, 0);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = alternativeRepo.rowCount();
        alternativeRepo.insert(alternative);
        assertTrue(alternativeRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        Alternative returnedAlt = alternativeRepo.insert(alternative);
        Alternative alternative = alternativeRepo.selectByID(returnedAlt.getAlternativeID());
        assertNotNull(alternative);
        returnedAlt.setAlternativePhrase("p 2");
        alternativeRepo.insert(returnedAlt);
        alternative = alternativeRepo.selectByID(returnedAlt.getAlternativeID());
        assertEquals(returnedAlt.getAlternativePhrase(), alternative.getAlternativePhrase());
    }

    @Test
    public void testSelectByAlternativeID() {
        Alternative returnedAlt = alternativeRepo.insert(alternative);
        Alternative alts = alternativeRepo.selectByID(returnedAlt.getAlternativeID());
        assertNotNull(alts);
    }

    @Test
    public void testSelectByCorrectPointID() {
        Alternative returnedAlt = alternativeRepo.insert(alternative);
        List<Alternative> alts = alternativeRepo.selectByCorrectPointID(returnedAlt.getCorrectPointID());
        assertEquals(1, alts.size());
    }

    @Test
    public void testDelete() {
        Alternative returnedAlt = alternativeRepo.insert(alternative);
        alternativeRepo.delete(returnedAlt.getAlternativeID());
        Alternative alts = alternativeRepo.selectByID(returnedAlt.getAlternativeID());
        assertNull(alts);
    }
}
