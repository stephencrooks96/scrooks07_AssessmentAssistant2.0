package com.pgault04.controller;

import com.pgault04.entities.QuestionType;
import com.pgault04.entities.TestQuestion;
import com.pgault04.entities.Tests;
import com.pgault04.pojos.QuestionAndAnswer;
import com.pgault04.pojos.QuestionAndBase64;
import com.pgault04.pojos.TutorQuestionPojo;
import com.pgault04.repositories.QuestionTypeRepo;
import com.pgault04.services.TestService;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * @author Paul Gault
 * @since December 2018
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

    @CrossOrigin
    @RequestMapping(value = "/submitTest", method = RequestMethod.POST)
    public boolean submitTest(Principal principal, @RequestBody List<QuestionAndAnswer> script) {
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
    @RequestMapping(value = "/getByTestIDTutorView", method = RequestMethod.GET)
    public Tests getByTestIDTutorView(Principal principal, Long testID) {
        return testService.getByTestIDTutorView(principal.getName(), testID);
    }

    @CrossOrigin
    @RequestMapping(value = "/getQuestionsStudent", method = RequestMethod.GET)
    public List<QuestionAndBase64> getQuestionsStudent(Principal principal, Long testID) throws Base64DecodingException, SQLException {
        return testService.getQuestionsStudent(principal.getName(), testID);
    }

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
    public List<TutorQuestionPojo> getQuestionsByTestIDTutorView(Principal principal, Long testID) throws Base64DecodingException, SQLException {
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
    public List<TutorQuestionPojo> getOldQuestions(Principal principal, Long testID) throws Base64DecodingException, SQLException {
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
    public TutorQuestionPojo editQuestion(@RequestBody TutorQuestionPojo questionData, Principal principal) {
        try {
            return testService.newQuestion(questionData, principal.getName(), true);
        } catch (Exception e) { e.printStackTrace(); return null; }
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
    public TutorQuestionPojo newQuestion(@RequestBody TutorQuestionPojo questionData, Principal principal) {
        try {
            return testService.newQuestion(questionData, principal.getName(), false);
        } catch (Exception e) { e.printStackTrace(); return null; }
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
        try {
            return testService.addExistingQuestion(questionID, testID, principal.getName());
        } catch (Exception e) { return null; }
    }

    @CrossOrigin
    @RequestMapping(value = "/duplicateQuestion", method = RequestMethod.GET)
    public Boolean addExistingQuestion(Long questionID, Principal principal) {
        try {
            return testService.duplicateQuestion(questionID, principal.getName());
        } catch (Exception e) { return null; }
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

    @CrossOrigin
    @RequestMapping(value = "/removeCorrectPoint", method = RequestMethod.DELETE)
    public Boolean removeCorrectPoint(Long correctPointID, Principal principal) {
        return testService.removeCorrectPoint(correctPointID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/removeOption", method = RequestMethod.DELETE)
    public Boolean removeOption(Long optionID, Principal principal) {
        return testService.removeOption(optionID, principal.getName());
    }

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
    public Boolean scheduleTest(Long testID, Principal principal) {
        return testService.scheduleTest(testID, principal.getName());
    }
}