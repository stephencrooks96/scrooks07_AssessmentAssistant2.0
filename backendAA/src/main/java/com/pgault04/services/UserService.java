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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Performs logic for all user features
 * e.g. changing/resetting password, creating/editing profile, changing access levels, removing users
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Service
public class UserService {

    public static final int APPROVED = 1;
    public static final int UNAPPROVED = 0;
    private static final Logger logger = LogManager.getLogger(MainController.class);
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
     * Gets tutor requests for admins to view
     *
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
     * Approves tutor request and notifies the user
     * Email call is asynchronous
     *
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

    /**
     * Logic performed to promote a user to admin
     * Asynchronously notifies the user that they are now an admin
     *
     * @param userID   the user to become an admin
     * @param username the admin promoting the user
     */
    public void makeAdmin(Long userID, String username) {
        User admin = userRepo.selectByUsername(username);
        if (admin.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            User user = userRepo.selectByUserID(userID);
            user.setUserRoleID(user.getUserRoleID().equals(UserRole.ROLE_USER) ? UserRole.ROLE_ADMIN : UserRole.ROLE_USER);
            userRepo.insert(user);
            emailSender.sendAdminApproved(user);
        }
    }

    /**
     * Logic performed to promote a user to a tutor
     * Asynchronously notifies the user that they are now an tutor
     *
     * @param userID   the user to become a tutor
     * @param username the admin promoting the user
     */
    public void makeTutor(Long userID, String username) {
        User admin = userRepo.selectByUsername(username);
        if (admin.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            User user = userRepo.selectByUserID(userID);
            user.setTutor(user.getTutor() == 0 ? 1 : 0);
            userRepo.insert(user);
            emailSender.sendTutorRequestApproved(user);
        }
    }

    /**
     * Logic performed to remove a user from the system
     * Asynchronously notifies the user that they have been removed
     *
     * @param userID   the user to be removed
     * @param username the admin performing the removal
     */
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
     * Added on the front end via CSV this is converted to a list of users to be inserted in to the database
     * Asynchronously notifies each user that they have been added
     *
     * @param users    the users to add
     * @param username the user name who is adding them
     */
    public void addUsers(List<User> users, String username) {
        User admin = userRepo.selectByUsername(username);
        if (admin.getUserRoleID().equals(UserRole.ROLE_ADMIN)) {
            for (User u : users) {
                User user = userRepo.selectByUsername(u.getUsername());
                if (user == null) {
                    String password = PasswordUtil.generateRandomString();
                    user = userRepo.insert(new User(u.getUsername(),
                                                    PasswordUtil.encrypt(password),
                                                    u.getFirstName(), u.getLastName(), 0, UserRole.ROLE_USER, 0));

                    passwordResetRepo.insert(new PasswordReset(user.getUserID(), PasswordUtil.generateRandomString()));
                    userSessionRepo.insert(new UserSession(user.getUsername(),
                                                            new String(Base64.getEncoder().encode((user.getUsername() + ":" + password).getBytes())),
                                                            new Timestamp(System.currentTimeMillis())));

                    emailSender.sendNewAccountMessageFromSystemToUser(user, password, username);
                }
            }
        }
    }

    /**
     * Gets all the admins to be displayed to a user on the front end
     * So that the user may get in contact with an admin
     *
     * @return the list of admin users
     */
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
     * Rejects tutor request and notifies user asynchronously
     *
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

    /**
     * Gets the currently logged in user's info from database
     *
     * @param username the username of the currently logged in user
     * @return the user or throw and exception if no user logged in
     */
    public User getUser(String username) {
        User user = userRepo.selectByUsername(username);
        if (user != null) {
            logger.info("Returning user {}", username);
            user.setPassword(null);
            return user;
        }
        throw new IllegalArgumentException("No principal user.");
    }

