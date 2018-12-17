package com.pgault04.controller;

import com.pgault04.entities.Module;
import com.pgault04.entities.Tests;
import com.pgault04.entities.User;
import com.pgault04.pojos.ModuleWithTutor;
import com.pgault04.pojos.Performance;
import com.pgault04.pojos.TestAndGrade;
import com.pgault04.pojos.TestMarking;
import com.pgault04.repositories.ModuleAssociationRepo;
import com.pgault04.repositories.ModuleRepo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.services.ModuleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RestController
@RequestMapping("/modules")
public class ModuleController {

    /**
     * Logs useful information for debugging and problem resolution
     */
    private static final Logger logger = LogManager.getLogger(ModuleController.class);

    @Autowired
    ModuleRepo modRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ModuleAssociationRepo modAssocRepo;

    @Autowired
    ModuleService modService;

    /**
     * Provides interface for user to make requests and have module returned
     *
     * @param moduleID the module id
     * @return the module
     */
    @CrossOrigin
    @RequestMapping(value = "/getByModuleID", method = RequestMethod.GET)
    public Module getModuleByID(Long moduleID) {
        logger.info("Request made for module information module #{}", moduleID);
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

        logger.info("Request made for module with tutor info, module #{}", moduleID);
        Module m = modRepo.selectByModuleID(moduleID);
        User u = userRepo.selectByUserID(m.getTutorUserID());
        u.setPassword(null); // Protects password from outside view
        return new ModuleWithTutor(u, m);
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

        logger.info("Requests for all module and tutor data for modules associated with user {}", principal.getName());
        List<Module> modules = modService.myModules(principal.getName());
        List<ModuleWithTutor> modTutors = new ArrayList<>();
        for (Module m : modules) {
            User u = userRepo.selectByUserID(m.getTutorUserID());
            u.setPassword(null); // Protects password from outside view
            modTutors.add(new ModuleWithTutor(u, m));
        }
        return modTutors;
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

        logger.info("Request made for active tests for module #{}", moduleID);
        if (modService.checkValidAssociation(principal.getName(), moduleID) != null) {
            return modService.activeTests(moduleID);
        } else {
            return null;
        }
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
    public List<TestAndGrade> getActiveResults(Principal principal, Long moduleID) {

        logger.info("Request made for active results for module #{}", moduleID);
        if ("student".equals(modService.checkValidAssociation(principal.getName(), moduleID))) {
            return modService.activeResults(moduleID, principal.getName());
        } else {
            return null;
        }
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

        logger.info("Request made for scheduled tests for module with id #{}", moduleID);
        if ("tutor".equals(modService.checkValidAssociation(principal.getName(), moduleID))) {
            return modService.scheduledTests(moduleID);
        } else {
            return null;
        }
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

        logger.info("Request made for drafted tests for module with id #{}", moduleID);
        if ("tutor".equals(modService.checkValidAssociation(principal.getName(), moduleID))) {
            return modService.testDrafts(moduleID);
        } else {
            return null;
        }
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

        logger.info("Request made for all marking ready to be reviewed for module with id #{}", moduleID);
        if ("tutor".equals(modService.checkValidAssociation(principal.getName(), moduleID))) {
            return modService.reviewMarking(moduleID);
        } else {
            return null;
        }
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

        logger.info("Request made for marking info for module with id #{}", moduleID);
        String check = modService.checkValidAssociation(principal.getName(), moduleID);
        if (!"student".equals(check) && check != null) {
            return modService.marking(moduleID, principal.getName(), check);
        } else {
            return null;
        }
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
    public List<Performance> getPerformance(Principal principal, Long moduleID) {

        logger.info("Request made for performance statistics for module with id #{}", moduleID);
        String check = modService.checkValidAssociation(principal.getName(), moduleID);
        if (!"teaching assistant".equalsIgnoreCase(check) && check != null) {
            return modService.generatePerformance(moduleID, principal);
        } else {
            return null;
        }
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
    public String getModuleAssociation(Principal principal, Long moduleID) {

        logger.info("Request made for {}'s association with module #{}", principal.getName(), moduleID);
        return "\"" + modService.checkValidAssociation(principal.getName(), moduleID) + "\"";

    }

}
