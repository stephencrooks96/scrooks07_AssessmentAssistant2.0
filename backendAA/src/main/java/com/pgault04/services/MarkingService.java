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
 * Performs logic for all marking features
 * e.g. viewing data on marking, manually marking,
 * adding new parameters to mark scheme during marking phase
 *
 * @author Paul Gault 40126005
 * @since November 2019
 */
@Service
public class MarkingService {

    private static final int UNMARKED = 0;
    private static final String RED_HEX = "#dc3545";
    private static final String YELLOW_HEX = "#ffc107";
    private static final String GREEN_HEX = "#28a745";
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

    /**
     * Gets data for markers for a given test
     * Data is used in a chart on a front end
     * Includes all the markers, whether they have marked or unmarked their allocated answers
     *
     * @param testID   the test in question
     * @param username the username requesting to see the chart
     * @return the chart data
     */
    public MarkerWithChart getMarkersData(Long testID, String username) {
        Tests test = testsRepo.selectByTestID(testID);
        List<Marker> markers = new ArrayList<>();
        Long check = modService.checkValidAssociation(username, test.getModuleID());
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

    /*
     * Gets marker info for getMarkersData()
     */
    private void populateMarkerData(Long testID, Tests test, List<Marker> markers, ModuleAssociation m) {
        User marker = userRepo.selectByUserID(m.getUserID());
        Marker markerObj = new Marker(test, marker, "", new ArrayList<>(), 0, 0);
        getMarkerType(m, markerObj);
        populateScriptsAndMarkingRatio(testID, marker, markerObj);
        markers.add(markerObj);
    }

    /*
     * Gets colors, labels and number of scripts for each data point (for each marker)
     */
    private void populateChartData(List<Marker> markers, MarkerWithChart markerWithChart) {
        for (Marker ms : markers) {
            markerWithChart.getLabels().add(ms.getMarker().getFirstName() + " " + ms.getMarker().getLastName());
            markerWithChart.getData().add(ms.getScripts().size());
            markerWithChart.getColours().add(ChartUtil.chartColourGenerate());
        }
    }

    /*
     * Separates the scripts belong to each marker into marked and unmarked
     */
    private void populateScriptsAndMarkingRatio(Long testID, User marker, Marker markerObj) {
        List<Answer> allAnswers = answerRepo.selectByTestID(testID);
        for (Answer a : allAnswers) {
            if (a.getMarkerID().equals(marker.getUserID())) {
                markerObj.getScripts().add(a);
                if (a.getMarkerApproved() == UNMARKED) {
                    markerObj.setUnmarked(markerObj.getUnmarked() + 1);
                } else {
                    markerObj.setMarked(markerObj.getMarked() + 1);
                }
            }
        }
    }

    /*
     * Gets which type of marker the marker is for inclusion in marker's data point in the chart
     */
    private void getMarkerType(ModuleAssociation m, Marker markerObj) {
        if (m.getAssociationType() == AssociationType.TEACHING_ASSISTANT) {
            markerObj.setMarkerType("Assistant");
        } else if (m.getAssociationType() == AssociationType.TUTOR) {
            markerObj.setMarkerType("Tutor");
        }
    }

    /**
     * Method to allow for editing score/feedback for given answer by marker/tutor
     * Performs tasks that overlap between editScore and editFeedback methods
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

    /**
     * Allows user to edit feedback provided to an answer
     * Also triggers search for identical answers to match their feedback accordingly
     *
     * @param username the user performing the manual editing
     * @param answer   the answer being manually changed
     * @return the answer after manual change
     * @throws IllegalArgumentException when user is not allowed to perform this action
     */
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

    /*
     * Different approaches must be made to check similarity of different question types
     * This check the type and then performs the update
     */
    private void checkTypeAndUpdateFeedback(Answer answer, List<Inputs> inputs, List<OptionEntries> optionEntries, Answer a) {
        if (a.getQuestionID().equals(answer.getQuestionID())) {
            Question questionToCompare = questionRepo.selectByID(a.getQuestionID());
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

    /*
     * Checks if identical options selected by other users before updating feedback
     */
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

    /*
     * Performs the actual feedback update (common among all question types)
     */
    private void updateFeedback(Answer answer, Answer a) {
        a.setMarkerApproved(1);
        a.setFeedback(answer.getFeedback());
        answerRepo.insert(a);
    }

    /*
     * When a teaching assistant performs the edit feedback or score methods
     * the answer list affected must only include answers they are markers of
     * For tutor this is not necessary as their decision is allowed to affect all scripts
     */
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

    /**
     * Allows marker to manually edit score for an answer they are marking
     * Check all identical submissions and updates their scores accordingly
     *
     * @param username the username of user performing action
     * @param answer   the answer having its score updated
     * @return the updated answer
     * @throws IllegalArgumentException when user is not permitted to perform such an action
     */
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

    /*
     * Similar to update feedback
     * different question types require different checks
     * before knowing whether to update score for identicals
     */
    private void checkTypeAndUpdateScore(Answer answer, List<OptionEntries> optionEntries, List<Inputs> inputs, Answer a) {
        if (a.getQuestionID().equals(answer.getQuestionID())) {
            Question questionToCompare = questionRepo.selectByID(a.getQuestionID());
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

    /*
     * Checks if identical options selected by other users before updating score
     */
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

    /*
     * Performs the actual score update (common among all question types)
     */
    private void updateScore(Answer answer, Answer a) {
        a.setMarkerApproved(1);
        a.setScore(answer.getScore());
        answerRepo.insert(a);
    }

    /*
     * Checks inputs for answers are identical to answer being checked against
     * If every element in the list matches the the size of list should be returned
     */
    private int checkInputsForSimilarity(List<Inputs> inputs, Answer a) {
        List<Inputs> inputToCompare = inputsRepo.selectByAnswerID(a.getAnswerID());
        return (int) inputs.stream().
                filter(i -> inputToCompare.stream().anyMatch(iToCompare ->
                        i.getInputIndex().equals(iToCompare.getInputIndex())
                                && i.getInputValue().equalsIgnoreCase(iToCompare.getInputValue()))).count();
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
        Answer answer = answerRepo.selectByID(answerID);
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

    /*
     * If approver is tutor then it is tutor approved as well as marker approved
     */
    private Answer tutorApprove(String username, Answer answer, Tests test) {
        if (modService.checkValidAssociation(username, test.getModuleID()) == AssociationType.TUTOR) {
            answer.setTutorApproved(answer.getTutorApproved() == 0 ? 1 : 0);
            answer = answerRepo.insert(answer);
        }
        return answer;
    }

    /*
     * If approver is not the tutor then this is performed without tutor approval
     */
    private Answer markerApprove(User user, Answer answer) {
        if (user.getUserID().equals(answer.getMarkerID())) {
            answer.setMarkerApproved(answer.getMarkerApproved() == 0 ? 1 : 0);
            answer = answerRepo.insert(answer);
        }
        return answer;
    }

    /**
     * Gets scripts for a given marker
     * To allow them to perform their marking
     *
     * @param testID   the test under question
     * @param username the username of requester
     * @return the list of scripts
     * @throws SQLException thrown error in converting image from binary to base64
     */
    public List<AnswerData> getScriptsMarker(Long testID, String username) throws SQLException {
        User marker = userRepo.selectByUsername(username);
        List<Answer> answers = answerRepo.selectByMarkerID(marker.getUserID());
        return getScripts(testID, answers);
    }

    /*
     * Gets everything required for a script
     * Depends on type of question
     * Gets all mark scheme info and students answer
     */
    private List<AnswerData> getScripts(Long testID, List<Answer> answers) throws SQLException {
        List<AnswerData> scripts = new ArrayList<>();
        for (Answer a : answers) {
            if (a.getTestID().equals(testID)) {
                Question question = questionRepo.selectByID(a.getQuestionID());
                AnswerData answerData = new AnswerData(new QuestionAndAnswer(new QuestionAndBase64("", new ArrayList<>(), testService.findMathLines(question.getQuestionID()), question), a, new LinkedList<>(), new ArrayList<>(), new ArrayList<>()), userRepo.selectByUserID(a.getAnswererID()));
                prepareBlobToBaseForAnswerData(question, answerData);
                prepareMultipleChoiceForAnswerData(a, question, answerData);
                prepareCorrectPointsForAnswerData(a, question, answerData);
                scripts.add(answerData);
            }
        }
        return scripts;
    }

    /*
     * Gets correct points together and ready for display
     */
    private void prepareCorrectPointsForAnswerData(Answer a, Question question, AnswerData answerData) {
        if (question.getQuestionType() == QuestionType.INSERT_THE_WORD || question.getQuestionType() == QuestionType.TEXT_BASED || question.getQuestionType() == QuestionType.TEXT_MATH) {
            answerData.getQuestionAndAnswer().setCorrectPoints(correctPointRepo.selectByQuestionID(a.getQuestionID()));
            answerData.getQuestionAndAnswer().getCorrectPoints().forEach(c -> c.setAlternatives(alternativeRepo.selectByCorrectPointID(c.getCorrectPointID())));
            prepareInputsForAnswerData(a, question, answerData);
        }
    }

    /*
     * Gets inputs together and ready for display
     * i.e. orders them correctly
     */
    private void prepareInputsForAnswerData(Answer a, Question question, AnswerData answerData) {
        if (question.getQuestionType() == QuestionType.INSERT_THE_WORD || question.getQuestionType() == QuestionType.TEXT_MATH) {
            sortCorrectPointsForInsertTheWord(question, answerData);
            answerData.getQuestionAndAnswer().setInputs(inputsRepo.selectByAnswerID(a.getAnswerID()));
            answerData.getQuestionAndAnswer().getInputs().sort(Comparator.comparingInt(Inputs::getInputIndex));
        }
    }

    /*
     * Ensure correct points are sorted to match index inside insert the word questions
     */
    private void sortCorrectPointsForInsertTheWord(Question question, AnswerData answerData) {
        if (question.getQuestionType() == QuestionType.INSERT_THE_WORD) {
            answerData.getQuestionAndAnswer().getCorrectPoints().sort(Comparator.comparingInt(CorrectPoint::getIndexedAt));
        }
    }

    /*
     * Gets all options and options entries for display in multiple choice questions
     */
    private void prepareMultipleChoiceForAnswerData(Answer a, Question question, AnswerData answerData) {
        if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
            answerData.getQuestionAndAnswer().getQuestion().setOptions(optionRepo.selectByQuestionID(question.getQuestionID()));
            answerData.getQuestionAndAnswer().setOptionEntries(optionEntriesRepo.selectByAnswerID(a.getAnswerID()));
        }
    }

    /*
     * Triggers logic required to convert sql blob version of image to base64 screen for output on frontend
     */
    private void prepareBlobToBaseForAnswerData(Question question, AnswerData answerData) throws SQLException {
        if (question.getQuestionFigure() != null) {
            answerData.getQuestionAndAnswer().getQuestion().setBase64(BlobUtil.blobToBase(question.getQuestionFigure()));
            answerData.getQuestionAndAnswer().getQuestion().getQuestion().setQuestionFigure(null);
        }
    }

    /*
     * Updates users test result when new submission of practice test
     */
    void insertAndUpdateTestResult(Long testID, String username) throws SQLException {
        User user = userRepo.selectByUsername(username);
        List<Answer> answers = filterAnswers(testID, user);
        List<AnswerData> answerData = getScripts(testID, answers);
        TestResult testResult = initialiseTestResult(testID, user.getUserID());
        answerData.forEach(ad -> testResult.setTestScore(testResult.getTestScore() + ad.getQuestionAndAnswer().getAnswer().getScore()));
        updateNonNullTestResult(testID, user, testResult);
        testResultRepo.insert(testResult);
    }

    /*
     * If test result existed it needs to be updated
     * If not a new one is entered instead
     */
    private void updateNonNullTestResult(Long testID, User user, TestResult testResult) {
        TestResult testResultInDb = testResultRepo.selectByTestIDAndStudentID(testID, user.getUserID());
        if (testResultInDb != null) {
            testResult.setTestResultID(testResultInDb.getTestResultID());
        }
    }

    /*
     * Selects all answers for a given user and filters them to only output ones for specific test requested
     */
    private List<Answer> filterAnswers(Long testID, User user) {
        List<Answer> answersAll = answerRepo.selectByAnswererID(user.getUserID());
        return answersAll.stream().filter(a -> a.getTestID().equals(testID)).collect(Collectors.toCollection(LinkedList::new));
    }

    /*
     * Updates all users results when tutor decides to publish/republish grades or results
     */
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

    /*
     * Gets all answers for specific student and totals up their score to add to the test result
     */
    private void filterAnswerData(List<AnswerData> answerData, Long script, TestResult testResult) {
        answerData.stream().filter(ad -> ad.getStudent().getUserID().equals(script))
                .forEachOrdered(ad ->
                        testResult.setTestScore(testResult.getTestScore() + ad.getQuestionAndAnswer().getAnswer().getScore()));
    }

    /*
     * Creates a new test result when one has been made
     */
    private TestResult initialiseTestResult(Long testID, Long script) {
        TestResult testResult = new TestResult();
        testResult.setStudentID(script);
        testResult.setTestID(testID);
        testResult.setTestScore(0);
        return testResult;
    }

    /**
     * Publishes users results for given test
     * Can also be used to retract the results
     *
     * @param testID   the test
     * @param username user performing action
     * @return success / failure
     * @throws IllegalAccessException thrown if user isnt tutor
     * @throws SQLException           thrown if issue converting image types in db
     */
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

    /**
     * Publishes users grades for given test
     * Can also be used to retract the results
     *
     * @param testID   the test
     * @param username the user performing action
     * @return success / failure
     * @throws IllegalAccessException if user isn't the tutor
     * @throws SQLException           if error converting image types from db
     */
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

    /**
     * Gets scripts for review marking phase
     * All scripts are available here for tutor
     *
     * @param testID   the test
     * @param username the user making the request
     * @return the list of scripts for whole class
     * @throws IllegalAccessException is request made by non-tutor
     * @throws SQLException           if issue converting images types from database
     */
    public List<AnswerData> getScriptsTutor(Long testID, String username) throws IllegalAccessException, SQLException {
        Tests test = testsRepo.selectByTestID(testID);
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && check == AssociationType.TUTOR) {
            List<Answer> answers = answerRepo.selectByTestID(testID);
            return getScripts(testID, answers);
        } else {
            logger.info("Access denied to getScriptsTutor({}, {}) - user is not a tutor for the module.", testID, username);
            throw new IllegalAccessException("Must be a tutor to access this.");
        }
    }

    /**
     * Reassigns answers from one marker to another
     *
     * @param testID           the test
     * @param username         the user performing this action
     * @param reassignmentData previous to current marker, specified question and amount
     * @return success / failure (failure if not tutor)
     */
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

    /*
     * Calculates number of scripts to reassign from percentage given and rellocates
     * Answer's marker in db
     */
    private void calculateReassignmentLevelAndReassign(MarkerAndReassigned m, List<Answer> questionAnswers) {
        int reassignLevel = (questionAnswers.size() * m.getNumberToReassign().intValue()) / 100;
        for (int loop = 0; loop < questionAnswers.size(); loop++) {
            if (loop < reassignLevel) {
                questionAnswers.get(loop).setMarkerID(m.getMarkerID());
                answerRepo.insert(questionAnswers.get(loop));
            }
        }
    }

    /*
     * Gathers up answers for reassignment from previous markers unmarked scripts
     */
    private List<Answer> collectAnswersForReassignment(Long testID, List<Answer> answers) {
        return answers.stream().filter(a ->
                a.getTestID().equals(testID) && a.getMarkerApproved() == 0).collect(Collectors.toList());
    }

    /*
     * Gathers up answers for reassignment from previous markers unmarked scripts of a specific question
     */
    private void reassignSpecificQuestion(MarkerAndReassigned m, List<Answer> testAnswers, List<Answer> questionAnswers) {
        for (Answer a : testAnswers) {
            if (a.getQuestionID().equals(m.getSpecifyQuestion())) {
                questionAnswers.add(a);
            }
        }
    }

    /**
     * Finds the correct points for a specific question for output in marking phase
     *
     * @param questionID the question
     * @param username   the requester
     * @param testID     the test
     * @return the list of correct points
     */
    public List<CorrectPoint> findCorrectPoints(Long questionID, String username, Long testID) {
        Tests test = testsRepo.selectByTestID(testID);
        if (AssociationType.TUTOR == modService.checkValidAssociation(username, test.getModuleID())) {
            return testService.findCorrectPoints(questionID);
        } else {
            throw new IllegalArgumentException("You must be a marker or tutor to complete this action.");
        }
    }

    /**
     * Adds a new correct point during marking phase
     * Triggers check for answers that might include this and auto-marks them accordingly
     *
     * @param username     the requester
     * @param correctPoint the new correct point
     * @param testID       the test
     * @return success / failure
     * @throws Exception thrown if requester isnt tutor
     */
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

    /*
     * Tests with results already published shouldn't have their answers re-auto-marked without chance for approval
     */
    private void checkIfTestAllowsAutoMarkingNewCorrectPoint(String username, CorrectPoint correctPoint, TestQuestion tq, Tests t) throws IllegalAccessException, SQLException {
        if (t.getPublishGrades() != 1 && t.getPublishResults() != 1) {
            for (AnswerData s : getScriptsTutor(tq.getTestID(), username)) {
                autoMarkNewCorrectPoint(correctPoint, s.getQuestionAndAnswer().getAnswer());
            }
        }
    }

    /*
     * adds alternatives included in new correct point to database
     */
    private void addAlternativesFromNewCorrectPoint(CorrectPoint correctPoint, List<Alternative> alts) {
        for (Alternative alt : alts) {
            alt.setAlternativeID(-1L);
            alt.setCorrectPointID(correctPoint.getCorrectPointID());
            alternativeRepo.insert(alt);
        }
    }

    /**
     * Removes an alternative during marking phase
     * Subsequently much check for answers that included it and re-auto-mark them
     *
     * @param username      the requester
     * @param alternativeID the alternative to remove
     * @param testID        the test
     * @return success / failure
     * @throws Exception if requester isnt tutor
     */
    public Boolean removeAlternative(String username, Long alternativeID, Long testID) throws Exception {
        Alternative a = alternativeRepo.selectByID(alternativeID);
        Tests test = testsRepo.selectByTestID(testID);
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && check == AssociationType.TUTOR) {
            alternativeRepo.delete(alternativeID);
            return removeCorrectPoint(username, a.getCorrectPointID(), testID, false);
        } else {
            throw new IllegalArgumentException("Must be a tutor or marker to complete this action.");
        }
    }

    /**
     * Removes an correct point during marking phase
     * Subsequently much check for answers that included it and re-auto-mark them
     * removeAlternative uses this method for auto-marking check
     *
     * @param username       the requester
     * @param correctPointID the correct point to remove
     * @param testID         the test
     * @param remove         whether full removal is required (not required if only alternative is removed)
     * @return success / failure
     * @throws Exception thrown if user is not the tutor
     */
    public Boolean removeCorrectPoint(String username, Long correctPointID, Long testID, Boolean remove) throws Exception {
        CorrectPoint cp = correctPointRepo.selectByID(correctPointID);
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
                        Answer revised = answerRepo.selectByID(original.getAnswerID());
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

    /*
     * Auto-marks eligible answers inline with new correct point
     */
    private void autoMarkNewCorrectPoint(CorrectPoint correctPoint, Answer answer) {
        Question question = questionRepo.selectByID(answer.getQuestionID());
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

    /*
     * Auto-marks eligible answers inline with new correct point specifically for input based questions
     */
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

    /*
     * Auto-marks eligible answers inline with new alternative specifically for input based questions
     */
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

    /*
     * Auto-marks eligible answers inline with new alternative specifically for text based questions
     */
    private void autoMarkTextBaseAlternatives(CorrectPoint correctPoint, Answer answer) {
        for (Alternative alt : alternativeRepo.selectByCorrectPointID(correctPoint.getCorrectPointID())) {
            if (answer.getContent().toLowerCase().contains(alt.getAlternativePhrase().toLowerCase())) {
                updateScoreAndFeedbackFromNewCorrectPoint(correctPoint, answer);
                break;
            }
        }
    }

    /*
     * Updates feedback and score for new correct point for all question types that use correct points
     */
    private void updateScoreAndFeedbackFromNewCorrectPoint(CorrectPoint correctPoint, Answer answer) {
        answer.setScore(answer.getScore() + correctPoint.getMarksWorth().intValue());
        answer.setFeedback(answer.getFeedback() + "\n" + correctPoint.getFeedback());
        answer.setTutorApproved(0);
        answer.setMarkerApproved(0);
    }

    /*
     * Auto-marks eligible answers inline with new alternative
     */
    private void autoMarkNewAlternative(Alternative alternative, Answer answer) {
        Question question = questionRepo.selectByID(answer.getQuestionID());
        CorrectPoint correctPoint = correctPointRepo.selectByID(alternative.getCorrectPointID());
        boolean check = false;
        if (question.getQuestionType() == QuestionType.TEXT_BASED) {
            check = autoMarkNewAlternativeForTextBased(alternative, answer, correctPoint, check);
        } else if (question.getQuestionType() == QuestionType.TEXT_MATH) {
            List<Inputs> inputs = inputsRepo.selectByAnswerID(answer.getAnswerID());
            check = autoMarkNewAlternativeForInputs(alternative, correctPoint, check, inputs);
        }
        updateAnswerForNewAlternativeCriteriaMet(answer, question, correctPoint, check);
    }

    /*
     * Auto-marks eligible answers inline with new alternative specifically for text based questions
     */
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

    /*
     * Auto-marks eligible answers inline with new alternative specifically for input based questions
     */
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

    /*
     * Updates feedback and score for new alternative for all question types that use correct points
     * Check is specified in autoMarkNewAlternativeForInputs method for inputs
     * and autoMarkNewAlternativeForTextBased for text-based
     */
    private void updateAnswerForNewAlternativeCriteriaMet(Answer answer, Question question, CorrectPoint correctPoint, boolean check) {
        if (!check) {
            answer.setTutorApproved(0);
            answer.setScore(answer.getScore() + correctPoint.getMarksWorth().intValue());
            answer.setMarkerApproved(0);
            answer.setFeedback(answer.getFeedback() + "\n" + correctPoint.getFeedback());
            testService.validateScore(answer, question);
        }
    }

    /**
     * Adds a new alternative
     * Triggers check for answers that might include this and auto-marks them accordingly
     *
     * @param username    - the user adding the new alternative
     * @param alternative - the alternative to add
     * @param testID      - the test currently being marked
     * @return success / failure
     * @throws Exception thrown when user not allowed to add an alternative
     */
    public Boolean addAlternative(String username, Alternative alternative, Long testID) throws Exception {
        List<Alternative> alternatives = new ArrayList<>();
        alternatives.add(alternative);
        Tests test = testsRepo.selectByTestID(testID);
        Long check = modService.checkValidAssociation(username, test.getModuleID());
        if (check != null && check == AssociationType.TUTOR) {
            testService.addAlternatives(alternative.getCorrectPointID(), alternatives, false);
            CorrectPoint correctPoint = correctPointRepo.selectByID(alternative.getCorrectPointID());
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

    /*
     * Checks if auto-marking is allowed for answers that contain this new alternative
     * e.g. if grades or results are published auto-marking will not take place without markers having chance to approve
     */
    private void checkIfAutoMarkingIsAllowedForNewAlternative(String username, Alternative alternative, TestQuestion tq, Tests t) throws IllegalAccessException, SQLException {
        if (t.getPublishGrades() != 1 && t.getPublishResults() != 1) {
            List<AnswerData> scripts = getScriptsTutor(tq.getTestID(), username);
            scripts.forEach(s -> autoMarkNewAlternative(alternative, s.getQuestionAndAnswer().getAnswer()));
        }
    }

    /**
     * Generates data required for the chart that shows how each student performed in the test
     * on the review marking page
     *
     * @param testID   - the test the chart is for
     * @param username - the user requesting the chart
     * @return the chart data
     */
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
            if (answers.size() > 0) {
                for (Answer a : answers) {
                    users.add(a.getAnswererID());
                    classAverage += a.getScore();
                }
            }
            resultChartPojo.setClassAverage((int) (((classAverage / totalMarks) * 100) / users.size()));
            double standardDev = (calculateStandardDeviation(answers) / totalMarks) * 100;
            prepareEachPointOnResultChartGraph(answers, users, resultChartPojo, totalMarks, standardDev);
            return resultChartPojo;
        }
        return null;
    }

    /*
     * Calculates standard deviation in the score from a set of Answers
     */
    double calculateStandardDeviation(List<Answer> answers) {
        double sum = answers.stream().mapToDouble(answer -> answer.getScore().doubleValue()).sum();
        double mean = (sum) / (answers.size());
        double newSum = answers.stream().mapToDouble(answer -> ((answer.getScore().doubleValue() - mean) * (answer.getScore().doubleValue() - mean))).sum();
        return (Math.sqrt((newSum) / (answers.size())));
    }

    /*
     * Sets labels and scores for data points on the scatter graph
     */
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

    /*
     * Sets color of each data point based on how many standard deviations below the mean it is
     */
    private void populatePointColoursForResultChart(ResultChartPojo resultChartPojo, double standardDev, double userScore) {
        if (userScore >= resultChartPojo.getClassAverage()) {
            resultChartPojo.getColors().add(GREEN_HEX);
        } else if (userScore < resultChartPojo.getClassAverage() && userScore >= resultChartPojo.getClassAverage() - standardDev) {
            resultChartPojo.getColors().add(YELLOW_HEX);
        } else {
            resultChartPojo.getColors().add(RED_HEX);
        }
    }

    /*
     * Totals up the user's score from each answer
     */
    private double getUserScore(List<Answer> answers, Long user) {
        return answers.stream().filter(answer -> answer.getAnswererID().equals(user)).mapToDouble(Answer::getScore).sum();
    }

    /*
     * Gets the maximum possible score from a test
     */
    private double getTotalMarks(List<TestQuestion> testQuestions) {
        return testQuestions.stream().mapToDouble(testQuestion -> questionRepo.selectByID(testQuestion.getQuestionID()).getMaxScore()).sum();
    }

    /**
     * Generates a chart for the review marking page that shows the average score for the class on each question
     * and compares it against the total marks for the question
     *
     * @param testID   - the test the chart is for
     * @param username - the user requesting to view the chart
     * @return the chart information
     */
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

    /*
     * Prepares the two "charts' i.e each dataset
     * One dataset for average score in the question across all students
     * One dataset for total score available for the question
     */
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

    /*
     * Goes through each question in the test and sets the chart data for each
     */
    private void iterateQuestionsForQuestionResultChart(List<Answer> answers, ResultChartPojo resultChartPojo1, ResultChartPojo resultChartPojo2, Set<Long> questions) {
        int counter = 1;
        for (Long q : questions) {
            Question question = questionRepo.selectByID(q);
            resultChartPojo1.getLabels().add(counter++ + ". " + question.getQuestionContent());
            resultChartPojo1.getScores().add(question.getMaxScore());
            int totalScore = 0;
            int userCounter = 0;
            if (answers.size() > 0) {
                for (Answer a : answers) {
                    if (q.equals(a.getQuestionID())) {
                        totalScore += a.getScore();
                        userCounter++;
                    }
                }
                resultChartPojo2.getScores().add(totalScore / userCounter);
            }
        }
    }

    /*
     * Finds each question that is included in the test
     */
    private void getQuestionsForQuestionResultChart(List<Answer> answers, Set<Long> questions) {
        answers.stream().map(Answer::getQuestionID).forEach(questions::add);
    }
}