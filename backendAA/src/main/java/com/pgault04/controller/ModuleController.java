package com.pgault04.controller;

import com.pgault04.pojos.*;
import com.pgault04.entities.Module;
import com.pgault04.entities.Tests;
import com.pgault04.entities.User;
import com.pgault04.repositories.ModuleAssociationRepo;
import com.pgault04.repositories.ModuleRepo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.services.ModuleService;
import com.pgault04.services.MyModulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    ModuleRepo modRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    MyModulesService myModService;

    @Autowired
    ModuleAssociationRepo modAssocRepo;

    @Autowired
    ModuleService modServ;

    @CrossOrigin
    @RequestMapping(value = "/getByModuleID", method = RequestMethod.GET)
    public Module getModuleByID(Long moduleID) {
        return modRepo.selectByModuleID(moduleID);
    }

    @CrossOrigin
    @RequestMapping(value = "/getModuleAndTutor", method = RequestMethod.GET)
    public ModuleWithTutor getModuleWithTutor(Long moduleID) {

        Module m = modRepo.selectByModuleID(moduleID);
        User u = userRepo.selectByUserID(m.getTutorUserID());
        u.setPassword(null); // Protects password from outside view
        return new ModuleWithTutor(u, m);
    }

    @CrossOrigin
    @RequestMapping(value = "/getMyModulesWithTutors", method = RequestMethod.GET)
    public List<ModuleWithTutor> getModulesWithTutors(Principal principal) {

        List<Module> modules = myModService.myModules(principal.getName());
        List<ModuleWithTutor> modTutors = new ArrayList<>();
        for (Module m : modules) {
            User u = userRepo.selectByUserID(m.getTutorUserID());
            u.setPassword(null); // Protects password from outside view
            modTutors.add(new ModuleWithTutor(u, m));
        }

        return modTutors;
    }

    @CrossOrigin
    @RequestMapping(value = "/getActiveTests", method = RequestMethod.GET)
    public List<Tests> getActiveTests(Principal principal, Long moduleID) {

        if (modServ.checkValidAssociation(principal.getName(), moduleID) != null) {
            return modServ.activeTests(moduleID);
        } else {
            return null;
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/getActiveResults", method = RequestMethod.GET)
    public List<TestAndGrade> getActiveResults(Principal principal, Long moduleID) {

        if ("student".equals(modServ.checkValidAssociation(principal.getName(), moduleID))) {
            return modServ.activeResults(moduleID, principal.getName());
        } else {
            return null;
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/getScheduledTests", method = RequestMethod.GET)
    public List<Tests> getScheduledTests(Principal principal, Long moduleID) {

        if ("tutor".equals(modServ.checkValidAssociation(principal.getName(), moduleID))) {
            return modServ.scheduledTests(moduleID);
        } else {
            return null;
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/getTestDrafts", method = RequestMethod.GET)
    public List<Tests> getTestDrafts(Principal principal, Long moduleID) {

        if ("tutor".equals(modServ.checkValidAssociation(principal.getName(), moduleID))) {
            return modServ.testDrafts(moduleID);
        } else {
            return null;
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/getReviewMarking", method = RequestMethod.GET)
    public List<TestMarking> getReviewMarking(Principal principal, Long moduleID) {

        if ("tutor".equals(modServ.checkValidAssociation(principal.getName(), moduleID))) {
            return modServ.reviewMarking(moduleID);
        } else {
            return null;
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/getMarking", method = RequestMethod.GET)
    public List<TestMarking> getMarking(Principal principal, Long moduleID) {

        String check = modServ.checkValidAssociation(principal.getName(), moduleID);
        if (!"student".equals(check) && check != null) {
            return modServ.marking(moduleID, principal.getName(), check);
        } else {
            return null;
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/getPerformance", method = RequestMethod.GET)
    public List<Performance> getPerformance(Principal principal, Long moduleID) {

        String check = modServ.checkValidAssociation(principal.getName(), moduleID);
        if (!"teaching assistant".equalsIgnoreCase(check) && check != null) {
            return modServ.generatePerformance(moduleID, principal);
        } else {
            return null;
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/getModuleAssociation", method = RequestMethod.GET)
    public String getModuleAssociation(Principal principal, Long moduleID) {

        StringBuilder sb = new StringBuilder("\"");
        sb.append(modServ.checkValidAssociation(principal.getName(), moduleID)).append("\"");
        return sb.toString();
    }

}
