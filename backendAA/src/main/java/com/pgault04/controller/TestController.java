package com.pgault04.controller;

import com.pgault04.entities.QuestionType;
import com.pgault04.entities.Tests;
import com.pgault04.pojos.TutorQuestionPojo;
import com.pgault04.repositories.QuestionTypeRepo;
import com.pgault04.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tests")
public class TestController {

    @Autowired
    TestService testServ;

    @Autowired
    QuestionTypeRepo questionTypeRepo;

    public TestController() {}

    /**
     * @param principal
     * @param test
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/addTest", method = RequestMethod.POST)
    public Tests saveTest(Principal principal, @RequestBody Tests test) {
        return testServ.addTest(test, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/getByTestIDTutorView", method = RequestMethod.GET)
    public Tests getByTestIDTutorView(Principal principal, Long testID) {
        return testServ.getByTestIDTutorView(principal.getName(), testID);
    }

    @CrossOrigin
    @RequestMapping(value = "/getQuestionsByTestIDTutorView", method = RequestMethod.GET)
    public List<TutorQuestionPojo> getQuestionsByTestIDTutorView(Principal principal, Long testID) {
        return testServ.getQuestionsByTestIDTutorView(principal.getName(), testID);
    }

    @CrossOrigin
    @RequestMapping(value = "/getQuestionTypes", method = RequestMethod.GET)
    public List<QuestionType> getQuestionTypes() {
        return questionTypeRepo.selectAll();
    }

    /**
     *
     * @param questionData
     * @param principal
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/newQuestion", method = RequestMethod.POST)
    public Boolean newQuestion(@RequestBody TutorQuestionPojo questionData, Principal principal) {

        try {

            testServ.newQuestion(questionData, principal.getName());
            return true;

        } catch (Exception e) {

            return false;
        }


    }
}
