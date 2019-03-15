package com.pgault04.services;

import com.pgault04.controller.TestController;
import com.pgault04.entities.*;
import com.pgault04.pojos.Performance;
import com.pgault04.pojos.QuestionAndAnswer;
import com.pgault04.pojos.QuestionAndBase64;
import com.pgault04.pojos.TutorQuestionPojo;
import com.pgault04.repositories.*;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
public class TestTestService {

    private static final String USERNAME_IN_DB = "pgault04@qub.ac.uk";
    private static final String OTHER_USERNAME_IN_DB = "richard.gault@qub.ac.uk";
    private static final long USER_IN_DB = 1L;
    private static final long OTHER_IN_DB = 2L;

    @Autowired
    TestResultRepo testResultRepo;
    @Autowired
    TestController testController;
    @Autowired
    OptionRepo optionRepo;
    @Autowired
    AlternativeRepo alternativeRepo;
    @Autowired
    CorrectPointRepo correctPointRepo;
    @Autowired
    QuestionMathLineRepo questionMathLineRepo;
    @Autowired
    QuestionRepo questionRepo;
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
    ModuleAssociationRepo moduleAssociationRepo;
    @Autowired
    UserRepo userRepo;
    private Question question;
    private List<Option> options;
    private Option option;
    private List<Alternative> alternatives;
    private Alternative alternative;
    private List<CorrectPoint> correctPoints;
    private CorrectPoint correctPoint;
    private Tests testObj;
    private Module module;
    private QuestionMathLine questionMathLine;
    private List<QuestionMathLine> questionMathLines;

