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
import java.util.stream.Collectors;

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
        Long check =  modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && AssociationType.TUTOR == check) {
            List<ModuleAssociation> moduleAssociations = moduleAssociationRepo.selectByModuleID(test.getModuleID());
            for (ModuleAssociation m : moduleAssociations) {
                if (m.getAssociationType() != AssociationType.STUDENT) {
                    populateMarkerData(testID, test, markers, m);
                }
            }
            MarkerWithChart markerWithChart = new MarkerWithChart(markers, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            populateChartData(markers, markerWithChart);
            return markerWithChart;
        }
        return null;
    }

    private void populateMarkerData(Long testID, Tests test, List<Marker> markers, ModuleAssociation m) {
        User marker = userRepo.selectByUserID(m.getUserID());
        Marker markerObj = new Marker(test, marker, "", new ArrayList<>(), 0, 0);
        getMarkerType(m, markerObj);
        populateScriptsAndMarkingRatio(testID, marker, markerObj);
        markers.add(markerObj);
    }

    private void populateChartData(List<Marker> markers, MarkerWithChart markerWithChart) {
        for (Marker ms : markers) {
            markerWithChart.getLabels().add(ms.getMarker().getFirstName() + " " + ms.getMarker().getLastName());
            markerWithChart.getData().add(ms.getScripts().size());
            markerWithChart.getColours().add(ChartUtil.chartColourGenerate());
        }
    }

    private void populateScriptsAndMarkingRatio(Long testID, User marker, Marker markerObj) {
        List<Answer> allAnswers = answerRepo.selectByTestID(testID);
        for (Answer a : allAnswers) {
            if (a.getMarkerID().equals(marker.getUserID())) {
                markerObj.getScripts().add(a);
                if (a.getMarkerApproved() == 0) {
                    markerObj.setUnmarked(markerObj.getUnmarked() + 1);
                } else {
                    markerObj.setMarked(markerObj.getMarked() + 1);
                }
            }
        }
    }

    private void getMarkerType(ModuleAssociation m, Marker markerObj) {
        if (m.getAssociationType() == AssociationType.TEACHING_ASSISTANT) {
            markerObj.setMarkerType("Assistant");
        } else if (m.getAssociationType() == AssociationType.TUTOR) {
            markerObj.setMarkerType("Tutor");
        }
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
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && check == AssociationType.TUTOR || user != null && user.getUserID().equals(answer.getMarkerID())) {
            answer = answerRepo.insert(answer);
            return answer;
        } else {
            throw new IllegalArgumentException("Must be marker or tutor to complete this action.");
        }
    }

    public Answer editFeedback(String username, Answer answer) throws IllegalArgumentException {
        answer = editAnswer(username, answer);
        List<Answer> answers = answerRepo.selectByTestID(answer.getTestID());
        List<Inputs> inputs = inputsRepo.selectByAnswerID(answer.getAnswerID());
        List<OptionEntries> optionEntries = optionEntriesRepo.selectByAnswerID(answer.getAnswerID());
        List<Answer> answersForNonTutor = new ArrayList<>();
        answers = reviseAnswerListForTeachingAssistant(username, answer, answers, answersForNonTutor);
        for (Answer a : answers) {
            checkTypeAndUpdateFeedback(answer, inputs, optionEntries, a);
        }
        return answer;
    }

    private void checkTypeAndUpdateFeedback(Answer answer, List<Inputs> inputs, List<OptionEntries> optionEntries, Answer a) {
        if (a.getQuestionID().equals(answer.getQuestionID())) {
            Question questionToCompare = questionRepo.selectByQuestionID(a.getQuestionID());
            if (questionToCompare.getQuestionType() == QuestionType.TEXT_BASED && a.getContent().equalsIgnoreCase(answer.getContent())) {
                updateFeedback(answer, a);
            } else if (questionToCompare.getQuestionType() == QuestionType.INSERT_THE_WORD || questionToCompare.getQuestionType() == QuestionType.TEXT_MATH) {
                int inputCounter = checkInputsForSimilarity(inputs, a);
                if (inputCounter == inputs.size()) {
                    updateFeedback(answer, a);
                }
            } else if (questionToCompare.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                updateFeedbackForMultipleChoice(answer, optionEntries, a);
            }
        }
    }

    private void updateFeedbackForMultipleChoice(Answer answer, List<OptionEntries> optionEntries, Answer a) {
        List<Long> optionEntriesIDs = new ArrayList<>();
        List<OptionEntries> optionEntriesToCompare = optionEntriesRepo.selectByAnswerID(a.getAnswerID());
        List<Long> optionEntriesIDsToCompare = new ArrayList<>();
        for (OptionEntries o : optionEntriesToCompare) {
            optionEntriesIDsToCompare.add(o.getOptionID());
        }
        for (OptionEntries o : optionEntries) {
            optionEntriesIDs.add(o.getOptionID());
        }
        if (optionEntriesIDs.containsAll(optionEntriesIDsToCompare)) {
            updateFeedback(answer, a);
        }
    }

    private void updateFeedback(Answer answer, Answer a) {
        a.setMarkerApproved(1);
        a.setFeedback(answer.getFeedback());
        answerRepo.insert(a);
    }

    private List<Answer> reviseAnswerListForTeachingAssistant(String username, Answer answer, List<Answer> answers, List<Answer> answersForNonTutor) {
        Long check = modService.checkValidAssociation(username, testsRepo.selectByTestID(answer.getTestID()).getModuleID());
        if (check != null && check == AssociationType.TEACHING_ASSISTANT) {
            User user = userRepo.selectByUsername(username);
            for (Answer a : answers) {
                if (a.getMarkerID().equals(user.getUserID())) {
                    answersForNonTutor.add(a);
                }
            }
            answers = answersForNonTutor;
        }
        return answers;
    }

    public Answer editScore(String username, Answer answer) throws IllegalArgumentException {
        answer = editAnswer(username, answer);
        List<OptionEntries> optionEntries = optionEntriesRepo.selectByAnswerID(answer.getAnswerID());
        List<Answer> answers = answerRepo.selectByTestID(answer.getTestID());
        List<Answer> answersForNonTutor = new ArrayList<>();
        answers = reviseAnswerListForTeachingAssistant(username, answer, answers, answersForNonTutor);
        List<Inputs> inputs = inputsRepo.selectByAnswerID(answer.getAnswerID());
        for (Answer a : answers) {
            checkTypeAndUpdateScore(answer, optionEntries, inputs, a);
        }
        return answer;
    }

    private void checkTypeAndUpdateScore(Answer answer, List<OptionEntries> optionEntries, List<Inputs> inputs, Answer a) {
        if (a.getQuestionID().equals(answer.getQuestionID())) {
            Question questionToCompare = questionRepo.selectByQuestionID(a.getQuestionID());
            if (questionToCompare.getQuestionType() == QuestionType.TEXT_BASED && a.getContent().equalsIgnoreCase(answer.getContent())) {
                updateScore(answer, a);
            } else if (questionToCompare.getQuestionType() == QuestionType.INSERT_THE_WORD || questionToCompare.getQuestionType() == QuestionType.TEXT_MATH) {
                int inputCounter = checkInputsForSimilarity(inputs, a);
                if (inputCounter == inputs.size()) {
                    updateScore(answer, a);
                }
            } else if (questionToCompare.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                updateScoreForMultipleChoice(answer, optionEntries, a);
            }
        }
    }

    private void updateScoreForMultipleChoice(Answer answer, List<OptionEntries> optionEntries, Answer a) {
        List<Long> optionEntriesIDs = new LinkedList<>();
        for (OptionEntries o : optionEntries) {
            optionEntriesIDs.add(o.getOptionID());
        }
        List<OptionEntries> optionEntriesToCompare = optionEntriesRepo.selectByAnswerID(a.getAnswerID());
        List<Long> optionEntriesIDsToCompare = new LinkedList<>();
        for (OptionEntries o : optionEntriesToCompare) {
            optionEntriesIDsToCompare.add(o.getOptionID());
        }
        if (optionEntriesIDs.containsAll(optionEntriesIDsToCompare)) {
            updateScore(answer, a);
        }
    }

    private void updateScore(Answer answer, Answer a) {
        a.setMarkerApproved(1);
        a.setScore(answer.getScore());
        answerRepo.insert(a);
    }

    private int checkInputsForSimilarity(List<Inputs> inputs, Answer a) {
        List<Inputs> inputToCompare = inputsRepo.selectByAnswerID(a.getAnswerID());
        int inputCounter = 0;
        for (Inputs i : inputs) {
            for (Inputs iToCompare : inputToCompare) {
                if (i.getInputIndex().equals(iToCompare.getInputIndex()) && i.getInputValue().equalsIgnoreCase(iToCompare.getInputValue())) {
                    inputCounter++;
                    break;
                }
            }
        }
        return inputCounter;
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
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && check == AssociationType.TUTOR || user.getUserID().equals(answer.getMarkerID())) {
            answer = markerApprove(user, answer);
            answer = tutorApprove(username, answer, test);
            return answer;
        } else {
            throw new IllegalArgumentException("You must be a marker or tutor to complete this action.");
        }
    }

    private Answer tutorApprove(String username, Answer answer, Tests test) {
        if (modService.checkValidAssociation(username, test.getModuleID()) == AssociationType.TUTOR) {
            answer.setTutorApproved(answer.getTutorApproved() == 0 ? 1 : 0);
            answer = answerRepo.insert(answer);
        }
        return answer;
    }

    private Answer markerApprove(User user, Answer answer) {
        if (user.getUserID().equals(answer.getMarkerID())) {
            answer.setMarkerApproved(answer.getMarkerApproved() == 0 ? 1 : 0);
            answer = answerRepo.insert(answer);
        }
        return answer;
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
                AnswerData answerData = new AnswerData(new QuestionAndAnswer(new QuestionAndBase64("", new ArrayList<>(), testService.findMathLines(question.getQuestionID()), question), a, new LinkedList<>(), new ArrayList<>(), new ArrayList<>()), userRepo.selectByUserID(a.getAnswererID()));
                prepareBlobToBaseForAnswerData(question, answerData);
                prepareMultipleChoiceForAnswerData(a, question, answerData);
                prepareCorrectPointsForAnswerData(a, question, answerData);
                scripts.add(answerData);
            }
        }
        return scripts;
    }

    private void prepareCorrectPointsForAnswerData(Answer a, Question question, AnswerData answerData) {
        if (question.getQuestionType() == QuestionType.INSERT_THE_WORD || question.getQuestionType() == QuestionType.TEXT_BASED || question.getQuestionType() == QuestionType.TEXT_MATH) {
            answerData.getQuestionAndAnswer().setCorrectPoints(correctPointRepo.selectByQuestionID(a.getQuestionID()));
            answerData.getQuestionAndAnswer().getCorrectPoints().forEach(c -> c.setAlternatives(alternativeRepo.selectByCorrectPointID(c.getCorrectPointID())));
            prepareInputsForAnswerData(a, question, answerData);
        }
    }

    private void prepareInputsForAnswerData(Answer a, Question question, AnswerData answerData) {
        if (question.getQuestionType() == QuestionType.INSERT_THE_WORD || question.getQuestionType() == QuestionType.TEXT_MATH) {
            sortCorrectPointsForInsertTheWord(question, answerData);
            answerData.getQuestionAndAnswer().setInputs(inputsRepo.selectByAnswerID(a.getAnswerID()));
            answerData.getQuestionAndAnswer().getInputs().sort(Comparator.comparingInt(Inputs::getInputIndex));
        }
    }

    private void sortCorrectPointsForInsertTheWord(Question question, AnswerData answerData) {
        if (question.getQuestionType() == QuestionType.INSERT_THE_WORD) {
            answerData.getQuestionAndAnswer().getCorrectPoints().sort(Comparator.comparingInt(CorrectPoint::getIndexedAt));
        }
    }

    private void prepareMultipleChoiceForAnswerData(Answer a, Question question, AnswerData answerData) {
        if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
            answerData.getQuestionAndAnswer().getQuestion().setOptions(optionRepo.selectByQuestionID(question.getQuestionID()));
            answerData.getQuestionAndAnswer().setOptionEntries(optionEntriesRepo.selectByAnswerID(a.getAnswerID()));
        }
    }

    private void prepareBlobToBaseForAnswerData(Question question, AnswerData answerData) throws SQLException {
        if (question.getQuestionFigure() != null) {
            answerData.getQuestionAndAnswer().getQuestion().setBase64(BlobUtil.blobToBase(question.getQuestionFigure()));
            answerData.getQuestionAndAnswer().getQuestion().getQuestion().setQuestionFigure(null);
        }
    }

    void insertAndUpdateTestResult(Long testID, String username) throws SQLException {
        User user = userRepo.selectByUsername(username);
        List<Answer> answers = filterAnswers(testID, user);
        List<AnswerData> answerData = getScripts(testID, answers);
        TestResult testResult = initialiseTestResult(testID, user.getUserID());
        answerData.forEach(ad -> testResult.setTestScore(testResult.getTestScore() + ad.getQuestionAndAnswer().getAnswer().getScore()));
        updateNonNullTestResult(testID, user, testResult);
        testResultRepo.insert(testResult);
    }

    private void updateNonNullTestResult(Long testID, User user, TestResult testResult) {
        TestResult testResultInDb = testResultRepo.selectByTestIDAndStudentID(testID, user.getUserID());
        if (testResultInDb != null) {
            testResult.setTestResultID(testResultInDb.getTestResultID());
        }
    }

    private List<Answer> filterAnswers(Long testID, User user) {
        List<Answer> answersAll = answerRepo.selectByAnswererID(user.getUserID());
        return answersAll.stream().filter(a -> a.getTestID().equals(testID)).collect(Collectors.toCollection(LinkedList::new));
    }

    private void insertAndUpdateTestResults(Long testID, String username) throws IllegalAccessException, SQLException {
        List<AnswerData> answerData = getScriptsTutor(testID, username);
        Set<Long> scriptSet = answerData.stream().map(ad -> ad.getStudent().getUserID()).collect(Collectors.toSet());
        for (Long script : scriptSet) {
            TestResult testResult = initialiseTestResult(testID, script);
            filterAnswerData(answerData, script, testResult);
            updateNonNullTestResult(testID, userRepo.selectByUserID(script), testResult);
            testResultRepo.insert(testResult);
        }
    }

    private void filterAnswerData(List<AnswerData> answerData, Long script, TestResult testResult) {
        answerData.stream().filter(ad -> ad.getStudent().getUserID().equals(script))
                .forEachOrdered(ad ->
                        testResult.setTestScore(testResult.getTestScore() + ad.getQuestionAndAnswer().getAnswer().getScore()));
    }

    private TestResult initialiseTestResult(Long testID, Long script) {
        TestResult testResult = new TestResult();
        testResult.setStudentID(script);
        testResult.setTestID(testID);
        testResult.setTestScore(0);
        return testResult;
    }

    public Boolean publishResults(Long testID, String username) throws IllegalAccessException, SQLException {
        Tests test = testsRepo.selectByTestID(testID);
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && check == AssociationType.TUTOR) {
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
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && check == AssociationType.TUTOR) {
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
        Long check =  modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && check == AssociationType.TUTOR) {
            List<Answer> answers = answerRepo.selectByTestID(testID);
            return getScripts(testID, answers);
        } else {
            logger.info("Access denied to getScriptsTutor({}, {}) - user is not a tutor for the module.", testID, username);
            throw new IllegalAccessException("Must be a tutor to access this.");
        }
    }

    public Boolean reassignAnswers(Long testID, String username, List<MarkerAndReassigned> reassignmentData) {
        Tests test = testsRepo.selectByTestID(testID);
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && check == AssociationType.TUTOR) {
            for (MarkerAndReassigned m : reassignmentData) {
                if (m.getNumberToReassign() != null && m.getNumberToReassign() != 0) {
                    List<Answer> answers = answerRepo.selectByMarkerID(m.getPreviousMarkerID());
                    List<Answer> testAnswers = collectAnswersForReassignment(testID, answers);
                    List<Answer> questionAnswers = new ArrayList<>();
                    if (m.getSpecifyQuestion() == 0) {
                        questionAnswers = testAnswers;
                    } else {
                        reassignSpecificQuestion(m, testAnswers, questionAnswers);
                    }
                    calculateReassignmentLevelAndReassign(m, questionAnswers);
                }
            }
            return true;
        }
        return null;
    }

    private void calculateReassignmentLevelAndReassign(MarkerAndReassigned m, List<Answer> questionAnswers) {
        int reassignLevel = (questionAnswers.size() * m.getNumberToReassign().intValue()) / 100;
        for (int loop = 0; loop < questionAnswers.size(); loop++) {
            if (loop < reassignLevel) {
                questionAnswers.get(loop).setMarkerID(m.getMarkerID());
                answerRepo.insert(questionAnswers.get(loop));
            }
        }
    }

    private List<Answer> collectAnswersForReassignment(Long testID, List<Answer> answers) {
        return answers.stream().filter(a ->
                a.getTestID().equals(testID) && a.getMarkerApproved() == 0).collect(Collectors.toList());
    }

    private void reassignSpecificQuestion(MarkerAndReassigned m, List<Answer> testAnswers, List<Answer> questionAnswers) {
        for (Answer a : testAnswers) {
            if (a.getQuestionID().equals(m.getSpecifyQuestion())) {
                questionAnswers.add(a);
            }
        }
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
        Tests test = testsRepo.selectByTestID(testID);
        if (AssociationType.TUTOR == modService.checkValidAssociation(username, test.getModuleID())) {
            List<Alternative> alts = correctPoint.getAlternatives();
            correctPoint = correctPointRepo.insert(correctPoint);
            addAlternativesFromNewCorrectPoint(correctPoint, alts);
            testService.addCorrectPoints(new ArrayList<>(), correctPoint.getQuestionID(), false);
            for (TestQuestion tq : testQuestionRepo.selectByQuestionID(correctPoint.getQuestionID())) {
                Tests t = testsRepo.selectByTestID(tq.getTestID());
                checkIfTestAllowsAutoMarkingNewCorrectPoint(username, correctPoint, tq, t);
            }
            return true;
        } else {
            throw new IllegalArgumentException("You must be a marker or tutor to complete this action.");
        }
    }

    private void checkIfTestAllowsAutoMarkingNewCorrectPoint(String username, CorrectPoint correctPoint, TestQuestion tq, Tests t) throws IllegalAccessException, SQLException {
        if (t.getPublishGrades() != 1 && t.getPublishResults() != 1) {
            for (AnswerData s : getScriptsTutor(tq.getTestID(), username)) {
                autoMarkNewCorrectPoint(correctPoint, s.getQuestionAndAnswer().getAnswer());
            }
        }
    }

    private void addAlternativesFromNewCorrectPoint(CorrectPoint correctPoint, List<Alternative> alts) {
        for (Alternative alt : alts) {
            alt.setAlternativeID(-1L);
            alt.setCorrectPointID(correctPoint.getCorrectPointID());
            alternativeRepo.insert(alt);
        }
    }

    public Boolean removeAlternative(String username, Long alternativeID, Long testID) throws Exception {
        Alternative a = alternativeRepo.selectByAlternativeID(alternativeID);
        Tests test = testsRepo.selectByTestID(testID);
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && check == AssociationType.TUTOR) {
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
                        int originalScore = original.getScore();
                        String originalFeedback = original.getFeedback();
                        testService.autoMarkCorrectPoints(s.getQuestionAndAnswer());
                        Answer revised = answerRepo.selectByAnswerID(original.getAnswerID());
                        if (originalScore != revised.getScore() || originalFeedback.equalsIgnoreCase(revised.getFeedback())) {
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
        if (question.getQuestionType() == QuestionType.TEXT_BASED) {
            if (answer.getContent().toLowerCase().contains(correctPoint.getPhrase().toLowerCase())) {
                updateScoreAndFeedbackFromNewCorrectPoint(correctPoint, answer);
            } else {
                autoMarkTextBaseAlternatives(correctPoint, answer);
            }
        } else if (question.getQuestionType() == QuestionType.TEXT_MATH) {
            List<Inputs> inputs = inputsRepo.selectByAnswerID(answer.getAnswerID());
            autoMarkNewCorrectPointForInput(correctPoint, answer, inputs);
        }
        testService.validateScore(answer, question);
    }

    private void autoMarkNewCorrectPointForInput(CorrectPoint correctPoint, Answer answer, List<Inputs> inputs) {
        for (Inputs i : inputs) {
            if (i.getInputValue().trim().equalsIgnoreCase(correctPoint.getPhrase())) {
                updateScoreAndFeedbackFromNewCorrectPoint(correctPoint, answer);
            } else {
                boolean altBroken = autoMarkInputBasedAlternatives(correctPoint, answer, i, false);
                if (altBroken) {
                    break;
                }
            }
        }
    }

    private boolean autoMarkInputBasedAlternatives(CorrectPoint correctPoint, Answer answer, Inputs i, boolean altBroken) {
        for (Alternative alt : alternativeRepo.selectByCorrectPointID(correctPoint.getCorrectPointID())) {
            if (i.getInputValue().contains(alt.getAlternativePhrase())) {
                updateScoreAndFeedbackFromNewCorrectPoint(correctPoint, answer);
                altBroken = true;
                break;
            }
        }
        return altBroken;
    }

    private void autoMarkTextBaseAlternatives(CorrectPoint correctPoint, Answer answer) {
        for (Alternative alt : alternativeRepo.selectByCorrectPointID(correctPoint.getCorrectPointID())) {
            if (answer.getContent().toLowerCase().contains(alt.getAlternativePhrase().toLowerCase())) {
                updateScoreAndFeedbackFromNewCorrectPoint(correctPoint, answer);
                break;
            }
        }
    }

    private void updateScoreAndFeedbackFromNewCorrectPoint(CorrectPoint correctPoint, Answer answer) {
        answer.setScore(answer.getScore() + correctPoint.getMarksWorth().intValue());
        answer.setFeedback(answer.getFeedback() + "\n" + correctPoint.getFeedback());
        answer.setTutorApproved(0);
        answer.setMarkerApproved(0);
    }

    private void autoMarkNewAlternative(Alternative alternative, Answer answer) {
        Question question = questionRepo.selectByQuestionID(answer.getQuestionID());
        CorrectPoint correctPoint = correctPointRepo.selectByCorrectPointID(alternative.getCorrectPointID());
        boolean check = false;
        if (question.getQuestionType() == QuestionType.TEXT_BASED) {
            check = autoMarkNewAlternativeForTextBased(alternative, answer, correctPoint, check);
        } else if (question.getQuestionType() == QuestionType.TEXT_MATH) {
            List<Inputs> inputs = inputsRepo.selectByAnswerID(answer.getAnswerID());
            check = autoMarkNewAlternativeForInputs(alternative, correctPoint, check, inputs);
        }
        updateAnswerForNewAlternativeCriteriaMet(answer, question, correctPoint, check);
    }

    private boolean autoMarkNewAlternativeForTextBased(Alternative alternative, Answer answer, CorrectPoint correctPoint, boolean check) {
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
        return check;
    }

    private boolean autoMarkNewAlternativeForInputs(Alternative alternative, CorrectPoint correctPoint, boolean check, List<Inputs> inputs) {
        for (Inputs i : inputs) {
            if (i.getInputValue().trim().equalsIgnoreCase(correctPoint.getPhrase())) {
                check = true;
            } else {
                for (Alternative alt : alternativeRepo.selectByCorrectPointID(correctPoint.getCorrectPointID())) {
                    if (!alternative.getAlternativeID().equals(alt.getAlternativeID()) && i.getInputValue().trim().equalsIgnoreCase(alt.getAlternativePhrase())) {
                        check = true;
                    } else if (alternative.getAlternativeID().equals(alt.getAlternativeID()) && !i.getInputValue().trim().equalsIgnoreCase(alt.getAlternativePhrase())) {
                        check = true;
                    }
                }
            }
        }
        return check;
    }

    private void updateAnswerForNewAlternativeCriteriaMet(Answer answer, Question question, CorrectPoint correctPoint, boolean check) {
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
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && check == AssociationType.TUTOR) {
            testService.addAlternatives(alternative.getCorrectPointID(), alternatives, false);
            CorrectPoint correctPoint = correctPointRepo.selectByCorrectPointID(alternative.getCorrectPointID());
            List<TestQuestion> testQuestions = testQuestionRepo.selectByQuestionID(correctPoint.getQuestionID());
            for (TestQuestion tq : testQuestions) {
                Tests t = testsRepo.selectByTestID(tq.getTestID());
                checkIfAutoMarkingIsAllowedForNewAlternative(username, alternative, tq, t);
            }
            return true;
        } else {
            throw new IllegalArgumentException("You must be a marker or tutor to complete this action.");
        }
    }

    private void checkIfAutoMarkingIsAllowedForNewAlternative(String username, Alternative alternative, TestQuestion tq, Tests t) throws IllegalAccessException, SQLException {
        if (t.getPublishGrades() != 1 && t.getPublishResults() != 1) {
            List<AnswerData> scripts = getScriptsTutor(tq.getTestID(), username);
            scripts.forEach(s -> autoMarkNewAlternative(alternative, s.getQuestionAndAnswer().getAnswer()));
        }
    }

    double calculateStandardDeviation(List<Answer> answers) {
        double sum = answers.stream().mapToDouble(answer -> answer.getScore().doubleValue()).sum();
        double mean = (sum) / (answers.size());
        double newSum = answers.stream().mapToDouble(answer -> ((answer.getScore().doubleValue() - mean)
                * (answer.getScore().doubleValue() - mean))).sum();
        return (Math.sqrt((newSum) / (answers.size())));
    }

    public ResultChartPojo generateResultChart(Long testID, String username) {
        logger.info("Request made for result statistics for test with id #{}", testID);
        Tests test = testsRepo.selectByTestID(testID);
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && AssociationType.TUTOR == check) {
            List<Answer> answers = answerRepo.selectByTestID(testID);
            Set<Long> users = new HashSet<>();
            List<TestQuestion> testQuestions = testQuestionRepo.selectByTestID(testID);
            ResultChartPojo resultChartPojo = new ResultChartPojo(new LinkedList<>(), new LinkedList<>(), 0, new LinkedList<>());
            double totalMarks;
            long classAverage = 0L;
            totalMarks = getTotalMarks(testQuestions);
            for (Answer a : answers) {
                users.add(a.getAnswererID());
                classAverage += a.getScore();
            }
            resultChartPojo.setClassAverage((int) (((classAverage / totalMarks) * 100) / users.size()));
            double standardDev = (calculateStandardDeviation(answers) / totalMarks) * 100;
            prepareEachPointOnResultChartGraph(answers, users, resultChartPojo, totalMarks, standardDev);
            return resultChartPojo;
        }
        return null;
    }

    private void prepareEachPointOnResultChartGraph(List<Answer> answers, Set<Long> users, ResultChartPojo resultChartPojo, double totalMarks, double standardDev) {
        for (Long user : users) {
            double userScore = getUserScore(answers, user);
            User u = userRepo.selectByUserID(user);
            resultChartPojo.getLabels().add(u.getFirstName() + " " + u.getLastName());
            resultChartPojo.getScores().add((int) (((userScore / totalMarks)) * 100));
            userScore = (((userScore / totalMarks)) * 100);
            populatePointColoursForResultChart(resultChartPojo, standardDev, userScore);
        }
    }

    private void populatePointColoursForResultChart(ResultChartPojo resultChartPojo, double standardDev, double userScore) {
        if (userScore >= resultChartPojo.getClassAverage()) {
            resultChartPojo.getColors().add("#28a745");
        } else if (userScore < resultChartPojo.getClassAverage() && userScore >= resultChartPojo.getClassAverage() - standardDev) {
            resultChartPojo.getColors().add("#ffc107");
        } else {
            resultChartPojo.getColors().add("#dc3545");
        }
    }

    private double getUserScore(List<Answer> answers, Long user) {
        return answers.stream().filter(answer -> answer.getAnswererID().equals(user)).mapToDouble(Answer::getScore).sum();
    }

    private double getTotalMarks(List<TestQuestion> testQuestions) {
        return testQuestions.stream().mapToDouble(testQuestion -> questionRepo.selectByQuestionID(testQuestion.getQuestionID()).getMaxScore()).sum();
    }

    public List<ResultChartPojo> generateQuestionResultChart(Long testID, String username) {
        logger.info("Request made for result statistics for test with id #{}", testID);
        Tests test = testsRepo.selectByTestID(testID);
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        List<ResultChartPojo> resultCharts = new LinkedList<>();
        if (check != null && AssociationType.TUTOR == check) {
            prepareQuestionResultCharts(testID, resultCharts);
            return resultCharts;
        }
        return null;
    }

    private void prepareQuestionResultCharts(Long testID, List<ResultChartPojo> resultCharts) {
        List<Answer> answers = answerRepo.selectByTestID(testID);
        ResultChartPojo resultChartPojo1 = new ResultChartPojo(new LinkedList<>(), new LinkedList<>(), null, new LinkedList<>());
        ResultChartPojo resultChartPojo2 = new ResultChartPojo(new LinkedList<>(), new LinkedList<>(), null, new LinkedList<>());
        Set<Long> questions = new HashSet<>();
        getQuestionsForQuestionResultChart(answers, questions);
        iterateQuestionsForQuestionResultChart(answers, resultChartPojo1, resultChartPojo2, questions);
        resultChartPojo2.getLabels().addAll(resultChartPojo1.getLabels());
        resultChartPojo1.setClassAverage(Collections.max(resultChartPojo1.getScores()));
        resultChartPojo2.setClassAverage(Collections.max(resultChartPojo1.getScores()));
        resultCharts.add(resultChartPojo1);
        resultCharts.add(resultChartPojo2);
    }

    private void iterateQuestionsForQuestionResultChart(List<Answer> answers, ResultChartPojo resultChartPojo1, ResultChartPojo resultChartPojo2, Set<Long> questions) {
        int counter = 1;
        for (Long q : questions) {
            Question question = questionRepo.selectByQuestionID(q);
            resultChartPojo1.getLabels().add(counter++ + ". " + question.getQuestionContent());
            resultChartPojo1.getScores().add(question.getMaxScore());
            int totalScore = 0;
            int userCounter = 0;
            for (Answer a : answers) {
                if (q.equals(a.getQuestionID())) {
                    totalScore += a.getScore();
                    userCounter++;
                }
            }
            resultChartPojo2.getScores().add(totalScore / userCounter);
        }
    }

    private void getQuestionsForQuestionResultChart(List<Answer> answers, Set<Long> questions) {
        answers.stream().map(Answer::getQuestionID).forEach(questions::add);
    }
}
