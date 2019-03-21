package com.pgault04.services;

import com.pgault04.entities.UserSession;
import com.pgault04.pojos.TokenPojo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.repositories.UserSessionsRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Performs logic for all main features
 * e.g. logging in and out
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Service
public class MainService {

    private static final Logger logger = LogManager.getLogger(MarkingService.class);

    @Autowired
    UserSessionsRepo userSessionsRepo;
    @Autowired
    UserRepo userRepo;

    /**
     * Generates a token to be returned to user when they log in
     * Used for communication back and forth to verify users authenticity
     * <p>
     * Throws an illegal argument exception if the user doesn't exist
     *
     * @param user the user's username
     * @return the token information
     */
    public TokenPojo generateToken(String user) {
        if (user != null) {
            logger.info("User logged in {}", user);
            TokenPojo tokenPojo = new TokenPojo();
            tokenPojo.setUser(userRepo.selectByUsername(user));
            tokenPojo.getUser().setPassword(null);
            UserSession userSession = userSessionsRepo.selectByUsername(user);
            tokenPojo.setToken(userSession.getToken());
            return tokenPojo;
        }
        throw new IllegalArgumentException("No user found.");
    }

    /**
     * Destroys the users token upon logging out by invalidating it
     * If the user doesnt exist then an error is thrown
     *
     * @param user the user's username
     */
    public void destroyToken(String user) {
        if (user != null) {
            logger.info("User logged out {}", user);
            UserSession userSession = userSessionsRepo.selectByUsername(user);
            userSession.setLastActive(new Timestamp(System.currentTimeMillis() - 1800000));
            userSessionsRepo.insert(userSession);
        } else {
            throw new IllegalArgumentException("No user found.");
        }
    }
}