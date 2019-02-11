package com.pgault04.services;

import com.pgault04.entities.*;
import com.pgault04.pojos.*;
import com.pgault04.repositories.*;
import com.pgault04.utilities.BlobUtil;
import com.pgault04.utilities.ChartUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

/**
 * @author Paul Gault 40126005
 * @since January 2019
 */
@Service
public class MarkingService {

    /**
     * Logs useful information for debugging and problem resolution
     */
    private static final Logger logger = LogManager.getLogger(MarkingService.class);

    @Autowired
    OptionEntriesRepo optionEntriesRepo;

    @Autowired
    AlternativeRepo alternativeRepo;

    @Autowired
    TestService testService;

    @Autowired
    TestQuestionRepo testQuestionRepo;

    @Autowired
    InputsRepo inputsRepo;

    @Autowired
    CorrectPointRepo correctPointRepo;

    @Autowired
    TestResultRepo testResultRepo;

    @Autowired
    OptionRepo optionRepo;

    @Autowired
    QuestionRepo questionRepo;

    @Autowired
    TestsRepo testsRepo;

    @Autowired
    ModuleAssociationRepo moduleAssociationRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ModuleService modService;

    @Autowired
    AnswerRepo answerRepo;

    public MarkerWithChart getMarkersData(Long testID, String username) {

        Tests test = testsRepo.selectByTestID(testID);
        List<Marker> markers = new ArrayList<>();
        if (AssociationType.TUTOR == modService.checkValidAssociation(username, test.getModuleID())) {
            List<ModuleAssociation> modAssocs = moduleAssociationRepo.selectByModuleID(test.getModuleID());

            for (ModuleAssociation m : modAssocs) {
                if (m.getAssociationType() != AssociationType.STUDENT) {
                    User marker = userRepo.selectByUserID(m.getUserID());
                    Integer marked = 0, unmarked = 0;
                    String markerType = "";

                    if (m.getAssociationType() == AssociationType.TEACHING_ASSISTANT) {
                        markerType = "Assistant";
                    } else if (m.getAssociationType() == AssociationType.TUTOR) {
                        markerType = "Tutor";
                    }

                    List<Answer> allAnswers = answerRepo.selectByTestID(testID);
                    List<Answer> answers = new ArrayList<>();
                    for (Answer a : allAnswers) {
                        if (a.getMarkerID().equals(marker.getUserID())) {
                            answers.add(a);
                            if (a.getMarkerApproved() == 0) {
                                unmarked++;
                            } else {
                                marked++;
                            }
                        }
                    }

                    markers.add(new Marker(test, marker, markerType, answers, marked, unmarked));
                }
            }
            List<String> labels = new LinkedList<>();
            List<Integer> data = new LinkedList<>();
            List<String> colours = new LinkedList<>();
            for (Marker ms : markers) {
                labels.add(ms.getMarker().getFirstName() + " " + ms.getMarker().getLastName());
                data.add(ms.getScripts().size());
                colours.add(ChartUtil.chartColourGenerate());
            }

            return new MarkerWithChart(markers, labels, data, colours);
        }
        return null;
    }

    /**
     * Method to allow for editing score/feedback for given answer by marker/tutor
     *
     * @param username the user
     * @param answer   the answer
     * @return the answer
     * @throws IllegalArgumentException when unauthorized user attempts
     */
    public Answer editAnswer(String username, Answer answer) throws IllegalArgumentException {

        User user = userRepo.selectByUsername(username);
        Tests test = testsRepo.selectByTestID(answer.getTestID());

        if (modService.checkValidAssociation(username, test.getModuleID()) == AssociationType.TUTOR || user.getUserID().equals(answer.getMarkerID())) {
            answer = answerRepo.insert(answer);
            return answer;
        } else {
            throw new IllegalArgumentException("Must be marker or tutor to complete this action.");
        }
    }

    public Answer editFeedback(String username, Answer answer) throws IllegalArgumentException {
        answer = editAnswer(username, answer);
        List<Answer> answers = answerRepo.selectByTestID(answer.getTestID());
        for (Answer a : answers) {
            if (a.getContent().equalsIgnoreCase(answer.getContent()) && a.getMarkerApproved() == 0) {
                a.setFeedback(answer.getFeedback());
                answerRepo.insert(a);
            }
        }
        return answer;
    }

