package com.pgault04.services;

import com.pgault04.controller.MainController;
import com.pgault04.entities.TutorRequests;
import com.pgault04.entities.User;
import com.pgault04.pojos.ChangePassword;
import com.pgault04.repositories.TutorRequestRepo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.utilities.PasswordEncrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    /**
     * Logs useful info for debugging and analysis needs
     */
    private static final Logger logger = LogManager.getLogger(MainController.class);

    @Autowired
    UserRepo userRepo;
    @Autowired
    TutorRequestRepo tutorRequestRepo;

    public User getUser(String username) {
        User user = userRepo.selectByUsername(username);

        if (user != null) {
            logger.info("Returning user {}", username);
            user.setPassword(null);
            return user;
        }
        throw new IllegalArgumentException("No principal user.");

    }
    public Boolean changePassword(ChangePassword changePassword, String username) {
        User user = userRepo.selectByUsername(username);
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        if (bcrypt.matches(changePassword.getPassword(), user.getPassword())) {
            user.setPassword(changePassword.getNewPassword());
            userRepo.insert(user);
            return true;
        } else {
            throw new IllegalArgumentException("Password does not match what is stored in database.");
        }
    }

    public TutorRequests submitTutorRequest(TutorRequests tutorRequest, String username) {
        User user = userRepo.selectByUsername(username);
        TutorRequests tutorRequestCheck = tutorRequestRepo.selectByUserID(user.getUserID());
        if (tutorRequestCheck == null) {
            tutorRequest.setApproved(0);
            tutorRequest.setTutorRequestID(-1L);
            tutorRequest.setUserID(user.getUserID());
            return tutorRequestRepo.insert(tutorRequest);
        } else {
            tutorRequest.setApproved(0);
            tutorRequest.setTutorRequestID(tutorRequestCheck.getTutorRequestID());
            tutorRequest.setUserID(user.getUserID());
            return tutorRequestRepo.insert(tutorRequest);
        }
    }

    public TutorRequests getTutorRequest(String username) {
        User user = userRepo.selectByUsername(username);
        TutorRequests tutorRequestCheck = tutorRequestRepo.selectByUserID(user.getUserID());
        if (tutorRequestCheck != null) {
            return tutorRequestCheck;
        } else {
            throw new IllegalArgumentException("No tutor request exists.");
        }
    }
}
