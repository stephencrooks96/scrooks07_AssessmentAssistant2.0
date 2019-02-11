package com.pgault04.controller;

import com.pgault04.entities.User;
import com.pgault04.pojos.ChangePassword;
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
    public Boolean changePassword(@RequestBody ChangePassword changePassword, Principal user) {
        return userService.changePassword(changePassword, user.getName());
    }

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
}
