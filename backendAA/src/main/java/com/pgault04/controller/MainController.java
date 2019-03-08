package com.pgault04.controller;

import com.pgault04.entities.User;
import com.pgault04.pojos.TokenPojo;
import com.pgault04.services.MainService;
import com.pgault04.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Paul Gault - 40126005
 * @since November 2018
 *         Controller for main functions such as logging in and out
 */
@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    UserService userService;
    @Autowired
    MainService mainService;

    /**
     * Returns the logged in user to the frontend
     *
     * @param principal - the user
     * @return the users username
     */
    @CrossOrigin
    @RequestMapping("/getUser")
    public User username(Principal principal) throws IllegalArgumentException { return userService.getUser(principal.getName()); }

    /**
     * Provides rest endpoint for user to log in to the system
     *
     * @param principal - the user
     * @return the user
     */
    @CrossOrigin
    @RequestMapping("/login")
    public TokenPojo user(Principal principal) { return mainService.generateToken(principal != null ? principal.getName() : null); }

    /**
     * Provides the rest endpoint for the user to log out of the system
     * @param principal - the user
     */
    @CrossOrigin
    @RequestMapping("/logout")
    public void logout(Principal principal) { mainService.destroyToken(principal != null ? principal.getName() : null); }
}