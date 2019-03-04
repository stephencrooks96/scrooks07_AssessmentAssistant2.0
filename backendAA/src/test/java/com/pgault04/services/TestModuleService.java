package com.pgault04.services;

import com.pgault04.entities.*;
import com.pgault04.pojos.ModuleWithTutor;
import com.pgault04.pojos.Performance;
import com.pgault04.pojos.TestAndGrade;
import com.pgault04.pojos.TestMarking;
import com.pgault04.repositories.*;
import com.pgault04.utilities.StringToDateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Paul Gault - 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestModuleService {

    private static final int HOUR_IN_MILLISECONDS = 3600000;
    private static final String USERNAME_IN_DB = "pgault04@qub.ac.uk";
    private static final String OTHER_USERNAME_IN_DB = "richard.gault@qub.ac.uk";
    private static final long QUESTION_IN_DB = 1L;
    private static final long USER_IN_DB = 1L;
    private static final long OTHER_IN_DB = 2L;

    @Autowired
    ModuleService moduleService;
    @Autowired
    TestsRepo testsRepo;
    @Autowired
    ModuleRepo moduleRepo;
    @Autowired
    TestQuestionRepo testQuestionRepo;
    @Autowired
    AnswerRepo answerRepo;
    @Autowired
    TestResultRepo testResultRepo;
    @Autowired
    ModuleAssociationRepo moduleAssociationRepo;
    @Autowired
    UserRepo userRepo;

    private Tests testObj;
    private List<Tests> tests;
    private Module module;
    private Answer markedByUser, notMarkedByUser;
    private ModuleAssociation modAssoc;

    @Before
    @Transactional
    public void setUp() throws Exception {
        module = new Module("module", "description", 1L, "dateC", "dateE", 1);
        module = moduleRepo.insert(module);
        tests = new ArrayList<>();
        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01 10:00:00", "2018-01-01 11:00:00", 0, 0, 0, 0);
        modAssoc = moduleAssociationRepo.insert(new ModuleAssociation(module.getModuleID(), userRepo.selectByUsername(USERNAME_IN_DB).getUserID(), 1L));
    }

    @Test
    @Transactional
    public void testActiveTests() throws ParseException {
        // Sets up test to be active
        testObj.setScheduled(1);
        Date startDate = new Date();
        testObj.setStartDateTime(StringToDateUtil.dateCorrectFormat(startDate));
        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + HOUR_IN_MILLISECONDS);
        testObj.setEndDateTime(StringToDateUtil.dateCorrectFormat(endDate));

        // Inserts test
        testObj = testsRepo.insert(testObj);

        // Calls method for user who has association
        tests = moduleService.activeTests(USERNAME_IN_DB, module.getModuleID());
        assertEquals(1, tests.size());
        assertEquals(testObj.toString(), tests.get(0).toString());

        // User with no association
        tests = moduleService.activeTests(OTHER_USERNAME_IN_DB, module.getModuleID());
        assertNull(tests);
    }

    @Test
    @Transactional
    public void testScheduledTests() throws ParseException {
        // Sets up test to be scheduled
        testObj.setScheduled(1);
        Date startDate = new Date();
        startDate.setTime(startDate.getTime() + HOUR_IN_MILLISECONDS);
        testObj.setStartDateTime(StringToDateUtil.dateCorrectFormat(startDate));
        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + 2 * HOUR_IN_MILLISECONDS);
        testObj.setEndDateTime(StringToDateUtil.dateCorrectFormat(endDate));

        // Inserts test
        testObj = testsRepo.insert(testObj);

        // Calls method for user who has valid association
        tests = moduleService.scheduledTests(USERNAME_IN_DB, module.getModuleID());
        assertEquals(1, tests.size());
        assertEquals(testObj.toString(), tests.get(0).toString());

        // User with no association
        tests = moduleService.scheduledTests(OTHER_USERNAME_IN_DB, module.getModuleID());
        assertNull(tests);
    }

    @Test
    public void testGetModuleWithTutor() {
        ModuleWithTutor modWithTutor = moduleService.getModuleWithTutor(module.getModuleID());
        assertEquals(USERNAME_IN_DB, modWithTutor.getTutor().getUsername());
        assertEquals(module.getModuleID(), modWithTutor.getModule().getModuleID());
    }

    @Test
    public void testGetMyModulesWithTutors() throws ParseException {
        List<ModuleWithTutor> modules;
        // 2 mod assocs already stored in tests.sql plus the one added in setup
        modules = moduleService.getMyModulesWithTutor(USERNAME_IN_DB);
        assertEquals(3, modules.size());
    }

    @Test
    @Transactional
    public void testMarking() throws ParseException {

        // Sets up test to be ready for marking
        testObj.setScheduled(1);
        Date startDate = new Date();
        startDate.setTime(startDate.getTime() - 2 * HOUR_IN_MILLISECONDS);
        testObj.setStartDateTime(StringToDateUtil.dateCorrectFormat(startDate));
        Date endDate = new Date();
        endDate.setTime(startDate.getTime() - HOUR_IN_MILLISECONDS);
        testObj.setEndDateTime(StringToDateUtil.dateCorrectFormat(endDate));

        // Inserts test
        testObj = testsRepo.insert(testObj);

        // Inserts question for test
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), QUESTION_IN_DB));

        // Answers added
        markedByUser = answerRepo.insert(new Answer(QUESTION_IN_DB, USER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 25, "feedback", 0, 0));
        notMarkedByUser = answerRepo.insert(new Answer(QUESTION_IN_DB, USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "content", 50, "feedback", 0, 0));
        markedByUser = answerRepo.insert(new Answer(QUESTION_IN_DB, USER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", null, "feedback", 0, 0));
        notMarkedByUser = answerRepo.insert(new Answer(QUESTION_IN_DB, USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "content", null, "feedback", 0, 0));

        // Tutor association
        List<TestMarking> tests = moduleService.marking(module.getModuleID(), USERNAME_IN_DB);
        assertEquals(1, tests.size());
        assertEquals(2, tests.get(0).getTotalForYou(), 0.0);
        assertEquals(1, tests.get(0).getToBeMarkedByYou(), 0.0);
        assertEquals(2, tests.get(0).getMarked(), 0.0);
        assertEquals(1, tests.get(0).getToBeMarkedByTAs(), 0.0);

        // User with no association
        tests = moduleService.marking(module.getModuleID(), OTHER_USERNAME_IN_DB);
        assertNull(tests);

        // Teaching Assistant association
        modAssoc.setAssociationType(3L);
        moduleAssociationRepo.insert(modAssoc);
        tests = moduleService.marking(module.getModuleID(), USERNAME_IN_DB);
        assertEquals(1, tests.size());
        assertEquals(2, tests.get(0).getTotalForYou(), 0.0);
        assertEquals(1, tests.get(0).getToBeMarkedByYou(), 0.0);
        assertEquals(1, tests.get(0).getMarked(), 0.0);
        assertEquals(0, tests.get(0).getToBeMarkedByTAs(), 0.0);
    }

    @Test
    @Transactional
    public void testActiveResults() throws SQLException {

        // Sets test up to be ready for active results
        testObj.setPublishGrades(1);
        testObj = testsRepo.insert(testObj);

        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), QUESTION_IN_DB));

        testResultRepo.insert(new TestResult(testObj.getTestID(), USER_IN_DB, 100));

        // Not-student association
        List<TestAndGrade> tests = moduleService.activeResults(module.getModuleID(), USERNAME_IN_DB);
        assertNull(tests);

        // Student association
        modAssoc.setAssociationType(2L);
        moduleAssociationRepo.insert(modAssoc);
        tests = moduleService.activeResults(module.getModuleID(), USERNAME_IN_DB);
        assertEquals(1, tests.size());
        assertEquals("A*", tests.get(0).getGrade());
    }

    @Test
    @Transactional
    public void testTestDrafts() {
        testObj = testsRepo.insert(testObj);

        // Tutor association
        tests = moduleService.testDrafts(USERNAME_IN_DB, module.getModuleID());
        assertEquals(1, tests.size());
        assertEquals(testObj.toString(), tests.get(0).toString());

        // Non-tutor assoc
        tests = moduleService.testDrafts(OTHER_USERNAME_IN_DB, module.getModuleID());
        assertNull(tests);
    }

    @Test
    @Transactional
    public void testReviewMarking() throws ParseException {
        testObj.setScheduled(1);
        Date startDate = new Date();
        startDate.setTime(startDate.getTime() - 2 * HOUR_IN_MILLISECONDS);
        testObj.setStartDateTime(StringToDateUtil.dateCorrectFormat(startDate));
        Date endDate = new Date();
        endDate.setTime(startDate.getTime() - HOUR_IN_MILLISECONDS);
        testObj.setEndDateTime(StringToDateUtil.dateCorrectFormat(endDate));
        testObj = testsRepo.insert(testObj);

        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), QUESTION_IN_DB));

        Answer answer = answerRepo.insert(new Answer(QUESTION_IN_DB, USER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", null, "feedback", 0, 0));

        // Tutor association
        List<TestMarking> tests = moduleService.reviewMarking(USERNAME_IN_DB, module.getModuleID());
        assertEquals(0, tests.size());
        answer.setScore(50);
        answerRepo.insert(answer);
        tests = moduleService.reviewMarking(USERNAME_IN_DB, module.getModuleID());
        assertEquals(1, tests.size());

        // No association
        tests = moduleService.reviewMarking(OTHER_USERNAME_IN_DB, module.getModuleID());
        assertNull(tests);
    }

     @Test
     @Transactional
     public void testGeneratePerformance() throws SQLException {
        // Sets test up to be ready for active results
         testObj.setPublishResults(1);
         testObj = testsRepo.insert(testObj);

         testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), QUESTION_IN_DB));

         testResultRepo.insert(new TestResult(testObj.getTestID(), USER_IN_DB, 100));

         // Teaching assistant or no association
         List<Performance> performances = moduleService.generatePerformance(module.getModuleID(), OTHER_USERNAME_IN_DB);
         assertNull(performances);

         // Tutor association
         performances = moduleService.generatePerformance(module.getModuleID(), USERNAME_IN_DB);
         assertEquals(1, performances.size());
         assertEquals(100, performances.get(0).getClassAverage(), 0.0);

         // Student association
         modAssoc.setAssociationType(2L);
         moduleAssociationRepo.insert(modAssoc);
         performances = moduleService.generatePerformance(module.getModuleID(), USERNAME_IN_DB);
         assertEquals(1, performances.size());
         assertEquals(100, performances.get(0).getClassAverage(), 0.0);
         assertEquals(100, performances.get(0).getTestAndResult().getPercentageScore(), 0.0);
     }

    @Test
    @Transactional
    public void testMyModules() throws ParseException {
        List<Module> modules;
        // 2 mod assocs already stored in tests.sql plus the one added in setup
        modules = moduleService.myModules(USERNAME_IN_DB);
        assertEquals(3, modules.size());

    }

    @Test
    @Transactional
    public void testCheckValidAssociation() throws ParseException {
        // Module and association for 'pgault04@qub.ac.uk' stored in db
        assertEquals("student", moduleService.checkValidAssociation(USERNAME_IN_DB, 1L));
        // No mod assoc for module input in setUp
        assertNull(moduleService.checkValidAssociation(OTHER_USERNAME_IN_DB, module.getModuleID()));
    }


    @Test
    public void testCheckGrade() {
        for (double loop = 0; loop <= 100; loop++) {

            if (loop <= 49) {
                assertEquals("F", moduleService.checkGrade(loop));
            }
            if (loop > 49 && loop <= 59) {
                assertEquals("D", moduleService.checkGrade(loop));
            }
            if (loop > 59 && loop <= 69) {
                assertEquals("C", moduleService.checkGrade(loop));
            }
            if (loop > 69 && loop <= 79) {
                assertEquals("B", moduleService.checkGrade(loop));
            }
            if (loop > 79 && loop <= 89) {
                assertEquals("A", moduleService.checkGrade(loop));
            }
            if (loop > 89) {
                assertEquals("A*", moduleService.checkGrade(loop));
            }
        }
    }
}
