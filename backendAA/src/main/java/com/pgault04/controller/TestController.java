package com.pgault04.controller;

import com.pgault04.entities.QuestionType;
import com.pgault04.entities.TestQuestion;
import com.pgault04.entities.Tests;
import com.pgault04.pojos.Performance;
import com.pgault04.pojos.QuestionAndAnswer;
import com.pgault04.pojos.TutorQuestionPojo;
import com.pgault04.repositories.QuestionTypeRepo;
import com.pgault04.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

/**
 * @author Paul Gault
 * @since December 2018
 * Rest controller for test functions
 */
@RestController
@RequestMapping("/tests")
public class TestController {

    @Autowired
    TestService testService;
    @Autowired
    QuestionTypeRepo questionTypeRepo;

    /**
     * The default constructor need for rest calls
     */
    public TestController() {}

    /**
     * Adds a new test to the database
     *
     * @param principal - the principal user
     * @param test      - the test
     * @return the saved test
     */
    @CrossOrigin
    @RequestMapping(value = "/addTest", method = RequestMethod.POST)
    public Tests saveTest(Principal principal, @RequestBody Tests test) {
        return testService.addTest(test, principal.getName());
    }

    /**
     * Rest endpoint to allow a user to submit a test
     *
     * @param principal - the logged in user
     * @param script    - the test script being inserted
     * @return success / failure
     * @throws SQLException - if the blob used for image is invalid
     */
    @CrossOrigin
    @RequestMapping(value = "/submitTest", method = RequestMethod.POST)
    public boolean submitTest(Principal principal, @RequestBody List<QuestionAndAnswer> script) throws SQLException {
        return testService.submitTest(script, principal.getName());
    }

    /**
     * Edits details about a given test in the database
     *
     * @param principal - the principal user
     * @param test      - the test
     * @return the saved test
     */
    @CrossOrigin
    @RequestMapping(value = "/editTest", method = RequestMethod.POST)
    public Tests editTest(Principal principal, @RequestBody Tests test) {
        return testService.editTest(test, principal.getName());
    }

    /**
     * Gets the test by its test id
     *
     * @param principal - the principal user
     * @param testID    - the test id
     * @return the test
     */
    @CrossOrigin
    @RequestMapping(value = "/getByTestID", method = RequestMethod.GET)
    public Tests getByTestIDTutorView(Principal principal, Long testID) {
        return testService.getByTestID(principal.getName(), testID);
    }

    /**
     * Gets questions in a form that students are allowed to see
     *
     * @param principal - the logged in user
     * @param testID    - the test
     * @return the questions and blank answer canvas
     * @throws SQLException - if the blob used for image is invalid
     */
    @CrossOrigin
    @RequestMapping(value = "/getQuestionsStudent", method = RequestMethod.GET)
    public List<QuestionAndAnswer> getQuestionsStudent(Principal principal, Long testID) throws SQLException {
        return testService.getQuestionsStudent(principal.getName(), testID);
    }

    /**
     * Gets performance data - how the student performed in a test
     *
     * @param principal - the logged in user
     * @param testID    - the test
     * @return the performance data
     * @throws SQLException             - if the blob used for image is invalid
     * @throws IllegalArgumentException - if user is not related to the module
     */
    @CrossOrigin
    @RequestMapping(value = "/getPerformance", method = RequestMethod.GET)
    public Performance getPerformance(Principal principal, Long testID) throws SQLException, IllegalArgumentException {
        return testService.getPerformance(testID, principal.getName());
    }

    /**
     * Gets feedback data - how the student performed in a test without exact result
     *
     * @param principal - the logged in user
     * @param testID    - the test
     * @return the performance data
     * @throws SQLException             - if the blob used for image is invalid
     * @throws IllegalArgumentException - if the user is not related to the module
     */
    @CrossOrigin
    @RequestMapping(value = "/getFeedback", method = RequestMethod.GET)
    public Performance getFeedback(Principal principal, Long testID) throws SQLException, IllegalArgumentException {
        return testService.getGrades(testID, principal.getName());
    }

    /**
     * Gets tests that the student has answered so as to not provide them with the take test link again
     *
     * @param principal - the logged in user
     * @return - the list of answered tests
     */
    @CrossOrigin
    @RequestMapping(value = "/getAnsweredTests", method = RequestMethod.GET)
    public Set<Integer> getAnsweredTests(Principal principal) {
        return testService.getAnsweredTests(principal.getName());
    }

    /**
     * Gets the questions that are currently being used in the test
     *
     * @param principal - the principal user
     * @param testID    - the test id
     * @return the questions with all info needed by tutor
     */
    @CrossOrigin
    @RequestMapping(value = "/getQuestionsByTestIDTutorView", method = RequestMethod.GET)
    public List<TutorQuestionPojo> getQuestionsByTestIDTutorView(Principal principal, Long testID) throws SQLException {
        return testService.getQuestionsByTestIDTutorView(principal.getName(), testID);
    }

    /**
     * Gets the questions written by the user that are not currently being used in the given test
     *
     * @param principal the principal user
     * @param testID    the test id
     * @return the list of questions
     */
    @CrossOrigin
    @RequestMapping(value = "/getOldQuestions", method = RequestMethod.GET)
    public List<TutorQuestionPojo> getOldQuestions(Principal principal, Long testID) throws SQLException {
        return testService.getOldQuestions(principal.getName(), testID);
    }

