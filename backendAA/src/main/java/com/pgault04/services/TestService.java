package com.pgault04.services;

import com.pgault04.entities.*;
import com.pgault04.pojos.TutorQuestionPojo;
import com.pgault04.repositories.*;
import com.pgault04.utilities.StringToDateUtil;
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

        List<TutorQuestionPojo> currents = getQuestionsByTestIDTutorView(username, testID);
        List<Question> allQuestions = questionRepo.selectByCreatorID(userRepo.selectByUsername(username).getUserID());
        List<TutorQuestionPojo> allTutorQuestions = new LinkedList<>();
        List<TutorQuestionPojo> tutorQuestionsToShow = new ArrayList<>();

        for (Question q : allQuestions) {
            TutorQuestionPojo tq = new TutorQuestionPojo(testID, q, findOptions(q.getQuestionID()), findCorrectPoints(q.getQuestionID()));
            allTutorQuestions.add(tq);
        }
        for (TutorQuestionPojo tq : allTutorQuestions) {
            if (!currents.contains(tq)) {
                tutorQuestionsToShow.add(tq);
            }
        }
        return tutorQuestionsToShow;
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
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return test;
        } else {
            return null;
        }
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

        Question question = questionData.getQuestion();
        List<CorrectPoint> correctPoints = questionData.getCorrectPoints();
        User user = userRepo.selectByUsername(username);
        question.setCreatorID(user.getUserID());

        questionData.setQuestion(questionRepo.insert(question));
        testQuestionRepo.insert(new TestQuestion(questionData.getTestID(), questionRepo.insert(question).getQuestionID()));
        questionData.setCorrectPoints(addCorrectPoints(correctPoints, questionData.getQuestion().getQuestionID()));
        return questionData;
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
     * @param correctPointID - the correct points
     * @param alternatives   - the alternative phrases
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
}
