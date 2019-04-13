package com.pgault04.repositories;

import com.pgault04.entities.Alternative;
import com.pgault04.entities.CorrectPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
public class TestCorrectPointRepo {

    private static final long QUESTION_ID_IN_DATABASE = 1L;

    @Autowired
    CorrectPointRepo correctPointRepo;

    private CorrectPoint correctPoint;
    private String phrase, feedback;

    private Double marksWorth;

    private List<Alternative> alts;

    @Before
    public void setUp() throws Exception {
        this.phrase = "phrase";
        this.feedback = "feedback";
        this.marksWorth = 0.0;
        this.alts = new ArrayList<>();
        correctPoint = new CorrectPoint(QUESTION_ID_IN_DATABASE, phrase, marksWorth, feedback, alts, 0, 0);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = correctPointRepo.rowCount();
        correctPointRepo.insert(correctPoint);
        // Checks one value is registered as in the table
        assertTrue(correctPointRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        CorrectPoint returnedCp = correctPointRepo.insert(correctPoint);
        CorrectPoint cp = correctPointRepo.selectByID(returnedCp.getCorrectPointID());
        assertNotNull(cp);
        returnedCp.setPhrase("p 2");
        correctPointRepo.insert(returnedCp);
        cp = correctPointRepo.selectByID(returnedCp.getCorrectPointID());
        assertEquals(returnedCp.getPhrase(), cp.getPhrase());
    }

    @Test
    public void testSelectByQuestionID() {
        CorrectPoint returnedCp = correctPointRepo.insert(correctPoint);
        List<CorrectPoint> cps = correctPointRepo.selectByQuestionID(returnedCp.getQuestionID());
        assertTrue(cps.size() >= 1);
    }

    @Test
    public void testSelectByCorrectPointID() {
        CorrectPoint returnedCp = correctPointRepo.insert(correctPoint);
        CorrectPoint cp = correctPointRepo.selectByID(returnedCp.getCorrectPointID());
        assertNotNull(cp);
    }

    @Test
    public void testDelete() {
        CorrectPoint returnedCp = correctPointRepo.insert(correctPoint);
        correctPointRepo.delete(returnedCp.getCorrectPointID());
        CorrectPoint cps = correctPointRepo.selectByID(returnedCp.getCorrectPointID());
        assertNull(cps);
    }
}