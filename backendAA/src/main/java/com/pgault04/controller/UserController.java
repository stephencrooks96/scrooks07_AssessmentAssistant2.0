package com.pgault04.controller;

import com.pgault04.entities.User;
import com.pgault04.repositories.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * Finds all users in the system
     *
     * @return the list of all users
     */
    @CrossOrigin
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<User> findAll() {
        logger.info("Request made for all users");
        return userRepo.selectAll();
    }
}
