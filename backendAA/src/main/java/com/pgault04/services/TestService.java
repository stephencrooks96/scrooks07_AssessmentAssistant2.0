package com.pgault04.services;

import com.pgault04.entities.*;
import com.pgault04.pojos.Input;
import com.pgault04.pojos.QuestionAndAnswer;
import com.pgault04.pojos.QuestionAndBase64;
import com.pgault04.pojos.TutorQuestionPojo;
import com.pgault04.repositories.*;
import com.pgault04.utilities.StringToDateUtil;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
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
    /**
     * Logs useful info for debugging and analysis needs
     */
    private static final Logger logger = LogManager.getLogger(TestService.class);
    @Autowired
    TestsRepo testRepo;

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
                autoMarkMultipleChoice(answer);
            }

            if (questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.INSERT_THE_WORD) || questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.TEXT_BASED)) {
                autoMarkCorrectPoints(answer);
            }

            if (questionAndAnswer.getQuestion().getQuestion().getQuestionType().equals(QuestionType.TEXT_BASED)) {
                autoMarkCorrectPoints(answer);
            }

        }
        return true;
    }

    public void autoMarkMultipleChoice(Answer answer) {

        Question question = questionRepo.selectByQuestionID(answer.getQuestionID());
        List<Option> options = findOptions(answer.getQuestionID());
        options.sort(Collections.reverseOrder(Comparator.comparingDouble(Option::getWorthMarks)));

        answer.setScore(0);
        if (options.size() > 0) {
            for (Option o : options) {

                if (answer.getContent().toLowerCase().contains(o.getOptionContent().toLowerCase())) {
                    answer.setScore(answer.getScore() + o.getWorthMarks());
                    answer.setFeedback(answer.getFeedback() + "\n" + o.getFeedback());
                }

            }
            validateScore(answer, question);
        }
    }

    public void autoMarkCorrectPoints(Answer answer) {

        Question question = questionRepo.selectByQuestionID(answer.getQuestionID());
        List<CorrectPoint> correctPoints = findCorrectPoints(answer.getQuestionID());
        correctPoints.sort(Collections.reverseOrder(Comparator.comparingDouble(CorrectPoint::getMarksWorth)));

        answer.setScore(0);
        answer.setContent(answer.getContent().trim());

        if (question.getQuestionType().equals(QuestionType.INSERT_THE_WORD) && answer.getContent().equalsIgnoreCase(question.getQuestionContent())) {
            answer.setScore(question.getMaxScore());
        }

        for (CorrectPoint cp : correctPoints) {

            if (question.getQuestionType().equals(QuestionType.INSERT_THE_WORD)) {
                if (answer.getContent().toLowerCase().contains(cp.getPhrase().toLowerCase()) && answer.getContent().indexOf(cp.getPhrase(), question.getQuestionContent().indexOf(cp.getPhrase()) - 2) == 0) {
                    answer.setScore(answer.getScore() + cp.getMarksWorth().intValue());
                    answer.setFeedback(answer.getFeedback() + "\n" + cp.getFeedback());
                } else {
                    for (Alternative alt : alternativeRepo.selectByCorrectPointID(cp.getCorrectPointID())) {
                        if (answer.getContent().toLowerCase().contains(alt.getAlternativePhrase().toLowerCase()) && answer.getContent().indexOf(alt.getAlternativePhrase(), question.getQuestionContent().indexOf(cp.getPhrase())) == 0) {
                            answer.setScore(answer.getScore() + cp.getMarksWorth().intValue());
                            answer.setFeedback(answer.getFeedback() + "\n" + cp.getFeedback());
                            break;
                        }
                    }
                }
            } else {
                if (answer.getContent().toLowerCase().contains(cp.getPhrase().toLowerCase())) {
                    answer.setScore(answer.getScore() + cp.getMarksWorth().intValue());
                    answer.setFeedback(answer.getFeedback() + "\n" + cp.getFeedback());
                } else {
                    for (Alternative alt : alternativeRepo.selectByCorrectPointID(cp.getCorrectPointID())) {
                        if (answer.getContent().toLowerCase().contains(alt.getAlternativePhrase().toLowerCase())) {
                            answer.setScore(answer.getScore() + cp.getMarksWorth().intValue());
                            answer.setFeedback(answer.getFeedback() + "\n" + cp.getFeedback());
                            break;
                        }
                    }
                }
            }
        }
        validateScore(answer, question);
    }

    private void validateScore(Answer answer, Question question) {
        if (answer.getScore() < (-1 * question.getMaxScore())) {
            answer.setScore(-1 * question.getMaxScore());
        } else if (answer.getScore() > question.getMaxScore()) {
            answer.setScore(question.getMaxScore());
        }
        answerRepo.insert(answer);
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
    public Tests getByTestIDTutorView(String username, Long testID) {
        logger.info("Request made for test #{} with tutor info by {}", testID, username);

        Tests test = testRepo.selectByTestID(testID);
        if (AssociationType.TUTOR == modServ.checkValidAssociation(username, test.getModuleID())) {
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

    public List<QuestionAndBase64> getQuestionsStudent(String username, Long testID) throws Base64DecodingException, SQLException {
        logger.info("Request made for questions for test #{} by {}", testID, username);

        List<TestQuestion> tqs = testQuestionRepo.selectByTestID(testID);
        if (modServ.checkValidAssociation(username, modRepo.selectByModuleID(testRepo.selectByTestID(testID).getModuleID()).getModuleID()) != null) {
            List<QuestionAndBase64> questions = new LinkedList<>();
            for (TestQuestion tq : tqs) {
                Question q = questionRepo.selectByQuestionID(tq.getQuestionID());
                if (q != null) {
                    List<Input> inputs = new LinkedList<>();
                    if (q.getQuestionType() == QuestionType.INSERT_THE_WORD) {
                        List<Object> insertions = prepareInsertTheWordForStudent(q);
                        q.setQuestionContent((String) insertions.get(0));
                        for (int loop = 0; loop < (Integer) insertions.get(1); loop++) {
                            inputs.add(new Input(""));
                        }
                    }


                    if (q.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                        List<Option> options = optionRepo.selectByQuestionID(q.getQuestionID());
                        for (Option o : options) {
                            o.setFeedback("");
                            o.setWorthMarks(0);
                        }
                    }

                    QuestionAndBase64 qToAdd = new QuestionAndBase64(prepareFigure(q), findOptions(q.getQuestionID()), inputs, q);
                    qToAdd.getQuestion().setQuestionFigure(null);
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
        TutorQuestionPojo tqToAdd = new TutorQuestionPojo(testID, q, findOptions(q.getQuestionID()), findCorrectPoints(q.getQuestionID()));
        tqToAdd.setBase64(prepareFigure(q));
        tqToAdd.getQuestion().setQuestionFigure(null);
        allTutorQuestions.add(tqToAdd);
        return allTutorQuestions;
    }

    private String prepareFigure(Question q) throws SQLException, Base64DecodingException {
        return blobToBase(q.getQuestionFigure());
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
    private List<CorrectPoint> findCorrectPoints(Long questionID) {
        List<CorrectPoint> correctPoints = cpRepo.selectByQuestionID(questionID);
        for (CorrectPoint cp : correctPoints) {
            cp.setAlternatives(findAlternatives(cp.getCorrectPointID()));
        }
        return correctPoints;
    }

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
                question.setQuestionFigure(baseToBlob(questionData.getBase64()));
            }
            List<CorrectPoint> correctPoints = questionData.getCorrectPoints();
            User user = userRepo.selectByUsername(username);
            question.setCreatorID(user.getUserID());

            questionData.setQuestion(questionRepo.insert(question));
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

            questionData.setBase64(blobToBase(questionData.getQuestion().getQuestionFigure()));
            questionData.getQuestion().setQuestionFigure(null);

            return questionData;
        }
        return null;
    }

    private SerialBlob baseToBlob(String base64) throws SQLException, Base64DecodingException {
        com.sun.org.apache.xml.internal.security.Init.init();
        String newBase64 = base64.substring(22);

        byte[] bytes = Base64.decode(newBase64);
        return new SerialBlob(bytes);
    }

    private String blobToBase(Blob blob) throws SQLException, Base64DecodingException {
        com.sun.org.apache.xml.internal.security.Init.init();
        byte[] bytes;
        String base = null;
        if (blob != null) {
            bytes = blob.getBytes(1, (int) blob.length());
            base = Base64.encode(bytes);
        }
        return base;
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
        User user = userRepo.selectByUsername(username);

        if (question.getCreatorID().equals(user.getUserID())) {

            question.setQuestionID(-1L);
            Question newQuestion = questionRepo.insert(question);
            if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                List<Option> options = optionRepo.selectByQuestionID(question.getQuestionID());
                for (Option opt : options) {
                    opt.setOptionID(-1L);
                    opt.setQuestionID(newQuestion.getQuestionID());
                    optionRepo.insert(opt);
                }
            }

            if (question.getQuestionType() != QuestionType.MULTIPLE_CHOICE) {
                List<CorrectPoint> cps = cpRepo.selectByQuestionID(question.getQuestionID());
                for (CorrectPoint cp : cps) {
                    List<Alternative> alts = alternativeRepo.selectByCorrectPointID(cp.getCorrectPointID());
                    cp.setCorrectPointID(-1L);
                    cp.setQuestionID(newQuestion.getQuestionID());
                    CorrectPoint newCorrectPoint = cpRepo.insert(cp);

                    for (Alternative alt : alts) {
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
    public List<CorrectPoint> addCorrectPoints(List<CorrectPoint> correctPoints, Long questionID, Boolean update) throws Exception {
        if (correctPoints != null && correctPoints.size() > 0) {
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
