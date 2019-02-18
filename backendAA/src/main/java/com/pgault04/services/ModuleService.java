package com.pgault04.services;

import com.pgault04.entities.*;
import com.pgault04.pojos.*;
import com.pgault04.repositories.*;
import com.pgault04.utilities.BlobUtil;
import com.pgault04.utilities.StringToDateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class to provide necessary services to transform module data input and
 * accumulate it and ready is for communication back and forth between the database
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Service
public class ModuleService {

    /**
     * Logs useful information for debugging and problem resolution
     */
    private static final Logger logger = LogManager.getLogger(ModuleService.class);

    private static final int SCHEDULED = 1;
    private static final int READY_FOR_REVIEW = 0;
    private static final int PUBLISH_TRUE = 1;
    private static final int MARKER_APPROVED = 1;
    public static final int APPROVED = 1;

    @Autowired
    TestsRepo testsRepo;
    @Autowired
    ModuleAssociationRepo modAssocRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    AssociationTypeRepo associationTypeRepo;
    @Autowired
    AnswerRepo answerRepo;
    @Autowired
    TestResultRepo testResultRepo;
    @Autowired
    TestQuestionRepo testQuestionRepo;
    @Autowired
    QuestionRepo questionRepo;
    @Autowired
    ModuleRepo moduleRepo;
    @Autowired
    OptionRepo optionRepo;
    @Autowired
    InputsRepo inputRepo;
    @Autowired
    OptionEntriesRepo optionEntriesRepo;
    @Autowired
    CorrectPointRepo cpRepo;
    @Autowired
    TestService testServ;

    /**
     * Performs necessary actions needed to retrieve the active tests
     * from database and return them to user for a given module
     *
     * @param moduleID the module id
     * @return the list of active tests
     */
    public List<Tests> activeTests(String username, Long moduleID) {
        logger.info("Request made for active tests for module #{}", moduleID);
        if (checkValidAssociation(username, moduleID) != null) {
            Date now = new Date();
            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            List<Tests> activeTests = new ArrayList<>();

            for (Tests t : tests) {
                try {
                    if (now.compareTo(StringToDateUtil.stringToDate(t.getStartDateTime())) >= 0
                            && now.compareTo(StringToDateUtil.stringToDate(t.getEndDateTime())) < 0
                            && t.getScheduled() == SCHEDULED) {
                        activeTests.add(t);
                    }
                } catch (ParseException e) { e.printStackTrace(); }
            }
            return activeTests;
        }
        return null;
    }

    /**
     * Gets a given module with its tutor info
     *
     * @param moduleID the module id
     * @return the module with tutor info
     */
    public ModuleWithTutor getModuleWithTutor(Long moduleID) {
        logger.info("Request made for module with tutor info, module #{}", moduleID);
        Module m = moduleRepo.selectByModuleID(moduleID);
        User u = userRepo.selectByUserID(m.getTutorUserID());
        u.setPassword(null); // Protects password from outside view
        return new ModuleWithTutor(u, m);
    }

    /**
     * Gets a list of all modules the user is affiliated with, with their tutors info
     *
     * @param username the user
     * @return the list of modules with tutors
     */
    public List<ModuleWithTutor> getMyModulesWithTutor(String username) {
        logger.info("Requests for all module and tutor data for modules associated with user {}", username);
        List<Module> modules = myModules(username);
        List<ModuleWithTutor> modTutors = new ArrayList<>();
        for (Module m : modules) {
            User u = userRepo.selectByUserID(m.getTutorUserID());
            u.setPassword(null); // Protects password from outside view
            modTutors.add(new ModuleWithTutor(u, m));
        }
        return modTutors;
    }

    /**
     * Performs necessary actions needed to retrieve the scheduled tests
     * from database and return them to user for a given module
     *
     * @param moduleID the module id
     * @return the scheduled tests
     */
    public List<Tests> scheduledTests(String username, Long moduleID) {
        logger.info("Request made for scheduled tests for module with id #{}", moduleID);
        if (AssociationType.TUTOR == checkValidAssociation(username, moduleID)) {

            Date now = new Date();
            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            List<Tests> scheduledTests = new ArrayList<>();

            for (Tests t : tests) {
                try {
                    if (t.getScheduled() == SCHEDULED
                            && now.compareTo(StringToDateUtil.stringToDate(t.getStartDateTime())) < 0) {
                        scheduledTests.add(t);
                    }
                } catch (ParseException e) { e.printStackTrace(); }
            }
            return scheduledTests;
        }
        return null;
    }

