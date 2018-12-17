/**
 *
 */
package com.pgault04.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    /**
     * Logs useful info for debugging and analysis needs
     */
    private static final Logger logger = LogManager.getLogger(MainController.class);

    /**
     * Get username method
     *
     * @param principal - the user
     * @return the users username
     */
    @CrossOrigin
    @RequestMapping("/getUsername")
    public String username(Principal principal) {
        logger.info("Returning username {}", principal.getName());
        return "\"" + principal.getName() + "\"";
    }

    /**
     * Login method
     * @param principal - the user
     * @return the user
     */
    @CrossOrigin
    @RequestMapping("/login")
    public Principal user(Principal principal) {
        logger.info("User logged in {}", principal.getName());
        return principal;
    }


}
