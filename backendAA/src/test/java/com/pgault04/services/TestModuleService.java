package com.pgault04.services;

import com.pgault04.controller.ModuleController;
import com.pgault04.entities.*;
import com.pgault04.pojos.*;
import com.pgault04.repositories.*;
import com.pgault04.utilities.StringToDateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.security.Principal;
import java.sql.SQLException;
import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

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
    ModuleController moduleController;
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
    private String commencementDate, endDate;

    @Before
    @Transactional
    public void setUp() throws Exception {
        this.commencementDate = "2018-09-01";
        this.endDate = "2018-09-01";
        module = new Module("module", "description", 1L, commencementDate, endDate, 1);
        module = moduleRepo.insert(module);
        tests = new ArrayList<>();
        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01 10:00:00", "2018-01-01 11:00:00", 0, 0, 0, 0);
        modAssoc = moduleAssociationRepo.insert(new ModuleAssociation(module.getModuleID(), userRepo.selectByUsername(USERNAME_IN_DB).getUserID(), AssociationType.TUTOR));
    }

    @Test
    @Transactional
    public void testGetModuleRequests() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        // Admin
        module.setApproved(0);
        moduleRepo.insert(module);
        List<ModuleRequestPojo> modules = moduleController.getModuleRequests(principal);
        assertTrue(modules.toString().contains(module.toString()));
        // Non-admin
        modules = moduleService.getModuleRequests(null);
        assertNull(modules);
    }

    @Test
    @Transactional
    public void testApproveModuleRequest() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        // Admins
        moduleRepo.insert(module);
        moduleController.approveModuleRequest(module.getModuleID(), principal);
        module = moduleRepo.selectByID(module.getModuleID());
        assertEquals(1, module.getApproved(), 0);

        // Non-admin
        module.setApproved(0);
        moduleRepo.insert(module);
        moduleService.approveModuleRequest(module.getModuleID(), OTHER_USERNAME_IN_DB);
        module = moduleRepo.selectByID(module.getModuleID());
        assertEquals(0, module.getApproved(), 0);
    }

    @Test
    @Transactional
    public void testRejectModuleRequest() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        // Admins
        moduleRepo.insert(module);
        moduleController.rejectModuleRequest(module.getModuleID(), principal);
        assertNull(moduleRepo.selectByID(module.getModuleID()));

        // Non-admin
        module.setModuleID(-1L);
        moduleRepo.insert(module);
        moduleService.rejectModuleRequest(module.getModuleID(), OTHER_USERNAME_IN_DB);
        module = moduleRepo.selectByID(module.getModuleID());
        assertNotNull(module.getApproved());
    }

    @Test
    @Transactional
    public void testRemoveAssociate() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        ModuleAssociation modAssoc2 = moduleAssociationRepo.insert(new ModuleAssociation(module.getModuleID(), userRepo.selectByUsername(OTHER_USERNAME_IN_DB).getUserID(), AssociationType.STUDENT));
        // Non-tutor
        moduleService.removeAssociate(OTHER_USERNAME_IN_DB, module.getModuleID(), OTHER_USERNAME_IN_DB);
        List<ModuleAssociation> modAssocs = moduleAssociationRepo.selectByModuleID(module.getModuleID());
        assertTrue(modAssocs.toString().contains(modAssoc2.toString()));

        // Tutor
        moduleController.removeAssociate(OTHER_USERNAME_IN_DB, module.getModuleID(), principal);
        modAssocs = moduleAssociationRepo.selectByModuleID(module.getModuleID());
        assertFalse(modAssocs.toString().contains(modAssoc2.toString()));
    }

    @Test
    @Transactional
    public void testGetAssociates() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        // Associated User
        List<Associate> associates = moduleController.getAssociates(module.getModuleID(), principal);
        User user = userRepo.selectByUsername(USERNAME_IN_DB);
        Associate associate = new Associate("Tutor", user.getUsername(), user.getFirstName(), user.getLastName());
        assertTrue(associates.toString().contains(associate.toString()));

        modAssoc.setAssociationType(AssociationType.STUDENT);
        moduleAssociationRepo.insert(modAssoc);
        associates = moduleService.getAssociates(module.getModuleID(), USERNAME_IN_DB);
        user = userRepo.selectByUsername(USERNAME_IN_DB);
        associate = new Associate("Student", user.getUsername(), user.getFirstName(), user.getLastName());
        assertTrue(associates.toString().contains(associate.toString()));

        modAssoc.setAssociationType(AssociationType.TEACHING_ASSISTANT);
        moduleAssociationRepo.insert(modAssoc);
        associates = moduleService.getAssociates(module.getModuleID(), USERNAME_IN_DB);
        user = userRepo.selectByUsername(USERNAME_IN_DB);
        associate = new Associate("Teaching Assistant", user.getUsername(), user.getFirstName(), user.getLastName());
        assertTrue(associates.toString().contains(associate.toString()));

        // Non associated user
        assertNull(moduleService.getAssociates(module.getModuleID(), OTHER_USERNAME_IN_DB));
    }

    @Test
    @Transactional
    public void testActiveTests() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

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
        tests = moduleController.getActiveTests(principal, module.getModuleID());
        assertEquals(1, tests.size());
        assertEquals(testObj.toString(), tests.get(0).toString());

        // User with no association
        tests = moduleService.activeTests(OTHER_USERNAME_IN_DB, module.getModuleID());
        assertNull(tests);
    }

    @Test
    @Transactional
    public void testPracticeTests() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        // Sets up test to be practice
        testObj.setPractice(1);
        testObj.setScheduled(1);
        Date startDate = new Date();
        testObj.setStartDateTime(StringToDateUtil.dateCorrectFormat(startDate));
        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + HOUR_IN_MILLISECONDS);
        testObj.setEndDateTime(StringToDateUtil.dateCorrectFormat(endDate));

        // Inserts test
        testObj = testsRepo.insert(testObj);

        // Calls method for user who has association
        tests = moduleController.getPracticeTests(principal, module.getModuleID());
        assertEquals(1, tests.size());
        assertEquals(testObj.toString(), tests.get(0).toString());

        // User with no association
        tests = moduleService.practiceTests(OTHER_USERNAME_IN_DB, module.getModuleID());
        assertNull(tests);
    }

    @Test
    @Transactional
    public void testAddModule() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        module.setModuleID(-1L);
        module.setModuleName("UniqueModuleName");
        List<Associate> associations = new ArrayList<>();
        User user = userRepo.selectByUsername(USERNAME_IN_DB);
        Associate associate = new Associate("TA", user.getUsername(), user.getFirstName(), user.getLastName());
        associations.add(associate);
        ModulePojo modulePojo = new ModulePojo(module, associations);

        moduleController.addModule(modulePojo, principal);
        List<Module> modules = moduleRepo.selectByModuleName(modulePojo.getModule().getModuleName());
        Set<Long> userIDs = new HashSet<>();
        for (ModuleAssociation ma : moduleAssociationRepo.selectByModuleID(modules.get(0).getModuleID())) {
            userIDs.add(ma.getUserID());
        }
        assertTrue(userIDs.contains(user.getUserID()));
    }

    @Test
    @Transactional
    public void testAddAssociates() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        module.setModuleID(-1L);
        module.setModuleName("UniqueModuleName");
        List<Associate> associations = new ArrayList<>();
        Associate associate = new Associate("S", "dummyusername", "dummy", "username");
        associations.add(associate);
        ModulePojo modulePojo = new ModulePojo(module, new ArrayList<>());
        moduleController.addModule(modulePojo, principal);
        moduleController.addAssociations(3L, associations, principal);
        assertNotNull(userRepo.selectByUsername("dummyusername"));
    }

    @Transactional
    @Test (expected = IllegalArgumentException.class)
    public void testAddAssociatesInvalidType() {
        module.setModuleID(-1L);
        module.setModuleName("UniqueModuleName");
        List<Associate> associations = new ArrayList<>();
        Associate associate = new Associate("Invalid", "dummyusername", "dummy", "username");
        associations.add(associate);
        ModulePojo modulePojo = new ModulePojo(module, associations);
        moduleService.addModule(modulePojo, USERNAME_IN_DB);
    }

    @Transactional
    @Test (expected = IllegalArgumentException.class)
    public void testAddAssociatesInvalidPrivileges() {
        module.setModuleID(-1L);
        module.setModuleName("UniqueModuleName2");
        List<Associate> associations = new ArrayList<>();
        Associate associate = new Associate("S", "dummyusername", "dummy", "username");
        associations.add(associate);
        ModulePojo modulePojo = new ModulePojo(module, associations);
        moduleService.addModule(modulePojo, USERNAME_IN_DB);
        List<Module> modules = moduleRepo.selectByModuleName("UniqueModuleName2");
        moduleService.addAssociations(modules.get(0).getModuleID(), associations, OTHER_USERNAME_IN_DB);
    }

    @Transactional
    @Test
    public void testModulesPendingApproval() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        module.setApproved(0);
        moduleRepo.insert(module);
        List<Module> modules = moduleController.getModulesPendingApproval(principal);
        assertTrue(modules.toString().contains(module.toString()));
    }

    @Test
    @Transactional
    public void testScheduledTests() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

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
        tests = moduleController.getScheduledTests(principal, module.getModuleID());
        assertEquals(1, tests.size());
        assertEquals(testObj.toString(), tests.get(0).toString());

        // User with no association
        tests = moduleService.scheduledTests(OTHER_USERNAME_IN_DB, module.getModuleID());
        assertNull(tests);
    }

    @Test
    public void testGetModuleWithTutor() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        ModuleWithTutor modWithTutor = moduleController.getModuleWithTutor(module.getModuleID());
        assertEquals(USERNAME_IN_DB, modWithTutor.getTutor().getUsername());
        assertEquals(module.getModuleID(), modWithTutor.getModule().getModuleID());
    }

    @Test
    public void testGetMyModulesWithTutors() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        List<ModuleWithTutor> modules;
        // 2 mod assocs already stored in tests.sql plus the one added in setup
        modules = moduleController.getModulesWithTutors(principal);
        assertEquals(3, modules.size());
    }

    @Test
    @Transactional
    public void testMarking() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

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
        markedByUser = answerRepo.insert(new Answer(QUESTION_IN_DB, USER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 25, "feedback", 1, 0));
        notMarkedByUser = answerRepo.insert(new Answer(QUESTION_IN_DB, USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "content", 50, "feedback", 1, 0));
        markedByUser = answerRepo.insert(new Answer(QUESTION_IN_DB, USER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", null, "feedback", 0, 0));
        notMarkedByUser = answerRepo.insert(new Answer(QUESTION_IN_DB, USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "content", null, "feedback", 0, 0));

        // Tutor association
        List<TestMarking> tests = moduleController.getMarking(principal, module.getModuleID());
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
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        // Sets test up to be ready for active results
        testObj.setPublishGrades(1);
        testObj = testsRepo.insert(testObj);

        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), QUESTION_IN_DB));

        testResultRepo.insert(new TestResult(testObj.getTestID(), USER_IN_DB, 100));

        // Not-student association
        List<TestAndGrade> tests = moduleController.getActiveResults(principal, module.getModuleID());
        assertNull(tests);

        // Student association
        modAssoc.setAssociationType(2L);
        moduleAssociationRepo.insert(modAssoc);
        tests = moduleService.activeGrades(module.getModuleID(), USERNAME_IN_DB);
        assertEquals(1, tests.size());
        assertEquals("A*", tests.get(0).getGrade());
    }

    @Test
    @Transactional
    public void testTestDrafts() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);

        // Tutor association
        tests = moduleController.getTestDrafts(principal, module.getModuleID());
        assertEquals(1, tests.size());
        assertEquals(testObj.toString(), tests.get(0).toString());

        // Non-tutor assoc
        tests = moduleService.testDrafts(OTHER_USERNAME_IN_DB, module.getModuleID());
        assertNull(tests);
    }

    @Test
    @Transactional
    public void testReviewMarking() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

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
        List<TestMarking> tests = moduleController.getReviewMarking(principal, module.getModuleID());
        assertEquals(0, tests.size());
        answer.setMarkerApproved(1);
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
         Principal principal = Mockito.mock(Principal.class);
         when(principal.getName()).thenReturn(USERNAME_IN_DB);

         // Sets test up to be ready for active results
         testObj.setPublishResults(1);
         testObj = testsRepo.insert(testObj);

         testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), QUESTION_IN_DB));

         testResultRepo.insert(new TestResult(testObj.getTestID(), USER_IN_DB, 100));

         // Teaching assistant or no association 0r tutor
         List<Performance> performances = moduleController.getPerformance(principal, module.getModuleID());
         assertNull(performances);

        // Student association
         modAssoc.setAssociationType(2L);
         moduleAssociationRepo.insert(modAssoc);
         performances = moduleService.generatePerformance(module.getModuleID(), USERNAME_IN_DB);
         assertEquals(1, performances.size());
         assertEquals(3333, performances.get(0).getClassAverage(), 1.0);
         assertEquals(3, performances.get(0).getTestAndResult().getPercentageScore(), 0.0);
     }

    @Test
    @Transactional
    public void testMyModules() {
        List<Module> modules;
        // 2 mod assocs already stored in tests.sql plus the one added in setup
        modules = moduleService.myModules(USERNAME_IN_DB);
        assertEquals(3, modules.size());

    }

    @Test
    @Transactional
    public void testCheckValidAssociation() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        // Module and association for 'pgault04@qub.ac.uk' stored in db
        assertEquals(AssociationType.STUDENT, moduleService.checkValidAssociation(USERNAME_IN_DB, 1L), 0);
        // No mod assoc for module input in setUp
        assertNull(moduleController.getModuleAssociation(principal, module.getModuleID()));
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
