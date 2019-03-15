package com.pgault04.controller;

import com.pgault04.entities.TutorRequests;
import com.pgault04.entities.User;
import com.pgault04.entities.UserSession;
import com.pgault04.pojos.ChangePassword;
import com.pgault04.pojos.TutorRequestPojo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * Logs useful info for debugging and analysis needs
     */
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    public UserController() {}

    /**
     * Finds all users in the system
     *
     * @return the list of all users
     */
    @CrossOrigin
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public UserSession changePassword(@RequestBody ChangePassword changePassword, Principal user) {
        return userService.changePassword(changePassword, user.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public Boolean resetPassword(String email, String newPassword, String resetString) {
        return userService.resetPassword(email, newPassword, resetString);
    }

    @CrossOrigin
    @RequestMapping(value = "/requestResetPassword", method = RequestMethod.GET)
    public Boolean requestResetPassword(String email) {
        return userService.requestResetPassword(email);
    }


    @CrossOrigin
    @RequestMapping(value = "/submitTutorRequest", method = RequestMethod.POST)
    public TutorRequests submitTutorRequest(@RequestBody TutorRequests tutorRequest, Principal user) {
        return userService.submitTutorRequest(tutorRequest, user.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/getTutorRequest", method = RequestMethod.GET)
    public TutorRequests getTutorRequest(Principal user) {
        return userService.getTutorRequest(user.getName());
    }

    /**
     * Checks if the username has been used before
     *
     * @param username the username to check
     * @return whether the username is used or not
     */
    @CrossOrigin
    @RequestMapping(value = "/getUsernames", method = RequestMethod.GET)
    public boolean getUsernames(Principal principal, String username) { return userService.usernameCheck(username, principal); }

    @CrossOrigin
    @RequestMapping(value = "/createProfile", method = RequestMethod.POST)
    public User createProfile(@RequestBody User user) { return userService.createProfile(user); }

    @CrossOrigin
    @RequestMapping(value = "/editProfile", method = RequestMethod.POST)
    public User editProfile(@RequestBody User user, Principal principal) { return userService.editProfile(user, principal); }

    /**
     * Finds all users in the system
     *
     * @return the list of all users
     */
    @CrossOrigin
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<User> findAll() {
        return userRepo.selectAll();
    }

    @CrossOrigin
    @RequestMapping(value = "/getTutorRequests", method = RequestMethod.GET)
    public List<TutorRequestPojo> getTutorRequests(Principal principal) {
        return userService.getTutorRequests(principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/approveTutorRequest", method = RequestMethod.GET)
    public void approveTutorRequest(Long userID, Principal principal) {
        userService.approveTutorRequest(userID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/makeAdmin", method = RequestMethod.GET)
    public void makeAdmin(Long userID, Principal principal) {
        userService.makeAdmin(userID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/makeTutor", method = RequestMethod.GET)
    public void makeTutor(Long userID, Principal principal) {
        userService.makeTutor(userID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/removeUser", method = RequestMethod.GET)
    public void removeUser(Long userID, Principal principal) {
        userService.removeUser(userID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/rejectTutorRequest", method = RequestMethod.GET)
    public void rejectTutorRequest(Long userID, Principal principal) {
        userService.rejectTutorRequest(userID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/getAdmins", method = RequestMethod.GET)
    public List<User> getAdmins() {
        return userService.getAdmins();
    }

    /**
     * Only available to admins - adds users to system
     *
     * @param users the users to add
     * @param principal the principal user
     */
    @CrossOrigin
    @RequestMapping(value = "/addUsers", method = RequestMethod.POST)
    public void addUsers(@RequestBody List<User> users, Principal principal) {
        userService.addUsers(users, principal.getName());
    }
}
