package com.pgault04.controller;

import com.pgault04.entities.QuestionType;
import com.pgault04.entities.Tests;
import com.pgault04.pojos.TutorQuestionPojo;
import com.pgault04.repositories.QuestionTypeRepo;
import com.pgault04.services.TestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * @author Paul Gault
 * @since December 2018
 */
@RestController
@RequestMapping("/tests")
public class TestController {

    /**
     * Logs useful info for debugging and analysis needs
     */
    private static final Logger logger = LogManager.getLogger(TestController.class);

    @Autowired
    TestService testService;

    @Autowired
    QuestionTypeRepo questionTypeRepo;

    /**
     * The default constructor need for rest calls
     */
    public TestController() {
    }

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
        logger.info("Request made to add a test to the database by {}", principal.getName());
        return testService.addTest(test, principal.getName());
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
        logger.info("Request made for test #{} with tutor info by {}", testID, principal.getName());
        return testService.getByTestIDTutorView(principal.getName(), testID);
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
    public List<TutorQuestionPojo> getQuestionsByTestIDTutorView(Principal principal, Long testID) {
        logger.info("Request made for questions and all necessary info requited by tutor for test #{} by {}", testID, principal.getName());
        return testService.getQuestionsByTestIDTutorView(principal.getName(), testID);
    }

    /**
     * Gets the questions written by the user that are not currently being used in the given test
     *
     * @param principal - the principal user
     * @param testID    - the test id
     * @return - the list of questions
     */
    @CrossOrigin
    @RequestMapping(value = "/getOldQuestions", method = RequestMethod.GET)
    public List<TutorQuestionPojo> getOldQuestions(Principal principal, Long testID) {
        logger.info("Request made for all old questions that aren't being used by test #{}", testID);
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
        logger.info("Request made for list of different question types.");
        return questionTypeRepo.selectAll();
    }

    /**
     * Adds a new question to the database
     *
     * @param questionData the data input by the tutor
     * @param principal    - the principal user (the tutor in this case)
     * @return the inserted question
     */
    @CrossOrigin
    @RequestMapping(value = "/addQuestion", method = RequestMethod.POST)
    public TutorQuestionPojo newQuestion(@RequestBody TutorQuestionPojo questionData, Principal principal) {
        logger.info("Request made to add new question in to the database by {}", principal.getName());
        try {
            return testService.newQuestion(questionData, principal.getName());
        } catch (Exception e) {
            return null;
        }
    }
}