    /**
     * Performs necessary actions needed to retrieve the marking data
     * from database and return it to user for a given module
     *
     * @param moduleID the module id
     * @param username the user
     * @return the marking data for this module
     */
    public List<TestMarking> marking(Long moduleID, String username) {
        logger.info("Request made for marking info for module with id #{}", moduleID);
        Long associationType = checkValidAssociation(username, moduleID);
        if (AssociationType.STUDENT != associationType) {

            Date now = new Date();
            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            User user = userRepo.selectByUsername(username);
            List<TestMarking> tmList = new ArrayList<>();

            try {
                for (Tests t : tests) {
                    if (now.compareTo(StringToDateUtil.stringToDate(t.getEndDateTime())) >= 0) {
                        List<Answer> answers = answerRepo.selectByTestID(t.getTestID());
                        int toBeMarkedByYou = 0, toBeMarkedByTAs = 0, marked = 0, totalForYou = 0, totalForTAs = 0;
                        for (Answer a : answers) {
                            if (AssociationType.TUTOR == associationType) {
                                if (a.getMarkerID().equals(user.getUserID())) {
                                    totalForYou++;
                                    if (a.getMarkerApproved() != null && a.getMarkerApproved() != MARKER_APPROVED) {
                                        toBeMarkedByYou++;
                                    } else {
                                        marked++;
                                    }
                                } else {
                                    totalForTAs++;
                                    if (a.getMarkerApproved() != null && a.getMarkerApproved() != MARKER_APPROVED) {
                                        toBeMarkedByTAs++;
                                    } else {
                                        marked++;
                                    }
                                }
                            } else {
                                if (a.getMarkerID().equals(user.getUserID())) {
                                    totalForYou++;
                                    if (a.getMarkerApproved() != null && a.getMarkerApproved() != MARKER_APPROVED) {
                                        toBeMarkedByYou++;
                                    } else {
                                        marked++;
                                    }
                                }
                            }
                        }
                        TestMarking tm = new TestMarking(t, toBeMarkedByYou, toBeMarkedByTAs, marked, totalForYou, totalForTAs);
                        tmList.add(tm);
                    }
                }
            } catch (ParseException e) { e.printStackTrace(); }
            return tmList;
        }
        return null;
    }

    /**
     * Performs necessary actions needed to retrieve the active results
     * from database and return them to user for a given module
     *
     * @param moduleID the module id
     * @param username the user
     * @return the active results
     */
    public List<TestAndGrade> activeResults(Long moduleID, String username) throws SQLException {
        logger.info("Request made for active results for module #{}", moduleID);
        if (AssociationType.STUDENT == checkValidAssociation(username, moduleID)) {

            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            User user = userRepo.selectByUsername(username);
            List<TestAndGrade> testAndGradeList = new ArrayList<>();

            for (Tests test : tests) {
                if (test.getPublishGrades() == PUBLISH_TRUE) {
                    for (TestResult testResult : testResultRepo.selectByTestID(test.getTestID())) {
                        if (testResult.getStudentID().equals(user.getUserID())) {

                            List<QuestionAndAnswer> questions = addQuestionsToList(test, user.getUserID());

                            double percentageScore = 0;
                            for (QuestionAndAnswer q : questions) {
                                percentageScore += q.getQuestion().getQuestion().getMaxScore();
                            }

                            String grade = checkGrade(testResult.getTestScore() / percentageScore * 100);

                            TestAndGrade tar = new TestAndGrade(test, grade);
                            testAndGradeList.add(tar);
                        }
                    }
                }
            }
            return testAndGradeList;
        }
        return null;
    }

    /**
     * Adds questions from a test to a list and returns the list
     *
     * @param test - the test
     * @return the list of questions
     */
    List<QuestionAndAnswer> addQuestionsToList(Tests test, Long userID) throws SQLException {
        List<QuestionAndAnswer> questions = new ArrayList<>();
        for (TestQuestion testQuestion : testQuestionRepo.selectByTestID(test.getTestID())) {
            Question questionToAdd = questionRepo.selectByQuestionID(testQuestion.getQuestionID());
            Answer answer = answerRepo.selectByQuestionIDAndAnswererID(questionToAdd.getQuestionID(), userID);
            String base64 = BlobUtil.blobToBase(questionToAdd.getQuestionFigure());
            questionToAdd.setQuestionFigure(null);
            QuestionAndBase64 questionAndBase64 = new QuestionAndBase64(base64, optionRepo.selectByQuestionID(questionToAdd.getQuestionID()), questionToAdd);
            QuestionAndAnswer questionAndAnswer = new QuestionAndAnswer(questionAndBase64, answer, inputRepo.selectByAnswerID(answer.getAnswerID()), optionEntriesRepo.selectByAnswerID(answer.getAnswerID()), testServ.findCorrectPoints(questionToAdd.getQuestionID()));
            questions.add(questionAndAnswer);
        }
        return questions;
    }

    /**
     * Performs the necessary actions needed to get the
     * drafted tests and return them to the user
     *
     * @param moduleID - the module id
     * @return the drafted tests
     */
    public List<Tests> testDrafts(String username, Long moduleID) {
        logger.info("Request made for drafted tests for module with id #{}", moduleID);
        if (AssociationType.TUTOR == checkValidAssociation(username, moduleID)) {

            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            List<Tests> testReturn = new ArrayList<>();

            for (Tests t : tests) {
                if (t.getScheduled() != SCHEDULED) {
                    testReturn.add(t);
                }
            }
            return testReturn;
        }
        return null;
    }

