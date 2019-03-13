package com.pgault04.services;

import com.pgault04.entities.*;
import com.pgault04.pojos.*;
import com.pgault04.repositories.*;
import com.pgault04.utilities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

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
    private static final int APPROVED = 1;
    private static final int UNAPPROVED = 0;

    @Autowired
    EmailUtil emailSender;
    @Autowired
    UserSessionsRepo userSessionRepo;
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
    @Autowired
    PasswordResetRepo passwordResetRepo;

    /**
     * @return all tutor requests along with the tutor's information
     */
    public List<ModuleRequestPojo> getModuleRequests(String username) {
        User user = userRepo.selectByUsername(username);
        if (user != null && user.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            List<Module> requests = moduleRepo.selectByApproved(UNAPPROVED);
            List<ModuleRequestPojo> moduleRequests = new ArrayList<>();
            for (Module m : requests) {
                User tutor = userRepo.selectByUserID(m.getTutorUserID());
                tutor.setPassword(null);
                moduleRequests.add(new ModuleRequestPojo(tutor, m));
            }
            return moduleRequests;
        }
        return null;
    }

    /**
     * approves module request and notifies the tutor
     * @param moduleID the module
     */
    public void approveModuleRequest(Long moduleID, String username) {
        User admin = userRepo.selectByUsername(username);
        if (admin != null && admin.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            Module module = moduleRepo.selectByModuleID(moduleID);
            module.setApproved(APPROVED);
            moduleRepo.insert(module);
            emailSender.sendModuleRequestApproved(module);
            List<ModuleAssociation> moduleAssociations = modAssocRepo.selectByModuleID(moduleID);
            for (ModuleAssociation ma : moduleAssociations) {
                User user = userRepo.selectByUserID(ma.getUserID());
                emailSender.sendEnrollmentMessageFromSystemToAssociate(module, user);
            }
        }
    }

    /**
     * Rejects module request and notifies tutor
     * @param moduleID the module
     */
    public void rejectModuleRequest(Long moduleID, String username) {
        User user = userRepo.selectByUsername(username);
        if (user != null && user.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            Module module = moduleRepo.selectByModuleID(moduleID);
            moduleRepo.delete(moduleID);
            emailSender.sendModuleRequestRejected(module);
        }
    }

    public void removeAssociate(String username, Long moduleID, String principal) {
        Long check = checkValidAssociation(principal, moduleID);
        if (check != null && check == AssociationType.TUTOR) {
            User user = userRepo.selectByUsername(username);
            List<ModuleAssociation> moduleAssocs = modAssocRepo.selectByModuleID(moduleID);
            for (ModuleAssociation m : moduleAssocs) {
                if (m.getUserID().equals(user.getUserID())) {
                    modAssocRepo.delete(m.getAssociationID());
                }
            }
            emailSender.sendRemovedFromModule(username, moduleID);
        }
    }

    public List<Associate> getAssociates(Long moduleID, String principal) {
        if (checkValidAssociation(principal, moduleID) != null) {
            List<ModuleAssociation> moduleAssociations = modAssocRepo.selectByModuleID(moduleID);
            List<Associate> associates = new ArrayList<>();
            for (ModuleAssociation ma : moduleAssociations) {
                User user = userRepo.selectByUserID(ma.getUserID());
                String associationType;
                if (ma.getAssociationType().equals(AssociationType.TUTOR)) {
                    associationType = "Tutor";
                } else if (ma.getAssociationType().equals(AssociationType.STUDENT)) {
                    associationType = "Student";
                } else {
                    associationType = "Teaching Assistant";
                }
                associates.add(new Associate(associationType, user.getUsername(), user.getFirstName(), user.getLastName()));
            }
            return associates;
        }
        return null;
    }

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
                            && t.getScheduled() == SCHEDULED && t.getPractice() == 0) {
                        activeTests.add(t);
                    }
                } catch (ParseException e) { e.printStackTrace(); }
            }
            return activeTests;
        }
        return null;
    }

    public List<Tests> practiceTests(String username, Long moduleID) {
        logger.info("Request made for practice tests for module #{}", moduleID);
        if (checkValidAssociation(username, moduleID) != null) {
            Date now = new Date();
            List<Tests> activeTests = new ArrayList<>();
            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            for (Tests t : tests) {
                try {
                    if (now.compareTo(StringToDateUtil.stringToDate(t.getEndDateTime())) < 0
                            && now.compareTo(StringToDateUtil.stringToDate(t.getStartDateTime())) >= 0
                            && t.getScheduled() == SCHEDULED && t.getPractice() == 1) {
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

    public void addModule(ModulePojo modulePojo, String username) throws IllegalArgumentException {
        final User user = userRepo.selectByUsername(username);
        modulePojo.getModule().setApproved(UNAPPROVED);
        modulePojo.getModule().setTutorUserID(user.getUserID());
        modulePojo.getModule().setModuleID(-1L);
        modulePojo.setModule(moduleRepo.insert(modulePojo.getModule()));
        modAssocRepo.insert(new ModuleAssociation(modulePojo.getModule().getModuleID(), user.getUserID(), AssociationType.TUTOR));
        if (modulePojo.getAssociations().size() > 0) {
            addAssociations(modulePojo.getModule().getModuleID(), modulePojo.getAssociations(), username);
        }
        // Email tutor
        emailSender.sendNewModuleMessageFromSystemToTutor(user.getUsername(), modulePojo.getModule());
        // Email admins
        emailAdmins(modulePojo, user);
    }

    private void emailAdmins(ModulePojo modulePojo, User user) {
        List<User> admins = userRepo.selectAll();
        for (User u : admins) {
            if (u.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
                emailSender.sendNewModuleMessageFromSystemToAdmin(modulePojo.getModule(), user.getUsername(), u.getUsername());
            }
        }
    }

    public void addAssociations(Long moduleID, List<Associate> associations, String username) throws IllegalArgumentException{
        boolean associationTypeError = false;
        Long check = checkValidAssociation(username, moduleID);
        if (check != null && check == AssociationType.TUTOR) {
            for (Associate a : associations) {
                User user = userRepo.selectByUsername(a.getUsername());
                if (user == null) {
                    user = newUserFromCsv(username, a);
                }
                ModuleAssociation moduleAssociation = new ModuleAssociation();
                if (a.getAssociateType().equalsIgnoreCase("TA")) {
                    moduleAssociation.setAssociationType(AssociationType.TEACHING_ASSISTANT);
                } else if (a.getAssociateType().equalsIgnoreCase("S")) {
                    moduleAssociation.setAssociationType(AssociationType.STUDENT);
                } else {
                    associationTypeError = true;
                    continue;
                }
                moduleAssociation.setAssociationID(-1L);
                moduleAssociation.setUserID(user.getUserID());
                moduleAssociation.setModuleID(moduleID);
                modAssocRepo.insert(moduleAssociation);
            }
            if (associationTypeError) {
                throw new IllegalArgumentException("One or more associations have included a type which is not TA or S.");
            }
        } else {
            throw new IllegalArgumentException("You must be the module tutor to perform this action.");
        }
    }

    private User newUserFromCsv(String username, Associate a) {
        User user;
        String password = PasswordUtil.generateRandomString();
        int tutor = 0;
        user = userRepo.insert(new User(a.getUsername(), PasswordUtil.encrypt(password), a.getFirstName(), a.getLastName(), 0, UserRole.ROLE_USER, tutor));
        userSessionRepo.insert(new UserSession(user.getUsername(), new String(Base64.getEncoder().encode((user.getUsername() + ":" + password).getBytes())), new Timestamp(System.currentTimeMillis())));
        passwordResetRepo.insert(new PasswordReset(user.getUserID(), PasswordUtil.generateRandomString()));
        emailSender.sendNewAccountMessageFromSystemToUser(user, password, username);
        return user;
    }

    /**
     * Gets the modules that are awaiting approval
     * @param username the user
     * @return unapproved modules
     */
    public List<Module> getModulesPendingApproval(String username) {
        logger.info("Requests for all pending modules for user {}", username);
        User user = userRepo.selectByUsername(username);
        List<Module> modules = moduleRepo.selectByTutorID(user.getUserID());
        List<Module> modulesToReturn = new ArrayList<>();
        for (Module m : modules) {
            if (m.getApproved().equals(UNAPPROVED)) {
                modulesToReturn.add(m);
            }
        }
        return modulesToReturn.size() > 0 ? modulesToReturn : Collections.emptyList();
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
        Long check = checkValidAssociation(username, moduleID);
        if (check != null && check.equals(AssociationType.TUTOR)) {
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
        if (associationType != null && AssociationType.STUDENT != associationType) {
            Date now = new Date();
            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            User user = userRepo.selectByUsername(username);
            List<TestMarking> tmList = new ArrayList<>();
            try {
                for (Tests t : tests) {
                    if (now.compareTo(StringToDateUtil.stringToDate(t.getEndDateTime())) >= 0) {
                        populateMarkingData(associationType, user, tmList, t);
                    }
                }
            } catch (ParseException e) { e.printStackTrace(); }
            return tmList;
        }
        return null;
    }

    private void populateMarkingData(Long associationType, User user, List<TestMarking> tmList, Tests t) {
        List<Answer> answers = answerRepo.selectByTestID(t.getTestID());
        TestMarking tm = new TestMarking(t, 0, 0, 0, 0, 0);
        for (Answer a : answers) {
            if (AssociationType.TUTOR == associationType) {
                if (a.getMarkerID().equals(user.getUserID())) {
                    populateMarkingDataForUser(tm, a);
                } else {
                    populateMarkingDataForTeachingAssistants(tm, a);
                }
            } else {
                if (a.getMarkerID().equals(user.getUserID())) {
                    populateMarkingDataForUser(tm, a);
                }
            }
        }
        tmList.add(tm);
    }

    private void populateMarkingDataForTeachingAssistants(TestMarking tm, Answer a) {
        tm.setTotalForTAs(tm.getTotalForTAs() + 1);
        if (a.getMarkerApproved() != null && a.getMarkerApproved() != MARKER_APPROVED) {
            tm.setToBeMarkedByTAs(tm.getToBeMarkedByTAs() + 1);
        } else {
            tm.setMarked(tm.getMarked() + 1);
        }
    }

    private void populateMarkingDataForUser(TestMarking tm, Answer a) {
        tm.setTotalForYou(tm.getTotalForYou() + 1);
        if (a.getMarkerApproved() != null && a.getMarkerApproved() != MARKER_APPROVED) {
            tm.setToBeMarkedByYou(tm.getToBeMarkedByYou() + 1);
        } else {
            tm.setMarked(tm.getMarked() + 1);
        }
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
        Long check = checkValidAssociation(username, moduleID);
        if (check != null && AssociationType.STUDENT == check) {
            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            User user = userRepo.selectByUsername(username);
            List<TestAndGrade> testAndGradeList = new ArrayList<>();
            populateActiveResults(tests, user, testAndGradeList);
            return testAndGradeList;
        }
        return null;
    }

    private void populateActiveResults(List<Tests> tests, User user, List<TestAndGrade> testAndGradeList) throws SQLException {
        for (Tests test : tests) {
            if (test.getPublishGrades() == PUBLISH_TRUE && test.getPractice() == 0) {
                for (TestResult testResult : testResultRepo.selectByTestID(test.getTestID())) {
                    if (testResult.getStudentID().equals(user.getUserID())) {
                        List<QuestionAndAnswer> questions = addQuestionsToList(test, user.getUserID());
                        double percentageScore = 0;
                        percentageScore = getPercentageScore(questions, percentageScore);
                        String grade = checkGrade(testResult.getTestScore() / percentageScore * 100);
                        TestAndGrade tar = new TestAndGrade(test, grade);
                        testAndGradeList.add(tar);
                    }
                }
            }
        }
    }

    private double getPercentageScore(List<QuestionAndAnswer> questions, double percentageScore) {
        for (QuestionAndAnswer q : questions) {
            percentageScore += q.getQuestion().getQuestion().getMaxScore();
        }
        return percentageScore;
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
            populateQuestionAndAnswer(test, userID, questions, testQuestion);
        }
        return questions;
    }

    private void populateQuestionAndAnswer(Tests test, Long userID, List<QuestionAndAnswer> questions, TestQuestion testQuestion) throws SQLException {
        Question questionToAdd = questionRepo.selectByQuestionID(testQuestion.getQuestionID());
        Answer answer = answerRepo.selectByQuestionIDAndAnswererIDAndTestID(questionToAdd.getQuestionID(), userID, test.getTestID());
        String base64 = BlobUtil.blobToBase(questionToAdd.getQuestionFigure());
        questionToAdd.setQuestionFigure(null);
        QuestionAndBase64 questionAndBase64 = new QuestionAndBase64(base64, optionRepo.selectByQuestionID(questionToAdd.getQuestionID()), testServ.findMathLines(questionToAdd.getQuestionID()), questionToAdd);
        QuestionAndAnswer questionAndAnswer = new QuestionAndAnswer(questionAndBase64, answer, answer != null ? inputRepo.selectByAnswerID(answer.getAnswerID()) : null, answer != null ? optionEntriesRepo.selectByAnswerID(answer.getAnswerID()) : null, testServ.findCorrectPoints(questionToAdd.getQuestionID()));
        questions.add(questionAndAnswer);
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
        Long check = checkValidAssociation(username, moduleID);
        if (check != null && AssociationType.TUTOR == check) {
            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            List<Tests> testReturn = new ArrayList<>();
            populateDraftTests(tests, testReturn);
            return testReturn;
        }
        return null;
    }

    private void populateDraftTests(List<Tests> tests, List<Tests> testReturn) {
        for (Tests t : tests) {
            if (t.getScheduled() != SCHEDULED || t.getPractice() == 1) {
                testReturn.add(t);
            }
        }
    }

    /**
     * Performs necessary actions needed to find the tests that are ready to be reviewed and return them to the user
     *
     * @param moduleID - the module id
     * @return the tests ready to be reviewed by the tutor
     */
    public List<TestMarking> reviewMarking(String username, Long moduleID) {
        logger.info("Request made for all marking ready to be reviewed for module with id #{}", moduleID);
        Long check = checkValidAssociation(username, moduleID);
        if (check != null && AssociationType.TUTOR == check) {
            Date now = new Date();
            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            List<TestMarking> tmList = new ArrayList<>();
            populateTestsReadyForReview(now, tests, tmList);
            return tmList;
        }
        return null;
    }

    private void populateTestsReadyForReview(Date now, List<Tests> tests, List<TestMarking> tmList) {
        for (Tests t : tests) {
            try {
                if (now.compareTo(StringToDateUtil.stringToDate(t.getEndDateTime())) >= 0 || t.getPractice() == 1) {
                    int answersUnmarked = 0;
                    List<Answer> answers = answerRepo.selectByTestID(t.getTestID());
                    answersUnmarked = getAnswersUnmarked(answersUnmarked, answers);
                    if (answersUnmarked == READY_FOR_REVIEW || t.getPractice() == 1) {
                        TestMarking tm = new TestMarking(t, 0, 0, answers.size(), 0, 0);
                        tmList.add(tm);
                    }
                }
            } catch (ParseException e) { e.printStackTrace(); }
        }
    }

    private int getAnswersUnmarked(int answersUnmarked, List<Answer> answers) {
        for (Answer a : answers) {
            if (a.getMarkerApproved() != null && a.getMarkerApproved() == 0) {
                answersUnmarked++;
            }
        }
        return answersUnmarked;
    }

    /**
     * Displays all the modules that a given user is associated with as string of
     * HTML5 tags
     *
     * @param username the user
     * @return moduleMessage
     */
    List<Module> myModules(String username) {
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
        if (check != null && AssociationType.STUDENT == check) {
            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            User user = userRepo.selectByUsername(username);
            List<Performance> performanceList = new ArrayList<>();
            populatePerformanceList(tests, user, performanceList);
            return performanceList;
        }
        return null;
    }

    private void populatePerformanceList(List<Tests> tests, User user, List<Performance> performanceList) throws SQLException {
        for (Tests test : tests) {
            if (test.getPublishResults() == PUBLISH_TRUE && test.getPractice() != 1) {
                double classAverage = 0.0;
                TestAndResult tar = null;
                List<TestResult> testResults = testResultRepo.selectByTestID(test.getTestID());
                for (TestResult testResult : testResults) {
                    tar = getTestAndResult(user, test, tar, testResult);
                    classAverage += testResult.getTestScore();
                }
                int questionTotal = 0;
                questionTotal = testServ.getQuestionTotal(test, questionTotal);
                classAverage = ((classAverage * 100) / questionTotal) / testResults.size();
                performanceList.add(new Performance(tar, classAverage));
            }
        }
    }

    private TestAndResult getTestAndResult(User user, Tests test, TestAndResult tar, TestResult testResult) throws SQLException {
        if (testResult.getStudentID().equals(user.getUserID())) {
            tar = new TestAndResult(test, testResult, addQuestionsToList(test, user.getUserID()), user);
        }
        return tar;
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
        return getTheAssociationTypeID(ma, u);
    }

    private Long getTheAssociationTypeID(List<ModuleAssociation> ma, User u) {
        Long theAssociationTypeID = null;
        for (ModuleAssociation m : ma) {
            if (u != null && m.getUserID().equals(u.getUserID())) {
                theAssociationTypeID = m.getAssociationType();
            }
        }
        return theAssociationTypeID != null ? associationTypeRepo.selectByAssociationTypeID(theAssociationTypeID).getAssociationTypeID() : null;
    }

    /**
     * Used to assign a users score to a grade for occasions
     * when a tutor wants to reveal a grade and not score
     *
     * @param score the score
     * @return the grade
     */
    String checkGrade(double score) {
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