    /**
     * Changes the users password while logged in
     * Checks old password matches before allowing new one
     *
     * @param changePassword object consisting of all needed info to change password e.g. old password and new password
     * @param username       the user who is logged in and changing their password
     * @return new token info to the front end user for authentication throughout rest of session
     */
    public UserSession changePassword(ChangePassword changePassword, String username) {
        User user = userRepo.selectByUsername(username);
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        if (bcrypt.matches(changePassword.getPassword(), user.getPassword())) {
            user.setPassword(PasswordUtil.encrypt(changePassword.getNewPassword()));
            UserSession userSession = userSessionRepo.insert(new UserSession(user.getUsername(), new String(Base64.getEncoder().encode((user.getUsername() + ":" + changePassword.getNewPassword()).getBytes())), new Timestamp(System.currentTimeMillis())));
            userRepo.insert(user);
            return userSession;
        } else {
            throw new IllegalArgumentException("Password does not match what is stored in database.");
        }
    }

    /**
     * Logic performed to allow user to create a new password while not logged in
     * Each user has a reset string stored in database, this is emailed out to them and
     * must be included in the link for the password reset to work
     *
     * @param email       the user's email / username
     * @param newPassword the new password they have chosen
     * @param resetString the reset string held in database
     * @return success / failure
     */
    public Boolean resetPassword(String email, String newPassword, String resetString) {
        User user = userRepo.selectByUsername(email);
        PasswordReset passwordReset = passwordResetRepo.selectByID(user.getUserID());
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
     * Email will include the user's reset string
     *
     * @param email the email
     * @return boolean marker
     */
    public Boolean requestResetPassword(String email) {
        User user = userRepo.selectByUsername(email);
        if (user != null) {
            PasswordReset passwordReset = passwordResetRepo.selectByID(user.getUserID());
            emailSender.sendPasswordResetMessageFromSystemToUser(user, passwordReset);
            return true;
        } else {
            throw new IllegalArgumentException("No user associated with email: " + email);
        }
    }

    /**
     * Submits a user's request to become a tutor
     * If no request submitted before then a fresh one is entered in to the database
     * If not the current one is simply updated
     *
     * @param tutorRequest the tutor request object includes reason for request
     * @param username     the logged in user i.e. the one making the request
     * @return the tutor request
     */
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

    /**
     * Gets a user's tutor request providing they have already made one
     *
     * @param username the logged in user whose request must be retrieved
     * @return the tutor request
     */
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
     * Can be for user creating an account or editing theirs
     *
     * @param username the username to check
     * @return returns true when the username is taken
     */
    public boolean usernameCheck(String username, Principal principal) {
        List<User> users = userRepo.selectAll();
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                if (principal == null || !principal.getName().equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a new user in the system sets all necessary data based on what user has entered
     * Must pass username check to be allowed
     *
     * @param user the user to create
     * @return the created user
     * @throws IllegalArgumentException thrown if username is already in use
     */
    public User createProfile(User user) throws IllegalArgumentException {
        if (!usernameCheck(user.getUsername(), null)) {
            user.setUserID(-1L);
            user.setEnabled(0);
            user.setTutor(0);
            String passwordUnencrypted = user.getPassword();
            user.setPassword(PasswordUtil.encrypt(passwordUnencrypted));
            user.setUserRoleID(UserRole.ROLE_USER);
            user = userRepo.insert(user);
            userSessionRepo.insert(new UserSession(user.getUsername(), new String(Base64.getEncoder().encode((user.getUsername() + ":" + passwordUnencrypted).getBytes())), new Timestamp(System.currentTimeMillis())));
            PasswordReset passwordReset = new PasswordReset(user.getUserID(), PasswordUtil.generateRandomString());
            passwordResetRepo.insert(passwordReset);
            return user;
        }
        throw new IllegalArgumentException("Username already in use.");
    }

    /**
     * Edits the users profile
     * Must pass username check
     *
     * @param user      the new user data for the principal user
     * @param principal the principal user
     * @return the user
     * @throws IllegalArgumentException if the user name is in use
     */
    public UserSession editProfile(User user, Principal principal) throws IllegalArgumentException {
        User principalUser = userRepo.selectByUsername(principal.getName());
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
            return userSession;
        }
        throw new IllegalArgumentException("Username already in use.");
    }
}