package com.pgault04.services;

import com.pgault04.controller.MainController;
import com.pgault04.entities.*;
import com.pgault04.pojos.ChangePassword;
import com.pgault04.pojos.TutorRequestPojo;
import com.pgault04.repositories.PasswordResetRepo;
import com.pgault04.repositories.TutorRequestRepo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.repositories.UserSessionsRepo;
import com.pgault04.utilities.EmailUtil;
import com.pgault04.utilities.PasswordUtil;
import com.pgault04.utilities.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class UserService {

    /**
     * Logs useful info for debugging and analysis needs
     */
    private static final Logger logger = LogManager.getLogger(MainController.class);
    public static final int APPROVED = 1;
    public static final int UNAPPROVED = 0;

    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordResetRepo passwordResetRepo;
    @Autowired
    TutorRequestRepo tutorRequestRepo;
    @Autowired
    EmailUtil emailSender;
    @Autowired
    UserSessionsRepo userSessionRepo;


    /**
     * @return all tutor requests along with the tutor's information
     */
    public List<TutorRequestPojo> getTutorRequests(String username) {
        User user = userRepo.selectByUsername(username);
        if (user.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            List<TutorRequests> requests = tutorRequestRepo.selectByApproved(UNAPPROVED);
            List<TutorRequestPojo> tutorRequests = new ArrayList<>();
            for (TutorRequests request : requests) {
                User tutor = userRepo.selectByUserID(request.getUserID());
                tutor.setPassword(null);
                tutorRequests.add(new TutorRequestPojo(tutor, request));
            }
            return tutorRequests;
        }
        return null;
    }

    /**
     * approves tutor request and notifies the user
     * @param userID the user
     */
    public void approveTutorRequest(Long userID, String username) {
        User admin = userRepo.selectByUsername(username);
        if (admin.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            TutorRequests requests = tutorRequestRepo.selectByUserID(userID);
            requests.setApproved(APPROVED);
            tutorRequestRepo.insert(requests);
            User user = userRepo.selectByUserID(userID);
            user.setTutor(APPROVED);
            userRepo.insert(user);
            emailSender.sendTutorRequestApproved(user);
        }
    }

    public void makeAdmin(Long userID, String username) {
        User admin = userRepo.selectByUsername(username);
        if (admin.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            User user = userRepo.selectByUserID(userID);
            user.setUserRoleID(user.getUserRoleID().equals(UserRole.ROLE_USER) ? UserRole.ROLE_ADMIN : UserRole.ROLE_USER);
            userRepo.insert(user);
            emailSender.sendAdminApproved(user);
        }
    }

    public void makeTutor(Long userID, String username) {
        User admin = userRepo.selectByUsername(username);
        if (admin.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            User user = userRepo.selectByUserID(userID);
            user.setTutor(user.getTutor() == 0 ? 1 : 0);
            userRepo.insert(user);
            emailSender.sendTutorRequestApproved(user);
        }
    }

    public void removeUser(Long userID, String username) {
        User admin = userRepo.selectByUsername(username);
        if (admin.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            User user = userRepo.selectByUserID(userID);
            userRepo.delete(userID);
            emailSender.emailDeleteUser(user);
        }
    }

    /**
     * Allows admins to add users to the system
     *
     * @param users the users to add
     * @param username the user name who is adding them
     */
    public void addUsers(List<User> users, String username) {
        User admin = userRepo.selectByUsername(username);
        if (admin.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            for (User u : users) {
                User user = userRepo.selectByUsername(u.getUsername());
                if (user == null) {
                    String password = PasswordUtil.generateRandomString();
                    user = userRepo.insert(new User(u.getUsername(), PasswordUtil.encrypt(password), u.getFirstName(), u.getLastName(), 0, UserRole.ROLE_USER, u.getTutor()));
                    passwordResetRepo.insert(new PasswordReset(user.getUserID(), PasswordUtil.generateRandomString()));
                    userSessionRepo.insert(new UserSession(user.getUsername(), new String(Base64.getEncoder().encode((user.getUsername() + ":" + password).getBytes())), new Timestamp(System.currentTimeMillis())));
                    emailSender.sendNewAccountMessageFromSystemToUser(user, password, username);
                }
            }
        }
    }

    public List<User> getAdmins() {
        List<User> users = userRepo.selectAll();
        List<User> admins = new ArrayList<>();

        for (User u : users) {
            u.setPassword(null);
            if (u.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
                admins.add(u);
            }
        }
        return admins;
    }

    /**
     * Rejects tutor request and notifies user
     * @param userID the user
     */
    public void rejectTutorRequest(Long userID, String username) {
        User admin = userRepo.selectByUsername(username);
        if (admin.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            tutorRequestRepo.delete(userID);
            User user = userRepo.selectByUserID(userID);
            user.setTutor(UNAPPROVED);
            userRepo.insert(user);
            emailSender.sendTutorRequestRejected(user);
        }
    }

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
            user.setPassword(PasswordUtil.encrypt(changePassword.getNewPassword()));
            userSessionRepo.insert(new UserSession(user.getUsername(), new String(Base64.getEncoder().encode((user.getUsername() + ":" + changePassword.getNewPassword()).getBytes())), new Timestamp(System.currentTimeMillis())));
            userRepo.insert(user);
            return true;
        } else {
            throw new IllegalArgumentException("Password does not match what is stored in database.");
        }
    }

    public Boolean resetPassword(String email, String newPassword, String resetString) {
        User user = userRepo.selectByUsername(email);
        PasswordReset passwordReset = passwordResetRepo.selectByUserID(user.getUserID());

        if (resetString.equals(passwordReset.getResetString())) {
            user.setPassword(PasswordUtil.encrypt(newPassword));
            userRepo.insert(user);
            userSessionRepo.insert(new UserSession(user.getUsername(), new String(Base64.getEncoder().encode((user.getUsername() + ":" + newPassword).getBytes())), new Timestamp(System.currentTimeMillis())));
            passwordReset.setResetString(PasswordUtil.generateRandomString());
            passwordResetRepo.insert(passwordReset);
            return true;
        } else {
            throw new IllegalArgumentException("Reset String does not match the one stored in the database.");
        }
    }

    /**
     * Requests a reset email to be sent out to email associated with users account
     * @param email the email
     * @return boolean marker
     */
    public Boolean requestResetPassword(String email) {
        User user = userRepo.selectByUsername(email);
        if (user != null) {
            PasswordReset passwordReset = passwordResetRepo.selectByUserID(user.getUserID());
            emailSender.sendPasswordResetMessageFromSystemToUser(user, passwordReset);
            return true;
        } else {
            throw new IllegalArgumentException("No user associated with email: " + email);
        }
    }

    public TutorRequests submitTutorRequest(TutorRequests tutorRequest, String username) {
        User user = userRepo.selectByUsername(username);
        TutorRequests tutorRequestCheck = tutorRequestRepo.selectByUserID(user.getUserID());
        if (tutorRequestCheck == null) {
            tutorRequest.setApproved(0);
            tutorRequest.setTutorRequestID(-1L);
            tutorRequest.setUserID(user.getUserID());
            List<User> admins = userRepo.selectAll();
            for (User u : admins) {
                if (u.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
                    emailSender.sendTutorRequestMessageFromSystemToAdmin(tutorRequest, username, u.getUsername());
                }
            }
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
            return null;
        }
    }

    /**
     * Checks if the username is already used
     *
     * @param username the username to check
     * @return whether the username is used or not
     */
    public boolean usernameCheck(String username, String principal) {
        List<User> users = userRepo.selectAll();
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                if (principal == null || !principal.equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a new user in the system
     * @param user the user to create
     * @return the created user
     * @throws IllegalArgumentException thrown if username is already in use
     */
    public User createProfile(User user) throws IllegalArgumentException {
        if (!usernameCheck(user.getUsername(), null)) {
            user.setUserID(-1L);
            user.setEnabled(0);
            String passwordUnencrypted = user.getPassword();
            user.setPassword(PasswordUtil.encrypt(passwordUnencrypted));
            user.setUserRoleID(UserRole.ROLE_USER);
            user = userRepo.insert(user);
            userSessionRepo.insert(new UserSession(user.getUsername(), new String(Base64.getEncoder().encode((user.getUsername() + ":" + passwordUnencrypted).getBytes())), new Timestamp(System.currentTimeMillis())));
            return user;
        }
        throw new IllegalArgumentException("Username already in use.");
    }

    /**
     * Edits the users profile
     * @param user the new user data for the principal user
     * @param principal the principal user
     * @return the user
     * @throws IllegalArgumentException if the user name is in use
     */
    public User editProfile(User user, String principal) throws IllegalArgumentException {
        User principalUser = userRepo.selectByUsername(principal);
        if (!usernameCheck(user.getUsername(), principal)) {
            UserSession userSession = userSessionRepo.selectByUsername(principalUser.getUsername());
            String userSessionBefore = new String(Base64.getDecoder().decode((userSession.getToken())));
            String decodedPassword = userSessionBefore.substring(principalUser.getUsername().length() + 1);
            principalUser.setUsername(user.getUsername());
            principalUser.setFirstName(user.getFirstName());
            principalUser.setLastName(user.getLastName());
            principalUser = userRepo.insert(principalUser);
            userSession = new UserSession(principalUser.getUsername(), new String(Base64.getEncoder().encode((principalUser.getUsername() + ":" + decodedPassword).getBytes())), new Timestamp(System.currentTimeMillis()));
            userSessionRepo.insert(userSession);
            return user;
        }
        throw new IllegalArgumentException("Username already in use.");
    }
}
