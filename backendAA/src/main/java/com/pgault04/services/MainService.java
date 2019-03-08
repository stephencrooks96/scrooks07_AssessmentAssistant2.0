package com.pgault04.services;

import com.pgault04.controller.MainController;
import com.pgault04.entities.UserSession;
import com.pgault04.pojos.TokenPojo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.repositories.UserSessionsRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;

@Service
public class MainService {

    /**
     * Logs useful info for debugging and analysis needs
     */
    private static final Logger logger = LogManager.getLogger(MarkingService.class);

    @Autowired
    UserSessionsRepo userSessionsRepo;
    @Autowired
    UserRepo userRepo;

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