    /**
     * Gets all types of question for output and selection on the front end
     *
     * @return the question types
     */
    @CrossOrigin
    @RequestMapping(value = "/getQuestionTypes", method = RequestMethod.GET)
    public List<QuestionType> getQuestionTypes() {
        return questionTypeRepo.selectAll();
    }

    /**
     * Edits a question to the database
     *
     * @param questionData the data input by the tutor
     * @param principal    the principal user (the tutor in this case)
     * @return the inserted question
     */
    @CrossOrigin
    @RequestMapping(value = "/editQuestion", method = RequestMethod.POST)
    public TutorQuestionPojo editQuestion(@RequestBody TutorQuestionPojo questionData, Principal principal) throws SQLException {
        return testService.newQuestion(questionData, principal.getName(), true);

    }

    /**
     * Adds a new question to the database
     *
     * @param questionData the data input by the tutor
     * @param principal    the principal user (the tutor in this case)
     * @return the inserted question
     */
    @CrossOrigin
    @RequestMapping(value = "/addQuestion", method = RequestMethod.POST)
    public TutorQuestionPojo newQuestion(@RequestBody TutorQuestionPojo questionData, Principal principal) throws SQLException {
        return testService.newQuestion(questionData, principal.getName(), false);
    }

    /**
     * Provides an entry point for requests made to add a question that a user has used before to another test they are creating
     *
     * @param questionID the id of the question to add
     * @param testID     the id of the test to add to
     * @param principal  the user
     * @return the object representing the record in the TestQuestion table after addition
     */
    @CrossOrigin
    @RequestMapping(value = "/addExistingQuestion", method = RequestMethod.GET)
    public TestQuestion addExistingQuestion(Long questionID, Long testID, Principal principal) {
        return testService.addExistingQuestion(questionID, testID, principal.getName());
    }

    /**
     * Duplicates a question for use on another test without interfering with past marks
     *
     * @param questionID - the question to duplicate
     * @param principal  - the logged in user
     * @return the duplicated question
     */
    @CrossOrigin
    @RequestMapping(value = "/duplicateQuestion", method = RequestMethod.GET)
    public Boolean duplicateQuestion(Long questionID, Principal principal) {
        return testService.duplicateQuestion(questionID, principal.getName());
    }

    /**
     * Rest mapping allowing for requests to be made to remove a question from a test by a tutor
     *
     * @param testID     - the test's id
     * @param questionID - the question's id
     * @param principal  - the user making the request
     * @return boolean flag indicating status of request
     */
    @CrossOrigin
    @RequestMapping(value = "/removeQuestionFromTest", method = RequestMethod.DELETE)
    public Boolean removeQuestionFromTest(Long testID, Long questionID, Principal principal) {
        return testService.removeQuestionFromTest(testID, questionID, principal.getName());
    }

    /**
     * Removes a correct point during the question creation / editing phase
     *
     * @param correctPointID - the correct point to remove
     * @param principal      - the logged in user
     * @return success / failure
     */
    @CrossOrigin
    @RequestMapping(value = "/removeCorrectPoint", method = RequestMethod.DELETE)
    public Boolean removeCorrectPoint(Long correctPointID, Principal principal) {
        return testService.removeCorrectPoint(correctPointID, principal.getName());
    }

    /**
     * Removes a question math line during the question creation / editing phase
     *
     * @param questionMathLineID - the question math line to remove
     * @param principal          - the logged in user
     * @return success / failure
     */
    @CrossOrigin
    @RequestMapping(value = "/removeQuestionMathLine", method = RequestMethod.DELETE)
    public Boolean removeQuestionMathLine(Long questionMathLineID, Principal principal) {
        return testService.removeQuestionMathLine(questionMathLineID, principal.getName());
    }

    /**
     * Removes an option during the question creation / editing phase
     *
     * @param optionID  - the option to remove
     * @param principal - the logged in user
     * @return success / failure
     */
    @CrossOrigin
    @RequestMapping(value = "/removeOption", method = RequestMethod.DELETE)
    public Boolean removeOption(Long optionID, Principal principal) {
        return testService.removeOption(optionID, principal.getName());
    }

    /**
     * Removes an alternative during the question creation / editing phase
     *
     * @param alternativeID - the alternative to remove
     * @param principal     - the logged in user
     * @return success / failure
     */
    @CrossOrigin
    @RequestMapping(value = "/removeAlternative", method = RequestMethod.DELETE)
    public Boolean removeAlternative(Long alternativeID, Principal principal) {
        return testService.removeAlternative(alternativeID, principal.getName());
    }

    /**
     * Provides an entry point for requests made by a user to delete a given test
     *
     * @param testID    the test's id
     * @param principal the user
     * @return true or false on whether the test is deleted or not
     */
    @CrossOrigin
    @RequestMapping(value = "/deleteTest", method = RequestMethod.DELETE)
    public Boolean deleteTest(Long testID, Principal principal) {
        return testService.deleteTest(testID, principal.getName());
    }

    /**
     * Provides an entry point to this backend app for users wanting to schedule a test for release to students
     *
     * @param testID    the test's id
     * @param principal the user making the request
     * @return true/false flag
     */
    @CrossOrigin
    @RequestMapping(value = "/scheduleTest", method = RequestMethod.GET)
    public Boolean scheduleTest(Long testID, Principal principal) throws ParseException {
        return testService.scheduleTest(testID, principal.getName());
    }
}