    /**
     * Performs necessary actions needed to find the tests that are ready to be reviewed and return them to the user
     *
     * @param moduleID - the module id
     * @return the tests ready to be reviewed by the tutor
     */
    public List<TestMarking> reviewMarking(String username, Long moduleID) {
        logger.info("Request made for all marking ready to be reviewed for module with id #{}", moduleID);
        if (AssociationType.TUTOR == checkValidAssociation(username, moduleID)) {

            Date now = new Date();
            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            List<TestMarking> tmList = new ArrayList<>();

            for (Tests t : tests) {
                try {
                    if (now.compareTo(StringToDateUtil.stringToDate(t.getEndDateTime())) >= 0) {
                        int answersUnmarked = 0;
                        List<Answer> answers = answerRepo.selectByTestID(t.getTestID());
                        for (Answer a : answers) {
                            if (a.getMarkerApproved() != null && a.getMarkerApproved() == 0) {
                                answersUnmarked++;
                            }
                        }
                        if (answersUnmarked == READY_FOR_REVIEW) {
                            TestMarking tm = new TestMarking(t, 0, 0, answers.size(), 0, 0);
                            tmList.add(tm);
                        }
                    }
                } catch (ParseException e) { e.printStackTrace(); }
            }
            return tmList;
        }
        return null;
    }

    /**
     * Displays all the modules that a given user is associated with as string of
     * HTML5 tags
     *
     * @param username the user
     * @return moduleMessage
     */
    public List<Module> myModules(String username) {

        User user = userRepo.selectByUsername(username);
        List<ModuleAssociation> modAssociations = modAssocRepo.selectByUserID(user.getUserID());
        List<Module> modules = new ArrayList<>();

        for (ModuleAssociation m : modAssociations) {
            Module module = moduleRepo.selectByModuleID(m.getModuleID());
            if (module.getApproved().equals(APPROVED)) {
                modules.add(module);
            }
        }

        return modules;
    }

    /**
     * Performs the actions necessary to get the performance data and return it to the user on front end
     *
     * @param moduleID  - the module id
     * @param username - the user
     * @return the performance data
     */
    public List<Performance> generatePerformance(Long moduleID, String username) throws SQLException {
        logger.info("Request made for performance statistics for module with id #{}", moduleID);
        Long check = checkValidAssociation(username, moduleID);
        if (AssociationType.STUDENT == check) {

            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            User user = userRepo.selectByUsername(username);
            List<Performance> performanceList = new ArrayList<>();

            for (Tests test : tests) {
                if (test.getPublishResults() == PUBLISH_TRUE) {
                    double classAverage = 0.0;
                    TestAndResult tar = null;
                    List<TestResult> testResults = testResultRepo.selectByTestID(test.getTestID());
                    for (TestResult testResult : testResults) {
                        if (testResult.getStudentID().equals(user.getUserID())) {
                            tar = new TestAndResult(test, testResult, addQuestionsToList(test, user.getUserID()), user);
                        }
                        classAverage += testResult.getTestScore();
                    }
                    int questionTotal = 0;
                    for (TestQuestion tq : testQuestionRepo.selectByTestID(test.getTestID())) {
                        Question question = questionRepo.selectByQuestionID(tq.getQuestionID());
                        questionTotal += question.getMaxScore();
                    }
                    classAverage = ((classAverage * 100) / questionTotal) / testResults.size();
                    performanceList.add(new Performance(tar, classAverage));
                }
            }
            return performanceList;
        }
        return null;
    }

    /**
     * Checks the users association to the module and returns it
     *
     * @param username - the user
     * @param moduleID - the module id
     * @return the association
     */
    public Long checkValidAssociation(String username, Long moduleID) {
        logger.info("Request made for {}'s association with module #{}", username, moduleID);

        List<ModuleAssociation> ma = modAssocRepo.selectByModuleID(moduleID);
        User u = userRepo.selectByUsername(username);
        Long theAssociationTypeID = null;

        for (ModuleAssociation m : ma) {
            if (m.getUserID().equals(u.getUserID())) {
                theAssociationTypeID = m.getAssociationType();
            }
        }
        if (theAssociationTypeID != null) {
            return associationTypeRepo.selectByAssociationTypeID(theAssociationTypeID).getAssociationTypeID();
        } else {
            return null;
        }

    }

    /**
     * Used to assign a users score to a grade for occasions
     * when a tutor wants to reveal a grade and not score
     *
     * @param score the score
     * @return the grade
     */
    public String checkGrade(double score) {

        if (score > 89) {
            return "A*";
        } else if (score > 79) {
            return "A";
        } else if (score > 69) {
            return "B";
        } else if (score > 59) {
            return "C";
        } else if (score > 49) {
            return "D";
        } else {
            return "F";
        }
    }
}