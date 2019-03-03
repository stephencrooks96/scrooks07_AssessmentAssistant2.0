package com.pgault04.services;

import com.pgault04.entities.UserRole;
import com.pgault04.entities.UserSession;
import com.pgault04.pojos.TokenPojo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.repositories.UserSessionsRepo;
import com.pgault04.utilities.PasswordEncrypt;
import com.pgault04.utilities.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;

@Service
public class MainService {

    @Autowired
    UserSessionsRepo userSessionsRepo;
    @Autowired
    UserRepo userRepo;

    public TokenPojo generateToken(Principal user) {
        if (user != null) {
            TokenPojo tokenPojo = new TokenPojo();
            tokenPojo.setUser(userRepo.selectByUsername(user.getName()));
            tokenPojo.getUser().setPassword(null);
            UserSession userSession = userSessionsRepo.selectByUsername(user.getName());
            tokenPojo.setToken(userSession.getToken());
            return tokenPojo;
        }
        throw new IllegalArgumentException("No user found.");
    }

    public void destroyToken(Principal user) {
        if (user != null) {
            UserSession userSession = userSessionsRepo.selectByUsername(user.getName());
            userSession.setLastActive(new Timestamp(System.currentTimeMillis() - 1800000));
            userSessionsRepo.insert(userSession);
        } else {
            throw new IllegalArgumentException("No user found.");
        }
    }
}