    public Answer editScore(String username, Answer answer) throws IllegalArgumentException {
        answer = editAnswer(username, answer);
        List<Answer> answers = answerRepo.selectByTestID(answer.getTestID());
        for (Answer a : answers) {
            if (a.getContent().equalsIgnoreCase(answer.getContent()) && a.getMarkerApproved() == 0) {
                a.setScore(answer.getScore());
                answerRepo.insert(a);
            }
        }
        return answer;
    }

    /**
     * Method to allow tutors / markers to approve score / feedback given to an answer
     *
     * @param username the user
     * @param answerID the answers id
     * @return the answer
     * @throws IllegalArgumentException error handling
     */
    public Answer approve(String username, Long answerID) throws IllegalArgumentException {

        User user = userRepo.selectByUsername(username);
        Answer answer = answerRepo.selectByAnswerID(answerID);
        Tests test = testsRepo.selectByTestID(answer.getTestID());

        if (modService.checkValidAssociation(username, test.getModuleID()) == AssociationType.TUTOR || user.getUserID().equals(answer.getMarkerID())) {
            if (user.getUserID().equals(answer.getMarkerID())) {
                answer.setMarkerApproved(answer.getMarkerApproved() == 0 ? 1 : 0);
                answer = answerRepo.insert(answer);
            }

            if (modService.checkValidAssociation(username, test.getModuleID()) == AssociationType.TUTOR) {
                answer.setTutorApproved(answer.getTutorApproved() == 0 ? 1 : 0);
                answer = answerRepo.insert(answer);
            }

            return answer;
        } else {
            throw new IllegalArgumentException("You must be a marker or tutor to complete this action.");
        }
    }

    public List<AnswerData> getScriptsMarker(Long testID, String username) throws SQLException {
        User marker = userRepo.selectByUsername(username);
        List<Answer> answers = answerRepo.selectByMarkerID(marker.getUserID());
        return getScripts(testID, answers);
    }

