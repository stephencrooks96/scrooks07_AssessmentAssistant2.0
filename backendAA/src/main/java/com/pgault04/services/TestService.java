package com.pgault04.services;

import com.pgault04.entities.*;
import com.pgault04.pojos.*;
import com.pgault04.repositories.*;
import com.pgault04.utilities.BlobUtil;
import com.pgault04.utilities.EmailUtil;
import com.pgault04.utilities.StringToDateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Performs logic for method such as adding and editing tests, submitting tests and auto-marking submissions
 *
 * @author Paul Gault - 40126005
 * @since December 2018
 */
@Service
public class TestService {

    private static final int SCHEDULED = 1;
    private static final int UNSCHEDULED = 0;
    private static final int PUBLISH_TRUE = 1;
    private static final Logger logger = LogManager.getLogger(TestService.class);

    @Autowired
    TestsRepo testRepo;
    @Autowired
    EmailUtil emailSender;
    @Autowired
    OptionEntriesRepo optionEntriesRepo;
    @Autowired
    TestResultRepo trRepo;
    @Autowired
    InputsRepo inputsRepo;
    @Autowired
    AnswerRepo answerRepo;
    @Autowired
    ModuleRepo modRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    MarkingService markingService;
    @Autowired
    ModuleService modServ;
    @Autowired
    QuestionTypeRepo questionTypeRepo;
    @Autowired
    QuestionRepo questionRepo;
    @Autowired
    TestQuestionRepo testQuestionRepo;
    @Autowired
    CorrectPointRepo cpRepo;
    @Autowired
    AlternativeRepo alternativeRepo;
    @Autowired
    OptionRepo optionRepo;
    @Autowired
    QuestionMathLineRepo questionMathLineRepo;
    @Autowired
    ModuleAssociationRepo moduleAssociationRepo;

    /**
     * Method primes input data to be entered in to the database Ensures data size
     * limits are enforced
     * <p>
     * Ensures input data is correct for a new test and not tampered with on front
     * end i.e. testID=-1L to prevent user from updating a pre-existing test without
     * permission, ensures test is not scheduled for release immediately, ensures
     * results are not readied for publish immediately.
     *
     * @param test     - the test
     * @param username - the principal user
     * @return Test object or null
     */
    public Tests addTest(Tests test, String username) {
        logger.info("Request made to add a test to the database by {}", username);
        primeInitialTestInfo(test);
        User user = userRepo.selectByUsername(username);
        Module module = modRepo.selectByID(test.getModuleID());
        try {
            // Parse exceptions could be thrown here
            test.setEndDateTime(StringToDateUtil.convertInputDateToCorrectFormat(test.getEndDateTime()));
            test.setStartDateTime(StringToDateUtil.convertInputDateToCorrectFormat(test.getStartDateTime()));
            if (test.getTestTitle().length() <= 50
                    && test.getTestTitle().length() > 0
                    && user != null
                    && module != null
                    && user.getUserID().equals(module.getTutorUserID())) {
                return primeTestForUserView(testRepo.insert(test));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * Primes general info belonging to the test to be inserted in to the database
     */
    private void primeInitialTestInfo(Tests test) {
        test.setTestID(-1L);
        test.setScheduled(0);
        test.setPublishResults(0);
        test.setPublishGrades(test.getPractice() == 1 ? 1 : 0);
        test.setTestTitle(test.getTestTitle().trim());
    }

    /**
     * Logic performed for a user to submit their answers to a test
     *
     * @param script   - the list of answers to questions
     * @param username - the user making the submission
     * @return success / failure
     * @throws SQLException if error converting image types / date types
     */
    public boolean submitTest(List<QuestionAndAnswer> script, String username) throws SQLException {
        logger.info("Request made to add a test to the database by {}", username);
        User student = userRepo.selectByUsername(username);
        Tests test = testRepo.selectByTestID(script.get(0).getAnswer().getTestID());
        Module module = modRepo.selectByID(test.getModuleID());
        User tutor = userRepo.selectByUserID(module.getTutorUserID());

        for (QuestionAndAnswer questionAndAnswer : script) {
            prepareAnswerForSubmission(student, tutor, questionAndAnswer);
            deletePreviousSubmissions(student, test, questionAndAnswer);
            boolean answerMatch = false;
            answerMatch = checkForIdenticals(questionAndAnswer, answerMatch);
            if (questionAndAnswer.getAnswer().getContent() == null) {
                questionAndAnswer.getAnswer().setContent("");
            }
            answerRepo.insert(questionAndAnswer.getAnswer());
            automark(questionAndAnswer, answerMatch);
        }
        updateResultForPracticeTest(username, test);
        return true;
    }

    /*
     * If the test is a practice test then this is called to update the test result automatically
     */
    private void updateResultForPracticeTest(String username, Tests test) throws SQLException {
        if (test.getPractice() == 1) {
            markingService.insertAndUpdateTestResult(test.getTestID(), username);
        }
    }

    /*
     * Calls auto-marking methods for questions of certain types
     */
    private void automark(QuestionAndAnswer questionAndAnswer, boolean answerMatch) {
        if (!answerMatch && questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
            autoMarkMultipleChoice(questionAndAnswer);
        } else if (!answerMatch && (questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.INSERT_THE_WORD) || questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.TEXT_MATH) || questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.TEXT_BASED))) {
            autoMarkCorrectPoints(questionAndAnswer);
        }
    }

