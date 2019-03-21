package com.pgault04.controller;

import com.pgault04.entities.TutorRequests;
import com.pgault04.entities.User;
import com.pgault04.entities.UserSession;
import com.pgault04.pojos.ChangePassword;
import com.pgault04.pojos.TutorRequestPojo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 * Rest Controller for user functions
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    UserService userService;

    /**
     * Default constructor
     */
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

    /**
     * Rest endpoint to reset a users password
     *
     * @param email       - the user
     * @param newPassword - the new password
     * @param resetString - the reset string corresponding to the users account
     * @return success / failure
     */
    @CrossOrigin
    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public Boolean resetPassword(String email, String newPassword, String resetString) {
        return userService.resetPassword(email, newPassword, resetString);
    }

    /**
     * Rest endpoint to allow user to request to be emailed a reset password link
     *
     * @param email - the user
     * @return success / failure
     */
    @CrossOrigin
    @RequestMapping(value = "/requestResetPassword", method = RequestMethod.GET)
    public Boolean requestResetPassword(String email) {
        return userService.requestResetPassword(email);
    }

    /**
     * Rest endpoint to allow user to submit a request to become a tutor
     *
     * @param tutorRequest - the tutor request info
     * @param user         - the logged in user
     * @return the tutor request
     */
    @CrossOrigin
    @RequestMapping(value = "/submitTutorRequest", method = RequestMethod.POST)
    public TutorRequests submitTutorRequest(@RequestBody TutorRequests tutorRequest, Principal user) {
        return userService.submitTutorRequest(tutorRequest, user.getName());
    }

    /**
     * Gets whether the user has made a tutor request before or not
     *
     * @param user - the logged in user
     * @return the tutor request (if any)
     */
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

    /**
     * Provides rest endpoint for user to request to create a profile
     *
     * @param user - the user data needed to create the profile
     * @return the user after creation
     */
    @CrossOrigin
    @RequestMapping(value = "/createProfile", method = RequestMethod.POST)
    public User createProfile(@RequestBody User user) { return userService.createProfile(user); }

    /**
     * Provides rest endpoint for user to request to edit their profile
     *
     * @param user      - the user's edited data
     * @param principal - the logged in user
     * @return the user after editing
     */
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

    /**
     * Gets all tutor request for admins to approve / reject
     *
     * @param principal - the logged in user
     * @return the tutor requests
     */
    @CrossOrigin
    @RequestMapping(value = "/getTutorRequests", method = RequestMethod.GET)
    public List<TutorRequestPojo> getTutorRequests(Principal principal) {
        return userService.getTutorRequests(principal.getName());
    }

    /**
     * Provides rest endpoint for admin to approve or reject a tutor request
     *
     * @param userID    - the user to approve
     * @param principal - the logged in user
     */
    @CrossOrigin
    @RequestMapping(value = "/approveTutorRequest", method = RequestMethod.GET)
    public void approveTutorRequest(Long userID, Principal principal) {
        userService.approveTutorRequest(userID, principal.getName());
    }

    /**
     * Provides rest endpoint for an admin to make another user an admin
     *
     * @param userID    - the user to make an admin
     * @param principal - the logged in user
     */
    @CrossOrigin
    @RequestMapping(value = "/makeAdmin", method = RequestMethod.GET)
    public void makeAdmin(Long userID, Principal principal) {
        userService.makeAdmin(userID, principal.getName());
    }

    /**
     * Provides a rest endpoint for an admin to make another user a tutor (without need for request)
     *
     * @param userID    - the user to make into a tutor
     * @param principal - the logged in user
     */
    @CrossOrigin
    @RequestMapping(value = "/makeTutor", method = RequestMethod.GET)
    public void makeTutor(Long userID, Principal principal) {
        userService.makeTutor(userID, principal.getName());
    }

    /**
     * Provides a rest endpoint for an admin to remove a user from the system
     *
     * @param userID    - the userID
     * @param principal - the logged in user
     */
    @CrossOrigin
    @RequestMapping(value = "/removeUser", method = RequestMethod.GET)
    public void removeUser(Long userID, Principal principal) {
        userService.removeUser(userID, principal.getName());
    }

    /**
     * Provides a rest endpoint for an admin to reject a tutors request
     *
     * @param userID    - the user whom is being reject
     * @param principal - the logged in user
     */
    @CrossOrigin
    @RequestMapping(value = "/rejectTutorRequest", method = RequestMethod.GET)
    public void rejectTutorRequest(Long userID, Principal principal) {
        userService.rejectTutorRequest(userID, principal.getName());
    }

    /**
     * @return the list of admins and contact info to be returned to the user
     */
    @CrossOrigin
    @RequestMapping(value = "/getAdmins", method = RequestMethod.GET)
    public List<User> getAdmins() {
        return userService.getAdmins();
    }

    /**
     * Only available to admins - adds users to system
     *
     * @param users     the users to add
     * @param principal the principal user
     */
    @CrossOrigin
    @RequestMapping(value = "/addUsers", method = RequestMethod.POST)
    public void addUsers(@RequestBody List<User> users, Principal principal) {
        userService.addUsers(users, principal.getName());
    }
}