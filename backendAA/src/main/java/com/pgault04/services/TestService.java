package com.pgault04.services;

import com.pgault04.entities.*;
import com.pgault04.pojos.TutorQuestionPojo;
import com.pgault04.repositories.*;
import com.pgault04.utilities.StringToDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 *
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
     * @param test
     * @param username
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

    public Tests getByTestIDTutorView(String username, Long testID) {

        Tests test = testRepo.selectByTestID(testID);

        if ("tutor".equals(modServ.checkValidAssociation(username, test.getModuleID()))) {
            return primeTestForUserView(test);
        }
        return null;
    }

    public List<TutorQuestionPojo> getQuestionsByTestIDTutorView(String username, Long testID) {

        List<TestQuestion> tqs = testQuestionRepo.selectByTestID(testID);
        if ("tutor".equals(modServ.checkValidAssociation(username, modRepo.selectByModuleID(testRepo.selectByTestID(testID).getModuleID()).getModuleID()))) {
            List<TutorQuestionPojo> tutorQuestions = new LinkedList<>();
            for (TestQuestion tq : tqs) {
                Question q = questionRepo.selectByQuestionID(tq.getQuestionID());
                if (q == null) {
                    continue;
                } else {
                    TutorQuestionPojo tutorQuestion = new TutorQuestionPojo(testID, q, findOptions(q.getQuestionID()), findCorrectPoints(q.getQuestionID()));
                    tutorQuestions.add(tutorQuestion);
                }
            }
            return tutorQuestions;
        }
        return null;
    }

    public List<Option> findOptions(Long questionID) {
        return optionRepo.selectByQuestionID(questionID);
    }

    public List<CorrectPoint> findCorrectPoints(Long questionID) {
        List<CorrectPoint> correctPoints = cpRepo.selectByQuestionID(questionID);
        for (CorrectPoint cp : correctPoints) {
            cp.setAlternatives(findAlternatives(cp.getCorrectPointID()));
        }
        return correctPoints;
    }

    public List<Alternative> findAlternatives(Long correctPointID) {
        return alternativeRepo.selectByCorrectPointID(correctPointID);
    }

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
     * @param questionData
     * @param username
     * @return
     */
    public void newQuestion(TutorQuestionPojo questionData, String username) throws Exception {

        Question question = questionData.getQuestion();
        List<CorrectPoint> correctPoints = questionData.getCorrectPoints();
        User user = userRepo.selectByUsername(username);
        question.setCreatorID(user.getUserID());

        question = questionRepo.insert(question);
        testQuestionRepo.insert(new TestQuestion(questionData.getTestID(), questionRepo.insert(question).getQuestionID()));
        addCorrectPoints(correctPoints);

    }

    /**
     * @param correctPoints
     * @return
     */
    public void addCorrectPoints(List<CorrectPoint> correctPoints) throws Exception {
        if (correctPoints != null && correctPoints.size() > 0) {
            for (CorrectPoint cp : correctPoints) {

                cpRepo.insert(cp);
            }
        }


    }
}