    /*
     * Calls identical checks for questions of certain types
     */
    private boolean checkForIdenticals(QuestionAndAnswer questionAndAnswer, boolean answerMatch) {
        List<Answer> answers = answerRepo.selectByQuestionID(questionAndAnswer.getQuestion().getQuestion().getQuestionID());
        if (questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.TEXT_BASED)) {
            answerMatch = checkForTextBasedIdenticals(questionAndAnswer, false, answers);
        } else if (questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.INSERT_THE_WORD)
                        || questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.TEXT_MATH)) {
            answerMatch = checkForInputBasedIdenticals(questionAndAnswer, false, answers);
        }
        return answerMatch;
    }

    /*
     * Checks for identical answers in input based questions
     */
    private boolean checkForInputBasedIdenticals(QuestionAndAnswer questionAndAnswer, boolean answerMatch, List<Answer> answers) {
        for (Answer a : answers) {
            List<Inputs> inputs = inputsRepo.selectByAnswerID(a.getAnswerID());
            String pastInput, userInput;
            pastInput = inputs.stream().map(Inputs::getInputValue).collect(Collectors.joining());
            userInput = questionAndAnswer.getInputs().stream().map(Inputs::getInputValue).collect(Collectors.joining());
            if (pastInput.equals(userInput)) {
                answerMatch = markAgainstIdenticalAnswer(questionAndAnswer, a);
                insertInputs(questionAndAnswer);
                break;
            }
        }
        return answerMatch;
    }

    /*
     * Checks for identical answers in text based questions
     */
    private boolean checkForTextBasedIdenticals(QuestionAndAnswer questionAndAnswer, boolean answerMatch, List<Answer> answers) {
        for (Answer a : answers) {
            if (questionAndAnswer.getAnswer().getContent().equalsIgnoreCase(a.getContent())) {
                answerMatch = markAgainstIdenticalAnswer(questionAndAnswer, a);
                break;
            }
        }
        return answerMatch;
    }

    /*
     * If an identical answer this is found this is called to give the current answer the same score and feedback
     */
    private boolean markAgainstIdenticalAnswer(QuestionAndAnswer questionAndAnswer, Answer a) {
        questionAndAnswer.getAnswer().setScore(a.getScore());
        questionAndAnswer.getAnswer().setFeedback(a.getFeedback());
        questionAndAnswer.getAnswer().setMarkerApproved(1);
        validateScore(questionAndAnswer.getAnswer(), questionRepo.selectByID(questionAndAnswer.getQuestion().getQuestion().getQuestionID()));
        return true;
    }

    /*
     * Gets a new answer ready to be submitted in to database
     */
    private void prepareAnswerForSubmission(User student, User tutor, QuestionAndAnswer questionAndAnswer) {
        questionAndAnswer.getAnswer().setQuestionID(questionAndAnswer.getQuestion().getQuestion().getQuestionID());
        questionAndAnswer.getAnswer().setAnswererID(student.getUserID());
        questionAndAnswer.getAnswer().setMarkerID(tutor.getUserID());
        questionAndAnswer.getAnswer().setTutorApproved(0);
        questionAndAnswer.getAnswer().setMarkerApproved(0);
    }

    /*
     * Deletes any previous submissions for this test by the student submitting
     */
    private void deletePreviousSubmissions(User student, Tests test, QuestionAndAnswer questionAndAnswer) {
        List<Answer> answers = answerRepo.selectByAnswererID(student.getUserID());
        if (answers != null && answers.size() > 0) {
            for (Answer a : answers) {
                if (a.getQuestionID().equals(questionAndAnswer.getQuestion().getQuestion().getQuestionID()) && a.getTestID().equals(test.getTestID())) {
                    answerRepo.delete(a.getAnswerID());
                }
            }
        }
    }

    /*
     * Auto-marks multiples choice questions based on score and feedback given to options in marking phase
     */
    private void autoMarkMultipleChoice(QuestionAndAnswer questionAndAnswer) {
        Question question = questionRepo.selectByID(questionAndAnswer.getAnswer().getQuestionID());
        List<Option> options = findOptions(questionAndAnswer.getAnswer().getQuestionID());
        options.sort(Collections.reverseOrder(Comparator.comparingDouble(Option::getWorthMarks)));
        prepareAnswerForAutoMarking(questionAndAnswer);
        if (options.size() > 0) {
            for (Option o : options) {
                for (OptionEntries oe : questionAndAnswer.getOptionEntries()) {
                    if (o.getOptionID().equals(oe.getOptionID())) {
                        addMarkAndFeedbackForCorrectOption(o, questionAndAnswer, oe);
                    }
                }
            }
            validateScore(questionAndAnswer.getAnswer(), question);
        }
    }

    /*
     * When an option is found in one of the users entries then the mark and feedback for the option is added to their answer
     */
    private void addMarkAndFeedbackForCorrectOption(Option o, QuestionAndAnswer questionAndAnswer, OptionEntries oe) {
        oe.setAnswerID(questionAndAnswer.getAnswer().getAnswerID());
        optionEntriesRepo.insert(oe);
        questionAndAnswer.getAnswer().setScore(questionAndAnswer.getAnswer().getScore() + o.getWorthMarks());
        questionAndAnswer.getAnswer().setFeedback(questionAndAnswer.getAnswer().getFeedback() + o.getFeedback() + "\n");
        questionAndAnswer.getAnswer().setMarkerApproved(1);
    }

    /*
     * Auto-marking method for all questions that use correct points
     * Calls relevant methods for their specific question types
     */
    void autoMarkCorrectPoints(QuestionAndAnswer questionAndAnswer) {
        Question question = questionRepo.selectByID(questionAndAnswer.getAnswer().getQuestionID());
        List<CorrectPoint> correctPoints = findCorrectPoints(questionAndAnswer.getAnswer().getQuestionID());
        correctPoints.sort(Collections.reverseOrder(Comparator.comparingDouble(CorrectPoint::getMarksWorth)));
        prepareAnswerForAutoMarking(questionAndAnswer);
        if (question.getQuestionType().equals(QuestionType.INSERT_THE_WORD) || question.getQuestionType().equals(QuestionType.TEXT_MATH)) {
            insertInputs(questionAndAnswer);
            automarkCorrectPointsForInputs(questionAndAnswer, question, correctPoints);
        } else {
            autoMarkCorrectPointsForTextBased(questionAndAnswer, correctPoints);
        }
        validateScore(questionAndAnswer.getAnswer(), question);
    }

    /*
     * Auto-marking method for questions that use inputs
     * Calls submethods for automarking insert the word and math type inputs
     */
    private void automarkCorrectPointsForInputs(QuestionAndAnswer questionAndAnswer, Question question, List<CorrectPoint> correctPoints) {
        for (CorrectPoint c : correctPoints) {
            for (Inputs i : questionAndAnswer.getInputs()) {
                if (question.getQuestionType().equals(QuestionType.INSERT_THE_WORD)) {
                    autoMarkCorrectPointsForInsertTheWord(questionAndAnswer, c, i);
                } else {
                    if (autoMarkCorrectPointsForTextMath(questionAndAnswer, c, i)) break;
                }
            }
        }
    }

    /*
     * Gets answer ready for auto-marking - i.e. removes prior feedback, score and trims the content to ensure random spaces to affect the content check
     */
    private void prepareAnswerForAutoMarking(QuestionAndAnswer questionAndAnswer) {
        questionAndAnswer.getAnswer().setScore(0);
        questionAndAnswer.getAnswer().setFeedback("");
        if (questionAndAnswer.getAnswer().getContent() != null) {
            questionAndAnswer.getAnswer().setContent(questionAndAnswer.getAnswer().getContent().trim());
        }
    }

    /*
     * Performs auto-marking for text based questions
     * Checks if answer contains a correct point and then if not check the correct points alternatives
     */
    private void autoMarkCorrectPointsForTextBased(QuestionAndAnswer questionAndAnswer, List<CorrectPoint> correctPoints) {
        for (CorrectPoint cp : correctPoints) {
            if (questionAndAnswer.getAnswer().getContent().toLowerCase().contains(cp.getPhrase().toLowerCase())) {
                setScoreAndFeedbackForCorrectPoint(questionAndAnswer, cp.getMarksWorth(), cp.getFeedback());
            } else {
                for (Alternative alt : alternativeRepo.selectByCorrectPointID(cp.getCorrectPointID())) {
                    if (questionAndAnswer.getAnswer().getContent().toLowerCase().contains(alt.getAlternativePhrase().toLowerCase())) {
                        setScoreAndFeedbackForCorrectPoint(questionAndAnswer, cp.getMarksWorth(), cp.getFeedback());
                        break;
                    }
                }
            }
        }
    }

    /*
     * Auto-marks text-math type questions
     * If the correct point is exactly contained the marks are given if not the alternatives are checked
     */
    private boolean autoMarkCorrectPointsForTextMath(QuestionAndAnswer questionAndAnswer, CorrectPoint c, Inputs i) {
        if (i.getInputValue().trim().equalsIgnoreCase(c.getPhrase())) {
            setScoreAndFeedbackForCorrectPoint(questionAndAnswer, c.getMarksWorth(), c.getFeedback());
            return true;
        } else {
            boolean altBroken = false;
            for (Alternative alt : alternativeRepo.selectByCorrectPointID(c.getCorrectPointID())) {
                if (i.getInputValue().contains(alt.getAlternativePhrase())) {
                    setScoreAndFeedbackForCorrectPoint(questionAndAnswer, c.getMarksWorth(), c.getFeedback());
                    altBroken = true;
                    break;
                }
            }
            return altBroken;
        }
    }

    /*
     * Auto-marks insert the word type questions
     * If the correct point or one of it's alternatives are contained in the correctly indexed input then mark is given
     */
    private void autoMarkCorrectPointsForInsertTheWord(QuestionAndAnswer questionAndAnswer, CorrectPoint c, Inputs i) {
        if (i.getInputValue().equalsIgnoreCase(c.getPhrase()) && i.getInputIndex().equals(c.getIndexedAt())) {
            setScoreAndFeedbackForCorrectPoint(questionAndAnswer, c.getMarksWorth(), c.getFeedback());
        } else {
            for (Alternative alt : alternativeRepo.selectByCorrectPointID(c.getCorrectPointID())) {
                if (i.getInputValue().equalsIgnoreCase(alt.getAlternativePhrase()) && i.getInputIndex().equals(c.getIndexedAt())) {
                    setScoreAndFeedbackForCorrectPoint(questionAndAnswer, c.getMarksWorth(), c.getFeedback());
                    break;
                }
            }
        }
    }

    /*
     * If the correct points is found its score and feedback are added to the answer in this method
     */
    private void setScoreAndFeedbackForCorrectPoint(QuestionAndAnswer questionAndAnswer, Double marksWorth, String feedback) {
        questionAndAnswer.getAnswer().setScore(questionAndAnswer.getAnswer().getScore() + marksWorth.intValue());
        questionAndAnswer.getAnswer().setFeedback(questionAndAnswer.getAnswer().getFeedback() + feedback + "\n");
        questionAndAnswer.getAnswer().setMarkerApproved(1);
    }

    /*
     * Inserts users inputs in to database
     */
    private void insertInputs(QuestionAndAnswer questionAndAnswer) {
        for (Inputs i : questionAndAnswer.getInputs()) {
            i.setAnswerID(questionAndAnswer.getAnswer().getAnswerID());
            inputsRepo.insert(i);
        }
    }

    /*
     * Checks if the score given is above or below max or minimum scores and alters to match
     */
    void validateScore(Answer answer, Question question) {
        if (answer.getScore() < question.getMinScore()) {
            answer.setScore(question.getMinScore());
        } else if (answer.getScore() > question.getMaxScore()) {
            answer.setScore(question.getMaxScore());
        }
        answerRepo.insert(answer);
    }

    /**
     * Gets the grade and feedback for the user for a given test to be shown in the feedback section
     *
     * @param testID   - the test
     * @param username - the user making the request
     * @return the performance data tailor to only show grade and class average grade
     * @throws SQLException             if issue arises converting image types
     * @throws IllegalArgumentException if test is not publishing grades
     */
    public Performance getGrades(Long testID, String username) throws SQLException, IllegalArgumentException {
        logger.info("Request made for grades for test with id #{} and user: {}", testID, username);
        Tests test = testRepo.selectByTestID(testID);
        User user = userRepo.selectByUsername(username);
        if (test.getPublishGrades() == PUBLISH_TRUE) {
            return populatePerformanceForGrade(test, user);
        }
        throw new IllegalArgumentException("This test is not publishing results or grades yet.");
    }

    /*
     * Checks what result the user has obtained and what the class average is
     * alters them to a corresponding grade minimum
     */
    private Performance populatePerformanceForGrade(Tests test, User user) throws SQLException {
        double classAverage = 0.0;
        int questionTotal = getQuestionTotal(test, 0);
        TestAndResult tar = new TestAndResult();
        List<TestResult> testResults = trRepo.selectByTestID(test.getTestID());
        for (TestResult testResult : testResults) {
            tar = getTestAndResult(test, user, tar, testResult);
            classAverage += testResult.getTestScore();
        }
        prepareMultipleChoiceAnInsertForView(tar);
        String grade = modServ.checkGrade((double) tar.getTestResult().getTestScore() * 100 / tar.getPercentageScore());
        alignTestScoreWithGrade(tar, grade);
        classAverage = ((classAverage * 100) / questionTotal) / testResults.size();
        String avg = modServ.checkGrade(classAverage);
        classAverage = alignClassAverageWithGrade(avg);
        return new Performance(tar, classAverage);
    }

    /*
     * Gets the requested test and the result the student received
     */
    private TestAndResult getTestAndResult(Tests test, User user, TestAndResult tar, TestResult testResult) throws SQLException {
        if (testResult.getStudentID().equals(user.getUserID())) {
            tar = new TestAndResult(test, testResult, modServ.addQuestionsToList(test, user.getUserID()), user);
        }
        return tar;
    }

    /*
     * Checks the class average grade and assigns a matching min score
     */
    private double alignClassAverageWithGrade(String avg) {
        double classAverage;
        switch (avg) {
            case "A*":
                classAverage = 90;
                break;
            case "A":
                classAverage = 80;
                break;
            case "B":
                classAverage = 70;
                break;
            case "C":
                classAverage = 60;
                break;
            case "D":
                classAverage = 50;
                break;
            default:
                classAverage = 40;
                break;
        }
        return classAverage;
    }

    /*
     * Checks what grade the student received and assigns a matching grade
     */
    private void alignTestScoreWithGrade(TestAndResult tar, String grade) {
        switch (grade) {
            case "A*":
                tar.getTestResult().setTestScore(90);
                break;
            case "A":
                tar.getTestResult().setTestScore(80);
                break;
            case "B":
                tar.getTestResult().setTestScore(70);
                break;
            case "C":
                tar.getTestResult().setTestScore(60);
                break;
            case "D":
                tar.getTestResult().setTestScore(50);
                break;
            default:
                tar.getTestResult().setTestScore(40);
                break;
        }
    }

    /*
     * Removes score from options for student to view when scores aren't being published but grades are
     * Removes correct insertions for the same reason
     */
    private void prepareMultipleChoiceAnInsertForView(TestAndResult tar) {
        for (QuestionAndAnswer qa : tar.getQuestions()) {
            qa.getAnswer().setScore(0);
            qa.setCorrectPoints(new ArrayList<>());
            for (Option o : qa.getQuestion().getOptions()) {
                o.setFeedback(null);
                o.setWorthMarks(null);
            }
            if (qa.getQuestion().getQuestion().getQuestionType() == QuestionType.INSERT_THE_WORD) {
                List<Object> insertions = prepareInsertTheWordForStudent(qa.getQuestion().getQuestion());
                qa.getQuestion().getQuestion().setQuestionContent((String) insertions.get(0));
            }
        }
    }

    /*
     * Totals up the questions for the given test
     */
    int getQuestionTotal(Tests test, int questionTotal) {
        for (TestQuestion tq : testQuestionRepo.selectByTestID(test.getTestID())) {
            Question question = questionRepo.selectByID(tq.getQuestionID());
            questionTotal += question.getMaxScore();
        }
        return questionTotal;
    }

    /**
     * Performs the actions necessary to get the performance data and return it to the user on front end
     *
     * @param testID   - the test id
     * @param username - the user
     * @return the performance data
     */
    public Performance getPerformance(Long testID, String username) throws SQLException, IllegalArgumentException {
        logger.info("Request made for performance statistics for test with id #{} and user: {}", testID, username);
        Tests test = testRepo.selectByTestID(testID);
        User user = userRepo.selectByUsername(username);
        if (test.getPublishResults() == PUBLISH_TRUE) {
            int questionTotal = 0;
            double classAverage = 0.0;
            TestAndResult tar = new TestAndResult();
            List<TestResult> testResults = trRepo.selectByTestID(test.getTestID());
            for (TestResult testResult : testResults) {
                classAverage += testResult.getTestScore();
                if (testResult.getStudentID().equals(user.getUserID())) {
                    tar = new TestAndResult(test, testResult, modServ.addQuestionsToList(test, user.getUserID()), user);
                }
            }
            questionTotal = getQuestionTotal(test, questionTotal);
            classAverage = ((classAverage * 100) / questionTotal) / testResults.size();
            return new Performance(tar, classAverage);
        }
        throw new IllegalArgumentException("This test is not publishing results or grades yet.");
    }

    /**
     * Method primes input data to be entered in to the database Ensures data size
     * limits are enforced
     * <p>
     * Ensures input data is correct for a test and not tampered with on front
     * end i.e. attempt to edit a test by a user who is not the tutor for the module
     *
     * @param test     - the test
     * @param username - the principal user
     * @return Test object or null
     */
    public Tests editTest(Tests test, String username) {
        logger.info("Request made to edit a test with id #{} to the database by {}", test.getTestID(), username);
        test.setTestTitle(test.getTestTitle().trim());
        User user = userRepo.selectByUsername(username);
        Module module = modRepo.selectByID(test.getModuleID());
        Long check = modServ.checkValidAssociation(username, test.getModuleID());
        if (check != null && AssociationType.TUTOR == check) {
            if (test.getPractice() == 1) {
                test.setPublishGrades(1);
            }
            try {
                if (checkTestEditIsValid(test, user, module)) return primeTestForUserView(testRepo.insert(test));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * Checks it is okay to edit the test
     * i.e. parameters don't cause problems with database constraints
     */
    private boolean checkTestEditIsValid(Tests test, User user, Module module) throws ParseException {
        // Parse exceptions could be thrown here
        test.setEndDateTime(StringToDateUtil.convertInputDateToCorrectFormat(test.getEndDateTime()));
        test.setStartDateTime(StringToDateUtil.convertInputDateToCorrectFormat(test.getStartDateTime()));
        return test.getTestTitle().length() <= 50 && test.getTestTitle().length() > 0 && user != null && module != null;
    }

    /**
     * Performs necessary actions required to get the test id for view by tutor and ensures that the request is made by the tutor
     *
     * @param username - the principal user
     * @param testID   - the test id number
     * @return the test
     */
    public Tests getByTestID(String username, Long testID) {
        logger.info("Request made for test #{} with tutor info by {}", testID, username);
        Tests test = testRepo.selectByTestID(testID);
        if (modServ.checkValidAssociation(username, test.getModuleID()) != null) {
            return primeTestForUserView(test);
        }
        return null;
    }

    /**
     * Looks for the tests that the user has submitted a script for so that they cannot submit again
     *
     * @param username the user
     * @return the tests that have been answered
     */
    public Set<Integer> getAnsweredTests(String username) {
        logger.info("Request made for answered tests by {}", username);
        Set<Integer> tests = new HashSet<>();
        User user = userRepo.selectByUsername(username);
        List<Answer> answers = answerRepo.selectByAnswererID(user.getUserID());
        for (Answer a : answers) {
            tests.add(a.getTestID().intValue());
        }
        return tests;
    }

    /**
     * Performs the necessary actions required to get the questions currently used by this test and output all the data needed by a tutor
     * also ensure the request is made by the tutor
     *
     * @param username - the principal user
     * @param testID   - the test
     * @return the list of questions being used by this test
     */
    public List<TutorQuestionPojo> getQuestionsByTestIDTutorView(String username, Long testID) throws SQLException {
        logger.info("Request made for questions and all necessary info requited by tutor for test #{} by {}", testID, username);
        Long check = modServ.checkValidAssociation(username, modRepo.selectByID(testRepo.selectByTestID(testID).getModuleID()).getModuleID());
        List<TestQuestion> tqs = testQuestionRepo.selectByTestID(testID);
        if (check != null && AssociationType.TUTOR == check) {
            List<TutorQuestionPojo> tutorQuestions = new LinkedList<>();
            for (TestQuestion tq : tqs) {
                Question q = questionRepo.selectByID(tq.getQuestionID());
                if (q != null) {
                    populateTutorQuestionList(testID, tutorQuestions, q);
                }
            }
            return tutorQuestions;
        }
        return null;
    }

    /**
     * Gets questions in a format that is okay for a student to view while taking the test
     * No mark scheme included etc
     *
     * @param username - the user making the request
     * @param testID   - the test being sat
     * @return the list of question and blank answers
     * @throws SQLException if an error arises converting image from database blob to base64
     */
    public List<QuestionAndAnswer> getQuestionsStudent(String username, Long testID) throws SQLException {
        logger.info("Request made for questions for test #{} by {}", testID, username);
        List<TestQuestion> tqs = testQuestionRepo.selectByTestID(testID);
        if (modServ.checkValidAssociation(username, modRepo.selectByID(testRepo.selectByTestID(testID).getModuleID()).getModuleID()) != null) {
            List<QuestionAndAnswer> questions = new LinkedList<>();
            populateQuestionsForStudent(tqs, questions);
            return questions;
        }
        return null;
    }

    /*
     * Populates the necessary data in to the questions for view
     */
    private void populateQuestionsForStudent(List<TestQuestion> tqs, List<QuestionAndAnswer> questions) throws SQLException {
        for (TestQuestion tq : tqs) {
            Question q = questionRepo.selectByID(tq.getQuestionID());
            if (q != null) {
                List<Inputs> inputs = populateInputsForStudent(q);
                List<OptionEntries> optionEntries = new LinkedList<>();
                List<Option> options = new LinkedList<>();
                if (q.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                    options = populateOptionsForStudent(q);
                    populateOptionEntriesForStudent(q, optionEntries, options);
                }
                QuestionAndAnswer qToAdd = new QuestionAndAnswer(new QuestionAndBase64(prepareFigure(q), options, findMathLines(q.getQuestionID()), q), new Answer(), inputs, optionEntries, null);
                qToAdd.getQuestion().getQuestion().setQuestionFigure(null);
                questions.add(qToAdd);
            }
        }
    }

    /*
     * Prepares the right amount of possible option entries
     */
    private void populateOptionEntriesForStudent(Question q, List<OptionEntries> optionEntries, List<Option> options) {
        if (q.getAllThatApply() == 0) {
            optionEntries.add(new OptionEntries(null, null));
        } else {
            for (int loop = 0; loop < options.size(); loop++) {
                optionEntries.add(new OptionEntries(null, null));
            }
        }
    }

    /*
     * Edits the options to show no marks and feedback
     */
    private List<Option> populateOptionsForStudent(Question q) {
        List<Option> options;
        options = optionRepo.selectByQuestionID(q.getQuestionID());
        for (Option o : options) {
            o.setFeedback("");
            o.setWorthMarks(0);
        }
        return options;
    }

    /*
     * Generate correct amount of inputs for the amount of missing words
     */
    private List<Inputs> populateInputsForStudent(Question q) {
        List<Inputs> inputs = new LinkedList<>();
        if (q.getQuestionType() == QuestionType.INSERT_THE_WORD) {
            List<Object> insertions = prepareInsertTheWordForStudent(q);
            q.setQuestionContent((String) insertions.get(0));
            for (int loop = 0; loop < (Integer) insertions.get(1); loop++) {
                inputs.add(new Inputs("", loop, null, 0));
            }
        }
        return inputs;
    }

    /*
     * Removes marking data from insert the word questions
     */
    private List<Object> prepareInsertTheWordForStudent(Question q) {
        List<CorrectPoint> correctPoints = cpRepo.selectByQuestionID(q.getQuestionID());
        int inputs = 0;
        List<Object> insertions = new ArrayList<>();
        inputs = removeCorrectPhraseFromQuestionForInsertTheWord(q, correctPoints, inputs);
        insertions.add(q.getQuestionContent());
        insertions.add(inputs);
        return insertions;
    }

    /*
     * Removes correct phrase from question content in insert the word and replaces with blank area
     */
    private int removeCorrectPhraseFromQuestionForInsertTheWord(Question q, List<CorrectPoint> correctPoints, int inputs) {
        for (CorrectPoint cp : correctPoints) {
            if (q.getQuestionContent().contains("[[" + cp.getPhrase() + "]]")) {
                inputs++;
                q.setQuestionContent(q.getQuestionContent().replaceAll("\\[\\[" + cp.getPhrase() + "\\]\\]", inputs + "._____"));
            }
        }
        return inputs;
    }

    /**
     * Performs the necessary actions need to all the questions written by the tutor that aren't currently being used by this given test
     *
     * @param username - the principal user
     * @param testID   - the test
     * @return the questions not currently being used by this test
     */
    public List<TutorQuestionPojo> getOldQuestions(String username, Long testID) throws SQLException {
        logger.info("Request made for all old questions that aren't being used by test #{}", testID);
        List<TutorQuestionPojo> currents = getQuestionsByTestIDTutorView(username, testID);
        List<TutorQuestionPojo> allTutorQuestions = new LinkedList<>();
        List<TutorQuestionPojo> tutorQuestionsToRemove = new ArrayList<>();
        for (Question q : questionRepo.selectByCreatorID(userRepo.selectByUsername(username).getUserID())) {
            populateTutorQuestionList(testID, allTutorQuestions, q);
        }
        getQuestionsToRemove(currents, allTutorQuestions, tutorQuestionsToRemove);
        allTutorQuestions.removeAll(tutorQuestionsToRemove);
        return allTutorQuestions;
    }

    /*
     * Gets the questions that need to be removed from old questions list
     * i.e. ones already being used in current test
     */
    private void getQuestionsToRemove(List<TutorQuestionPojo> currents, List<TutorQuestionPojo> allTutorQuestions, List<TutorQuestionPojo> tutorQuestionsToRemove) {
        for (TutorQuestionPojo next : allTutorQuestions) {
            for (TutorQuestionPojo c : currents) {
                if (next.getQuestion().getQuestionID().equals(c.getQuestion().getQuestionID())) {
                    tutorQuestionsToRemove.add(next);
                }
            }
        }
    }

    /*
     * Gets all questions that belong to tutor
     */
    private void populateTutorQuestionList(Long testID, List<TutorQuestionPojo> allTutorQuestions, Question q) throws SQLException {
        TutorQuestionPojo tqToAdd = new TutorQuestionPojo(testID, prepareFigure(q), q, findOptions(q.getQuestionID()), findMathLines(q.getQuestionID()), findCorrectPoints(q.getQuestionID()));
        tqToAdd.getQuestion().setQuestionFigure(null);
        allTutorQuestions.add(tqToAdd);
    }

    /*
     * Cals method that changes blob type to base64
     */
    private String prepareFigure(Question q) throws SQLException {
        return BlobUtil.blobToBase(q.getQuestionFigure());
    }

    /*
     * Finds all the necessary options that are associated with this question, if it is a question that uses this type of input
     */
    private List<Option> findOptions(Long questionID) {
        return optionRepo.selectByQuestionID(questionID);
    }

    /*
     * Find all the correct points that associated with this question
     */
    List<CorrectPoint> findCorrectPoints(Long questionID) {
        List<CorrectPoint> correctPoints = cpRepo.selectByQuestionID(questionID);
        for (CorrectPoint cp : correctPoints) {
            cp.setAlternatives(findAlternatives(cp.getCorrectPointID()));
        }
        return correctPoints;
    }

    /*
     * Returns the math lines for the question
     */
    List<QuestionMathLine> findMathLines(Long questionID) { return questionMathLineRepo.selectByQuestionID(questionID); }

    /*
     * finds all the alternatives that are equivalent to given correct points
     */
    private List<Alternative> findAlternatives(Long correctPointID) {
        return alternativeRepo.selectByCorrectPointID(correctPointID);
    }

    /*
     * Primes the test dates to make them readable by the users on the front end
     */
    Tests primeTestForUserView(Tests test) {
        if (test != null) {
            try {
                test.setStartDateTime(StringToDateUtil.convertDateToFrontEndFormat(test.getStartDateTime()));
                test.setEndDateTime(StringToDateUtil.convertDateToFrontEndFormat(test.getEndDateTime()));
            } catch (ParseException e) { e.printStackTrace(); }
        }
        return test;
    }

    /**
     * Carries out actions need to enter a question in to the database
     *
     * @param questionData - collection of all question data available to tutor
     * @param username     - the principal user
     * @return the collection of all question data available to tutor after insertion
     * @throws SQLException image conversion
     */
    public TutorQuestionPojo newQuestion(TutorQuestionPojo questionData, String username, Boolean update) throws SQLException {
        logger.info("Request made to add new question in to the database by {}", username);
        Long check = modServ.checkValidAssociation(username, testRepo.selectByTestID(questionData.getTestID()).getModuleID());
        if (check != null && AssociationType.TUTOR == check) {
            List<CorrectPoint> correctPoints = prepareQuestionGeneral(questionData, username, update);
            // Insert the word and Text-based
            prepareNonMultipleChoice(questionData, update, correctPoints);
            // Multiple choice
            prepareMultipleChoice(questionData, update);
            questionData.setBase64(BlobUtil.blobToBase(questionData.getQuestion().getQuestionFigure()));
            questionData.getQuestion().setQuestionFigure(null);
            return questionData;
        }
        return null;
    }

    /*
     * Sets the option for question in the question data to be output
     */
    private void prepareMultipleChoice(TutorQuestionPojo questionData, Boolean update) {
        if (questionData.getQuestion().getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
            questionData.setOptions(addOptions(questionData.getQuestion().getQuestionID(), questionData.getOptions(), update));
        }
    }

    /*
     * Sets correct points in question data to be output
     */
    private void prepareNonMultipleChoice(TutorQuestionPojo questionData, Boolean update, List<CorrectPoint> correctPoints) {
        if (!questionData.getQuestion().getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
            questionData.setCorrectPoints(addCorrectPoints(correctPoints, questionData.getQuestion().getQuestionID(), update));
        }
    }

    /*
     * Generic preparation for all question types to be output to tutor
     */
    private List<CorrectPoint> prepareQuestionGeneral(TutorQuestionPojo questionData, String username, Boolean update) throws SQLException {
        Question question = questionData.getQuestion();
        if (question.getQuestionID() == null || question.getQuestionID() < 1) {
            question.setQuestionID(-1L);
        }
        if (questionData.getBase64() != null) {
            question.setQuestionFigure(BlobUtil.baseToBlob(questionData.getBase64()));
        }
        List<CorrectPoint> correctPoints = questionData.getCorrectPoints();
        User user = userRepo.selectByUsername(username);
        question.setCreatorID(user.getUserID());
        questionData.setQuestion(questionRepo.insert(question));
        questionData.setMathLines(addMathLines(questionData.getQuestion().getQuestionID(), questionData.getMathLines()));
        if (!update) {
            testQuestionRepo.insert(new TestQuestion(questionData.getTestID(), questionRepo.insert(question).getQuestionID()));
        }
        return correctPoints;
    }


    /**
     * Carries out actions needed to add an existing question in to a new test
     * The question must have been created by the requesting user and they
     * must be a tutor of the module
     *
     * @param questionID the id of the question
     * @param testID     the id of the test
     * @param username   the user
     * @return the test question record
     */
    public TestQuestion addExistingQuestion(Long questionID, Long testID, String username) {

        logger.info("Request made to add question #{} in to test #{} by {}", questionID, testID, username);

        Tests test = testRepo.selectByTestID(testID);
        Question question = questionRepo.selectByID(questionID);
        Long check = modServ.checkValidAssociation(username, test.getModuleID());
        if (check != null && check == AssociationType.TUTOR && question.getCreatorID().equals(userRepo.selectByUsername(username).getUserID())) {
            return testQuestionRepo.insert(new TestQuestion(testID, questionID));
        }

        return null;
    }

    /**
     * Method to duplicate a question to ensure that if you want to edit it it doesnt affect answering of times it has been used before
     *
     * @param questionID - the question to duplicate
     * @param username   - the user requesting it
     * @return success / failure
     */
    public Boolean duplicateQuestion(Long questionID, String username) {
        logger.info("Request made to duplicate question #{} by {}", questionID, username);
        Question question = questionRepo.selectByID(questionID);
        List<Option> options = optionRepo.selectByQuestionID(question.getQuestionID());
        List<CorrectPoint> cps = cpRepo.selectByQuestionID(question.getQuestionID());
        getAlternativesForCorrectPoints(cps);
        User user = userRepo.selectByUsername(username);
        if (question.getCreatorID().equals(user.getUserID())) {
            addDetailsForSpecificQuestionTypes(question, options, cps, question);
            return true;
        }
        return false;
    }

    /*
     * Certain things need to be duplicated for certain question types such as options / correct points
     */
    private void addDetailsForSpecificQuestionTypes(Question question, List<Option> options, List<CorrectPoint> cps, Question newQuestion) {
        newQuestion.setQuestionID(-1L);
        newQuestion = questionRepo.insert(newQuestion);
        addMathLines(newQuestion.getQuestionID(), questionMathLineRepo.selectByQuestionID(question.getQuestionID()));
        duplicateForMultipleChoice(question, options, newQuestion);
        duplicateForNotMultipleChoice(question, cps, newQuestion);
    }

    /*
     * Finds each alternative that belongs to each correct point in a list
     */
    private void getAlternativesForCorrectPoints(List<CorrectPoint> cps) {
        for (CorrectPoint cp : cps) {
            cp.setAlternatives(alternativeRepo.selectByCorrectPointID(cp.getCorrectPointID()));
        }
    }

    /*
     * Duplicates a question that isn't multiple choice
     */
    private void duplicateForNotMultipleChoice(Question question, List<CorrectPoint> cps, Question newQuestion) {
        if (question.getQuestionType() != QuestionType.MULTIPLE_CHOICE) {
            for (CorrectPoint cp : cps) {
                cp.setCorrectPointID(-1L);
                cp.setQuestionID(newQuestion.getQuestionID());
                CorrectPoint newCorrectPoint = cpRepo.insert(cp);
                for (Alternative alt : cp.getAlternatives()) {
                    alt.setAlternativeID(-1L);
                    alt.setCorrectPointID(newCorrectPoint.getCorrectPointID());
                    alternativeRepo.insert(alt);
                }
            }
        }
    }

    /*
     * Duplicates a question that is a multiple choice
     */
    private void duplicateForMultipleChoice(Question question, List<Option> options, Question newQuestion) {
        if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
            for (Option opt : options) {
                opt.setOptionID(-1L);
                opt.setQuestionID(newQuestion.getQuestionID());
                optionRepo.insert(opt);
            }
        }
    }

    /*
     * Carries out actions needed to add correct points in to the database
     */
    List<CorrectPoint> addCorrectPoints(List<CorrectPoint> correctPoints, Long questionID, Boolean update) {
        Question question = questionRepo.selectByID(questionID);
        Map<Integer, CorrectPoint> correctPointTreeMap = new TreeMap<>();
        if (question.getQuestionType() == QuestionType.INSERT_THE_WORD) {
            correctPoints.addAll(cpRepo.selectByQuestionID(questionID));
            sortCorrectPointsBaseOnAppearanceInQuestion(correctPoints, question, correctPointTreeMap);
        }
        if (correctPoints.size() > 0) {
            prepareCorrectPointsForEntry(correctPoints, questionID, update);
        }
        return correctPoints;
    }

    /*
     * Prepares each correct point for entry in to database
     */
    private void prepareCorrectPointsForEntry(List<CorrectPoint> correctPoints, Long questionID, Boolean update) {
        for (CorrectPoint cp : correctPoints) {
            cp.setQuestionID(questionID);
            if (cp.getCorrectPointID() == null || cp.getCorrectPointID() < 1) {
                cp.setCorrectPointID(-1L);
            }
            cp = cpRepo.insert(cp);
            cp.setAlternatives(addAlternatives(cp.getCorrectPointID(), cp.getAlternatives(), update));
        }
    }

    /*
     * Aligns correct point in correct order when they belong to an insert the word question
     */
    private void sortCorrectPointsBaseOnAppearanceInQuestion(List<CorrectPoint> correctPoints, Question question, Map<Integer, CorrectPoint> correctPointTreeMap) {
        for (CorrectPoint c : correctPoints) {
            correctPointTreeMap.put(question.getQuestionContent().indexOf("[[" + c.getPhrase() + "]]"), c);
        }
        correctPoints.clear();
        int loop = 0;
        for (Map.Entry<Integer, CorrectPoint> entry : correctPointTreeMap.entrySet()) {
            entry.getValue().setIndexedAt(loop++);
            correctPoints.add(entry.getValue());
        }
    }

    /*
     * Carries out actions need to input alternatives in to the database
     */
    List<Alternative> addAlternatives(Long correctPointID, List<Alternative> alternatives, Boolean update) {
        if (alternatives != null && alternatives.size() > 0) {
            for (Alternative alt : alternatives) {
                alt.setCorrectPointID(correctPointID);
                if (alt.getAlternativeID() == null || alt.getAlternativeID() < 1) {
                    alt.setAlternativeID(-1L);
                }
                Alternative returned = alternativeRepo.insert(alt);
                alt.setAlternativeID(returned.getAlternativeID());
            }
        }
        return alternatives;
    }

    /*
     * Adds question math lines for question to database
     */
    private List<QuestionMathLine> addMathLines(Long questionID, List<QuestionMathLine> questionMathLines) {
        if (questionMathLines != null && questionMathLines.size() > 0) {
            for (QuestionMathLine qm : questionMathLines) {
                qm.setQuestionID(questionID);
                qm.setQuestionMathLineID(questionMathLineRepo.insert(qm).getQuestionMathLineID());
            }
            return questionMathLines;
        }
        return null;
    }

    /*
     * Adds options for question in to database
     */
    private List<Option> addOptions(Long questionID, List<Option> options, Boolean update) {
        if (options != null && options.size() > 0) {
            for (Option option : options) {
                option.setQuestionID(questionID);
                if (option.getOptionID() == null || option.getOptionID() < 1) {
                    option.setOptionID(-1L);
                }
                Option returned = optionRepo.insert(option);
                option.setOptionID(returned.getOptionID());
            }
        }
        return options;
    }

    /**
     * Method to carry out necessary actions needed for removing a question from a test,
     * checks that the users making the request is the tutor before allowing
     *
     * @param testID     the test id
     * @param questionID the questions id
     * @param username   the user who requests removal
     * @return boolean indicating whether request was completed of not
     */
    public Boolean removeQuestionFromTest(Long testID, Long questionID, String username) {
        logger.info("Request made to remove question #{} from test #{} by {}", questionID, testID, username);
        Long check = modServ.checkValidAssociation(username, modRepo.selectByID(testRepo.selectByTestID(testID).getModuleID()).getModuleID());
        if (check != null && AssociationType.TUTOR == check) {
            List<TestQuestion> testQuestions = testQuestionRepo.selectByTestID(testID);
            for (TestQuestion tq : testQuestions) {
                if (tq.getQuestionID().equals(questionID)) {
                    testQuestionRepo.delete(tq.getTestQuestionID());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes a math line from a question after it has been inserted in to the db
     *
     * @param questionMathLineID - the math line to delete
     * @param username           - the user performing the action
     * @return success / failure
     */
    public Boolean removeQuestionMathLine(Long questionMathLineID, String username) {
        logger.info("Request made to remove question math line #{} by {}", questionMathLineID, username);

        QuestionMathLine qm = questionMathLineRepo.selectByID(questionMathLineID);
        User user = userRepo.selectByUsername(username);

        if (user.getUserID().equals(questionRepo.selectByID(qm.getQuestionID()).getCreatorID())) {
            questionMathLineRepo.delete(questionMathLineID);
            return true;
        }
        return false;
    }

    /**
     * Removes a correct point form question after it has been entered to database
     *
     * @param correctPointID - the correct point to delete
     * @param username       - the user making the request
     * @return success / failure
     */
    public Boolean removeCorrectPoint(Long correctPointID, String username) {
        logger.info("Request made to remove correct point #{} by {}", correctPointID, username);
        CorrectPoint cp = cpRepo.selectByID(correctPointID);
        User user = userRepo.selectByUsername(username);
        if (user.getUserID().equals(questionRepo.selectByID(cp.getQuestionID()).getCreatorID())) {
            List<Alternative> alts = alternativeRepo.selectByCorrectPointID(correctPointID);
            for (Alternative alt : alts) {
                alternativeRepo.delete(alt.getAlternativeID());
            }
            cpRepo.delete(correctPointID);
            return true;
        }
        return false;
    }

    /**
     * Removes an alternative from a correct point after it has been entered in to the database
     *
     * @param alternativeID - the alternative to delete
     * @param username      - the user making the request
     * @return success / failure
     */
    public Boolean removeAlternative(Long alternativeID, String username) {
        logger.info("Request made to remove alternative #{} by {}", alternativeID, username);
        User user = userRepo.selectByUsername(username);
        Alternative alt = alternativeRepo.selectByID(alternativeID);
        CorrectPoint cp = cpRepo.selectByID(alt.getCorrectPointID());
        if (user.getUserID().equals(questionRepo.selectByID(cp.getQuestionID()).getCreatorID())) {
            alternativeRepo.delete(alt.getAlternativeID());
            return true;
        }
        return false;
    }

    /**
     * Removes an option from a question after it has been added to the database
     *
     * @param optionID - the option to remove
     * @param username - the user requesting the removal
     * @return success / failure
     */
    public Boolean removeOption(Long optionID, String username) {
        logger.info("Request made to remove option #{} by {}", optionID, username);
        User user = userRepo.selectByUsername(username);
        Option option = optionRepo.selectByID(optionID);
        if (user.getUserID().equals(questionRepo.selectByID(option.getQuestionID()).getCreatorID())) {
            optionRepo.delete(option.getOptionID());
            return true;
        }
        return false;
    }

    /**
     * Method to carry out necessary actions to delete a test from the database
     * Requires that the user requesting the delete is the tutor of the module the test belongs to
     *
     * @param testID   the test's id
     * @param username the user
     * @return true or false on whether the test is deleted of not
     */
    public Boolean deleteTest(Long testID, String username) {
        logger.info("Request made to delete test #{} by {}", testID, username);
        Long check = modServ.checkValidAssociation(username, modRepo.selectByID(testRepo.selectByTestID(testID).getModuleID()).getModuleID());
        if (check != null && AssociationType.TUTOR == check) {
            testRepo.delete(testID);
            return true;
        }
        return false;
    }

    /**
     * Performs necessary actions needed schedule a test for release
     *
     * @param testID   the test's id
     * @param username the user making the request
     * @return the true/false flag
     */
    public Boolean scheduleTest(Long testID, String username) throws ParseException {
        logger.info("Request made to schedule test #{} by {}", testID, username);
        Long check = modServ.checkValidAssociation(username, modRepo.selectByID(testRepo.selectByTestID(testID).getModuleID()).getModuleID());
        if (check != null && AssociationType.TUTOR == check) {
            Tests test = testRepo.selectByTestID(testID);

            if (test.getScheduled().equals(SCHEDULED)) {
                test.setScheduled(UNSCHEDULED);
            } else {
                test.setScheduled(SCHEDULED);
                sendNewTestToAssociates(test);
            }
            testRepo.insert(test);
            return true;
        }
        return false;
    }

    /*
     * Emails students that a new test has been added
     */
    private void sendNewTestToAssociates(Tests test) throws ParseException {
        List<ModuleAssociation> moduleAssociations = moduleAssociationRepo.selectByModuleID(test.getModuleID());
        Module module = modRepo.selectByID(test.getModuleID());
        for (ModuleAssociation moduleAssociation : moduleAssociations) {
            if (moduleAssociation.getAssociationType().equals(AssociationType.STUDENT)) {
                User user = userRepo.selectByUserID(moduleAssociation.getUserID());
                emailSender.sendNewTestEmail(test, user, module);
            }
        }
    }
}