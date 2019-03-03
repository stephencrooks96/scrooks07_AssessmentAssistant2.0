package com.pgault04.utilities;

import com.pgault04.entities.*;
import com.pgault04.repositories.ModuleAssociationRepo;
import com.pgault04.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.List;

@EnableAsync
@Component
public class EmailUtil {

    @Value("${app.email}")
    String SYSTEM_EMAIL_ADDRESS;
    @Value("${app.url}")
    String appUrl;
    @Value("${app.url.reset}")
    String resetUrl;

    @Autowired
    JavaMailSender emailSender;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ModuleAssociationRepo moduleAssociationRepo;

    @Async
    public void sendNewModuleMessageFromSystemToTutor(String to, Module module) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("New module added: " + module.getModuleName() + ", with ID: " + module.getModuleID());
        message.setText("This is an autogenerated email from Assessment Assistant:\nYour new module has been added and is pending approval " +
                "from administrators, we will inform you when a change has been made.");
        message.setFrom(SYSTEM_EMAIL_ADDRESS);
        emailSender.send(message);
    }

    @Async
    public void sendRemovedFromModule(String to, Long moduleID) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Module Removal:");
        message.setText("This is an autogenerated email from Assessment Assistant:\nYour tutor has removed you from module: " + moduleID);
        message.setFrom(SYSTEM_EMAIL_ADDRESS);
        emailSender.send(message);
    }

    @Async
    public void sendEnrollmentMessageFromSystemToAssociate(Module module, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        User tutor = userRepo.selectByUserID(module.getTutorUserID());
        message.setTo(user.getUsername());
        message.setSubject("New module added: " + module.getModuleName());
        message.setText("This is an autogenerated email from Assessment Assistant:\nYou have been added to a new module by " + tutor.getUsername() +
                ".\nThe module will run from: " + module.getCommencementDate() + " - " + module.getEndDate() + "\nPlease log on to review your options for this module.");
        message.setFrom(SYSTEM_EMAIL_ADDRESS);
        emailSender.send(message);

    }

    @Async
    public void emailDeleteUser(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setSubject("User removed");
        message.setText("This is an autogenerated email from Assessment Assistant:\nSorry, the admins have decided to remove your Assessment Assistant account.");
        message.setFrom(SYSTEM_EMAIL_ADDRESS);
        emailSender.send(message);

    }

    @Async
    public void sendNewAccountMessageFromSystemToUser(User user, String password, String creator) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setSubject("Account Creation: " + user.getUsername());
        message.setText("This is an autogenerated email from Assessment Assistant:\nAn account has been created for you on assessment assistant by: " + creator + ".\nPlease log on using this temporary password: " + password);
        message.setFrom(SYSTEM_EMAIL_ADDRESS);
        emailSender.send(message);
    }

    @Async
    public void sendPasswordResetMessageFromSystemToUser(User user, PasswordReset passwordReset) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setSubject("Password Reset for: " + user.getUsername());
        message.setText("This is an autogenerated email from Assessment Assistant:\nYou have requested to reset your password, to do so please navigate to the following link: " + appUrl + resetUrl + passwordReset.getResetString());
        message.setFrom(SYSTEM_EMAIL_ADDRESS);
        emailSender.send(message);
    }

    @Async
    public void sendModuleRequestApproved(Module module) {
        SimpleMailMessage message = new SimpleMailMessage();
        User user = userRepo.selectByUserID(module.getTutorUserID());
        message.setTo(user.getUsername());
        message.setSubject("Module request approved.");
        message.setText("This is an autogenerated email from Assessment Assistant:\nYour module request has been approved.\nFor: " + module.getModuleName() + "\nYou may now log on and use this module.");
        message.setFrom(SYSTEM_EMAIL_ADDRESS);
        emailSender.send(message);
    }

    @Async
    public void sendModuleRequestRejected(Module module) {
        SimpleMailMessage message = new SimpleMailMessage();
        User user = userRepo.selectByUserID(module.getTutorUserID());
        message.setTo(user.getUsername());
        message.setSubject("Module request rejected.");
        message.setText("This is an autogenerated email from Assessment Assistant:\nYour module request has been rejected.\nFor: " + module.getModuleName() + "\nPlease pick a more suitable module and try again.");
        message.setFrom(SYSTEM_EMAIL_ADDRESS);
        emailSender.send(message);
    }

    @Async
    public void sendTutorRequestApproved(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setSubject("Tutor request approved.");
        message.setText("This is an autogenerated email from Assessment Assistant:\nYour tutor request has been approved.\nYou have permission to create modules.");
        message.setFrom(SYSTEM_EMAIL_ADDRESS);
        emailSender.send(message);
    }

    @Async
    public void sendAdminApproved(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setSubject("You are now an admin.");
        message.setText("This is an autogenerated email from Assessment Assistant:\nYou have been granted admin status.");
        message.setFrom(SYSTEM_EMAIL_ADDRESS);
        emailSender.send(message);
    }

    @Async
    public void sendTutorRequestRejected(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setSubject("Tutor request rejected.");
        message.setText("This is an autogenerated email from Assessment Assistant:\nYour tutor request has been rejected.\nPlease update your reason and try again at a later date.");
        message.setFrom(SYSTEM_EMAIL_ADDRESS);
        emailSender.send(message);
    }

    @Async
    public void sendTutorRequestMessageFromSystemToAdmin(TutorRequests tutorRequests, String addedBy, String to) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("New tutor request added: " + addedBy);
        message.setText("This is an autogenerated email from Assessment Assistant:\nA new tutor request has been added by " + addedBy + ".\nWith reason:\n" + tutorRequests.getReason() + "\nPlease log on to review this.");
        message.setFrom(SYSTEM_EMAIL_ADDRESS);
        emailSender.send(message);

    }

    @Async
    public void sendNewModuleMessageFromSystemToAdmin(Module module, String addedBy, String to) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("New module added: " + module.getModuleName() + ", with ID: " + module.getModuleID());
            message.setText("This is an autogenerated email from Assessment Assistant:\nA new module has been added by " + addedBy + " please log on to review this.");
            message.setFrom(SYSTEM_EMAIL_ADDRESS);
            emailSender.send(message);

    }
}
