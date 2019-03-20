package com.pgault04.services;

import com.pgault04.controller.MarkingController;
import com.pgault04.entities.*;
import com.pgault04.pojos.AnswerData;
import com.pgault04.pojos.MarkerAndReassigned;
import com.pgault04.pojos.MarkerWithChart;
import com.pgault04.pojos.ResultChartPojo;
import com.pgault04.repositories.*;
import com.pgault04.utilities.BlobUtil;
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
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author Paul Gault - 40126005
 * @since Jan 2019
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMarkingService {

    private static final String USERNAME_IN_DB = "pgault04@qub.ac.uk";
    private static final String OTHER_USERNAME_IN_DB = "richard.gault@qub.ac.uk";
    private static final long USER_IN_DB = 1L;
    private static final long OTHER_IN_DB = 2L;

    Principal principal = Mockito.mock(Principal.class);
    Principal principalOther = Mockito.mock(Principal.class);

    @Autowired
    MarkingController markingController;
    @Autowired
    AlternativeRepo alternativeRepo;
    @Autowired
    TestsRepo testsRepo;
    @Autowired
    ModuleRepo moduleRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ModuleAssociationRepo moduleAssociationRepo;
    @Autowired
    QuestionRepo questionRepo;
    @Autowired
    AnswerRepo answerRepo;
    @Autowired
    OptionRepo optionRepo;
    @Autowired
    InputsRepo inputsRepo;
    @Autowired
    OptionEntriesRepo optionEntriesRepo;
    @Autowired
    MarkingService markingService;
    @Autowired
    TestResultRepo testResultRepo;
    @Autowired
    TestQuestionRepo testQuestionRepo;
    @Autowired
    CorrectPointRepo correctPointRepo;

    private Tests testObj;
    private Module module;
    private Question question;
    private ModuleAssociation modAssoc1, modAssoc2;

    @Before
    @Transactional
    public void setUp() throws Exception {
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        when(principalOther.getName()).thenReturn(OTHER_USERNAME_IN_DB);
        String commencementDate = "2018-09-01";
        String endDate = "2018-09-01";
        module = new Module("module", "description", 1L, commencementDate, endDate, 1);
        module = moduleRepo.insert(module);
        modAssoc1 = moduleAssociationRepo.insert(new ModuleAssociation(module.getModuleID(), USER_IN_DB, AssociationType.TUTOR));
        modAssoc2 = moduleAssociationRepo.insert(new ModuleAssociation(module.getModuleID(), OTHER_IN_DB, AssociationType.TEACHING_ASSISTANT));
        testObj = new Tests(module.getModuleID(), "Test Title", "2018-01-01T10:00:00", "2018-01-01T11:00:00", 0, 0, 0, 0);
        question = new Question(1L, "questionContent", null, 10, 0, USER_IN_DB, 0);
        questionRepo.insert(question);
    }

    @Transactional
    @Test
    public void testGetMarkersData() {
        testsRepo.insert(testObj);
        Answer answerTutorMarked = new Answer(question.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "phraseTex", 0, "", 1, 0);
        answerRepo.insert(answerTutorMarked);
        Answer answerTeachingAssistantUnmarked = new Answer(question.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "altTex1", 0, "", 0, 0);
        answerRepo.insert(answerTeachingAssistantUnmarked);

        MarkerWithChart markerWithChart = markingController.getMarkersData(principal, testObj.getTestID());

        assertEquals(testObj.getTestID(), markerWithChart.getMarkers().get(0).getTest().getTestID());
        assertEquals(userRepo.selectByUserID(USER_IN_DB).toString(), markerWithChart.getMarkers().get(0).getMarker().toString());
        assertEquals("Tutor", markerWithChart.getMarkers().get(0).getMarkerType());
        assertEquals(1, (int) markerWithChart.getMarkers().get(0).getUnmarked());
        assertEquals(0, (int) markerWithChart.getMarkers().get(0).getMarked());

        assertEquals(testObj.getTestID(), markerWithChart.getMarkers().get(1).getTest().getTestID());
        assertEquals(userRepo.selectByUserID(OTHER_IN_DB).toString(), markerWithChart.getMarkers().get(1).getMarker().toString());
        assertEquals("Assistant", markerWithChart.getMarkers().get(1).getMarkerType());
        assertEquals(0, (int) markerWithChart.getMarkers().get(1).getUnmarked());
        assertEquals(1, (int) markerWithChart.getMarkers().get(1).getMarked());

        assertNull(markingController.getMarkersData(principalOther, testObj.getTestID()));
    }

    @Transactional
    @Test
    public void testEditFeedbackAndEditScore() {
        testsRepo.insert(testObj);

        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "content", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        Answer answerUserTex = new Answer(questionTex.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "correctAnswer", 10, "correctFeedback", 0, 0);
        answerUserTex = answerRepo.insert(answerUserTex);
        Answer answerOtherUserTex = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "correctAnswer", 0, "", 0, 0);
        answerOtherUserTex = answerRepo.insert(answerOtherUserTex);
        markingController.editFeedback(principalOther, answerUserTex);
        markingController.editScore(principalOther, answerUserTex);
        answerOtherUserTex = answerRepo.selectByAnswerID(answerOtherUserTex.getAnswerID());
        assertEquals(answerUserTex.getScore(), answerOtherUserTex.getScore());
        assertEquals(answerUserTex.getFeedback(), answerOtherUserTex.getFeedback());

        // Math
        Question questionMat = new Question(QuestionType.TEXT_MATH, "content", null, 10, 0, USER_IN_DB, 0);
        questionMat = questionRepo.insert(questionMat);
        Answer answerUserMat = new Answer(questionMat.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "", 10, "correct", 0, 0);
        answerUserMat = answerRepo.insert(answerUserMat);
        inputsRepo.insert(new Inputs("correct", 0, answerUserMat.getAnswerID(), 1));
        Answer answerOtherUserMat = new Answer(questionMat.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "", 0, "", 0, 0);
        answerOtherUserMat = answerRepo.insert(answerOtherUserMat);
        inputsRepo.insert(new Inputs("correct", 0, answerOtherUserMat.getAnswerID(), 1));
        markingController.editFeedback(principal, answerUserMat);
        markingController.editScore(principal, answerUserMat);
        answerOtherUserMat = answerRepo.selectByAnswerID(answerOtherUserMat.getAnswerID());
        assertEquals(answerUserMat.getScore(), answerOtherUserMat.getScore());
        assertEquals(answerUserMat.getFeedback(), answerOtherUserMat.getFeedback());

        // Multiple Choice
        Question questionOpt = new Question(QuestionType.MULTIPLE_CHOICE, "content", null, 10, 0, USER_IN_DB, 0);
        questionOpt = questionRepo.insert(questionOpt);
        Option optionOpt = optionRepo.insert(new Option(questionOpt.getQuestionID(), "option", -1, "feedbackOpt"));
        Answer answerUserOpt = new Answer(questionOpt.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 10, "correct", 0, 0);
        answerUserOpt = answerRepo.insert(answerUserOpt);
        optionEntriesRepo.insert(new OptionEntries(optionOpt.getOptionID(), answerUserOpt.getAnswerID()));
        Answer answerOtherUserOpt = new Answer(questionOpt.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        answerOtherUserOpt = answerRepo.insert(answerOtherUserOpt);
        optionEntriesRepo.insert(new OptionEntries(optionOpt.getOptionID(), answerOtherUserOpt.getAnswerID()));
        markingController.editFeedback(principal, answerUserOpt);
        markingController.editScore(principal, answerUserOpt);
        answerOtherUserOpt = answerRepo.selectByAnswerID(answerOtherUserOpt.getAnswerID());
        assertEquals(answerUserOpt.getScore(), answerOtherUserOpt.getScore());
        assertEquals(answerUserOpt.getFeedback(), answerOtherUserOpt.getFeedback());

        // Insert the word
        Question questionIns = new Question(QuestionType.INSERT_THE_WORD, "content", null, 10, 0, USER_IN_DB, 0);
        questionIns = questionRepo.insert(questionIns);
        Answer answerUserIns = new Answer(questionIns.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 10, "feedback", 0, 0);
        answerUserIns = answerRepo.insert(answerUserIns);
        inputsRepo.insert(new Inputs("correct", 0, answerUserIns.getAnswerID(), 0));
        Answer answerOtherUserIns = new Answer(questionIns.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        answerOtherUserIns = answerRepo.insert(answerOtherUserIns);
        inputsRepo.insert(new Inputs("correct", 0, answerOtherUserIns.getAnswerID(), 0));
        markingController.editFeedback(principal, answerUserIns);
        markingController.editScore(principal, answerUserIns);
        answerOtherUserIns = answerRepo.selectByAnswerID(answerOtherUserIns.getAnswerID());
        assertEquals(answerUserIns.getScore(), answerOtherUserIns.getScore());
        assertEquals(answerUserIns.getFeedback(), answerOtherUserIns.getFeedback());
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testEditScoreAndFeedbackInvalid() {
        testsRepo.insert(testObj);
        markingController.editAnswer(principalOther, new Answer(1L, USER_IN_DB, USER_IN_DB, testObj.getTestID(), null, 10, "correct", 0, 0));
    }

    @Transactional
    @Test
    public void testApprove() {
        testsRepo.insert(testObj);

        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "content", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        Answer answerUserTex = new Answer(questionTex.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "correctAnswer", 10, "correctFeedback", 0, 0);
        answerUserTex = answerRepo.insert(answerUserTex);

        answerUserTex = markingController.approve(principalOther, answerUserTex.getAnswerID());
        assertEquals(1, (int) answerUserTex.getMarkerApproved());
        answerUserTex = markingController.approve(principal, answerUserTex.getAnswerID());
        assertEquals(1, (int) answerUserTex.getTutorApproved());

    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testApproveInvalid() {
        testsRepo.insert(testObj);
        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "content", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        Answer answerUserTex = new Answer(questionTex.getQuestionID(), USER_IN_DB, USER_IN_DB, testObj.getTestID(), "correctAnswer", 10, "correctFeedback", 0, 0);
        answerUserTex = answerRepo.insert(answerUserTex);
        markingController.approve(principalOther, answerUserTex.getAnswerID());
    }

    @Transactional
    @Test
    public void testCalculateStandardDeviation() {
        Answer answer1 = new Answer(1L, 1L, 1L, 1L, "content", 10, "feedback", 0, 0);
        Answer answer2 = new Answer(1L, 1L, 1L, 1L, "content", 33, "feedback", 0, 0);
        Answer answer3 = new Answer(1L, 1L, 1L, 1L, "content", 41, "feedback", 0, 0);
        Answer answer4 = new Answer(1L, 1L, 1L, 1L, "content", 67, "feedback", 0, 0);
        Answer answer5 = new Answer(1L, 1L, 1L, 1L, "content", 91, "feedback", 0, 0);

        List<Answer> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);
        answers.add(answer5);
        assertEquals(28.025702488965376, markingService.calculateStandardDeviation(answers), 0);
    }

    @Transactional
    @Test
    public void testPublishGrades() throws SQLException, IllegalAccessException {
        testsRepo.insert(testObj);
        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "content", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        Answer answerUserTex = new Answer(questionTex.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "correctAnswer", 10, "correctFeedback", 0, 0);
        answerRepo.insert(answerUserTex);
        assertTrue(markingController.publishGrades(principal, testObj.getTestID()));
        testObj = testsRepo.selectByTestID(testObj.getTestID());
        assertEquals(1, (int) testObj.getPublishGrades());
        assertEquals(10.0, (double) testResultRepo.selectByTestIDAndStudentID(testObj.getTestID(), USER_IN_DB).getTestScore(), 0);
        answerUserTex.setScore(11);
        answerRepo.insert(answerUserTex);
        assertTrue(markingController.publishGrades(principal, testObj.getTestID()));
        testObj = testsRepo.selectByTestID(testObj.getTestID());
        assertEquals(0, (int) testObj.getPublishGrades());
        assertEquals(11.0, (double) testResultRepo.selectByTestIDAndStudentID(testObj.getTestID(), USER_IN_DB).getTestScore(), 0);
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testPublishGradesInvalid() throws SQLException, IllegalAccessException {
        testsRepo.insert(testObj);
        moduleAssociationRepo.delete(modAssoc2.getAssociationID());
        markingController.publishGrades(principalOther, testObj.getTestID());
    }

    @Transactional
    @Test
    public void testPublishResults() throws SQLException, IllegalAccessException {
        testsRepo.insert(testObj);
        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "content", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        Answer answerUserTex = new Answer(questionTex.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), "correctAnswer", 10, "correctFeedback", 0, 0);
        answerRepo.insert(answerUserTex);
        assertTrue(markingController.publishResults(principal, testObj.getTestID()));
        testObj = testsRepo.selectByTestID(testObj.getTestID());
        assertEquals(1, (int) testObj.getPublishResults());
        assertEquals(10.0, (double) testResultRepo.selectByTestIDAndStudentID(testObj.getTestID(), USER_IN_DB).getTestScore(), 0);
        answerUserTex.setScore(11);
        answerRepo.insert(answerUserTex);
        assertTrue(markingController.publishResults(principal, testObj.getTestID()));
        testObj = testsRepo.selectByTestID(testObj.getTestID());
        assertEquals(0, (int) testObj.getPublishResults());
        assertEquals(11.0, (double) testResultRepo.selectByTestIDAndStudentID(testObj.getTestID(), USER_IN_DB).getTestScore(), 0);
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testPublishResultsInvalid() throws SQLException, IllegalAccessException {
        testsRepo.insert(testObj);
        moduleAssociationRepo.delete(modAssoc2.getAssociationID());
        markingController.publishResults(principalOther, testObj.getTestID());
    }

    @Transactional
    @Test
    public void testGetScriptsTutor() throws SQLException, IllegalAccessException {
        testsRepo.insert(testObj);
        // Text
        Question questionOpt = new Question(QuestionType.MULTIPLE_CHOICE, "content", BlobUtil.baseToBlob("01234567891011121314151617181920212223"), 10, 0, USER_IN_DB, 0);
        questionOpt = questionRepo.insert(questionOpt);
        Option optionOpt = new Option(questionOpt.getQuestionID(), "option", -1, "feedbackOpt");
        optionOpt = optionRepo.insert(optionOpt);

        Answer answerUserOpt = new Answer(questionOpt.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        answerUserOpt = answerRepo.insert(answerUserOpt);
        optionEntriesRepo.insert(new OptionEntries(optionOpt.getOptionID(), answerUserOpt.getAnswerID()));

        List<AnswerData> scripts = markingController.getScriptsTutor(principal, testObj.getTestID());
        assertNotNull(scripts.get(0).getQuestionAndAnswer().getQuestion().getBase64());
        assertEquals(1, scripts.get(0).getQuestionAndAnswer().getOptionEntries().size());
        assertEquals(1, scripts.get(0).getQuestionAndAnswer().getQuestion().getOptions().size());
        assertNull(scripts.get(0).getQuestionAndAnswer().getQuestion().getQuestion().getQuestionFigure());
    }

    @Transactional
    @Test (expected = IllegalAccessException.class)
    public void testGetScriptsTutorInvalid() throws SQLException, IllegalAccessException {
        testsRepo.insert(testObj);
        // Text
        Question questionOpt = new Question(QuestionType.MULTIPLE_CHOICE, "content", BlobUtil.baseToBlob("01234567891011121314151617181920212223"), 10, 0, USER_IN_DB, 0);
        questionOpt = questionRepo.insert(questionOpt);
        Option optionOpt = new Option(questionOpt.getQuestionID(), "option", -1, "feedbackOpt");
        optionOpt = optionRepo.insert(optionOpt);

        Answer answerUserOpt = new Answer(questionOpt.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        answerUserOpt = answerRepo.insert(answerUserOpt);
        optionEntriesRepo.insert(new OptionEntries(optionOpt.getOptionID(), answerUserOpt.getAnswerID()));

        markingController.getScriptsTutor(principalOther, testObj.getTestID());
    }

    @Transactional
    @Test
    public void testGetScriptsMarker() throws SQLException {
        testsRepo.insert(testObj);
        // Text
        Question questionOpt = new Question(QuestionType.MULTIPLE_CHOICE, "content", BlobUtil.baseToBlob("01234567891011121314151617181920212223"), 10, 0, USER_IN_DB, 0);
        questionOpt = questionRepo.insert(questionOpt);
        Option optionOpt = new Option(questionOpt.getQuestionID(), "option", -1, "feedbackOpt");
        optionOpt = optionRepo.insert(optionOpt);

        Answer answerUserOpt = new Answer(questionOpt.getQuestionID(), USER_IN_DB, OTHER_IN_DB, testObj.getTestID(), null, 0, "", 0, 0);
        answerUserOpt = answerRepo.insert(answerUserOpt);
        optionEntriesRepo.insert(new OptionEntries(optionOpt.getOptionID(), answerUserOpt.getAnswerID()));

        List<AnswerData> scripts = markingController.getScriptsMarker(principalOther, testObj.getTestID());
        assertNotNull(scripts.get(0).getQuestionAndAnswer().getQuestion().getBase64());
        assertEquals(1, scripts.get(0).getQuestionAndAnswer().getOptionEntries().size());
        assertEquals(1, scripts.get(0).getQuestionAndAnswer().getQuestion().getOptions().size());
        assertNull(scripts.get(0).getQuestionAndAnswer().getQuestion().getQuestion().getQuestionFigure());
    }

    @Transactional
    @Test
    public void testReassignAnswers() {
        testsRepo.insert(testObj);
        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "content", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        Answer answer1 = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 10, "feedback", 0, 0);
        answerRepo.insert(answer1);
        Answer answer2 = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 33, "feedback", 0, 0);
        answerRepo.insert(answer2);
        Answer answer3 = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 41, "feedback", 0, 0);
        answerRepo.insert(answer3);

        List<MarkerAndReassigned> reassignedList = new ArrayList<>();
        MarkerAndReassigned markerAndReassigned = new MarkerAndReassigned(OTHER_IN_DB, USER_IN_DB, 0L, 100L);
        reassignedList.add(markerAndReassigned);
        assertEquals(0, (int) answerRepo.selectByMarkerID(OTHER_IN_DB).stream().filter(a -> a.getTestID().equals(testObj.getTestID())).count());
        assertEquals(3, (int) answerRepo.selectByMarkerID(USER_IN_DB).stream().filter(a -> a.getTestID().equals(testObj.getTestID())).count());
        assertTrue(markingController.reassignAnswers(principal, testObj.getTestID(),reassignedList));
        assertEquals(3, (int) answerRepo.selectByMarkerID(OTHER_IN_DB).stream().filter(a -> a.getTestID().equals(testObj.getTestID())).count());
        assertEquals(0, (int) answerRepo.selectByMarkerID(USER_IN_DB).stream().filter(a -> a.getTestID().equals(testObj.getTestID())).count());

        markerAndReassigned.setSpecifyQuestion(questionTex.getQuestionID());
        markerAndReassigned.setPreviousMarkerID(OTHER_IN_DB);
        markerAndReassigned.setMarkerID(USER_IN_DB);
        assertEquals(0, (int) answerRepo.selectByMarkerID(USER_IN_DB).stream().filter(a -> a.getTestID().equals(testObj.getTestID())).count());
        assertEquals(3, (int) answerRepo.selectByMarkerID(OTHER_IN_DB).stream().filter(a -> a.getTestID().equals(testObj.getTestID())).count());
        markingController.reassignAnswers(principal, testObj.getTestID(),reassignedList);
        assertEquals(3, (int) answerRepo.selectByMarkerID(USER_IN_DB).stream().filter(a -> a.getTestID().equals(testObj.getTestID())).count());
        assertEquals(0, (int) answerRepo.selectByMarkerID(OTHER_IN_DB).stream().filter(a -> a.getTestID().equals(testObj.getTestID())).count());

        assertNull(markingController.reassignAnswers(principalOther, testObj.getTestID(), reassignedList));
    }

    @Transactional
    @Test
    public void testGenerateResultChart() {
        testsRepo.insert(testObj);
        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "content", null, 50, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionTex.getQuestionID()));
        Question questionTex2 = new Question(QuestionType.TEXT_BASED, "content", null, 50, 0, USER_IN_DB, 0);
        questionTex2 = questionRepo.insert(questionTex2);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionTex2.getQuestionID()));
        Answer answer1 = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 10, "feedback", 0, 0);
        answerRepo.insert(answer1);
        Answer answer2 = new Answer(questionTex.getQuestionID(), USER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 33, "feedback", 0, 0);
        answerRepo.insert(answer2);
        Answer answer3 = new Answer(questionTex2.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 31, "feedback", 0, 0);
        answerRepo.insert(answer3);
        Answer answer4 = new Answer(questionTex2.getQuestionID(), USER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 41, "feedback", 0, 0);
        answerRepo.insert(answer4);

        ResultChartPojo resultChartPojo = markingController.getResultChart(principal, testObj.getTestID());
        assertEquals(2, resultChartPojo.getColors().size());
        assertEquals("#28a745", resultChartPojo.getColors().get(0));
        assertEquals("#dc3545", resultChartPojo.getColors().get(1));
        assertEquals(2, resultChartPojo.getScores().size());
        assertEquals(74, (int) resultChartPojo.getScores().get(0));
        assertEquals(41, (int) resultChartPojo.getScores().get(1));
        assertEquals(2, resultChartPojo.getLabels().size());
        assertEquals("Paul Gault", resultChartPojo.getLabels().get(0));
        assertEquals("Richard Gault", resultChartPojo.getLabels().get(1));
        assertEquals(57, (int) resultChartPojo.getClassAverage());

        answer1.setScore(36);
        answerRepo.insert(answer1);
        resultChartPojo = markingController.getResultChart(principal, testObj.getTestID());
        assertEquals(67, (int) resultChartPojo.getScores().get(1));
        assertEquals("#ffc107", resultChartPojo.getColors().get(1));
        assertNull(markingController.getResultChart(principalOther, testObj.getTestID()));
    }

    @Transactional
    @Test
    public void testGenerateQuestionResultChart() {
        testsRepo.insert(testObj);
        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "question 1", null, 50, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionTex.getQuestionID()));
        Question questionTex2 = new Question(QuestionType.TEXT_BASED, "question 2", null, 50, 0, USER_IN_DB, 0);
        questionTex2 = questionRepo.insert(questionTex2);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionTex2.getQuestionID()));
        Answer answer1 = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 10, "feedback", 0, 0);
        answerRepo.insert(answer1);
        Answer answer2 = new Answer(questionTex.getQuestionID(), USER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 33, "feedback", 0, 0);
        answerRepo.insert(answer2);
        Answer answer3 = new Answer(questionTex2.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 31, "feedback", 0, 0);
        answerRepo.insert(answer3);
        Answer answer4 = new Answer(questionTex2.getQuestionID(), USER_IN_DB, USER_IN_DB, testObj.getTestID(), "content", 41, "feedback", 0, 0);
        answerRepo.insert(answer4);

        List<ResultChartPojo> resultChartPojo = markingController.getQuestionResultChart(principal, testObj.getTestID());
        assertEquals(2, resultChartPojo.size());
        assertEquals("1. question 1", resultChartPojo.get(0).getLabels().get(0));
        assertEquals("2. question 2", resultChartPojo.get(0).getLabels().get(1));
        assertEquals(50, (int) resultChartPojo.get(0).getScores().get(0));
        assertEquals(50, (int) resultChartPojo.get(0).getScores().get(1));
        assertEquals(50, (int) resultChartPojo.get(0).getClassAverage());
        assertEquals(0, resultChartPojo.get(0).getColors().size());

        assertEquals("1. question 1", resultChartPojo.get(1).getLabels().get(0));
        assertEquals("2. question 2", resultChartPojo.get(1).getLabels().get(1));
        assertEquals(21, (int) resultChartPojo.get(1).getScores().get(0));
        assertEquals(36, (int) resultChartPojo.get(1).getScores().get(1));
        assertEquals(50, (int) resultChartPojo.get(1).getClassAverage());
        assertEquals(0, resultChartPojo.get(1).getColors().size());

        assertNull(markingController.getQuestionResultChart(principalOther, testObj.getTestID()));
    }

    @Transactional
    @Test
    public void testAddCorrectPoint() throws Exception {
        testsRepo.insert(testObj);
        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "question 1", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionTex.getQuestionID()));
        Answer answer1 = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "correctPoint", 0, "", 0, 0);
        answerRepo.insert(answer1);
        Answer answer2 = new Answer(questionTex.getQuestionID(), USER_IN_DB, USER_IN_DB, testObj.getTestID(), "alt", 0, "", 0, 0);
        answerRepo.insert(answer2);

        CorrectPoint correctPoint = new CorrectPoint(questionTex.getQuestionID(), "correctPoint", 10.0, "Correct!!", new ArrayList<>(), 0, 0);
        Alternative alternative = new Alternative(correctPoint.getCorrectPointID(), "alt", 0);
        correctPoint.getAlternatives().add(alternative);
        markingController.addCorrectPoint(principal, correctPoint, testObj.getTestID());

        answer1 = answerRepo.selectByAnswerID(answer1.getAnswerID());
        answer2 = answerRepo.selectByAnswerID(answer2.getAnswerID());
        assertEquals(10, (int) answer1.getScore());
        assertEquals("\nCorrect!!", answer1.getFeedback());
        assertEquals(10, (int) answer2.getScore());
        assertEquals("\nCorrect!!", answer2.getFeedback());

        Question questionMath = new Question(QuestionType.TEXT_MATH, "question 1", null, 10, 0, USER_IN_DB, 0);
        questionMath = questionRepo.insert(questionMath);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionMath.getQuestionID()));
        Answer answer1Math = new Answer(questionMath.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "correctPoint", 0, "", 0, 0);
        answerRepo.insert(answer1Math);
        inputsRepo.insert(new Inputs("correctPoint", 0, answer1Math.getAnswerID(), 1));
        Answer answer2Math = new Answer(questionMath.getQuestionID(), USER_IN_DB, USER_IN_DB, testObj.getTestID(), "alt", 0, "", 0, 0);
        answerRepo.insert(answer2Math);
        inputsRepo.insert(new Inputs("alt", 0, answer2Math.getAnswerID(), 1));

        CorrectPoint correctPointMath = new CorrectPoint(questionMath.getQuestionID(), "correctPoint", 10.0, "Correct!!", new ArrayList<>(), 0, 1);
        Alternative alternativeMath = new Alternative(correctPointMath.getCorrectPointID(), "alt", 1);
        correctPointMath.getAlternatives().add(alternativeMath);
        markingController.addCorrectPoint(principal, correctPointMath, testObj.getTestID());

        answer1Math = answerRepo.selectByAnswerID(answer1Math.getAnswerID());
        answer2Math = answerRepo.selectByAnswerID(answer2Math.getAnswerID());
        assertEquals(10, (int) answer1Math.getScore());
        assertEquals("\nCorrect!!", answer1Math.getFeedback());
        assertEquals(10, (int) answer2Math.getScore());
        assertEquals("\nCorrect!!", answer2Math.getFeedback());
    }

    @Transactional
    @Test (expected = IllegalArgumentException.class)
    public void testAddCorrectPointInvalid() throws Exception {
        testsRepo.insert(testObj);
        Question questionTex = new Question(QuestionType.TEXT_BASED, "question 1", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        CorrectPoint correctPoint = new CorrectPoint(questionTex.getQuestionID(), "correctPoint", 10.0, "Correct!!", new ArrayList<>(), 0, 0);
        markingController.addCorrectPoint(principalOther, correctPoint
                , testObj.getTestID());
    }

    @Transactional
    @Test
    public void testAddAlternative() throws Exception {
        testsRepo.insert(testObj);
        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "question 1", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionTex.getQuestionID()));
        Answer answer1 = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "correctPoint", 0, "", 0, 0);
        answerRepo.insert(answer1);
        Answer answer2 = new Answer(questionTex.getQuestionID(), USER_IN_DB, USER_IN_DB, testObj.getTestID(), "alt", 0, "", 0, 0);
        answerRepo.insert(answer2);
        Answer answer3 = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "", 0, "", 0, 0);
        answerRepo.insert(answer3);
        Answer answer4 = new Answer(questionTex.getQuestionID(), USER_IN_DB, USER_IN_DB, testObj.getTestID(), "alt1", 0, "", 0, 0);
        answerRepo.insert(answer4);

        CorrectPoint correctPoint = new CorrectPoint(questionTex.getQuestionID(), "correctPoint", 10.0, "Correct!!", new ArrayList<>(), 0, 0);
        correctPoint = correctPointRepo.insert(correctPoint);
        Alternative alternative1 = new Alternative(correctPoint.getCorrectPointID(), "alt1", 0);
        alternativeRepo.insert(alternative1);
        Alternative alternative = new Alternative(correctPoint.getCorrectPointID(), "alt", 0);
        markingController.addAlternative(principal, alternative, testObj.getTestID());

        answer1 = answerRepo.selectByAnswerID(answer1.getAnswerID());
        answer2 = answerRepo.selectByAnswerID(answer2.getAnswerID());
        assertEquals(0, (int) answer1.getScore());
        assertEquals("", answer1.getFeedback());
        assertEquals(10, (int) answer2.getScore());
        assertEquals("\nCorrect!!", answer2.getFeedback());

        Question questionMath = new Question(QuestionType.TEXT_MATH, "question 1", null, 10, 0, USER_IN_DB, 0);
        questionMath = questionRepo.insert(questionMath);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionMath.getQuestionID()));
        Answer answer1Math = new Answer(questionMath.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "correctPoint", 0, "", 0, 0);
        answerRepo.insert(answer1Math);
        inputsRepo.insert(new Inputs("correctPoint", 0, answer1Math.getAnswerID(), 1));
        Answer answer2Math = new Answer(questionMath.getQuestionID(), USER_IN_DB, USER_IN_DB, testObj.getTestID(), "alt", 0, "", 0, 0);
        answerRepo.insert(answer2Math);
        inputsRepo.insert(new Inputs("alt", 0, answer2Math.getAnswerID(), 1));
        Answer answer3Math = new Answer(questionMath.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "", 0, "", 0, 0);
        answerRepo.insert(answer3Math);
        inputsRepo.insert(new Inputs("correctPoint", 0, answer3Math.getAnswerID(), 1));
        Answer answer4Math = new Answer(questionMath.getQuestionID(), USER_IN_DB, USER_IN_DB, testObj.getTestID(), "alt1", 0, "", 0, 0);
        answerRepo.insert(answer4Math);
        inputsRepo.insert(new Inputs("alt", 0, answer4Math.getAnswerID(), 1));

        CorrectPoint correctPointMath = new CorrectPoint(questionMath.getQuestionID(), "correctPoint", 10.0, "Correct!!", new ArrayList<>(), 0, 1);
        correctPointMath = correctPointRepo.insert(correctPointMath);
        Alternative alternative1Math = new Alternative(correctPointMath.getCorrectPointID(), "alt1", 0);
        alternativeRepo.insert(alternative1Math);
        Alternative alternativeMath = new Alternative(correctPointMath.getCorrectPointID(), "alt", 1);
        markingController.addAlternative(principal, alternativeMath, testObj.getTestID());

        answer1Math = answerRepo.selectByAnswerID(answer1Math.getAnswerID());
        answer2Math = answerRepo.selectByAnswerID(answer2Math.getAnswerID());
        assertEquals(0, (int) answer1Math.getScore());
        assertEquals("", answer1Math.getFeedback());
        assertEquals(10, (int) answer2Math.getScore());
        assertEquals("\nCorrect!!", answer2Math.getFeedback());
    }

    @Transactional
    @Test (expected = IllegalArgumentException.class)
    public void testAddAlternativeInvalid() throws Exception {
        testsRepo.insert(testObj);
        Question questionTex = new Question(QuestionType.TEXT_BASED, "question 1", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        CorrectPoint correctPoint = new CorrectPoint(questionTex.getQuestionID(), "correctPoint", 10.0, "Correct!!", new ArrayList<>(), 0, 0);
        correctPoint = correctPointRepo.insert(correctPoint);
        Alternative alternative = new Alternative(correctPoint.getCorrectPointID(), "alt", 0);
        markingController.addAlternative(principalOther, alternative
                , testObj.getTestID());
    }

    @Transactional
    @Test
    public void testRemoveAlternative() throws Exception {
        testsRepo.insert(testObj);
        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "question 1", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionTex.getQuestionID()));
        Answer answer1 = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "correctPoint", 10, "Correct!!", 0, 0);
        answerRepo.insert(answer1);
        Answer answer2 = new Answer(questionTex.getQuestionID(), USER_IN_DB, USER_IN_DB, testObj.getTestID(), "alt1", 10, "Correct!!", 0, 0);
        answerRepo.insert(answer2);

        CorrectPoint correctPoint = new CorrectPoint(questionTex.getQuestionID(), "correctPoint", 10.0, "Correct!!", new ArrayList<>(), 0, 0);
        correctPoint = correctPointRepo.insert(correctPoint);
        Alternative alternative1 = new Alternative(correctPoint.getCorrectPointID(), "alt1", 0);
        alternativeRepo.insert(alternative1);
        markingController.removeAlternative(principal, alternative1.getAlternativeID(), testObj.getTestID());

        answer1 = answerRepo.selectByAnswerID(answer1.getAnswerID());
        answer2 = answerRepo.selectByAnswerID(answer2.getAnswerID());
        assertEquals(10, (int) answer1.getScore());
        assertEquals("Correct!!\n", answer1.getFeedback());
        assertEquals(0, (int) answer2.getScore());
        assertEquals("", answer2.getFeedback());
    }

    @Transactional
    @Test (expected = IllegalArgumentException.class)
    public void testRemoveAlternativeInvalid() throws Exception {
        testsRepo.insert(testObj);
        Question questionTex = new Question(QuestionType.TEXT_BASED, "question 1", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        CorrectPoint correctPoint = new CorrectPoint(questionTex.getQuestionID(), "correctPoint", 10.0, "Correct!!", new ArrayList<>(), 0, 0);
        correctPoint = correctPointRepo.insert(correctPoint);
        Alternative alternative = new Alternative(correctPoint.getCorrectPointID(), "alt", 0);
        alternative = alternativeRepo.insert(alternative);
        markingController.removeAlternative(principalOther, alternative.getAlternativeID()
                , testObj.getTestID());
    }

    @Transactional
    @Test
    public void testRemoveCorrectPoint() throws Exception {
        testsRepo.insert(testObj);
        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "question 1", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionTex.getQuestionID()));
        Answer answer1 = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "correctPoint", 10, "Correct!!", 0, 0);
        answerRepo.insert(answer1);

        CorrectPoint correctPoint = new CorrectPoint(questionTex.getQuestionID(), "correctPoint", 10.0, "Correct!!", new ArrayList<>(), 0, 0);
        correctPoint = correctPointRepo.insert(correctPoint);
        markingController.removeCorrectPoints(principal, correctPoint.getCorrectPointID(), testObj.getTestID());

        answer1 = answerRepo.selectByAnswerID(answer1.getAnswerID());
        assertEquals(0, (int) answer1.getScore());
        assertEquals("", answer1.getFeedback());
    }

    @Transactional
    @Test (expected = IllegalArgumentException.class)
    public void testRemoveCorrectPointInvalid() throws Exception {
        testsRepo.insert(testObj);
        Question questionTex = new Question(QuestionType.TEXT_BASED, "question 1", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        CorrectPoint correctPoint = new CorrectPoint(questionTex.getQuestionID(), "correctPoint", 10.0, "Correct!!", new ArrayList<>(), 0, 0);
        correctPoint = correctPointRepo.insert(correctPoint);
        markingController.removeCorrectPoints(principalOther, correctPoint.getCorrectPointID()
                , testObj.getTestID());
    }

    @Transactional
    @Test
    public void testFindCorrectPoints() throws Exception {
        testsRepo.insert(testObj);
        // Text
        Question questionTex = new Question(QuestionType.TEXT_BASED, "question 1", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        testQuestionRepo.insert(new TestQuestion(testObj.getTestID(), questionTex.getQuestionID()));
        Answer answer1 = new Answer(questionTex.getQuestionID(), OTHER_IN_DB, USER_IN_DB, testObj.getTestID(), "correctPoint", 10, "Correct!!", 0, 0);
        answerRepo.insert(answer1);

        CorrectPoint correctPoint = new CorrectPoint(questionTex.getQuestionID(), "correctPoint", 10.0, "Correct!!", new ArrayList<>(), 0, 0);
        correctPoint = correctPointRepo.insert(correctPoint);
        List<CorrectPoint> correctPoints = markingController.getCorrectPoints(principal, questionTex.getQuestionID(), testObj.getTestID());

        assertEquals(1, correctPoints.size());
        assertEquals(correctPoint.getCorrectPointID(), correctPoints.get(0).getCorrectPointID());
    }

    @Transactional
    @Test (expected = IllegalArgumentException.class)
    public void testFindCorrectPointsInvalid() {
        testsRepo.insert(testObj);
        Question questionTex = new Question(QuestionType.TEXT_BASED, "question 1", null, 10, 0, USER_IN_DB, 0);
        questionTex = questionRepo.insert(questionTex);
        CorrectPoint correctPoint = new CorrectPoint(questionTex.getQuestionID(), "correctPoint", 10.0, "Correct!!", new ArrayList<>(), 0, 0);
        correctPoint = correctPointRepo.insert(correctPoint);
        markingController.getCorrectPoints(principalOther, questionTex.getQuestionID()
                , testObj.getTestID());
    }
}