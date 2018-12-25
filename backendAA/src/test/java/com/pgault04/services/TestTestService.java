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
import java.text.ParseException;
import java.util.ArrayList;
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

    private Tests testObj;
    private List<Tests> tests;
    private Module module;
    private Answer markedByUser, notMarkedByUser;
    private ModuleAssociation modAssoc;

    @Before
    @Transactional
    public void setUp() throws Exception {
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
        assertEquals(testObj.toString(), testService.getByTestIDTutorView(USERNAME_IN_DB, testObj.getTestID()).toString());

        // Invalid tutor
        assertNull(testService.getByTestIDTutorView(OTHER_USERNAME_IN_DB, testObj.getTestID()));


    }

    @Test
    @Transactional
    public void testNewQuestion() throws Exception {
        Question question = new Question(1L, "questionContent", "hello", 10, USER_IN_DB);
        List<Option> options = new ArrayList<>();
        Option option = new Option(null, "optionContent", 1);
        options.add(option);
        List<Alternative> alternatives = new ArrayList<>();
        Alternative alternative = new Alternative(null, "alternativePhrase");
        alternatives.add(alternative);
        List<CorrectPoint> correctPoints = new ArrayList<>();
        CorrectPoint correctPoint = new CorrectPoint(null, "phrase", 10.0, "feedback", alternatives);
        correctPoints.add(correctPoint);


        testObj = testService.addTest(testObj, USERNAME_IN_DB);

        // Valid tutor
        TutorQuestionPojo returnedQuestionData = testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), question, options, correctPoints), USERNAME_IN_DB);
        assertEquals(question.getQuestionContent(), returnedQuestionData.getQuestion().getQuestionContent());
        assertEquals(option.getOptionContent(), returnedQuestionData.getOptions().get(0).getOptionContent());
        assertEquals(correctPoint.getPhrase(), returnedQuestionData.getCorrectPoints().get(0).getPhrase());
        assertEquals(alternative.getAlternativePhrase(), returnedQuestionData.getCorrectPoints().get(0).getAlternatives().get(0).getAlternativePhrase());

        // Invalid tutor
        returnedQuestionData = testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), question, options, correctPoints), OTHER_USERNAME_IN_DB);
        assertNull(returnedQuestionData);
    }
}
