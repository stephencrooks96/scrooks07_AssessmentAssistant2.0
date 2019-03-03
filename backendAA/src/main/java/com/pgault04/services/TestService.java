package com.pgault04.services;

import com.pgault04.entities.*;
import com.pgault04.pojos.*;
import com.pgault04.repositories.*;
import com.pgault04.utilities.BlobUtil;
import com.pgault04.utilities.StringToDateUtil;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

/**
 * @author Paul Gault - 40126005
 * @since December 2018
 */
@Service
public class TestService {


    public static final int SCHEDULED = 1;
    public static final int UNSCHEDULED = 0;
    public static final int PUBLISH_TRUE = 1;
    /**
     * Logs useful info for debugging and analysis needs
     */
    private static final Logger logger = LogManager.getLogger(TestService.class);
    @Autowired
    TestsRepo testRepo;

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

        test.setTestID(-1L);
        test.setScheduled(0);
        test.setPublishResults(0);
        test.setTestTitle(test.getTestTitle().trim());
        User user = userRepo.selectByUsername(username);
        Module module = modRepo.selectByModuleID(test.getModuleID());

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

    public boolean submitTest(List<QuestionAndAnswer> script, String username) {
        logger.info("Request made to add a test to the database by {}", username);

        User student = userRepo.selectByUsername(username);
        Tests test = testRepo.selectByTestID(script.get(0).getAnswer().getTestID());
        Module module = modRepo.selectByModuleID(test.getModuleID());
        User tutor = userRepo.selectByUserID(module.getTutorUserID());

        for (QuestionAndAnswer questionAndAnswer : script) {
            questionAndAnswer.getAnswer().setQuestionID(questionAndAnswer.getQuestion().getQuestion().getQuestionID());
            questionAndAnswer.getAnswer().setAnswererID(student.getUserID());
            questionAndAnswer.getAnswer().setMarkerID(tutor.getUserID());
            questionAndAnswer.getAnswer().setTutorApproved(0);
            questionAndAnswer.getAnswer().setMarkerApproved(0);

            Answer answer = answerRepo.insert(questionAndAnswer.getAnswer());

            if (questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
                autoMarkMultipleChoice(questionAndAnswer);
            }

            if (questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.INSERT_THE_WORD) || questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.TEXT_MATH)  || questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.TEXT_BASED)) {
                autoMarkCorrectPoints(questionAndAnswer);
            }

        }
        return true;
    }

    public void autoMarkMultipleChoice(QuestionAndAnswer questionAndAnswer) {

        Question question = questionRepo.selectByQuestionID(questionAndAnswer.getAnswer().getQuestionID());
        List<Option> options = findOptions(questionAndAnswer.getAnswer().getQuestionID());
        options.sort(Collections.reverseOrder(Comparator.comparingDouble(Option::getWorthMarks)));

        questionAndAnswer.getAnswer().setScore(0);
        questionAndAnswer.getAnswer().setFeedback("");
        if (options.size() > 0) {
            for (Option o : options) {

                for (OptionEntries oe : questionAndAnswer.getOptionEntries()) {
                    if (o.getOptionID().equals(oe.getOptionID())) {
                        oe.setAnswerID(questionAndAnswer.getAnswer().getAnswerID());
                        optionEntriesRepo.insert(oe);
                        questionAndAnswer.getAnswer().setScore(questionAndAnswer.getAnswer().getScore() + o.getWorthMarks());
                        questionAndAnswer.getAnswer().setFeedback(questionAndAnswer.getAnswer().getFeedback() + "\n" + o.getFeedback());
                        questionAndAnswer.getAnswer().setMarkerApproved(1);
                    }
                }

            }
            validateScore(questionAndAnswer.getAnswer(), question);
        }
    }

    public void autoMarkCorrectPoints(QuestionAndAnswer questionAndAnswer) {

        Question question = questionRepo.selectByQuestionID(questionAndAnswer.getAnswer().getQuestionID());
        List<CorrectPoint> correctPoints = findCorrectPoints(questionAndAnswer.getAnswer().getQuestionID());
        correctPoints.sort(Collections.reverseOrder(Comparator.comparingDouble(CorrectPoint::getMarksWorth)));

        questionAndAnswer.getAnswer().setScore(0);
        questionAndAnswer.getAnswer().setFeedback("");
        if (questionAndAnswer.getAnswer().getContent() != null) {
            questionAndAnswer.getAnswer().setContent(questionAndAnswer.getAnswer().getContent().trim());
        }

        if (question.getQuestionType().equals(QuestionType.INSERT_THE_WORD) || question.getQuestionType().equals(QuestionType.TEXT_MATH)) {

            for (Inputs i : questionAndAnswer.getInputs()) {
                i.setAnswerID(questionAndAnswer.getAnswer().getAnswerID());
                i = inputsRepo.insert(i);
            }
            for (CorrectPoint c : correctPoints) {
                for (Inputs i : questionAndAnswer.getInputs()) {
                    if (question.getQuestionType().equals(QuestionType.INSERT_THE_WORD)) {
                        if (i.getInputValue().equalsIgnoreCase(c.getPhrase()) && i.getInputIndex().equals(c.getIndexedAt())) {
                            questionAndAnswer.getAnswer().setScore(questionAndAnswer.getAnswer().getScore() + c.getMarksWorth().intValue());
                            questionAndAnswer.getAnswer().setFeedback(questionAndAnswer.getAnswer().getFeedback() + "\n" + c.getFeedback());
                            questionAndAnswer.getAnswer().setMarkerApproved(1);
                        } else {
                            for (Alternative alt : alternativeRepo.selectByCorrectPointID(c.getCorrectPointID())) {
                                if (i.getInputValue().equalsIgnoreCase(alt.getAlternativePhrase()) && i.getInputIndex().equals(c.getIndexedAt())) {
                                    questionAndAnswer.getAnswer().setScore(questionAndAnswer.getAnswer().getScore() + c.getMarksWorth().intValue());
                                    questionAndAnswer.getAnswer().setFeedback(questionAndAnswer.getAnswer().getFeedback() + "\n" + c.getFeedback());
                                    break;
                                }
                            }
                        }
                    } else {
                        if (i.getInputValue().trim().equalsIgnoreCase(c.getPhrase())) {
                            questionAndAnswer.getAnswer().setScore(questionAndAnswer.getAnswer().getScore() + c.getMarksWorth().intValue());
                            questionAndAnswer.getAnswer().setFeedback(questionAndAnswer.getAnswer().getFeedback() + "\n" + c.getFeedback());
                            break;
                        } else {
                            boolean altBroken = false;
                            for (Alternative alt : alternativeRepo.selectByCorrectPointID(c.getCorrectPointID())) {
                                if (i.getInputValue().contains(alt.getAlternativePhrase())) {
                                    questionAndAnswer.getAnswer().setScore(questionAndAnswer.getAnswer().getScore() + c.getMarksWorth().intValue());
                                    questionAndAnswer.getAnswer().setFeedback(questionAndAnswer.getAnswer().getFeedback() + "\n" + c.getFeedback());
                                    altBroken = true;
                                    break;
                                }
                            }
                            if (altBroken) {
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            for (CorrectPoint cp : correctPoints) {
                if (questionAndAnswer.getAnswer().getContent().toLowerCase().contains(cp.getPhrase().toLowerCase())) {
                    questionAndAnswer.getAnswer().setScore(questionAndAnswer.getAnswer().getScore() + cp.getMarksWorth().intValue());
                    questionAndAnswer.getAnswer().setFeedback(questionAndAnswer.getAnswer().getFeedback() + "\n" + cp.getFeedback());
                } else {
                    for (Alternative alt : alternativeRepo.selectByCorrectPointID(cp.getCorrectPointID())) {
                        if (questionAndAnswer.getAnswer().getContent().toLowerCase().contains(alt.getAlternativePhrase().toLowerCase())) {
                            questionAndAnswer.getAnswer().setScore(questionAndAnswer.getAnswer().getScore() + cp.getMarksWorth().intValue());
                            questionAndAnswer.getAnswer().setFeedback(questionAndAnswer.getAnswer().getFeedback() + "\n" + cp.getFeedback());
                            break;
                        }
                    }
                }
            }
        }
        validateScore(questionAndAnswer.getAnswer(), question);
    }

    void validateScore(Answer answer, Question question) {
        if (answer.getScore() < question.getMinScore()) {
            answer.setScore(question.getMinScore());
        } else if (answer.getScore() > question.getMaxScore()) {
            answer.setScore(question.getMaxScore());
        }
        answerRepo.insert(answer);
    }

    public Performance getGrades(Long testID, String username) throws SQLException, IllegalArgumentException {
        logger.info("Request made for grades for test with id #{} and user: {}", testID, username);

        Tests test = testRepo.selectByTestID(testID);
        User user = userRepo.selectByUsername(username);
        if (test.getPublishGrades() == PUBLISH_TRUE) {
            int questionTotal = 0;
            double classAverage = 0.0;

            for (TestQuestion tq : testQuestionRepo.selectByTestID(test.getTestID())) {
                Question question = questionRepo.selectByQuestionID(tq.getQuestionID());
                questionTotal += question.getMaxScore();
            }
            TestAndResult tar = new TestAndResult();
            List<TestResult> testResults = trRepo.selectByTestID(test.getTestID());
            for (TestResult testResult : testResults) {
                if (testResult.getStudentID().equals(user.getUserID())) {
                    tar = new TestAndResult(test, testResult, modServ.addQuestionsToList(test, user.getUserID()), user);
                }
                classAverage += testResult.getTestScore();
            }
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
            String grade = modServ.checkGrade((double) tar.getTestResult().getTestScore() * 100 / tar.getPercentageScore());
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
            classAverage = ((classAverage * 100) / questionTotal) / testResults.size();
            String avg = modServ.checkGrade(classAverage);
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
            return new Performance(tar, classAverage);
        }
        throw new IllegalArgumentException("This test is not publishing results or grades yet.");
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

            for (TestQuestion tq : testQuestionRepo.selectByTestID(test.getTestID())) {
                questionTotal += questionRepo.selectByQuestionID(tq.getQuestionID()).getMaxScore();
            }

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
        Module module = modRepo.selectByModuleID(test.getModuleID());

        if (AssociationType.TUTOR == modServ.checkValidAssociation(username, test.getModuleID())) {
            try {
                // Parse exceptions could be thrown here
                test.setEndDateTime(StringToDateUtil.convertInputDateToCorrectFormat(test.getEndDateTime()));
                test.setStartDateTime(StringToDateUtil.convertInputDateToCorrectFormat(test.getStartDateTime()));
                if (test.getTestTitle().length() <= 50
                        && test.getTestTitle().length() > 0
                        && user != null
                        && module != null) {
                    return primeTestForUserView(testRepo.insert(test));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
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
    public List<TutorQuestionPojo> getQuestionsByTestIDTutorView(String username, Long testID) throws Base64DecodingException, SQLException {
        logger.info("Request made for questions and all necessary info requited by tutor for test #{} by {}", testID, username);

        List<TestQuestion> tqs = testQuestionRepo.selectByTestID(testID);
        if (AssociationType.TUTOR == modServ.checkValidAssociation(username, modRepo.selectByModuleID(testRepo.selectByTestID(testID).getModuleID()).getModuleID())) {
            List<TutorQuestionPojo> tutorQuestions = new LinkedList<>();
            for (TestQuestion tq : tqs) {
                Question q = questionRepo.selectByQuestionID(tq.getQuestionID());
                if (q != null) {
                    tutorQuestions = populateTutorQuestionList(testID, tutorQuestions, q);
                }
            }
            return tutorQuestions;
        }
        return null;
    }

    public List<QuestionAndAnswer> getQuestionsStudent(String username, Long testID) throws Base64DecodingException, SQLException {
        logger.info("Request made for questions for test #{} by {}", testID, username);

        List<TestQuestion> tqs = testQuestionRepo.selectByTestID(testID);
        if (modServ.checkValidAssociation(username, modRepo.selectByModuleID(testRepo.selectByTestID(testID).getModuleID()).getModuleID()) != null) {
            List<QuestionAndAnswer> questions = new LinkedList<>();
            for (TestQuestion tq : tqs) {
                Question q = questionRepo.selectByQuestionID(tq.getQuestionID());
                if (q != null) {
                    List<Inputs> inputs = new LinkedList<>();
                    if (q.getQuestionType() == QuestionType.INSERT_THE_WORD) {
                        List<Object> insertions = prepareInsertTheWordForStudent(q);
                        q.setQuestionContent((String) insertions.get(0));
                        for (int loop = 0; loop < (Integer) insertions.get(1); loop++) {
                            inputs.add(new Inputs("", loop, null, 0));
                        }
                    }
                    List<OptionEntries> optionEntries = new LinkedList<>();
                    List<Option> options = new LinkedList<>();
                    if (q.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                        options = optionRepo.selectByQuestionID(q.getQuestionID());
                        for (Option o : options) {
                            o.setFeedback("");
                            o.setWorthMarks(0);
                        }
                        if (q.getAllThatApply() == 0) {
                            optionEntries.add(new OptionEntries(null, null));
                        } else {
                            for (int loop = 0; loop < options.size(); loop++) {
                                optionEntries.add(new OptionEntries(null, null));
                            }
                        }
                    }
                    QuestionAndAnswer qToAdd = new QuestionAndAnswer(new QuestionAndBase64(prepareFigure(q), options, findMathLines(q.getQuestionID()), q), new Answer(), inputs, optionEntries, null);
                    qToAdd.getQuestion().getQuestion().setQuestionFigure(null);
                    questions.add(qToAdd);
                }
            }
            return questions;
        }
        return null;
    }

    public List<Object> prepareInsertTheWordForStudent(Question q) {
        List<CorrectPoint> correctPoints = cpRepo.selectByQuestionID(q.getQuestionID());
        int inputs = 0;
        List<Object> insertions = new ArrayList<>();
        for (CorrectPoint cp : correctPoints) {
            if (q.getQuestionContent().contains("[[" + cp.getPhrase() + "]]")) {
                inputs++;
                q.setQuestionContent(q.getQuestionContent().replaceAll("\\[\\[" + cp.getPhrase() + "\\]\\]", inputs + "._____"));
            }
        }
        insertions.add(q.getQuestionContent());
        insertions.add(inputs);
        return insertions;
    }

    /**
     * Performs the necessary actions need to all the questions written by the tutor that aren't currently being used by this given test
     *
     * @param username - the principal user
     * @param testID   - the test
     * @return the questions not currently being used by this test
     */
    public List<TutorQuestionPojo> getOldQuestions(String username, Long testID) throws Base64DecodingException, SQLException {
        logger.info("Request made for all old questions that aren't being used by test #{}", testID);

        List<TutorQuestionPojo> currents = getQuestionsByTestIDTutorView(username, testID);
        List<TutorQuestionPojo> allTutorQuestions = new LinkedList<>();
        List<TutorQuestionPojo> tutorQuestionsToRemove = new ArrayList<>();

        for (Question q : questionRepo.selectByCreatorID(userRepo.selectByUsername(username).getUserID())) {
            allTutorQuestions = populateTutorQuestionList(testID, allTutorQuestions, q);
        }

        for (TutorQuestionPojo next : allTutorQuestions) {
            for (TutorQuestionPojo c : currents) {
                if (next.getQuestion().getQuestionID().equals(c.getQuestion().getQuestionID())) {
                    tutorQuestionsToRemove.add(next);
                }
            }
        }

        allTutorQuestions.removeAll(tutorQuestionsToRemove);
        return allTutorQuestions;
    }

    private List<TutorQuestionPojo> populateTutorQuestionList(Long testID, List<TutorQuestionPojo> allTutorQuestions, Question q) throws SQLException, Base64DecodingException {
        TutorQuestionPojo tqToAdd = new TutorQuestionPojo(testID, q, findOptions(q.getQuestionID()), findMathLines(q.getQuestionID()), findCorrectPoints(q.getQuestionID()));
        tqToAdd.setBase64(prepareFigure(q));
        tqToAdd.getQuestion().setQuestionFigure(null);
        allTutorQuestions.add(tqToAdd);
        return allTutorQuestions;
    }

    private String prepareFigure(Question q) throws SQLException, Base64DecodingException {
        return BlobUtil.blobToBase(q.getQuestionFigure());
    }

    /**
     * Finds all the necessary options that are associated with this question, if it is a question that uses this type of input
     *
     * @param questionID - the question
     * @return the answerable options for this question
     */
    private List<Option> findOptions(Long questionID) {
        return optionRepo.selectByQuestionID(questionID);
    }

    /**
     * Find all the correct points that associated with this question
     *
     * @param questionID the questions id
     * @return the correct points for the question
     */
    public List<CorrectPoint> findCorrectPoints(Long questionID) {
        List<CorrectPoint> correctPoints = cpRepo.selectByQuestionID(questionID);
        for (CorrectPoint cp : correctPoints) {
            cp.setAlternatives(findAlternatives(cp.getCorrectPointID()));
        }
        return correctPoints;
    }

    /**
     * Returns the math lines for the question
     * @param questionID the question
     * @return the math lines
     */
    public List<QuestionMathLine> findMathLines(Long questionID) { return questionMathLineRepo.selectByQuestionID(questionID); }

    /**
     * finds all the alternatives that are equivalent to given correct points
     *
     * @param correctPointID - the correct point
     * @return - its alternatives
     */
    private List<Alternative> findAlternatives(Long correctPointID) {
        return alternativeRepo.selectByCorrectPointID(correctPointID);
    }

    /**
     * Primes the test dates to make them readable by the users on the front end
     *
     * @param test - the test
     * @return the test after alteration
     */
    Tests primeTestForUserView(Tests test) {
        if (test != null) {
            try {
                test.setStartDateTime(StringToDateUtil.convertDateToFrontEndFormat(test.getStartDateTime()));
                test.setEndDateTime(StringToDateUtil.convertDateToFrontEndFormat(test.getEndDateTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return test;
    }

    /**
     * Carries out actions need to enter a question in to the database
     *
     * @param questionData - collection of all question data available to tutor
     * @param username     - the principal user
     * @return the collection of all question data available to tutor after insertion
     * @throws Exception generic
     */
    public TutorQuestionPojo newQuestion(TutorQuestionPojo questionData, String username, Boolean update) throws Exception {
        logger.info("Request made to add new question in to the database by {}", username);
        if (AssociationType.TUTOR == modServ.checkValidAssociation(username, testRepo.selectByTestID(questionData.getTestID()).getModuleID())) {
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
            // Insert the word and Text-based
            if (!questionData.getQuestion().getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
                questionData.setCorrectPoints(addCorrectPoints(correctPoints, questionData.getQuestion().getQuestionID(), update));
            }

            // Multiple choice
            if (questionData.getQuestion().getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
                questionData.setOptions(addOptions(questionData.getQuestion().getQuestionID(), questionData.getOptions(), update));
            }

            questionData.setBase64(BlobUtil.blobToBase(questionData.getQuestion().getQuestionFigure()));
            questionData.getQuestion().setQuestionFigure(null);

            return questionData;
        }
        return null;
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
        Question question = questionRepo.selectByQuestionID(questionID);
        if (AssociationType.TUTOR == modServ.checkValidAssociation(username, test.getModuleID()) && question.getCreatorID().equals(userRepo.selectByUsername(username).getUserID())) {
            return testQuestionRepo.insert(new TestQuestion(testID, questionID));
        }
        return null;
    }

    public Boolean duplicateQuestion(Long questionID, String username) {
        logger.info("Request made to duplicate question #{} by {}", questionID, username);

        Question question = questionRepo.selectByQuestionID(questionID);
        List<Option> options = optionRepo.selectByQuestionID(question.getQuestionID());
        List<CorrectPoint> cps = cpRepo.selectByQuestionID(question.getQuestionID());
        for (CorrectPoint cp : cps) {
            cp.setAlternatives(alternativeRepo.selectByCorrectPointID(cp.getCorrectPointID()));
        }
        Question newQuestion = question;
        User user = userRepo.selectByUsername(username);

        if (question.getCreatorID().equals(user.getUserID())) {

            newQuestion.setQuestionID(-1L);
            newQuestion = questionRepo.insert(newQuestion);
            addMathLines(newQuestion.getQuestionID(), questionMathLineRepo.selectByQuestionID(question.getQuestionID()));
            if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                for (Option opt : options) {
                    opt.setOptionID(-1L);
                    opt.setQuestionID(newQuestion.getQuestionID());
                    optionRepo.insert(opt);
                }
            }

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


            return true;
        }
        return false;
    }

    /**
     * Carries out actions needed to add correct points in to the database
     *
     * @param correctPoints - the correct points
     * @param questionID    - the id of the question
     * @return the correct points
     * @throws Exception generic
     */
    List<CorrectPoint> addCorrectPoints(List<CorrectPoint> correctPoints, Long questionID, Boolean update) throws Exception {
        Question question = questionRepo.selectByQuestionID(questionID);

        if (question.getQuestionType() == QuestionType.INSERT_THE_WORD) {
            List<CorrectPoint> sortedCPForInsertTheWord = new LinkedList<>();
            correctPoints.addAll(cpRepo.selectByQuestionID(questionID));

            for (int x = 0; x < correctPoints.size(); x++) {
                if (x == 0) {
                    sortedCPForInsertTheWord.add(correctPoints.get(x));
                } else {
                    int position = question.getQuestionContent().indexOf("[[" + correctPoints.get(x).getPhrase() + "]]");
                    int index = sortedCPForInsertTheWord.size();
                    int lowestSoFar = -1;
                    for (int y = 0; y < sortedCPForInsertTheWord.size(); y++) {
                        int thisIndex = question.getQuestionContent().indexOf("[[" + sortedCPForInsertTheWord.get(y).getPhrase() + "]]");
                        if (position < thisIndex) {
                            if (lowestSoFar == -1) {
                                index = y;
                                lowestSoFar = thisIndex;
                            } else {
                                if (thisIndex < lowestSoFar) {
                                    lowestSoFar = thisIndex;
                                    index = y;
                                }
                            }
                        }
                    }
                    sortedCPForInsertTheWord.add(index, correctPoints.get(x));
                }
            }

            for (int x = 0; x < sortedCPForInsertTheWord.size(); x++) {
                sortedCPForInsertTheWord.get(x).setIndexedAt(x);
            }

            correctPoints = sortedCPForInsertTheWord;
        }

        if (correctPoints.size() > 0) {
            for (CorrectPoint cp : correctPoints) {

                cp.setQuestionID(questionID);
                if (cp.getCorrectPointID() == null || cp.getCorrectPointID() < 1) {
                    cp.setCorrectPointID(-1L);
                }
                cp = cpRepo.insert(cp);
                cp.setAlternatives(addAlternatives(cp.getCorrectPointID(), cp.getAlternatives(), update));
            }
        }
        return correctPoints;
    }

    /**
     * Carries out actions need to input alternatives in to the database
     *
     * @param correctPointID the correct points
     * @param alternatives   the alternative phrases
     * @return the list of alternatives
     * @throws Exception generic
     */
    public List<Alternative> addAlternatives(Long correctPointID, List<Alternative> alternatives, Boolean update) throws Exception {
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

    public List<QuestionMathLine> addMathLines(Long questionID, List<QuestionMathLine> questionMathLines) {
        if (questionMathLines != null && questionMathLines.size() > 0) {
            for (QuestionMathLine qm : questionMathLines) {
                qm.setQuestionID(questionID);
                qm.setQuestionMathLineID(questionMathLineRepo.insert(qm).getQuestionMathLineID());
            }
            return questionMathLines;
        }
        return null;
    }

    public List<Option> addOptions(Long questionID, List<Option> options, Boolean update) throws Exception {
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

        if (AssociationType.TUTOR == modServ.checkValidAssociation(username, modRepo.selectByModuleID(testRepo.selectByTestID(testID).getModuleID()).getModuleID())) {
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

    public Boolean removeQuestionMathLine(Long questionMathLineID, String username) {
        logger.info("Request made to remove question math line #{} by {}", questionMathLineID, username);

        QuestionMathLine qm = questionMathLineRepo.selectByQuestionMathLineID(questionMathLineID);
        User user = userRepo.selectByUsername(username);

        if (user.getUserID().equals(questionRepo.selectByQuestionID(qm.getQuestionID()).getCreatorID())) {
            questionMathLineRepo.delete(questionMathLineID);
            return true;
        }
        return false;
    }

    public Boolean removeCorrectPoint(Long correctPointID, String username) {
        logger.info("Request made to remove correct point #{} by {}", correctPointID, username);

        CorrectPoint cp = cpRepo.selectByCorrectPointID(correctPointID);
        User user = userRepo.selectByUsername(username);

        if (user.getUserID().equals(questionRepo.selectByQuestionID(cp.getQuestionID()).getCreatorID())) {
            List<Alternative> alts = alternativeRepo.selectByCorrectPointID(correctPointID);
            for (Alternative alt : alts) {
                alternativeRepo.delete(alt.getAlternativeID());
            }
            cpRepo.delete(correctPointID);
            return true;
        }
        return false;
    }

    public Boolean removeAlternative(Long alternativeID, String username) {
        logger.info("Request made to remove alternative #{} by {}", alternativeID, username);

        User user = userRepo.selectByUsername(username);
        Alternative alt = alternativeRepo.selectByAlternativeID(alternativeID);
        CorrectPoint cp = cpRepo.selectByCorrectPointID(alt.getCorrectPointID());

        if (user.getUserID().equals(questionRepo.selectByQuestionID(cp.getQuestionID()).getCreatorID())) {
            alternativeRepo.delete(alt.getAlternativeID());
            return true;
        }
        return false;
    }

    public Boolean removeOption(Long optionID, String username) {
        logger.info("Request made to remove option #{} by {}", optionID, username);

        User user = userRepo.selectByUsername(username);
        Option option = optionRepo.selectByOptionID(optionID);

        if (user.getUserID().equals(questionRepo.selectByQuestionID(option.getQuestionID()).getCreatorID())) {
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

        if (AssociationType.TUTOR == modServ.checkValidAssociation(username, modRepo.selectByModuleID(testRepo.selectByTestID(testID).getModuleID()).getModuleID())) {
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
    public Boolean scheduleTest(Long testID, String username) {
        logger.info("Request made to schedule test #{} by {}", testID, username);

        if (AssociationType.TUTOR == modServ.checkValidAssociation(username, modRepo.selectByModuleID(testRepo.selectByTestID(testID).getModuleID()).getModuleID())) {
            Tests test = testRepo.selectByTestID(testID);

            if (test.getScheduled().equals(SCHEDULED)) {
                test.setScheduled(UNSCHEDULED);
            } else {
                test.setScheduled(SCHEDULED);
            }
            testRepo.insert(test);
            return true;
        }
        return false;
    }
}
