package com.pgault04.services;

import com.pgault04.controller.MainController;
import com.pgault04.controller.UserController;
import com.pgault04.entities.PasswordReset;
import com.pgault04.entities.TutorRequests;
import com.pgault04.entities.User;
import com.pgault04.entities.UserRole;
import com.pgault04.pojos.ChangePassword;
import com.pgault04.pojos.TutorRequestPojo;
import com.pgault04.repositories.PasswordResetRepo;
import com.pgault04.repositories.TutorRequestRepo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.repositories.UserSessionsRepo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserService {

    private static final String USERNAME_IN_DB = "pgault04@qub.ac.uk";
    private static final String OTHER_IN_DB = "richard.gault@qub.ac.uk";

    @Autowired
    MainController mainController;
    @Autowired
    UserController userController;
    @Autowired
    UserSessionsRepo userSessionsRepo;
    @Autowired
    PasswordResetRepo passwordResetRepo;
    @Autowired
    UserService userService;
    @Autowired
    UserRepo userRepo;
    @Autowired
    private TutorRequestRepo tutorRequestRepo;
    private TutorRequests tutorRequest;
    private User user, user2;

    @Before
    public void setUp() throws Exception {
        user = userRepo.selectByUsername(USERNAME_IN_DB);
        user2 = userRepo.selectByUsername(OTHER_IN_DB);
        tutorRequest = new TutorRequests(user.getUserID(), "reason", 0);
        tutorRequest = tutorRequestRepo.insert(tutorRequest);
    }

    @Transactional
    @Test
    public void testGetTutorRequests() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        // As admin
        List<TutorRequestPojo> tutorRequests = userController.getTutorRequests(principal);
        assertTrue(tutorRequests.toString().contains(tutorRequest.toString()));

        // Not admin
        tutorRequests = userService.getTutorRequests(OTHER_IN_DB);
        assertNull(tutorRequests);
    }

    @Transactional
    @Test
    public void testApproveTutorRequest() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principal2 = Mockito.mock(Principal.class);
        when(principal2.getName()).thenReturn(OTHER_IN_DB);
        // Not admin
        userController.approveTutorRequest(user.getUserID(), principal2);
        assertEquals(0, (int) tutorRequestRepo.selectByUserID(user.getUserID()).getApproved());
        // As admin
        userController.approveTutorRequest(user.getUserID(), principal);
        assertEquals(1, (int) tutorRequestRepo.selectByUserID(user.getUserID()).getApproved());
    }

    @Transactional
    @Test
    public void testMakeAdmin() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principal2 = Mockito.mock(Principal.class);
        when(principal2.getName()).thenReturn(OTHER_IN_DB);
        // Not admin
        userController.makeAdmin(user2.getUserID(), principal2);
        Assert.assertNotEquals(UserRole.ROLE_ADMIN, userRepo.selectByUserID(user2.getUserID()).getUserRoleID());
        // As admin
        userController.makeAdmin(user2.getUserID(), principal);
        assertEquals(UserRole.ROLE_ADMIN, userRepo.selectByUserID(user2.getUserID()).getUserRoleID());
    }

    @Transactional
    @Test
    public void testMakeTutor() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principal2 = Mockito.mock(Principal.class);
        when(principal2.getName()).thenReturn(OTHER_IN_DB);
        // Not admin
        userController.makeTutor(user.getUserID(), principal2);
        Assert.assertNotEquals(1, (int) userRepo.selectByUserID(user.getUserID()).getTutor());
        // As admin
        userController.makeTutor(user.getUserID(), principal);
        assertEquals(1, (int) userRepo.selectByUserID(user.getUserID()).getTutor());
    }

    @Transactional
    @Test
    public void testRemoveUser() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principal2 = Mockito.mock(Principal.class);
        when(principal2.getName()).thenReturn(OTHER_IN_DB);
        // Not admin
        userController.removeUser(user.getUserID(), principal2);
        assertNotNull(userRepo.selectByUserID(user.getUserID()));
        // As admin
        userController.removeUser(user.getUserID(), principal);
        assertNull(userRepo.selectByUserID(user.getUserID()));
    }

    @Transactional
    @Test
    public void testAddUsers() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principal2 = Mockito.mock(Principal.class);
        when(principal2.getName()).thenReturn(OTHER_IN_DB);
        User userNew = new User("newUser", "password", "firstName", "lastName", 0, 2L, 0);
        List<User> users = new ArrayList<>();
        users.add(userNew);

        // Not admin
        userController.addUsers(users, principal2);
        assertNull(userRepo.selectByUsername(userNew.getUsername()));
        // As admin
        userController.addUsers(users, principal);
        assertNotNull(userRepo.selectByUsername(userNew.getUsername()));
    }

    @Transactional
    @Test
    public void testGetAdmins() {
        List<User> users = userController.getAdmins();
        assertTrue(users.toString().contains(user.getUsername()));
    }

    @Transactional
    @Test
    public void testRejectTutorRequest() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);
        Principal principal2 = Mockito.mock(Principal.class);
        when(principal2.getName()).thenReturn(OTHER_IN_DB);

        tutorRequest.setApproved(1);
        tutorRequestRepo.insert(tutorRequest);
        // Not admin
        userController.rejectTutorRequest(user.getUserID(), principal2);
        assertEquals(1, (int) tutorRequestRepo.selectByUserID(user.getUserID()).getApproved());

        // As admin
        userController.rejectTutorRequest(user.getUserID(), principal);
        assertEquals(0, (int) userRepo.selectByUserID(user.getUserID()).getTutor());
        assertNull(tutorRequestRepo.selectByUserID(user.getUserID()));
    }

    @Transactional
    @Test
    public void testGetUser() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        User returnedUser = mainController.username(principal);
        assertEquals(user.getUserID(), returnedUser.getUserID());
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testGetUserInvalid() {
        userService.getUser("randomUsername");
    }

    @Transactional
    @Test
    public void testChangePassword() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        ChangePassword changePassword = new ChangePassword("123", "newPassword", "newPassword");
        userController.changePassword(changePassword, principal);
        user = userRepo.selectByUsername(USERNAME_IN_DB);
        assertTrue(bcrypt.matches("newPassword", user.getPassword()));
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testChangePasswordInvalid() {
        ChangePassword changePassword = new ChangePassword("WRONG_PASSWORD", "newPassword", "newPassword");
        userService.changePassword(changePassword, USERNAME_IN_DB);
    }

    @Transactional
    @Test
    public void testResetPassword() {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        PasswordReset passwordReset = passwordResetRepo.selectByID(user.getUserID());
        userController.resetPassword(USERNAME_IN_DB, "newPassword1", passwordReset.getResetString());
        user = userRepo.selectByUsername(USERNAME_IN_DB);
        assertTrue(bcrypt.matches("newPassword1", user.getPassword()));
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testResetPasswordInvalid() {
        userService.resetPassword(USERNAME_IN_DB, "newPassword1", "NOT_A_VALID_RESET_STRING");
    }

    @Transactional
    @Test
    public void testRequestResetPassword() {
        assertTrue(userController.requestResetPassword(USERNAME_IN_DB));
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testRequestResetPasswordInvalid() {
        userService.requestResetPassword(null);
    }

    @Transactional
    @Test
    public void testSubmitTutorRequest() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME_IN_DB);

        TutorRequests tutorRequest1 = userController.submitTutorRequest(tutorRequest, principal);
        // New
        assertNotEquals(-1L, (long) tutorRequest1.getTutorRequestID());
        // Update
        tutorRequest1.setReason("newReason");
        tutorRequest1 = userService.submitTutorRequest(tutorRequest, USERNAME_IN_DB);
        assertEquals("newReason", tutorRequest1.getReason());
    }

    @Transactional
    @Test
    public void testGetTutorRequest() {
        Principal principal2 = Mockito.mock(Principal.class);
        when(principal2.getName()).thenReturn(OTHER_IN_DB);

        assertNull(userController.getTutorRequest(principal2));
        userService.submitTutorRequest(tutorRequest, OTHER_IN_DB);
        // New
        assertNotNull(userService.getTutorRequest(OTHER_IN_DB));
    }

    @Transactional
    @Test
    public void testUsernameCheck() {
        Principal principal2 = Mockito.mock(Principal.class);
        when(principal2.getName()).thenReturn(OTHER_IN_DB);

        assertFalse(userService.usernameCheck("randomUsername", null));
        assertTrue(userController.getUsernames(principal2, USERNAME_IN_DB));
    }

    @Transactional
    @Test
    public void testCreateProfile() {
        User user = new User("username", "password", "firstName", "lastName", 0, UserRole.ROLE_USER, 0);
        user = userController.createProfile(user);
        assertTrue(user.getUserID() > -1L);
        assertNotNull(userRepo.selectByUsername("username"));
        assertNotNull(userSessionsRepo.selectByUsername("username"));
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testCreateProfileInvalid() {
        User user = new User("username", "password", "firstName", "lastName", 0, UserRole.ROLE_USER, 0);
        user = userService.createProfile(user);
        user.setUserID(-1L);
        userService.createProfile(user);
    }

    @Transactional
    @Test
    public void testEditProfile() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("username");

        User user = new User("username", "password", "firstName", "lastName", 0, UserRole.ROLE_USER, 0);
        user = userController.createProfile(user);
        user.setUsername("username2");
        user = userController.editProfile(user, principal);
        assertEquals("username2", user.getUsername());
    }

    @Transactional
    @Test(expected = IllegalArgumentException.class)
    public void testEditProfileInvalid() {
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("username");
        User user = new User("username", "password", "firstName", "lastName", 0, UserRole.ROLE_USER, 0);
        user = userService.createProfile(user);
        user.setUsername((USERNAME_IN_DB));
        userService.editProfile(user, principal);
    }
}