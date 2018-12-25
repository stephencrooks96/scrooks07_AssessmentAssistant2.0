package com.pgault04.services;

import com.pgault04.controller.TestController;
import com.pgault04.entities.*;
import com.pgault04.pojos.TutorQuestionPojo;
import com.pgault04.repositories.*;
import com.pgault04.utilities.StringToDateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Paul Gault - 40126005
 * @since December 2018
 */
@Service
public class TestService {


    /**
     * Logs useful info for debugging and analysis needs
     */
    private static final Logger logger = LogManager.getLogger(TestService.class);
    public static final int SCHEDULED = 1;
    @Autowired
    TestsRepo testRepo;

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
        if ("tutor".equals(modServ.checkValidAssociation(username, test.getModuleID()))) {
            return primeTestForUserView(test);
        }
        return null;
    }

    /**
     * Performs the necessary actions required to get the questions currently used by this test and output all the data needed by a tutor
     * also ensure the request is made by the tutor
     *
     * @param username - the principal user
     * @param testID   - the test
     * @return the list of questions being used by this test
     */
    public List<TutorQuestionPojo> getQuestionsByTestIDTutorView(String username, Long testID) {
        logger.info("Request made for questions and all necessary info requited by tutor for test #{} by {}", testID, username);

        List<TestQuestion> tqs = testQuestionRepo.selectByTestID(testID);
        if ("tutor".equals(modServ.checkValidAssociation(username, modRepo.selectByModuleID(testRepo.selectByTestID(testID).getModuleID()).getModuleID()))) {
            List<TutorQuestionPojo> tutorQuestions = new LinkedList<>();
            for (TestQuestion tq : tqs) {
                Question q = questionRepo.selectByQuestionID(tq.getQuestionID());
                if (q != null) {
                    TutorQuestionPojo tutorQuestion = new TutorQuestionPojo(testID, q, findOptions(q.getQuestionID()), findCorrectPoints(q.getQuestionID()));
                    tutorQuestions.add(tutorQuestion);
                }
            }
            return tutorQuestions;
        }
        return null;
    }

    /**
     * Performs the necessary actions need to all the questions written by the tutor that aren't currently being used by this given test
     *
     * @param username - the principal user
     * @param testID   - the test
     * @return the questions not currently being used by this test
     */
    public List<TutorQuestionPojo> getOldQuestions(String username, Long testID) {
        logger.info("Request made for all old questions that aren't being used by test #{}", testID);

        List<TutorQuestionPojo> currents = getQuestionsByTestIDTutorView(username, testID);
        List<TutorQuestionPojo> allTutorQuestions = new LinkedList<>();
        List<TutorQuestionPojo> tutorQuestionsToRemove = new ArrayList<>();

        for (Question q : questionRepo.selectByCreatorID(userRepo.selectByUsername(username).getUserID())) {
            TutorQuestionPojo tq = new TutorQuestionPojo(testID, q, findOptions(q.getQuestionID()), findCorrectPoints(q.getQuestionID()));
            allTutorQuestions.add(tq);
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

    /**
     * Finds all the necessary options that are associated with this question, if it is a question that uses this type of input
     *
     * @param questionID - the question
     * @return the answerable options for this question
     */
    public List<Option> findOptions(Long questionID) {
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
     * finds all the alternatives that are equivalent to given correct points
     *
     * @param correctPointID - the correct point
     * @return - its alternatives
     */
    public List<Alternative> findAlternatives(Long correctPointID) {
        return alternativeRepo.selectByCorrectPointID(correctPointID);
    }

    /**
     * Primes the test dates to make them readable by the users on the front end
     *
     * @param test - the test
     * @return the test after alteration
     */
    public Tests primeTestForUserView(Tests test) {
        if (test != null) {
            try {
                test.setStartDateTime(StringToDateUtil.convertReadableFormat(test.getStartDateTime()));
                test.setEndDateTime(StringToDateUtil.convertReadableFormat(test.getEndDateTime()));
                return test;
            } catch (ParseException e) { e.printStackTrace(); }
        }
            return null;
    }

    /**
     * Carries out actions need to enter a question in to the database
     *
     * @param questionData - collection of all question data available to tutor
     * @param username     - the principal user
     * @return the collection of all question data available to tutor after insertion
     * @throws Exception generic
     */
    public TutorQuestionPojo newQuestion(TutorQuestionPojo questionData, String username) throws Exception {
        logger.info("Request made to add new question in to the database by {}", username);
        if ("tutor".equals(modServ.checkValidAssociation(username, testRepo.selectByTestID(questionData.getTestID()).getModuleID()))) {

            Question question = questionData.getQuestion();
            List<CorrectPoint> correctPoints = questionData.getCorrectPoints();
            User user = userRepo.selectByUsername(username);
            question.setCreatorID(user.getUserID());

            questionData.setQuestion(questionRepo.insert(question));
            testQuestionRepo.insert(new TestQuestion(questionData.getTestID(), questionRepo.insert(question).getQuestionID()));
            questionData.setCorrectPoints(addCorrectPoints(correctPoints, questionData.getQuestion().getQuestionID()));
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
        if ("tutor".equalsIgnoreCase(modServ.checkValidAssociation(username, test.getModuleID())) && question.getCreatorID().equals(userRepo.selectByUsername(username).getUserID())) {
            return testQuestionRepo.insert(new TestQuestion(testID, questionID));
        }
        return null;
    }

    /**
     * Carries out actions needed to add correct points in to the database
     *
     * @param correctPoints - the correct points
     * @param questionID    - the id of the question
     * @return the correct points
     * @throws Exception generic
     */
    public List<CorrectPoint> addCorrectPoints(List<CorrectPoint> correctPoints, Long questionID) throws Exception {
        if (correctPoints != null && correctPoints.size() > 0) {
            for (CorrectPoint cp : correctPoints) {
                cp.setQuestionID(questionID);
                cp.setCorrectPointID(-1L);
                cp = cpRepo.insert(cp);
                cp.setAlternatives(addAlternatives(cp.getCorrectPointID(), cp.getAlternatives()));
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
    public List<Alternative> addAlternatives(Long correctPointID, List<Alternative> alternatives) throws Exception {
        if (alternatives != null && alternatives.size() > 0) {
            for (Alternative alt : alternatives) {
                alt.setCorrectPointID(correctPointID);
                alt.setAlternativeID(-1L);
                Alternative returned = alternativeRepo.insert(alt);
                alt.setAlternativeID(returned.getAlternativeID());
            }
        }
        return alternatives;
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

        if ("tutor".equals(modServ.checkValidAssociation(username, modRepo.selectByModuleID(testRepo.selectByTestID(testID).getModuleID()).getModuleID()))) {
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
     * Method to carry out necessary actions to delete a test from the database
     * Requires that the user requesting the delete is the tutor of the module the test belongs to
     *
     * @param testID   the test's id
     * @param username the user
     * @return true or false on whether the test is deleted of not
     */
    public Boolean deleteTest(Long testID, String username) {
        logger.info("Request made to delete test #{} by {}", testID, username);

        if ("tutor".equals(modServ.checkValidAssociation(username, modRepo.selectByModuleID(testRepo.selectByTestID(testID).getModuleID()).getModuleID()))) {
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

        if ("tutor".equals(modServ.checkValidAssociation(username, modRepo.selectByModuleID(testRepo.selectByTestID(testID).getModuleID()).getModuleID()))) {
            Tests test = testRepo.selectByTestID(testID);
            test.setScheduled(SCHEDULED);
            testRepo.insert(test);
            return true;
        }
        return false;
    }
}
