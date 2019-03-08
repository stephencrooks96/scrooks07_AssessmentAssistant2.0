package com.pgault04.repositories;

import com.pgault04.entities.OptionEntries;
import com.pgault04.entities.TutorRequests;
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
public class TestTutorRequestRepo {


    private static final long USER_IN_DB = 1L;

    @Autowired
    private TutorRequestRepo tutorRequestRepo;
    private TutorRequests tutorRequest;
    private String reason;
    private Integer approved;

    @Before
    public void setUp() throws Exception {
        this.reason = "reason";
        this.approved = 1;
        tutorRequest = new TutorRequests(USER_IN_DB, reason, approved);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = tutorRequestRepo.rowCount();
        // Inserts one tutorRequest to table
        tutorRequestRepo.insert(tutorRequest);
        // Checks one value is registered as in the table
        assertTrue(tutorRequestRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        // Inserts one tutorRequest to table
        TutorRequests tr = tutorRequestRepo.insert(tutorRequest);
        assertNotNull(tr);
        assertNotEquals(-1L, tr.getTutorRequestID(), 0);
        // Updates the tutorRequest in the table
        String updateCheck = "reason2";
        tr.setReason(updateCheck);
        // Inserts one tutorRequest to table
        TutorRequests updatedTr = tutorRequestRepo.insert(tr);
        assertEquals(tr.getTutorRequestID(), updatedTr.getTutorRequestID());
        assertEquals(updateCheck, updatedTr.getReason());
    }

    @Test
    public void testSelectByApproved() {
        // Inserts one tutorRequest to table
        TutorRequests tr = tutorRequestRepo.insert(tutorRequest);
        List<TutorRequests> trs = tutorRequestRepo.selectByApproved(tr.getApproved());
        assertTrue(trs.toString().contains(tr.toString()));
    }

    @Test
    public void testSelectAll() {
        // Inserts one tutorRequest to table
        TutorRequests tr = tutorRequestRepo.insert(tutorRequest);
        List<TutorRequests> trs = tutorRequestRepo.selectAll();
        assertTrue(trs.toString().contains(tr.toString()));
    }

    @Test
    public void testSelectByUserID() {
        // Inserts one tutorRequest to table
        TutorRequests tr = tutorRequestRepo.insert(tutorRequest);
        tr = tutorRequestRepo.selectByUserID(tr.getUserID());
        assertNotNull(tr);
    }

    @Test
    public void testDelete() {
        // Inserts one tutorRequest to table
        TutorRequests tr = tutorRequestRepo.insert(tutorRequest);
        tutorRequestRepo.delete(tr.getTutorRequestID());
        tr = tutorRequestRepo.selectByUserID(tr.getUserID());
        assertNull(tr);
    }
}
