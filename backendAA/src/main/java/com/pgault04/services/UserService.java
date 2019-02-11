package com.pgault04.services;

import com.pgault04.entities.User;
import com.pgault04.pojos.ChangePassword;
import com.pgault04.repositories.UserRepo;
import com.pgault04.utilities.PasswordEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

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
}
