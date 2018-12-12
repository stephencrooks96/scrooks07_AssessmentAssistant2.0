/**
 *
 */
package com.pgault04.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Paul Gault - 40126005
 *
 *         Controller for main functions such as logging in and out
 */
@RestController
@RequestMapping("/main")
public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    /**
     * Login method
     *
     * @param
     * @return
     */
    @CrossOrigin
    @RequestMapping("/getUsername")
    public String username(Principal principal) {
        StringBuilder sb = new StringBuilder();
        return sb.append("\"").append(principal.getName()).append("\"").toString();
    }

    @CrossOrigin
    @RequestMapping("/login")
    public Principal user(Principal principal) {
        return principal;
    }

    /**
     * Logout method
     *
     * @param
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public void logoutSuccessfulPage() {

    }


}