    private List<AnswerData> getScripts(Long testID, List<Answer> answers) throws SQLException {
        List<AnswerData> scripts = new ArrayList<>();
        for (Answer a : answers) {
            if (a.getTestID().equals(testID)) {
                Question question = questionRepo.selectByQuestionID(a.getQuestionID());
                String base64 = "";
                List<Option> options = new ArrayList<>();
                List<OptionEntries> optionEntries = new ArrayList<>();
                List<CorrectPoint> correctPoints = new ArrayList<>();
                List<Inputs> inputs = new LinkedList<>();
                if (question.getQuestionFigure() != null) {
                    base64 = BlobUtil.blobToBase(question.getQuestionFigure());
                    question.setQuestionFigure(null);
                }

                if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                    options = optionRepo.selectByQuestionID(question.getQuestionID());
                    optionEntries = optionEntriesRepo.selectByAnswerID(a.getAnswerID());
                }

                if (question.getQuestionType() == QuestionType.INSERT_THE_WORD || question.getQuestionType() == QuestionType.TEXT_BASED) {
                    correctPoints = correctPointRepo.selectByQuestionID(a.getQuestionID());
                    for (CorrectPoint c : correctPoints) {
                        c.setAlternatives(alternativeRepo.selectByCorrectPointID(c.getCorrectPointID()));
                    }

                    if (question.getQuestionType() == QuestionType.INSERT_THE_WORD) {
                        correctPoints.sort(Comparator.comparingInt(CorrectPoint::getIndex));
                        inputs = inputsRepo.selectByAnswerID(a.getAnswerID());
                        inputs.sort(Comparator.comparingInt(Inputs::getInputIndex));
                    }
                }

                User student = userRepo.selectByUserID(a.getAnswererID());
                scripts.add(new AnswerData(new QuestionAndAnswer(new QuestionAndBase64(base64, options, question), a, inputs, optionEntries), student, correctPoints));
            }
        }
        return scripts;
    }

    public void insertAndUpdateTestResults(Long testID, String username) throws IllegalAccessException, SQLException {
        List<AnswerData> answerData = getScriptsTutor(testID, username);
        Set<Long> scriptSet = new HashSet<>();
        for (AnswerData ad : answerData) {
            scriptSet.add(ad.getStudent().getUserID());
        }

        for (Long script : scriptSet) {
            TestResult testResult = new TestResult();
            testResult.setStudentID(script);
            testResult.setTestID(testID);
            testResult.setTestScore(0);
            for (AnswerData ad : answerData) {
                if (ad.getStudent().getUserID().equals(script)) {
                    testResult.setTestScore(testResult.getTestScore() + ad.getQuestionAndAnswer().getAnswer().getScore());
                }
            }
            TestResult testResultInDb = testResultRepo.selectByTestIDAndStudentID(testID, script);
            if (testResultInDb != null) {
                testResult.setTestResultID(testResultInDb.getTestResultID());
            }
            testResultRepo.insert(testResult);
        }
    }

    public Boolean publishResults(Long testID, String username) throws IllegalAccessException, SQLException {
        Tests test = testsRepo.selectByTestID(testID);
        if (modService.checkValidAssociation(username, test.getModuleID()) == AssociationType.TUTOR) {
            test.setPublishResults(test.getPublishResults() == 0 ? 1 : 0);
            testsRepo.insert(test);
            insertAndUpdateTestResults(testID, username);
            return true;
        } else {
            throw new IllegalArgumentException("You must be a tutor to complete this action.");
        }
    }

    public Boolean publishGrades(Long testID, String username) throws IllegalAccessException, SQLException {
        Tests test = testsRepo.selectByTestID(testID);
        if (modService.checkValidAssociation(username, test.getModuleID()) == AssociationType.TUTOR) {
            test.setPublishGrades(test.getPublishGrades() == 0 ? 1 : 0);
            testsRepo.insert(test);
            insertAndUpdateTestResults(testID, username);
            return true;
        } else {
            throw new IllegalArgumentException("You must be a tutor to complete this action.");
        }
    }

    public List<AnswerData> getScriptsTutor(Long testID, String username) throws IllegalAccessException, SQLException {

        Tests test = testsRepo.selectByTestID(testID);
        if (AssociationType.TUTOR == modService.checkValidAssociation(username, test.getModuleID())) {
            List<Answer> answers = answerRepo.selectByTestID(testID);
            return getScripts(testID, answers);
        } else {
            logger.info("Access denied to getScriptsTutor({}, {}) - user is not a tutor for the module.", testID, username);
            throw new IllegalAccessException("Must be a tutor to access this.");
        }

    }

    public Boolean reassignAnswers(Long testID, String username, List<MarkerAndReassigned> reassignmentData) {

        Tests test = testsRepo.selectByTestID(testID);
        if (AssociationType.TUTOR == modService.checkValidAssociation(username, test.getModuleID())) {

            for (MarkerAndReassigned m : reassignmentData) {
                if (m.getNumberToReassign() != null && m.getNumberToReassign() != 0) {
                    List<Answer> answers = answerRepo.selectByMarkerID(m.getPreviousMarkerID());
                    List<Answer> testAnswers = new ArrayList<>();
                    for (Answer a : answers) {
                        if (a.getTestID().equals(testID) && a.getMarkerApproved() == 0) {
                            testAnswers.add(a);
                        }
                    }
                    List<Answer> questionAnswers = new ArrayList<>();
                    if (m.getSpecifyQuestion() == 0) {
                        questionAnswers = testAnswers;
                    } else {
                        for (Answer a : testAnswers) {
                            if (a.getQuestionID().equals(m.getSpecifyQuestion())) {
                                questionAnswers.add(a);
                            }
                        }
                    }
                    int reassignLevel = (questionAnswers.size() * (m.getNumberToReassign().intValue() / 100));
                    for (int loop = 0; loop < questionAnswers.size(); loop++) {
                        if (loop < reassignLevel) {
                            questionAnswers.get(loop).setMarkerID(m.getMarkerID());
                            answerRepo.insert(questionAnswers.get(loop));
                        }
                    }
                }
            }
            return true;
        }
        return null;
    }

    public List<CorrectPoint> findCorrectPoints(Long questionID, String username, Long testID) {

        Tests test = testsRepo.selectByTestID(testID);
        if (AssociationType.TUTOR == modService.checkValidAssociation(username, test.getModuleID())) {

            return testService.findCorrectPoints(questionID);
        } else {
            throw new IllegalArgumentException("You must be a marker or tutor to complete this action.");
        }
    }

    public Boolean addCorrectPoint(String username, CorrectPoint correctPoint, Long testID) throws Exception {

        List<CorrectPoint> correctPoints = new ArrayList<>();
        correctPoints.add(correctPoint);

        Tests test = testsRepo.selectByTestID(testID);
        if (AssociationType.TUTOR == modService.checkValidAssociation(username, test.getModuleID())) {
            List<Alternative> alts = correctPoint.getAlternatives();
            correctPoint = correctPointRepo.insert(correctPoint);
            for (Alternative alt : alts) {
                alt.setAlternativeID(-1L);
                alt.setCorrectPointID(correctPoint.getCorrectPointID());
                alternativeRepo.insert(alt);
            }
            testService.addCorrectPoints(new ArrayList<>(), correctPoint.getQuestionID(), false);
            for (TestQuestion tq : testQuestionRepo.selectByQuestionID(correctPoint.getQuestionID())) {

                Tests t = testsRepo.selectByTestID(tq.getTestID());

                if (t.getPublishGrades() != 1 && t.getPublishResults() != 1) {
                    for (AnswerData s : getScriptsTutor(tq.getTestID(), username)) {
                        autoMarkNewCorrectPoint(correctPoint, s.getQuestionAndAnswer().getAnswer());
                    }
                }
            }
            return true;
        } else {
            throw new IllegalArgumentException("You must be a marker or tutor to complete this action.");
        }
    }

    public Boolean removeAlternative(String username, Long alternativeID, Long testID) throws Exception {

        Alternative a = alternativeRepo.selectByAlternativeID(alternativeID);
        Tests test = testsRepo.selectByTestID(testID);
        if (AssociationType.TUTOR == modService.checkValidAssociation(username, test.getModuleID())) {
            alternativeRepo.delete(alternativeID);
            return removeCorrectPoint(username, a.getCorrectPointID(), testID, false);
        } else {
            throw new IllegalArgumentException("Must be a tutor or marker to complete this action.");
        }
    }

    public Boolean removeCorrectPoint(String username, Long correctPointID, Long testID, Boolean remove) throws Exception {

        CorrectPoint cp = correctPointRepo.selectByCorrectPointID(correctPointID);

        Tests test = testsRepo.selectByTestID(testID);
        if (AssociationType.TUTOR == modService.checkValidAssociation(username, test.getModuleID())) {
            if (remove) {
                correctPointRepo.delete(correctPointID);
            }
            for (TestQuestion tq : testQuestionRepo.selectByQuestionID(cp.getQuestionID())) {
                Tests t = testsRepo.selectByTestID(tq.getTestID());
                if (t.getPublishGrades() != 1 && t.getPublishResults() != 1) {
                    for (AnswerData s : getScriptsTutor(tq.getTestID(), username)) {
                        Answer original = s.getQuestionAndAnswer().getAnswer();
                        testService.autoMarkCorrectPoints(s.getQuestionAndAnswer());
                        Answer revised = answerRepo.selectByAnswerID(original.getAnswerID());

                        if (!original.getScore().equals(revised.getScore()) || !original.getFeedback().equals(revised.getFeedback())) {
                            revised.setMarkerApproved(0);
                            revised.setTutorApproved(0);
                        }
                    }
                }
            }
            return true;
        } else {
            throw new IllegalArgumentException("You must be a marker or tutor to complete this action.");
        }
    }

    private void autoMarkNewCorrectPoint(CorrectPoint correctPoint, Answer answer) {

        Question question = questionRepo.selectByQuestionID(answer.getQuestionID());
        if (answer.getContent().toLowerCase().contains(correctPoint.getPhrase().toLowerCase())) {
            answer.setScore(answer.getScore() + correctPoint.getMarksWorth().intValue());
            answer.setFeedback(answer.getFeedback() + "\n" + correctPoint.getFeedback());
            answer.setTutorApproved(0);
            answer.setMarkerApproved(0);
        } else {
            for (Alternative alt : alternativeRepo.selectByCorrectPointID(correctPoint.getCorrectPointID())) {
                if (answer.getContent().toLowerCase().contains(alt.getAlternativePhrase().toLowerCase())) {
                    answer.setTutorApproved(0);
                    answer.setMarkerApproved(0);
                    answer.setScore(answer.getScore() + correctPoint.getMarksWorth().intValue());
                    answer.setFeedback(answer.getFeedback() + "\n" + correctPoint.getFeedback());
                    break;
                }
            }
        }

        testService.validateScore(answer, question);
    }

    private void autoMarkNewAlternative(Alternative alternative, Answer answer) {

        Question question = questionRepo.selectByQuestionID(answer.getQuestionID());
        CorrectPoint correctPoint = correctPointRepo.selectByCorrectPointID(alternative.getCorrectPointID());
        boolean check = false;
        if (answer.getContent().toLowerCase().contains(correctPoint.getPhrase().toLowerCase())) {
            check = true;
        } else {
            for (Alternative alt : alternativeRepo.selectByCorrectPointID(correctPoint.getCorrectPointID())) {
                if (!alternative.getAlternativeID().equals(alt.getAlternativeID()) && answer.getContent().toLowerCase().contains(alt.getAlternativePhrase().toLowerCase())) {
                    check = true;
                } else if (alternative.getAlternativeID().equals(alt.getAlternativeID()) && !answer.getContent().toLowerCase().contains(alt.getAlternativePhrase().toLowerCase())) {
                    check = true;
                }
            }
        }
        if (!check) {
            answer.setTutorApproved(0);
            answer.setScore(answer.getScore() + correctPoint.getMarksWorth().intValue());
            answer.setMarkerApproved(0);
            answer.setFeedback(answer.getFeedback() + "\n" + correctPoint.getFeedback());
            testService.validateScore(answer, question);
        }
    }

    public Boolean addAlternative(String username, Alternative alternative, Long testID) throws Exception {

        List<Alternative> alternatives = new ArrayList<>();
        alternatives.add(alternative);

        Tests test = testsRepo.selectByTestID(testID);
        if (AssociationType.TUTOR == modService.checkValidAssociation(username, test.getModuleID())) {
            testService.addAlternatives(alternative.getCorrectPointID(), alternatives, false);
            CorrectPoint correctPoint = correctPointRepo.selectByCorrectPointID(alternative.getCorrectPointID());
            List<TestQuestion> testQuestions = testQuestionRepo.selectByQuestionID(correctPoint.getQuestionID());
            for (TestQuestion tq : testQuestions) {
                Tests t = testsRepo.selectByTestID(tq.getTestID());
                if (t.getPublishGrades() != 1 && t.getPublishResults() != 1) {
                    List<AnswerData> scripts = getScriptsTutor(tq.getTestID(), username);
                    for (AnswerData s : scripts) {
                        autoMarkNewAlternative(alternative, s.getQuestionAndAnswer().getAnswer());
                    }
                }
            }
            return true;
        } else {
            throw new IllegalArgumentException("You must be a marker or tutor to complete this action.");
        }
    }


    public ResultChartPojo generateResultChart(Long testID, String username) {
        logger.info("Request made for result statistics for test with id #{}", testID);
        Tests test = testsRepo.selectByTestID(testID);
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        if (AssociationType.TUTOR == check) {

            List<Answer> answers = answerRepo.selectByTestID(testID);
            Set<Long> users = new HashSet<>();
            List<TestQuestion> testQuestions = testQuestionRepo.selectByTestID(testID);
            LinkedList<String> labels = new LinkedList<>();
            LinkedList<Integer> scores = new LinkedList<>();
            LinkedList<String> colors = new LinkedList<>();

            double totalMarks = 0.0;
            for (TestQuestion testQuestion : testQuestions) {
                totalMarks += questionRepo.selectByQuestionID(testQuestion.getQuestionID()).getMaxScore();
            }
            for (Answer a : answers) {
                users.add(a.getAnswererID());
            }
            long classAverage = 0L;
            for (Long user : users) {
                double userScore = 0.0;
                for (Answer answer : answers) {
                    if (answer.getAnswererID().equals(user)) {
                        userScore += answer.getScore();
                    }
                }
                classAverage += userScore;
                User u = userRepo.selectByUserID(user);
                labels.add(u.getFirstName() + " " + u.getLastName());
                scores.add((int) (((userScore / totalMarks)) * 100));
                colors.add(ChartUtil.chartColourGenerate());
            }
            return new ResultChartPojo(labels, scores, (int) (((classAverage / totalMarks) * 100) / users.size()), colors);
        }
        return null;
    }
}
