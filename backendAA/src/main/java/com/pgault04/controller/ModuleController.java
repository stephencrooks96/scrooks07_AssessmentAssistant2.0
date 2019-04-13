package com.pgault04.controller;

import com.pgault04.entities.Module;
import com.pgault04.entities.Tests;
import com.pgault04.pojos.*;
import com.pgault04.repositories.ModuleAssociationRepo;
import com.pgault04.repositories.ModuleRepo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 * Rest controller for module functions
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

    /**
     * Default constructor
     */
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
        return modRepo.selectByID(moduleID);
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
    public List<ModuleWithTutor> getModulesWithTutors(Principal principal) { return modService.getMyModulesWithTutor(principal.getName()); }

    /**
     * Provides rest endpoint for user to add a new module
     *
     * @param modulePojo - data required to add module
     * @param principal  - logged in user
     * @throws IllegalArgumentException - if the user is not a valid tutor
     */
    @CrossOrigin
    @RequestMapping(value = "/addModule", method = RequestMethod.POST)
    public void addModule(@RequestBody ModulePojo modulePojo, Principal principal) throws IllegalArgumentException {
        modService.addModule(modulePojo, principal.getName());
    }

    /**
     * Rest endpoint to add associates to a module
     *
     * @param moduleID         - the module
     * @param associationPojos - the data required for the associates
     * @param principal        - the logged in user
     * @throws IllegalArgumentException - if the user is not the tutor of the module
     */
    @CrossOrigin
    @RequestMapping(value = "/addAssociations", method = RequestMethod.POST)
    public void addAssociations(Long moduleID, @RequestBody List<Associate> associationPojos, Principal principal) throws IllegalArgumentException {
        modService.addAssociations(moduleID, associationPojos, principal.getName());
    }

    /**
     * Rest endpoint to deliver which modules need to be approved to the user who requested them
     *
     * @param principal - the logged in user
     * @return the list of modules
     */
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

    /**
     * Rest endpoint to retrieve practice tests
     *
     * @param principal - the logged in user
     * @param moduleID  - the module
     * @return the tests
     */
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
        return modService.activeGrades(moduleID, principal.getName());
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

    /**
     * Gets the modules that have been requested and returns it to admin area
     *
     * @param principal - the logged in user
     * @return the modules requested
     */
    @CrossOrigin
    @RequestMapping(value = "/getModuleRequests", method = RequestMethod.GET)
    public List<ModuleRequestPojo> getModuleRequests(Principal principal) {
        return modService.getModuleRequests(principal.getName());
    }

    /**
     * Rest endpoint to allow an admin to approve a module request
     *
     * @param moduleID  - the module
     * @param principal - the logged in user
     */
    @CrossOrigin
    @RequestMapping(value = "/approveModuleRequest", method = RequestMethod.GET)
    public void approveModuleRequest(Long moduleID, Principal principal) {
        modService.approveModuleRequest(moduleID, principal.getName());
    }

    /**
     * Rest endpoint to allow an admin to reject a module request
     *
     * @param moduleID  - the module
     * @param principal - the logged in user
     */
    @CrossOrigin
    @RequestMapping(value = "/rejectModuleRequest", method = RequestMethod.GET)
    public void rejectModuleRequest(Long moduleID, Principal principal) {
        modService.rejectModuleRequest(moduleID, principal.getName());
    }

    /**
     * Gets the associates for a module
     *
     * @param moduleID  - the module
     * @param principal - the logged in user
     * @return the associates
     */
    @CrossOrigin
    @RequestMapping(value = "/getAssociates", method = RequestMethod.GET)
    public List<Associate> getAssociates(Long moduleID, Principal principal) {
        return modService.getAssociates(moduleID, principal.getName());
    }

    /**
     * Removes an associate from a module
     *
     * @param username  - the associate to remove
     * @param moduleID  - the module
     * @param principal - the logged in user
     */
    @CrossOrigin
    @RequestMapping(value = "/removeAssociate", method = RequestMethod.GET)
    public void removeAssociate(String username, Long moduleID, Principal principal) {
        modService.removeAssociate(username, moduleID, principal.getName());
    }
}