    @Before
    @Transactional
    public void setUp() throws Exception {
        String commencementDate = "2018-09-01";
        String endDate = "2018-09-01";
        question = new Question(1L, "questionContent", null, 10, 0, USER_IN_DB, 0);
        options = new ArrayList<>();
        option = new Option(null, "optionContent", 1, "feedback");
        options.add(option);

        questionMathLine = new QuestionMathLine(question.getQuestionID(), "content", 0);
        questionMathLines = new ArrayList<>();
        questionMathLines.add(questionMathLine);
        alternatives = new ArrayList<>();
        alternative = new Alternative(null, "alternativePhrase", 0);
        alternatives.add(alternative);
        correctPoints = new ArrayList<>();
        correctPoint = new CorrectPoint(null, "phrase", 10.0, "feedback", alternatives, 0, 0);
        correctPoints.add(correctPoint);

        module = new Module("module", "description", 1L, commencementDate, endDate, 1);
        module = moduleRepo.insert(module);
        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0, 0);
        ModuleAssociation modAssoc = moduleAssociationRepo.insert(new ModuleAssociation(module.getModuleID(), userRepo.selectByUsername(USERNAME_IN_DB).getUserID(), 1L));
    }

    @Test
    @Transactional
    public void testSubmitTestText() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principalOther = Mockito.mock(Principal.class);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        // Text based question
        Question questionTex = new Question(QuestionType.TEXT_BASED, "content", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        List<CorrectPoint> correctPointsTex = new ArrayList<>();
        CorrectPoint correctPointTex = new CorrectPoint(questionTex.getQuestionID(), "phraseTex", 10.0, "feedbackTex", new ArrayList<>(), 0, 0);
        correctPointTex = correctPointRepo.insert(correctPointTex);
        Alternative alternativeTex = new Alternative(correctPointTex.getCorrectPointID(), "altTex1", 0);
        alternativeTex = alternativeRepo.insert(alternativeTex);
        correctPointTex.getAlternatives().add(alternativeTex);
        correctPointsTex.add(correctPointTex);

        Answer answerUserTex = new Answer(questionTex.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "phraseTex", 0, "", 0, 0);
        Answer answerOtherUserTex = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "altTex1", 0, "", 0, 0);

        QuestionAndAnswer questionAndAnswerTexUser = new QuestionAndAnswer(new QuestionAndBase64(null, null, null, questionTex), answerUserTex, null, null, correctPointsTex);
        QuestionAndAnswer questionAndAnswerTexOther = new QuestionAndAnswer(new QuestionAndBase64(null, null, null, questionTex), answerOtherUserTex, null, null, correctPointsTex);

        List<QuestionAndAnswer> scriptsUser = new ArrayList<>();
        scriptsUser.add(questionAndAnswerTexUser);

        List<QuestionAndAnswer> scriptsOther = new ArrayList<>();
        scriptsOther.add(questionAndAnswerTexOther);

        testController.submitTest(principal, scriptsUser);
        Answer returnedAnswer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(answerUserTex.getQuestionID(), answerUserTex.getAnswererID(), testObj.getTestID());
        assertEquals(10, (int) returnedAnswer.getScore());
        assertEquals("feedbackTex\n", returnedAnswer.getFeedback());

        testController.submitTest(principalOther, scriptsOther);
        returnedAnswer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(answerOtherUserTex.getQuestionID(), answerOtherUserTex.getAnswererID(), testObj.getTestID());
        assertEquals(10, (int) returnedAnswer.getScore());
        assertEquals("feedbackTex\n", returnedAnswer.getFeedback());
    }

    @Test
    @Transactional
    public void testSubmitTestTextIdenticalSoln() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principalOther = Mockito.mock(Principal.class);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        // Text based question
        Question questionTex = new Question(QuestionType.TEXT_BASED, "content", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);

        Answer answerUserTex = new Answer(questionTex.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "answerSoln", 0, "", 0, 0);
        Answer answerOtherUserTex = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "answerSoln", 0, "", 0, 0);

        QuestionAndAnswer questionAndAnswerTexUser = new QuestionAndAnswer(new QuestionAndBase64(null, null, null, questionTex), answerUserTex, null, null, new ArrayList<>());
        QuestionAndAnswer questionAndAnswerTexOther = new QuestionAndAnswer(new QuestionAndBase64(null, null, null, questionTex), answerOtherUserTex, null, null, new ArrayList<>());

        List<QuestionAndAnswer> scriptsUser = new ArrayList<>();
        scriptsUser.add(questionAndAnswerTexUser);

        List<QuestionAndAnswer> scriptsOther = new ArrayList<>();
        scriptsOther.add(questionAndAnswerTexOther);

        testController.submitTest(principal, scriptsUser);
        Answer returnedAnswer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(answerUserTex.getQuestionID(), answerUserTex.getAnswererID(), testObj.getTestID());
        returnedAnswer.setFeedback("feedbackToEnsureIdenticalAnswerWorks");
        returnedAnswer.setScore(10);
        answerRepo.insert(returnedAnswer);

        testController.submitTest(principalOther, scriptsOther);
        returnedAnswer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(answerOtherUserTex.getQuestionID(), answerOtherUserTex.getAnswererID(), testObj.getTestID());
        assertEquals(10, (int) returnedAnswer.getScore());
        assertEquals("feedbackToEnsureIdenticalAnswerWorks", returnedAnswer.getFeedback());
    }

    @Test
    @Transactional
    public void testCheckGrades() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        testObj.setPublishGrades(1);
        testObj = testsRepo.insert(testObj);

        Question questionOpt = new Question(QuestionType.MULTIPLE_CHOICE, "content", null, 10, 0, USER_IN_DB, 0);
        questionOpt = questionRepo.insert(questionOpt);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionOpt.getQuestionID()));
        List<Option> optionsOpt = new ArrayList<>();
        Option optionOpt = new Option(questionOpt.getQuestionID(), "option", 0, "feedbackOpt");
        optionOpt = optionRepo.insert(optionOpt);
        optionsOpt.add(optionOpt);

        Answer answerUserOpt = new Answer(questionOpt.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        List<OptionEntries> optionEntriesUser = new ArrayList<>();
        optionEntriesUser.add(new OptionEntries(optionOpt.getOptionID(), -1L));

        QuestionAndAnswer questionAndAnswerMatUser = new QuestionAndAnswer(new QuestionAndBase64(null, optionsOpt, null, questionOpt), answerUserOpt, null, optionEntriesUser, null);

        Question questionIns = new Question(QuestionType.INSERT_THE_WORD, "content", null, 10, 0, USER_IN_DB, 0);
        questionIns = questionRepo.insert(questionIns);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionIns.getQuestionID()));
        List<CorrectPoint> correctPointsIns = new ArrayList<>();
        CorrectPoint correctPointIns = new CorrectPoint(questionIns.getQuestionID(), "phraseTex", 10.0, "feedbackTex", new ArrayList<>(), 0, 0);
        correctPointIns = correctPointRepo.insert(correctPointIns);
        Alternative alternativeIns = new Alternative(correctPointIns.getCorrectPointID(), "altTex1", 0);
        alternativeIns = alternativeRepo.insert(alternativeIns);
        correctPointIns.getAlternatives().add(alternativeIns);
        correctPointsIns.add(correctPointIns);

        Answer answerUserIns = new Answer(questionIns.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        List<Inputs> inputsUserIns = new ArrayList<>();
        inputsUserIns.add(new Inputs("phraseTex", 0, -1L, 0));

        QuestionAndAnswer questionAndAnswerInsUser = new QuestionAndAnswer(new QuestionAndBase64(null, null, null, questionIns), answerUserIns, inputsUserIns, null, correctPointsIns);

        List<QuestionAndAnswer> scriptsUser = new ArrayList<>();
        scriptsUser.add(questionAndAnswerMatUser);
        scriptsUser.add(questionAndAnswerInsUser);

        testController.submitTest(principal, scriptsUser);

        TestResult testResult = new TestResult(testObj.getTestID(), USER_IN_DB, 18);
        testResult = testResultRepo.insert(testResult);
        Performance testAndResults = testController.getFeedback(principal, testObj.getTestID());
        assertEquals(90, testAndResults.getClassAverage(), 0);
        assertEquals(90, testAndResults.getTestAndResult().getTestResult().getTestScore(), 0);

        testResult.setTestScore(16);
        testResult = testResultRepo.insert(testResult);
        testAndResults = testController.getFeedback(principal, testObj.getTestID());
        assertEquals(80, testAndResults.getClassAverage(), 0);
        assertEquals(80, testAndResults.getTestAndResult().getTestResult().getTestScore(), 0);

        testResult.setTestScore(14);
        testResult = testResultRepo.insert(testResult);
        testAndResults = testController.getFeedback(principal, testObj.getTestID());
        assertEquals(70, testAndResults.getClassAverage(), 0);
        assertEquals(70, testAndResults.getTestAndResult().getTestResult().getTestScore(), 0);

        testResult.setTestScore(12);
        testResult = testResultRepo.insert(testResult);
        testAndResults = testController.getFeedback(principal, testObj.getTestID());
        assertEquals(60, testAndResults.getClassAverage(), 0);
        assertEquals(60, testAndResults.getTestAndResult().getTestResult().getTestScore(), 0);

        testResult.setTestScore(10);
        testResult = testResultRepo.insert(testResult);
        testAndResults = testController.getFeedback(principal, testObj.getTestID());
        assertEquals(50, testAndResults.getClassAverage(), 0);
        assertEquals(50, testAndResults.getTestAndResult().getTestResult().getTestScore(), 0);

        testResult.setTestScore(8);
        testResultRepo.insert(testResult);
        testAndResults = testController.getFeedback(principal, testObj.getTestID());
        assertEquals(40, testAndResults.getClassAverage(), 0);
        assertEquals(40, testAndResults.getTestAndResult().getTestResult().getTestScore(), 0);
    }

    @Test
    @Transactional
    public void testGetPerformance() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        testObj.setPublishResults(1);
        testObj = testsRepo.insert(testObj);

        Question questionOpt = new Question(QuestionType.MULTIPLE_CHOICE, "content", null, 10, 0, USER_IN_DB, 0);
        questionOpt = questionRepo.insert(questionOpt);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionOpt.getQuestionID()));
        List<Option> optionsOpt = new ArrayList<>();
        Option optionOpt = new Option(questionOpt.getQuestionID(), "option", 0, "feedbackOpt");
        optionOpt = optionRepo.insert(optionOpt);
        optionsOpt.add(optionOpt);

        Answer answerUserOpt = new Answer(questionOpt.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        List<OptionEntries> optionEntriesUser = new ArrayList<>();
        optionEntriesUser.add(new OptionEntries(optionOpt.getOptionID(), -1L));

        QuestionAndAnswer questionAndAnswerMatUser = new QuestionAndAnswer(new QuestionAndBase64(null, optionsOpt, null, questionOpt), answerUserOpt, null, optionEntriesUser, null);

        Question questionIns = new Question(QuestionType.INSERT_THE_WORD, "content", null, 10, 0, USER_IN_DB, 0);
        questionIns = questionRepo.insert(questionIns);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionIns.getQuestionID()));
        List<CorrectPoint> correctPointsIns = new ArrayList<>();
        CorrectPoint correctPointIns = new CorrectPoint(questionIns.getQuestionID(), "phraseTex", 10.0, "feedbackTex", new ArrayList<>(), 0, 0);
        correctPointIns = correctPointRepo.insert(correctPointIns);
        Alternative alternativeIns = new Alternative(correctPointIns.getCorrectPointID(), "altTex1", 0);
        alternativeIns = alternativeRepo.insert(alternativeIns);
        correctPointIns.getAlternatives().add(alternativeIns);
        correctPointsIns.add(correctPointIns);

        Answer answerUserIns = new Answer(questionIns.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        List<Inputs> inputsUserIns = new ArrayList<>();
        inputsUserIns.add(new Inputs("phraseTex", 0, -1L, 0));

        QuestionAndAnswer questionAndAnswerInsUser = new QuestionAndAnswer(new QuestionAndBase64(null, null, null, questionIns), answerUserIns, inputsUserIns, null, correctPointsIns);

        List<QuestionAndAnswer> scriptsUser = new ArrayList<>();
        scriptsUser.add(questionAndAnswerMatUser);
        scriptsUser.add(questionAndAnswerInsUser);

        testController.submitTest(principal, scriptsUser);

        TestResult testResult = new TestResult(testObj.getTestID(), USER_IN_DB, 20);
        testResultRepo.insert(testResult);
        Performance testAndResults = testController.getPerformance(principal, testObj.getTestID());
        assertEquals(100, testAndResults.getClassAverage(), 0);
        assertEquals(20, testAndResults.getTestAndResult().getTestResult().getTestScore(), 0);
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testGetPerformanceInvalid() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        testObj.setPublishResults(0);
        testObj = testsRepo.insert(testObj);
        testController.getPerformance(principal, testObj.getTestID());
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testCheckGradesInvalid() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        testObj.setPublishGrades(0);
        testObj = testsRepo.insert(testObj);
        testController.getFeedback(principal, testObj.getTestID());
    }

    @Test
    @Transactional
    public void testSubmitTestMath() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principalOther = Mockito.mock(Principal.class);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        // Text based question
        Question questionMat = new Question(QuestionType.TEXT_MATH, "content", null, 10, 0, USER_IN_DB, 0);
        questionMat = questionRepo.insert(questionMat);
        List<CorrectPoint> correctPointsMat = new ArrayList<>();
        CorrectPoint correctPointMat = new CorrectPoint(questionMat.getQuestionID(), "phraseTex", 11.0, "feedbackTex", new ArrayList<>(), 0, 0);
        correctPointMat = correctPointRepo.insert(correctPointMat);
        Alternative alternativeMat = new Alternative(correctPointMat.getCorrectPointID(), "altTex1", 0);
        alternativeMat = alternativeRepo.insert(alternativeMat);
        correctPointMat.getAlternatives().add(alternativeMat);
        correctPointsMat.add(correctPointMat);

        Answer answerUserMat = new Answer(questionMat.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        List<Inputs> inputsUserMat = new ArrayList<>();
        inputsUserMat.add(new Inputs("phraseTex", 0, -1L, 1));
        Answer answerOtherUserMat = new Answer(questionMat.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        List<Inputs> inputsOtherMat = new ArrayList<>();
        inputsOtherMat.add(new Inputs("altTex1", 0, -1L, 1));

        QuestionAndAnswer questionAndAnswerMatUser = new QuestionAndAnswer(new QuestionAndBase64(null, null, null, questionMat), answerUserMat, inputsUserMat, null, correctPointsMat);
        QuestionAndAnswer questionAndAnswerMatOther = new QuestionAndAnswer(new QuestionAndBase64(null, null, null, questionMat), answerOtherUserMat, inputsOtherMat, null, correctPointsMat);

        List<QuestionAndAnswer> scriptsUser = new ArrayList<>();
        scriptsUser.add(questionAndAnswerMatUser);

        List<QuestionAndAnswer> scriptsOther = new ArrayList<>();
        scriptsOther.add(questionAndAnswerMatOther);

        testController.submitTest(principal, scriptsUser);
        Answer returnedAnswer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(answerUserMat.getQuestionID(), answerUserMat.getAnswererID(), testObj.getTestID());
        assertEquals(10, (int) returnedAnswer.getScore());
        assertEquals("feedbackTex\n", returnedAnswer.getFeedback());

        testController.submitTest(principalOther, scriptsOther);
        returnedAnswer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(answerOtherUserMat.getQuestionID(), answerOtherUserMat.getAnswererID(), testObj.getTestID());
        assertEquals(10, (int) returnedAnswer.getScore());
        assertEquals("feedbackTex\n", returnedAnswer.getFeedback());
    }

    @Test
    @Transactional
    public void testSubmitTestOpt() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        // Text based question
        Question questionOpt = new Question(QuestionType.MULTIPLE_CHOICE, "content", null, 10, 0, USER_IN_DB, 0);
        questionOpt = questionRepo.insert(questionOpt);
        List<Option> optionsOpt = new ArrayList<>();
        Option optionOpt = new Option(questionOpt.getQuestionID(), "option", -1, "feedbackOpt");
        optionOpt = optionRepo.insert(optionOpt);
        optionsOpt.add(optionOpt);

        Answer answerUserOpt = new Answer(questionOpt.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        List<OptionEntries> optionEntriesUser = new ArrayList<>();
        optionEntriesUser.add(new OptionEntries(optionOpt.getOptionID(), -1L));

        QuestionAndAnswer questionAndAnswerMatUser = new QuestionAndAnswer(new QuestionAndBase64(null, optionsOpt, null, questionOpt), answerUserOpt, null, optionEntriesUser, null);

        List<QuestionAndAnswer> scriptsUser = new ArrayList<>();
        scriptsUser.add(questionAndAnswerMatUser);

        testController.submitTest(principal, scriptsUser);
        Answer returnedAnswer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(answerUserOpt.getQuestionID(), answerUserOpt.getAnswererID(), testObj.getTestID());
        assertEquals(0, (int) returnedAnswer.getScore());
        assertEquals("feedbackOpt\n", returnedAnswer.getFeedback());
    }

    @Test
    @Transactional
    public void testSubmitTestIns() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principalOther = Mockito.mock(Principal.class);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        // Text based question
        Question questionIns = new Question(QuestionType.INSERT_THE_WORD, "content", null, 10, 0, USER_IN_DB, 0);
        questionIns = questionRepo.insert(questionIns);
        List<CorrectPoint> correctPointsIns = new ArrayList<>();
        CorrectPoint correctPointIns = new CorrectPoint(questionIns.getQuestionID(), "phraseTex", 10.0, "feedbackTex", new ArrayList<>(), 0, 0);
        correctPointIns = correctPointRepo.insert(correctPointIns);
        Alternative alternativeIns = new Alternative(correctPointIns.getCorrectPointID(), "altTex1", 0);
        alternativeIns = alternativeRepo.insert(alternativeIns);
        correctPointIns.getAlternatives().add(alternativeIns);
        correctPointsIns.add(correctPointIns);

        Answer answerUserIns = new Answer(questionIns.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        List<Inputs> inputsUserIns = new ArrayList<>();
        inputsUserIns.add(new Inputs("phraseTex", 0, -1L, 0));
        Answer answerOtherUserIns = new Answer(questionIns.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        List<Inputs> inputsOtherIns = new ArrayList<>();
        inputsOtherIns.add(new Inputs("altTex1", 0, -1L, 0));

        QuestionAndAnswer questionAndAnswerInsUser = new QuestionAndAnswer(new QuestionAndBase64(null, null, null, questionIns), answerUserIns, inputsUserIns, null, correctPointsIns);
        QuestionAndAnswer questionAndAnswerInsOther = new QuestionAndAnswer(new QuestionAndBase64(null, null, null, questionIns), answerOtherUserIns, inputsOtherIns, null, correctPointsIns);

        List<QuestionAndAnswer> scriptsUser = new ArrayList<>();
        scriptsUser.add(questionAndAnswerInsUser);

        List<QuestionAndAnswer> scriptsOther = new ArrayList<>();
        scriptsOther.add(questionAndAnswerInsOther);

        testController.submitTest(principal, scriptsUser);
        Answer returnedAnswer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(answerUserIns.getQuestionID(), answerUserIns.getAnswererID(), testObj.getTestID());
        assertEquals(10, (int) returnedAnswer.getScore());
        assertEquals("feedbackTex\n", returnedAnswer.getFeedback());

        testController.submitTest(principalOther, scriptsOther);
        returnedAnswer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(answerOtherUserIns.getQuestionID(), answerOtherUserIns.getAnswererID(), testObj.getTestID());
        assertEquals(10, (int) returnedAnswer.getScore());
        assertEquals("feedbackTex\n", returnedAnswer.getFeedback());

        // Check that multiple submissions deletes the first answer
        testObj.setPractice(1);
        testsRepo.insert(testObj);
        scriptsOther.get(0).getAnswer().setAnswerID(-1L);
        testController.submitTest(principalOther, scriptsOther);
        Answer deletionChecker = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(answerOtherUserIns.getQuestionID(), answerOtherUserIns.getAnswererID(), testObj.getTestID());
        assertNotEquals(deletionChecker.getAnswerID(), returnedAnswer.getAnswerID());
        assertEquals(10.0, (double) testResultRepo.selectByTestIDAndStudentID(testObj.getTestID(), OTHER_IN_DB).getTestScore(), 0);

        scriptsOther.get(0).getAnswer().setAnswerID(-1L);
        scriptsOther.get(0).getInputs().get(0).setInputValue("none");
        testController.submitTest(principalOther, scriptsOther);
        assertEquals(0.0, (double) testResultRepo.selectByTestIDAndStudentID(testObj.getTestID(), OTHER_IN_DB).getTestScore(), 0);
    }

    @Test
    @Transactional
    public void testSubmitTestInsIdenticalSolns() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principalOther = Mockito.mock(Principal.class);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        // Text based question
        Question questionIns = new Question(QuestionType.INSERT_THE_WORD, "content", null, 10, 0, USER_IN_DB, 0);
        questionIns = questionRepo.insert(questionIns);

        Answer answerUserIns = new Answer(questionIns.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        List<Inputs> inputsUserIns = new ArrayList<>();
        inputsUserIns.add(new Inputs("answer", 0, -1L, 0));
        Answer answerOtherUserIns = new Answer(questionIns.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        List<Inputs> inputsOtherIns = new ArrayList<>();
        inputsOtherIns.add(new Inputs("answer", 0, -1L, 0));

        QuestionAndAnswer questionAndAnswerInsUser = new QuestionAndAnswer(new QuestionAndBase64(null, null, null, questionIns), answerUserIns, inputsUserIns, null, new ArrayList<>());
        QuestionAndAnswer questionAndAnswerInsOther = new QuestionAndAnswer(new QuestionAndBase64(null, null, null, questionIns), answerOtherUserIns, inputsOtherIns, null, new ArrayList<>());

        List<QuestionAndAnswer> scriptsUser = new ArrayList<>();
        scriptsUser.add(questionAndAnswerInsUser);

        List<QuestionAndAnswer> scriptsOther = new ArrayList<>();
        scriptsOther.add(questionAndAnswerInsOther);

        testController.submitTest(principal, scriptsUser);
        Answer returnedAnswer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(answerUserIns.getQuestionID(), answerUserIns.getAnswererID(), testObj.getTestID());
        returnedAnswer.setScore(10);
        returnedAnswer.setFeedback("feedbackToShowIdenticalSolnWorks");
        answerRepo.insert(returnedAnswer);

        testController.submitTest(principalOther, scriptsOther);
        returnedAnswer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(answerOtherUserIns.getQuestionID(), answerOtherUserIns.getAnswererID(), testObj.getTestID());
        assertEquals(10, (int) returnedAnswer.getScore());
        assertEquals("feedbackToShowIdenticalSolnWorks", returnedAnswer.getFeedback());
    }

    @Test
    @Transactional
    public void testEditTest() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principalOther = Mockito.mock(Principal.class);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        testObj.setPublishGrades(1);
        testObj.setPractice(1);
        testObj.setScheduled(1);
        testObj.setStartDateTime("2018-01-01T11:11:11");
        testObj.setEndDateTime("2018-01-01T12:11:12");
        testObj.setPublishResults(1);
        testObj.setTestTitle("testTitleNew");

        testObj = testController.editTest(principal, testObj);
        assertEquals(1, (int) testObj.getPublishGrades());
        assertEquals(1, (int) testObj.getPractice());
        assertEquals(1, (int) testObj.getPublishResults());
        assertEquals(1, (int) testObj.getScheduled());
        assertEquals("2018-01-01T11:11:00", testObj.getStartDateTime());
        assertEquals("2018-01-01T12:11:00", testObj.getEndDateTime());
        assertEquals("testTitleNew", testObj.getTestTitle());

        assertNull(testController.editTest(principalOther, testObj));
    }

    @Transactional
    @Test
    public void testGetAnsweredTests() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        // Text based question
        Question questionOpt = new Question(QuestionType.MULTIPLE_CHOICE, "content", null, 10, 0, USER_IN_DB, 0);
        questionOpt = questionRepo.insert(questionOpt);
        List<Option> optionsOpt = new ArrayList<>();
        Option optionOpt = new Option(questionOpt.getQuestionID(), "option", -1, "feedbackOpt");
        optionOpt = optionRepo.insert(optionOpt);
        optionsOpt.add(optionOpt);

        Answer answerUserOpt = new Answer(questionOpt.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        List<OptionEntries> optionEntriesUser = new ArrayList<>();
        optionEntriesUser.add(new OptionEntries(optionOpt.getOptionID(), -1L));

        QuestionAndAnswer questionAndAnswerMatUser = new QuestionAndAnswer(new QuestionAndBase64(null, optionsOpt, null, questionOpt), answerUserOpt, null, optionEntriesUser, null);

        List<QuestionAndAnswer> scriptsUser = new ArrayList<>();
        scriptsUser.add(questionAndAnswerMatUser);

        testController.submitTest(principal, scriptsUser);
        assertTrue(testController.getAnsweredTests(principal).contains(testObj.getTestID().intValue()));
    }

    @Transactional
    @Test
    public void testGetQuestionsStudent() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principalOther = Mockito.mock(Principal.class);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        // Text based question
        Question questionIns = new Question(QuestionType.INSERT_THE_WORD, "[[phraseTex]]", null, 10, 0, USER_IN_DB, 0);
        questionIns = questionRepo.insert(questionIns);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionIns.getQuestionID()));
        CorrectPoint correctPointIns = new CorrectPoint(questionIns.getQuestionID(), "phraseTex", 10.0, "feedbackTex", new ArrayList<>(), 0, 0);
        correctPointIns = correctPointRepo.insert(correctPointIns);
        Alternative alternativeIns = new Alternative(correctPointIns.getCorrectPointID(), "altTex1", 0);
        alternativeIns = alternativeRepo.insert(alternativeIns);
        correctPointIns.getAlternatives().add(alternativeIns);

        Question questionOpt = new Question(QuestionType.MULTIPLE_CHOICE, "content", null, 10, 0, USER_IN_DB, 0);
        questionOpt = questionRepo.insert(questionOpt);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionOpt.getQuestionID()));
        Option optionOpt = new Option(questionOpt.getQuestionID(), "option", -1, "feedbackOpt");
        optionRepo.insert(optionOpt);

        Question questionOpt2 = new Question(QuestionType.MULTIPLE_CHOICE, "content", null, 10, 0, USER_IN_DB, 1);
        questionOpt2 = questionRepo.insert(questionOpt2);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionOpt2.getQuestionID()));
        Option optionOpt2 = new Option(questionOpt2.getQuestionID(), "option", -1, "feedbackOpt");
        Option optionOpt3 = new Option(questionOpt2.getQuestionID(), "option", -1, "feedbackOpt3");
        optionRepo.insert(optionOpt2);
        optionRepo.insert(optionOpt3);

        List<QuestionAndAnswer> questionAndAnswer = testController.getQuestionsStudent(principal, testObj.getTestID());
        assertEquals(1, questionAndAnswer.get(0).getInputs().size());
        for (Inputs i : questionAndAnswer.get(0).getInputs()) {
            assertFalse(i.getInputValue().contains("[[phraseTex]]"));
        }
        assertNull(questionAndAnswer.get(0).getCorrectPoints());
        assertEquals(1, questionAndAnswer.get(1).getOptionEntries().size());
        for (Option o : questionAndAnswer.get(1).getQuestion().getOptions()) {
            assertEquals(0, (int) o.getWorthMarks());
            assertEquals("", o.getFeedback());
        }
        assertEquals(2, questionAndAnswer.get(2).getOptionEntries().size());
        for (Option o : questionAndAnswer.get(2).getQuestion().getOptions()) {
            assertEquals(0, (int) o.getWorthMarks());
            assertEquals("", o.getFeedback());
        }
        assertNull(testController.getQuestionsStudent(principalOther, testObj.getTestID()));
    }

    @Transactional
    @Test
    public void testDuplicateQuestion() throws SQLException {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principalOther = Mockito.mock(Principal.class);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        // Text based question
        Question questionIns = new Question(QuestionType.INSERT_THE_WORD, "[[phraseTex]]", null, 10, 0, USER_IN_DB, 0);
        questionIns = questionRepo.insert(questionIns);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionIns.getQuestionID()));
        CorrectPoint correctPointIns = new CorrectPoint(questionIns.getQuestionID(), "phraseTex", 10.0, "feedbackTex", new ArrayList<>(), 0, 0);
        correctPointIns = correctPointRepo.insert(correctPointIns);
        Alternative alternativeIns = new Alternative(correctPointIns.getCorrectPointID(), "altTex1", 0);
        alternativeIns = alternativeRepo.insert(alternativeIns);
        correctPointIns.getAlternatives().add(alternativeIns);
        assertTrue(testController.duplicateQuestion(questionIns.getQuestionID(), principal));

        Question questionOpt = new Question(QuestionType.MULTIPLE_CHOICE, "content", null, 10, 0, USER_IN_DB, 0);
        questionOpt = questionRepo.insert(questionOpt);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionOpt.getQuestionID()));
        Option optionOpt = new Option(questionOpt.getQuestionID(), "option", -1, "feedbackOpt");
        optionRepo.insert(optionOpt);
        assertTrue(testController.duplicateQuestion(questionOpt.getQuestionID(), principal));
        assertFalse(testController.duplicateQuestion(questionOpt.getQuestionID(), principalOther));
    }

    @Transactional
    @Test
    public void testEditTestInvalid() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        testObj = testsRepo.insert(testObj);
        testObj.setEndDateTime("x");
        assertNull(testController.editTest(principal, testObj));
    }

    @Test
    @Transactional
    public void testRemoveQuestionMathLine() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principalOther = Mockito.mock(Principal.class);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        Question question = new Question(QuestionType.TEXT_BASED, "content", null, 10, 0, USER_IN_DB, 0);
        question = questionRepo.insert(question);

        questionMathLine.setQuestionID(question.getQuestionID());
        questionMathLine = questionMathLineRepo.insert(questionMathLine);
        assertFalse(testController.removeQuestionMathLine(questionMathLine.getQuestionMathLineID(), principalOther));
        assertTrue(testController.removeQuestionMathLine(questionMathLine.getQuestionMathLineID(), principal));
        assertNull(questionMathLineRepo.selectByQuestionMathLineID(questionMathLine.getQuestionMathLineID()));
    }

    @Test
    @Transactional
    public void testRemoveCorrectPoint() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principalOther = Mockito.mock(Principal.class);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        Question question = new Question(QuestionType.TEXT_BASED, "content", null, 10, 0, USER_IN_DB, 0);
        question = questionRepo.insert(question);

        correctPoint.setQuestionID(question.getQuestionID());
        correctPoint = correctPointRepo.insert(correctPoint);
        alternative.setCorrectPointID(correctPoint.getCorrectPointID());
        alternative = alternativeRepo.insert(alternative);
        assertFalse(testController.removeCorrectPoint(correctPoint.getCorrectPointID(), principalOther));
        assertTrue(testController.removeCorrectPoint(correctPoint.getCorrectPointID(), principal));
        assertNull(correctPointRepo.selectByCorrectPointID(correctPoint.getCorrectPointID()));
        assertNull(alternativeRepo.selectByAlternativeID(alternative.getAlternativeID()));
    }

    @Test
    @Transactional
    public void testRemoveAlternative() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principalOther = Mockito.mock(Principal.class);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        Question question = new Question(QuestionType.TEXT_BASED, "content", null, 10, 0, USER_IN_DB, 0);
        question = questionRepo.insert(question);

        correctPoint.setQuestionID(question.getQuestionID());
        correctPoint = correctPointRepo.insert(correctPoint);
        alternative.setCorrectPointID(correctPoint.getCorrectPointID());
        alternative = alternativeRepo.insert(alternative);
        assertFalse(testController.removeAlternative(alternative.getAlternativeID(), principalOther));
        assertTrue(testController.removeAlternative(alternative.getAlternativeID(), principal));
        assertNull(alternativeRepo.selectByAlternativeID(alternative.getAlternativeID()));
    }

    @Test
    @Transactional
    public void testRemoveOption() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principalOther = Mockito.mock(Principal.class);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testsRepo.insert(testObj);
        Question question = new Question(QuestionType.TEXT_BASED, "content", null, 10, 0, USER_IN_DB, 0);
        question = questionRepo.insert(question);

        option.setQuestionID(question.getQuestionID());
        option = optionRepo.insert(option);
        assertFalse(testController.removeOption(option.getOptionID(), principalOther));
        assertTrue(testController.removeOption(option.getOptionID(), principal));
        assertNull(optionRepo.selectByOptionID(option.getOptionID()));
    }

    @Test
    @Transactional
    public void testAddTestValid() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        testObj = testController.saveTest(principal, testObj);
        // Only one test in the db for this module
        Tests testObjInDB = testService.primeTestForUserView(testsRepo.selectByModuleID(module.getModuleID()).get(0));
        assertEquals(testObj.toString(), testObjInDB.toString());

        testObj.setPractice(1);
        testObj = testController.saveTest(principal, testObj);
        // Only one test in the db for this module
        testObjInDB = testService.primeTestForUserView(testsRepo.selectByModuleID(module.getModuleID()).get(1));
        assertEquals(testObj.toString(), testObjInDB.toString());
    }

    @Test
    @Transactional
    public void testAddTestInValid() {

        // Title too short
        Tests testToInsertInvalid = testObj;
        testToInsertInvalid.setTestTitle("");
        assertNull(testService.addTest(testToInsertInvalid, USERNAME_IN_DB));

        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0, 0);
        // Title too long
        testToInsertInvalid = testObj;
        StringBuilder tooLong = new StringBuilder();
        for (int loop = 0; loop <= 55; loop++) {
            tooLong.append("x");
        }
        testToInsertInvalid.setTestTitle(tooLong.toString());
        assertNull(testService.addTest(testToInsertInvalid, USERNAME_IN_DB));

        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0, 0);
        // Null user
        testToInsertInvalid = testObj;
        assertNull(testService.addTest(testToInsertInvalid, ""));

        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0, 0);
        // Null module
        testToInsertInvalid = testObj;
        testToInsertInvalid.setModuleID(0L);
        assertNull(testService.addTest(testToInsertInvalid, USERNAME_IN_DB));

        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0, 0);
        // User requesting not tutor
        testToInsertInvalid = testObj;
        assertNull(testService.addTest(testToInsertInvalid, OTHER_USERNAME_IN_DB));

        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0, 0);
        // Invalid start date format
        testToInsertInvalid = testObj;
        testToInsertInvalid.setStartDateTime("X");
        assertNull(testService.addTest(testToInsertInvalid, OTHER_USERNAME_IN_DB));

        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0, 0);
        // Invalid end date format
        testToInsertInvalid = testObj;
        testToInsertInvalid.setEndDateTime("X");
        assertNull(testService.addTest(testToInsertInvalid, OTHER_USERNAME_IN_DB));
    }

    @Test
    @Transactional
    public void testGetByTestIDTutorView() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        // Valid tutor
        assertEquals(testObj.toString(), testController.getByTestIDTutorView(principal, testObj.getTestID()).toString());
        // Invalid tutor
        assertNull(testService.getByTestID(OTHER_USERNAME_IN_DB, testObj.getTestID()));
    }

    @Test
    @Transactional
    public void testNewQuestion() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principal2 = Mockito.mock(Principal.class);
        when(principal2.getName()).thenReturn(OTHER_USERNAME_IN_DB);

        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        CorrectPoint correctPointNew1 = new CorrectPoint(question.getQuestionID(), "phrase1", 10.0, "feedback1", alternatives, 0, 0);
        CorrectPoint correctPointNew2 = new CorrectPoint(question.getQuestionID(), "phrase2", 10.0, "feedback2", alternatives, 0, 0);
        CorrectPoint correctPointNew3 = new CorrectPoint(question.getQuestionID(), "phrase3", 10.0, "feedback3", alternatives, 0, 0);
        CorrectPoint correctPointNew4 = new CorrectPoint(question.getQuestionID(), "phrase4", 10.0, "feedback4", alternatives, 0, 0);

        question.setQuestionContent("[[" + correctPointNew4.getPhrase() + "]] [[" + correctPointNew3.getPhrase() + "]] [[" + correctPointNew2.getPhrase() + "]] [[" + correctPointNew1.getPhrase() + "]] [[" + correctPoint.getPhrase() + "]]");
        correctPoints.add(correctPointNew1);
        correctPoints.add(correctPointNew2);
        correctPoints.add(correctPointNew3);
        correctPoints.add(correctPointNew4);
        // Valid tutor
        TutorQuestionPojo returnedQuestionData = testController.newQuestion(new TutorQuestionPojo(testObj.getTestID(), null, question, options, questionMathLines, correctPoints), principal);
        assertEquals(question.getQuestionContent(), returnedQuestionData.getQuestion().getQuestionContent());
        assertEquals(option.getOptionContent(), returnedQuestionData.getOptions().get(0).getOptionContent());
        assertEquals(correctPoint.getPhrase(), returnedQuestionData.getCorrectPoints().get(0).getPhrase());
        assertEquals(alternative.getAlternativePhrase(), returnedQuestionData.getCorrectPoints().get(0).getAlternatives().get(0).getAlternativePhrase());

        // Valid tutor
        question.setQuestionType(QuestionType.MULTIPLE_CHOICE);
        question.setQuestionID(-1L);
        returnedQuestionData = testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), "01234567891011121314151617181920212223", question, options, questionMathLines, correctPoints), USERNAME_IN_DB, false);
        assertEquals(question.getQuestionContent(), returnedQuestionData.getQuestion().getQuestionContent());
        assertEquals(option.getOptionContent(), returnedQuestionData.getOptions().get(0).getOptionContent());
        assertEquals(correctPoint.getPhrase(), returnedQuestionData.getCorrectPoints().get(0).getPhrase());
        assertEquals(alternative.getAlternativePhrase(), returnedQuestionData.getCorrectPoints().get(0).getAlternatives().get(0).getAlternativePhrase());

        // Valid tutor
        question.setQuestionType(QuestionType.INSERT_THE_WORD);
        question.setQuestionID(-1L);
        returnedQuestionData = testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), "01234567891011121314151617181920212223", question, options, questionMathLines, correctPoints), USERNAME_IN_DB, false);
        assertEquals(question.getQuestionContent(), returnedQuestionData.getQuestion().getQuestionContent());
        assertEquals(option.getOptionContent(), returnedQuestionData.getOptions().get(0).getOptionContent());
        assertEquals(correctPointNew4.getPhrase(), returnedQuestionData.getCorrectPoints().get(0).getPhrase());
        assertEquals(alternative.getAlternativePhrase(), returnedQuestionData.getCorrectPoints().get(0).getAlternatives().get(0).getAlternativePhrase());


        // Invalid tutor
        returnedQuestionData = testController.editQuestion(new TutorQuestionPojo(testObj.getTestID(), null, question, options, questionMathLines, correctPoints), principal2);
        assertNull(returnedQuestionData);
    }

    @Test
    @Transactional
    public void testGetQuestionsByTestIDTutorView() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), null, question, options, new ArrayList<>(), correctPoints), USERNAME_IN_DB, false);

        // Valid tutor
        List<TutorQuestionPojo> returnedQuestionData = testController.getQuestionsByTestIDTutorView(principal, testObj.getTestID());
        assertEquals(question.getQuestionContent(), returnedQuestionData.get(0).getQuestion().getQuestionContent());
        assertEquals(correctPoint.getPhrase(), returnedQuestionData.get(0).getCorrectPoints().get(0).getPhrase());
        assertEquals(alternative.getAlternativePhrase(), returnedQuestionData.get(0).getCorrectPoints().get(0).getAlternatives().get(0).getAlternativePhrase());

        // Invalid tutor
        returnedQuestionData = testService.getQuestionsByTestIDTutorView(OTHER_USERNAME_IN_DB, testObj.getTestID());
        assertNull(returnedQuestionData);
    }

    @Test
    @Transactional
    public void testGetOldQuestions() throws Exception {

        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        // Test added and question added
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        TutorQuestionPojo returnedQuestionOne = testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), null, question, options, questionMathLines, correctPoints), USERNAME_IN_DB, false);
        question.setQuestionID(-1L);
        options.get(0).setOptionID(-1L);
        correctPoints.get(0).setCorrectPointID(-1L);
        correctPoints.get(0).getAlternatives().get(0).setAlternativeID(-1L);
        testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), null, question, options, new ArrayList<>(), correctPoints), USERNAME_IN_DB, false);

        // Test set back to make it insertable not updatable
        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0, 0);
        // New test added so old questions can be called
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        testController.addExistingQuestion(returnedQuestionOne.getQuestion().getQuestionID(), testObj.getTestID(), principal);

        // The question added to previous test by user should appear in their old questions list
        List<TutorQuestionPojo> returnedQuestionData = testController.getOldQuestions(principal, testObj.getTestID());
        assertEquals(question.getQuestionContent(), returnedQuestionData.get(0).getQuestion().getQuestionContent());
        assertEquals(correctPoint.getPhrase(), returnedQuestionData.get(0).getCorrectPoints().get(0).getPhrase());
        assertEquals(alternative.getAlternativePhrase(), returnedQuestionData.get(0).getCorrectPoints().get(0).getAlternatives().get(0).getAlternativePhrase());
    }

    @Test
    @Transactional
    public void testAddExistingQuestion() throws Exception {

        // Test added and question added
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        TutorQuestionPojo returnedQuestionOne = testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), null, question, options, questionMathLines, correctPoints), USERNAME_IN_DB, false);

        // Test set back to make it insertable not updatable
        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0, 0);
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
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        // Valid tutor
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        Boolean check = testController.deleteTest(testObj.getTestID(), principal);
        assertTrue(check);
        // Invalid tutor
        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0, 0);
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        check = testService.deleteTest(testObj.getTestID(), OTHER_USERNAME_IN_DB);
        assertFalse(check);

    }

    @Test
    @Transactional
    public void testScheduleTest() throws Exception {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        // Valid tutor
        testObj = testService.addTest(testObj, USERNAME_IN_DB);

        // schedule
        Boolean check = testController.scheduleTest(testObj.getTestID(), principal);
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
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        // Test added and question added
        testObj = testService.addTest(testObj, USERNAME_IN_DB);
        TutorQuestionPojo returnedQuestionOne = testService.newQuestion(new TutorQuestionPojo(testObj.getTestID(), null, question, options, questionMathLines, correctPoints), USERNAME_IN_DB, false);

        // Invalid tutor
        Boolean check = testService.removeQuestionFromTest(testObj.getTestID(), returnedQuestionOne.getQuestion().getQuestionID(), OTHER_USERNAME_IN_DB);
        assertFalse(check);

        // Valid tutor
        check = testController.removeQuestionFromTest(testObj.getTestID(), returnedQuestionOne.getQuestion().getQuestionID(), principal);
        assertTrue(check);
    }
}
