package com.pgault04.services;

import com.pgault04.entities.*;
import com.pgault04.pojos.TutorQuestionPojo;
import com.pgault04.repositories.*;
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
 * @author Paul Gault - 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTestService {

    private static final int HOUR_IN_MILLISECONDS = 3600000;
    private static final String USERNAME_IN_DB = "pgault04@qub.ac.uk";
    private static final String OTHER_USERNAME_IN_DB = "richard.gault@qub.ac.uk";
    private static final long QUESTION_IN_DB = 1L;
    private static final long USER_IN_DB = 1L;
    private static final long OTHER_IN_DB = 2L;

    @Autowired
    TestService testService;
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
    Question question;
    List<Option> options;
    Option option;
    List<Alternative> alternatives;
    Alternative alternative;
    List<CorrectPoint> correctPoints;
    CorrectPoint correctPoint;
    private Tests testObj;
    private List<Tests> tests;
    private Module module;
    private Answer markedByUser, notMarkedByUser;
    private ModuleAssociation modAssoc;

    @Before
    @Transactional
    public void setUp() throws Exception {
        question = new Question(1L, "questionContent", null/*"hello"*/, 10, USER_IN_DB, 0);
        options = new ArrayList<>();
        option = new Option(null, "optionContent", 1, "feedback");
        options.add(option);
        alternatives = new ArrayList<>();
        alternative = new Alternative(null, "alternativePhrase");
        alternatives.add(alternative);
        correctPoints = new ArrayList<>();
        correctPoint = new CorrectPoint(null, "phrase", 10.0, "feedback", alternatives, 0);
        correctPoints.add(correctPoint);

        module = new Module("module", "description", 1L, 2018);
        module = moduleRepo.insert(module);
        tests = new ArrayList<>();
        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0);
        modAssoc = moduleAssociationRepo.insert(new ModuleAssociation(module.getModuleID(), userRepo.selectByUsername(USERNAME_IN_DB).getUserID(), 1L));
    }

    @Test
    @Transactional
    public void testAddTestValid() {
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        // Only one test in the db for this module
        Tests testObjInDB = testService.primeTestForUserView(testsRepo.selectByModuleID(module.getModuleID()).get(0));
        assertEquals(testObj.toString(), testObjInDB.toString());
    }

    @Test
    @Transactional
    public void testAddTestInValid() {

        // Title too short
        Tests testToInsertInvalid = testObj;
        testToInsertInvalid.setTestTitle("");
        assertNull(testService.addTest(testToInsertInvalid, USERNAME_IN_DB));

        // Title too long
        testToInsertInvalid = testObj;
        StringBuilder tooLong = new StringBuilder();
        for (int loop = 0; loop <= 55; loop++) {
            tooLong.append("x");
        }
        testToInsertInvalid.setTestTitle(tooLong.toString());
        assertNull(testService.addTest(testToInsertInvalid, USERNAME_IN_DB));

        // Null user
        testToInsertInvalid = testObj;
        assertNull(testService.addTest(testToInsertInvalid, ""));

        // Null module
        testToInsertInvalid = testObj;
        testToInsertInvalid.setModuleID(0L);
        assertNull(testService.addTest(testToInsertInvalid, USERNAME_IN_DB));

        // User requesting not tutor
        testToInsertInvalid = testObj;
        assertNull(testService.addTest(testToInsertInvalid, OTHER_USERNAME_IN_DB));

        // Invalid start date format
        testToInsertInvalid = testObj;
        testToInsertInvalid.setStartDateTime("X");
        assertNull(testService.addTest(testToInsertInvalid, OTHER_USERNAME_IN_DB));

        // Invalid end date format
        testToInsertInvalid = testObj;
        testToInsertInvalid.setEndDateTime("X");
        assertNull(testService.addTest(testToInsertInvalid, OTHER_USERNAME_IN_DB));
    }

    @Test
    @Transactional
    public void testGetByTestIDTutorView() {


        testObj = testService.addTest(testObj, USERNAME_IN_DB);

        // Valid tutor
        assertEquals(testObj.toString(), testService.getByTestID(USERNAME_IN_DB, testObj.getTestID()).toString());

        // Invalid tutor
        assertNull(testService.getByTestID(OTHER_USERNAME_IN_DB, testObj.getTestID()));


    }

    @Test
    @Transactional
    public void testNewQuestion() throws Exception {

        testObj = testService.addTest(testObj, USERNAME_IN_DB);

        // Valid tutor
        TutorQuestionPojo returnedQuestionData = testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), question, options, correctPoints), USERNAME_IN_DB, false);
        assertEquals(question.getQuestionContent(), returnedQuestionData.getQuestion().getQuestionContent());
        assertEquals(option.getOptionContent(), returnedQuestionData.getOptions().get(0).getOptionContent());
        assertEquals(correctPoint.getPhrase(), returnedQuestionData.getCorrectPoints().get(0).getPhrase());
        assertEquals(alternative.getAlternativePhrase(), returnedQuestionData.getCorrectPoints().get(0).getAlternatives().get(0).getAlternativePhrase());

        // Invalid tutor
        returnedQuestionData = testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), question, options, correctPoints), OTHER_USERNAME_IN_DB, false);
        assertNull(returnedQuestionData);
    }

    @Test
    @Transactional
    public void testGetQuestionsByTestIDTutorView() throws Exception {
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), question, options, correctPoints), USERNAME_IN_DB, false);

        // Valid tutor
        List<TutorQuestionPojo> returnedQuestionData = testService.getQuestionsByTestIDTutorView(USERNAME_IN_DB, testObj.getTestID());
        assertEquals(question.getQuestionContent(), returnedQuestionData.get(0).getQuestion().getQuestionContent());
        assertEquals(option.getOptionContent(), returnedQuestionData.get(0).getOptions().get(0).getOptionContent());
        assertEquals(correctPoint.getPhrase(), returnedQuestionData.get(0).getCorrectPoints().get(0).getPhrase());
        assertEquals(alternative.getAlternativePhrase(), returnedQuestionData.get(0).getCorrectPoints().get(0).getAlternatives().get(0).getAlternativePhrase());

        // Invalid tutor
        returnedQuestionData = testService.getQuestionsByTestIDTutorView(OTHER_USERNAME_IN_DB, testObj.getTestID());
        assertNull(returnedQuestionData);
    }

    @Test
    @Transactional
    public void testGetOldQuestions() throws Exception {

        // Test added and question added
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        TutorQuestionPojo returnedQuestionOne = testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), question, options, correctPoints), USERNAME_IN_DB, false);
        question.setQuestionID(-1L);
        options.get(0).setOptionID(-1L);
        correctPoints.get(0).setCorrectPointID(-1L);
        correctPoints.get(0).getAlternatives().get(0).setAlternativeID(-1L);
        testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), question, options, correctPoints), USERNAME_IN_DB, false);

        // Test set back to make it insertable not updatable
        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0);
        // New test added so old questions can be called
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        testService.addExistingQuestion(returnedQuestionOne.getQuestion().getQuestionID(), testObj.getTestID(), USERNAME_IN_DB);

        // The question added to previous test by user should appear in their old questions list
        List<TutorQuestionPojo> returnedQuestionData = testService.getOldQuestions(USERNAME_IN_DB, testObj.getTestID());
        assertEquals(question.getQuestionContent(), returnedQuestionData.get(0).getQuestion().getQuestionContent());
        assertEquals(option.getOptionContent(), returnedQuestionData.get(0).getOptions().get(0).getOptionContent());
        assertEquals(correctPoint.getPhrase(), returnedQuestionData.get(0).getCorrectPoints().get(0).getPhrase());
        assertEquals(alternative.getAlternativePhrase(), returnedQuestionData.get(0).getCorrectPoints().get(0).getAlternatives().get(0).getAlternativePhrase());
    }

    @Test
    @Transactional
    public void testAddExistingQuestion() throws Exception {

        // Test added and question added
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        TutorQuestionPojo returnedQuestionOne = testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), question, options, correctPoints), USERNAME_IN_DB, false);

        // Test set back to make it insertable not updatable
        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0);
        // New test added so old questions can be called
        testObj = testService.addTest(testObj, USERNAME_IN_DB);

        // Valid tutor
        TestQuestion testQuestion = testService.addExistingQuestion(returnedQuestionOne.getQuestion().getQuestionID(), testObj.getTestID(), USERNAME_IN_DB);
        assertEquals(returnedQuestionOne.getQuestion().getQuestionID(), testQuestion.getQuestionID());

        // Invalid tutor
        testQuestion = testService.addExistingQuestion(returnedQuestionOne.getQuestion().getQuestionID(), testObj.getTestID(), OTHER_USERNAME_IN_DB);
        assertNull(testQuestion);
    }

    @Test
    @Transactional
    public void testDeleteTest() throws Exception {

        // Valid tutor
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        Boolean check = testService.deleteTest(testObj.getTestID(), USERNAME_IN_DB);
        assertTrue(check);

        // Invalid tutor
        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0);
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        check = testService.deleteTest(testObj.getTestID(), OTHER_USERNAME_IN_DB);
        assertFalse(check);

    }

    @Test
    @Transactional
    public void testScheduleTest() throws Exception {

        // Valid tutor
        testObj = testService.addTest(testObj, USERNAME_IN_DB);

        // schedule
        Boolean check = testService.scheduleTest(testObj.getTestID(), USERNAME_IN_DB);
        assertTrue(check);

        // unschedule
        check = testService.scheduleTest(testObj.getTestID(), USERNAME_IN_DB);
        assertTrue(check);

        // invalid tutor
        check = testService.scheduleTest(testObj.getTestID(), OTHER_USERNAME_IN_DB);
        assertFalse(check);
    }

    @Test
    @Transactional
    public void testRemoveQuestionFromTest() throws Exception {

        // Test added and question added
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        TutorQuestionPojo returnedQuestionOne = testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), question, options, correctPoints), USERNAME_IN_DB, false);

        // Invalid tutor
        Boolean check = testService.removeQuestionFromTest(testObj.getTestID(), returnedQuestionOne.getQuestion().getQuestionID(), OTHER_USERNAME_IN_DB);
        assertFalse(check);

        // Valid tutor
        check = testService.removeQuestionFromTest(testObj.getTestID(), returnedQuestionOne.getQuestion().getQuestionID(), USERNAME_IN_DB);
        assertTrue(check);
    }
}
