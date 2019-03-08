package com.pgault04.controller;

import com.pgault04.entities.Module;
import com.pgault04.entities.Tests;
import com.pgault04.entities.User;
import com.pgault04.pojos.*;
import com.pgault04.repositories.ModuleAssociationRepo;
import com.pgault04.repositories.ModuleRepo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.services.ModuleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.AddressException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RestController
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    ModuleRepo modRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ModuleAssociationRepo modAssocRepo;

    @Autowired
    ModuleService modService;

    public ModuleController() {}

    /**
     * Provides interface for user to make requests and have module returned
     *
     * @param moduleID the module id
     * @return the module
     */
    @CrossOrigin
    @RequestMapping(value = "/getByModuleID", method = RequestMethod.GET)
    public Module getModuleByID(Long moduleID) {
        return modRepo.selectByModuleID(moduleID);
    }

    /**
     * Provides interface for user to make requests and have module with tutor data returned
     *
     * @param moduleID the module id
     * @return the module along with tutor info
     */
    @CrossOrigin
    @RequestMapping(value = "/getModuleAndTutor", method = RequestMethod.GET)
    public ModuleWithTutor getModuleWithTutor(Long moduleID) {
        return modService.getModuleWithTutor(moduleID);
    }

    /**
     * Provides interface for user to make requests and have all their associated modules with tutor info returned
     *
     * @param principal the principal user
     * @return the list of their modules and tutors
     */
    @CrossOrigin
    @RequestMapping(value = "/getMyModulesWithTutors", method = RequestMethod.GET)
    public List<ModuleWithTutor> getModulesWithTutors(Principal principal) {
        return modService.getMyModulesWithTutor(principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/addModule", method = RequestMethod.POST)
    public void addModule(@RequestBody ModulePojo modulePojo, Principal principal) throws IllegalArgumentException {
        modService.addModule(modulePojo, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/addAssociations", method = RequestMethod.POST)
    public void addAssociations(Long moduleID, @RequestBody List<Associate> associationPojos, Principal principal) throws IllegalArgumentException {
        modService.addAssociations(moduleID, associationPojos, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/getModulesPendingApproval", method = RequestMethod.GET)
    public List<Module> getModulesPendingApproval(Principal principal) {
        return modService.getModulesPendingApproval(principal.getName());
    }

    /**
     * Provides interface for user to make requests and have actives tests for a given module returned
     *
     * @param principal the principal user
     * @param moduleID  the module id
     * @return the active tests for this module
     */
    @CrossOrigin
    @RequestMapping(value = "/getActiveTests", method = RequestMethod.GET)
    public List<Tests> getActiveTests(Principal principal, Long moduleID) {
        return modService.activeTests(principal.getName(), moduleID);
    }

    @CrossOrigin
    @RequestMapping(value = "/getPracticeTests", method = RequestMethod.GET)
    public List<Tests> getPracticeTests(Principal principal, Long moduleID) {
        return modService.practiceTests(principal.getName(), moduleID);
    }

    /**
     * Provides interface for user to make requests and have users active results for this module returned
     *
     * @param principal the user
     * @param moduleID  the module id
     * @return a list of relevant tests and grade obtained by the user
     */
    @CrossOrigin
    @RequestMapping(value = "/getActiveResults", method = RequestMethod.GET)
    public List<TestAndGrade> getActiveResults(Principal principal, Long moduleID) throws SQLException {
        return modService.activeResults(moduleID, principal.getName());
    }

    /**
     * Provides interface for user to make requests and have the scheduled tests for this module returned
     *
     * @param principal the user
     * @param moduleID  the module id
     * @return the scheduled tests
     */
    @CrossOrigin
    @RequestMapping(value = "/getScheduledTests", method = RequestMethod.GET)
    public List<Tests> getScheduledTests(Principal principal, Long moduleID) {
        return modService.scheduledTests(principal.getName(), moduleID);
    }

    /**
     * Provides interface for user to make requests and have drafted tests for this module returned
     *
     * @param principal the user (must be tutor)
     * @param moduleID  the module id
     * @return the list of test drafts
     */
    @CrossOrigin
    @RequestMapping(value = "/getTestDrafts", method = RequestMethod.GET)
    public List<Tests> getTestDrafts(Principal principal, Long moduleID) {
        return modService.testDrafts(principal.getName(), moduleID);
    }

    /**
     * Provides interface for user to make requests and have the tests that are ready for review from this module returned
     *
     * @param principal the user (must be tutor)
     * @param moduleID  module id
     * @return list of tests with marking data
     */
    @CrossOrigin
    @RequestMapping(value = "/getReviewMarking", method = RequestMethod.GET)
    public List<TestMarking> getReviewMarking(Principal principal, Long moduleID) {
        return modService.reviewMarking(principal.getName(), moduleID);
    }

    /**
     * Provides interface for user to make requests and have the marking data for the module returned
     *
     * @param principal the user (cant be a student)
     * @param moduleID  the module id
     * @return the test with marking data
     */
    @CrossOrigin
    @RequestMapping(value = "/getMarking", method = RequestMethod.GET)
    public List<TestMarking> getMarking(Principal principal, Long moduleID) {
        return modService.marking(moduleID, principal.getName());
    }

    /**
     * Provides interface for user to make requests and have performance stats for tests in this module returned
     *
     * @param principal the user
     * @param moduleID  the module id
     * @return the necessary performance statistics
     */
    @CrossOrigin
    @RequestMapping(value = "/getPerformance", method = RequestMethod.GET)
    public List<Performance> getPerformance(Principal principal, Long moduleID) throws SQLException {
        return modService.generatePerformance(moduleID, principal.getName());
    }

    /**
     * Provides interface for user to make requests and have module association returned for a given module
     *
     * @param principal the user
     * @param moduleID  the module id
     * @return the string containing the users association type
     */
    @CrossOrigin
    @RequestMapping(value = "/getModuleAssociation", method = RequestMethod.GET)
    public Long getModuleAssociation(Principal principal, Long moduleID) {
        return modService.checkValidAssociation(principal.getName(), moduleID);
    }

    @CrossOrigin
    @RequestMapping(value = "/getModuleRequests", method = RequestMethod.GET)
    public List<ModuleRequestPojo> getModuleRequests(Principal principal) {
        return modService.getModuleRequests(principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/approveModuleRequest", method = RequestMethod.GET)
    public void approveModuleRequest(Long moduleID, Principal principal) {
        modService.approveModuleRequest(moduleID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/rejectModuleRequest", method = RequestMethod.GET)
    public void rejectModuleRequest(Long moduleID, Principal principal) {
        modService.rejectModuleRequest(moduleID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/getAssociates", method = RequestMethod.GET)
    public List<Associate> getAssociates(Long moduleID, Principal principal) {
        return modService.getAssociates(moduleID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/removeAssociate", method = RequestMethod.GET)
    public void removeAssociate(String username, Long moduleID, Principal principal) {
        modService.removeAssociate(username, moduleID, principal.getName());
    }
}