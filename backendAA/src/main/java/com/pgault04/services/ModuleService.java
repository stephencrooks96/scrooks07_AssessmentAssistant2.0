package com.pgault04.services;

import com.pgault04.entities.*;
import com.pgault04.pojos.*;
import com.pgault04.repositories.*;
import com.pgault04.utilities.BlobUtil;
import com.pgault04.utilities.EmailUtil;
import com.pgault04.utilities.PasswordUtil;
import com.pgault04.utilities.StringToDateUtil;
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
     * Method to retrieve list of all module requests
     * and display them to the admins for approval / rejection
     *
     * @param username - the user requesting the list
     * @return the module requests or null (if user is not an admin)
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
     * Allows admin to approve a users request to add a certain module.
     * Also asynchronously informs associates that they have been added to the module
     * and informs the tutor their request has been approved.
     *
     * @param moduleID - the module
     * @param username - the users who is performing the action
     */
    public void approveModuleRequest(Long moduleID, String username) {
        User admin = userRepo.selectByUsername(username);
        if (admin != null && admin.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            Module module = moduleRepo.selectByID(moduleID);
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
     * Allows admin to reject a module request
     * Asynchronously informs the tutor that this module request has been rejected
     *
     * @param moduleID - the module to be rejected
     * @param username - the user performing the action
     */
    public void rejectModuleRequest(Long moduleID, String username) {
        User user = userRepo.selectByUsername(username);
        if (user != null && user.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            Module module = moduleRepo.selectByID(moduleID);
            moduleRepo.delete(moduleID);
            emailSender.sendModuleRequestRejected(module);
        }
    }

    /**
     * Removes an associate from a module
     * Asynchronously informs them that they have been removed
     *
     * @param username  - the user to be removed from the module
     * @param moduleID  - the module
     * @param principal - the user performing the action (must be the module tutor)
     */
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

    /**
     * Retrieves all associates for the module for display in the module home
     * using the Associate pojo
     *
     * @param moduleID  - the module
     * @param principal - the user making the request (must be the module tutor)
     * @return the associates or null if the user is not the tutor
     */
    public List<Associate> getAssociates(Long moduleID, String principal) {
        if (checkValidAssociation(principal, moduleID) != null) {
            List<ModuleAssociation> moduleAssociations = modAssocRepo.selectByModuleID(moduleID);
            List<Associate> associates = new ArrayList<>();
            for (ModuleAssociation ma : moduleAssociations) {
                User user = userRepo.selectByUserID(ma.getUserID());
                String associationType;
                associationType = setUserAssociationTypeForAssociateList(ma);
                associates.add(new Associate(associationType, user.getUsername(), user.getFirstName(), user.getLastName()));
            }
            return associates;
        }
        return null;
    }

    /*
     * Checks which type of associates they are and sets a string to add to the pojo
     */
    private String setUserAssociationTypeForAssociateList(ModuleAssociation ma) {
        String associationType;
        if (ma.getAssociationType().equals(AssociationType.TUTOR)) {
            associationType = "Tutor";
        } else if (ma.getAssociationType().equals(AssociationType.STUDENT)) {
            associationType = "Student";
        } else {
            associationType = "Teaching Assistant";
        }
        return associationType;
    }

    /**
     * Performs necessary actions needed to retrieve the active tests
     * from database and return them to user for a given module
     * Active test is one that is scheduled, not a practice test and current time is within the start-end period
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

    /**
     * Performs necessary actions needed to retrieve the practice tests
     * from database and return them to user for a given module
     * Practice test is one that is scheduled, marked as a practice test and current time is within the start-end period
     *
     * @param username - the user making the request
     * @param moduleID - the module
     * @return the list of practice tests or null if invalid association
     */
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
     * combined in a pojo
     *
     * @param moduleID the module id
     * @return the module with tutor info
     */
    public ModuleWithTutor getModuleWithTutor(Long moduleID) {
        logger.info("Request made for module with tutor info, module #{}", moduleID);
        Module m = moduleRepo.selectByID(moduleID);
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
     * Allows a tutor to add a new module
     * Sets all required data and emails the tutor informing them that their
     * request is pending approval
     * Also emails admins to remind them to approve the module
     *
     * @param modulePojo - the module request information
     * @param username   - the user requesting the module addition
     * @throws IllegalArgumentException thrown if user is not a tutor
     */
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

    /*
     * Asynchronously cycles through the admins for the system and emails them about a new module pending approval
     */
    private void emailAdmins(ModulePojo modulePojo, User user) {
        List<User> admins = userRepo.selectAll();
        for (User u : admins) {
            if (u.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
                emailSender.sendNewModuleMessageFromSystemToAdmin(modulePojo.getModule(), user.getUsername(), u.getUsername());
            }
        }
    }

    /**
     * Adds associations to a module
     * Can only be a teaching assistant (TA) or student (S)
     * Comes from CSV uploaded on front end and are converted to objects of Associate type
     *
     * @param moduleID     - the module they are being added to
     * @param associations - the new associations
     * @param username     - the user requesting to add the associations
     * @throws IllegalArgumentException - thrown when user attempting to add the associations is not a tutor
     *                                  or if incorrect AssociationType is added
     */
    public void addAssociations(Long moduleID, List<Associate> associations, String username) throws IllegalArgumentException {
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
                finaliseNewModuleAssociationAndInsert(moduleID, user, moduleAssociation);
            }
            if (associationTypeError) {
                throw new IllegalArgumentException("One or more associations have included a type which is not TA or S.");
            }
        } else {
            throw new IllegalArgumentException("You must be the module tutor to perform this action.");
        }
    }

    /*
     * Sets the association id to initial for insertion
     * Sets user and moduleID before entering
     */
    private void finaliseNewModuleAssociationAndInsert(Long moduleID, User user, ModuleAssociation moduleAssociation) {
        moduleAssociation.setAssociationID(-1L);
        moduleAssociation.setUserID(user.getUserID());
        moduleAssociation.setModuleID(moduleID);
        modAssocRepo.insert(moduleAssociation);
    }

    /*
     * Creates a new user when one doesn't exist corresponding to a user in the CSV file that's been uploaded
     * Adds the new user in to database
     * Gives the user a random password which is emailed to them
     * Generates a session and a password reset string for the user
     */
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
     *
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

    /*
     * Checks which type of marker the user is and calls methods to populate marking
     * data accordingly
     */
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

    /*
     * If the user is a tutor then this method is called to
     * populate how many answers the teaching assistants have and haven't marked so far
     */
    private void populateMarkingDataForTeachingAssistants(TestMarking tm, Answer a) {
        tm.setTotalForTAs(tm.getTotalForTAs() + 1);
        if (a.getMarkerApproved() != null && a.getMarkerApproved() != MARKER_APPROVED) {
            tm.setToBeMarkedByTAs(tm.getToBeMarkedByTAs() + 1);
        } else {
            tm.setMarked(tm.getMarked() + 1);
        }
    }

    /*
     * Populates the users own marking data
     * i.e. how many answers they have and haven't marked
     */
    private void populateMarkingDataForUser(TestMarking tm, Answer a) {
        tm.setTotalForYou(tm.getTotalForYou() + 1);
        if (a.getMarkerApproved() != null && a.getMarkerApproved() != MARKER_APPROVED) {
            tm.setToBeMarkedByYou(tm.getToBeMarkedByYou() + 1);
        } else {
            tm.setMarked(tm.getMarked() + 1);
        }
    }

    /**
     * Performs necessary actions needed to retrieve the active grades
     * from database and return them to user for a given module
     *
     * @param moduleID the module id
     * @param username the user
     * @return the active grades
     */
    public List<TestAndGrade> activeGrades(Long moduleID, String username) throws SQLException {
        logger.info("Request made for active results for module #{}", moduleID);
        Long check = checkValidAssociation(username, moduleID);
        if (check != null && AssociationType.STUDENT == check) {
            List<Tests> tests = testsRepo.selectByModuleID(moduleID);
            User user = userRepo.selectByUsername(username);
            List<TestAndGrade> testAndGradeList = new ArrayList<>();
            populateActiveGrades(tests, user, testAndGradeList);
            return testAndGradeList;
        }
        return null;
    }

    /*
     * Populates each grade and necessary data for any tests that are publishing grades currently
     */
    private void populateActiveGrades(List<Tests> tests, User user, List<TestAndGrade> testAndGradeList) throws SQLException {
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

    /*
     * Gets the total score of questions in the test the Result is being generated for
     */
    private double getPercentageScore(List<QuestionAndAnswer> questions, double percentageScore) {
        for (QuestionAndAnswer q : questions) {
            percentageScore += q.getQuestion().getQuestion().getMaxScore();
        }
        return percentageScore;
    }

    /*
     * Adds questions from a test to a list and returns the list
     */
    List<QuestionAndAnswer> addQuestionsToList(Tests test, Long userID) throws SQLException {
        List<QuestionAndAnswer> questions = new ArrayList<>();
        for (TestQuestion testQuestion : testQuestionRepo.selectByTestID(test.getTestID())) {
            populateQuestionAndAnswer(test, userID, questions, testQuestion);
        }
        return questions;
    }

    /*
     * Populates the question and answer object with all necessary date for display
     */
    private void populateQuestionAndAnswer(Tests test, Long userID, List<QuestionAndAnswer> questions, TestQuestion testQuestion) throws SQLException {
        Question questionToAdd = questionRepo.selectByID(testQuestion.getQuestionID());
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

    /*
     * Populates tests in to list that are still in the draft phase
     */
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

    /*
     * Populates tests that are in the marking review phase
     * i.e. tests where every answer has been approved by markers
     */
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

    /*
     * Checks for any answers for the test that are yet to be marked
     */
    private int getAnswersUnmarked(int answersUnmarked, List<Answer> answers) {
        for (Answer a : answers) {
            if (a.getMarkerApproved() != null && a.getMarkerApproved() == 0) {
                answersUnmarked++;
            }
        }
        return answersUnmarked;
    }

    /**
     * Outputs all modules that the logged in user is involved with
     *
     * @param username the user
     * @return moduleMessage
     */
    List<Module> myModules(String username) {
        User user = userRepo.selectByUsername(username);
        List<ModuleAssociation> modAssociations = modAssocRepo.selectByUserID(user.getUserID());
        List<Module> modules = new ArrayList<>();
        for (ModuleAssociation m : modAssociations) {
            Module module = moduleRepo.selectByID(m.getModuleID());
            if (module.getApproved().equals(APPROVED)) {
                modules.add(module);
            }
        }
        return modules;
    }

    /**
     * Performs the actions necessary to get the performance data and return it to the user on front end
     *
     * @param moduleID - the module id
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

    /*
     * Populates all data needed to show how student has performed
     * i.e.the student's result and the average score for the class
     */
    private void populatePerformanceList(List<Tests> tests, User user, List<Performance> performanceList) throws SQLException {
        for (Tests test : tests) {
            if (test.getPublishResults() == PUBLISH_TRUE && test.getPractice() != 1) {
                double classAverage = 0.0;
                TestAndResult tar = null;
                List<TestResult> testResults = testResultRepo.selectByTestID(test.getTestID());
                for (TestResult testResult : testResults) {
                    classAverage += testResult.getTestScore();
                    tar = getTestAndResult(user, test, tar, testResult);
                }
                int questionTotal = 0;
                questionTotal = testServ.getQuestionTotal(test, questionTotal);
                classAverage = ((classAverage * 100) / questionTotal) / testResults.size();
                performanceList.add(new Performance(tar, classAverage));
            }
        }
    }

    /*
     * Sets the test and result if it belongs to the user
     * For output with performance data
     */
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

    /*
     * Checks what kind of association the user has to the module
     */
    private Long getTheAssociationTypeID(List<ModuleAssociation> ma, User u) {
        Long theAssociationTypeID = null;
        for (ModuleAssociation m : ma) {
            if (u != null && m.getUserID().equals(u.getUserID())) {
                theAssociationTypeID = m.getAssociationType();
            }
        }
        return theAssociationTypeID != null ? associationTypeRepo.selectByID(theAssociationTypeID).getAssociationTypeID() : null;
    }

    /*
     * Used to assign a users score to a grade for occasions
     * when a tutor wants to reveal a grade and not score
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