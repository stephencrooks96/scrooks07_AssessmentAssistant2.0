/**
 *
 */
package com.pgault04.services;

import com.pgault04.repositories.UserRepo;
import com.pgault04.repositories.UserRoleRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Gault - 40126005
 * @since November 2018
 *         Class to facilitate allowing users to log in to the system
 */
@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    /**
     * Logs useful information for debugging and problem resolution
     */
    private static final Logger logger = LogManager.getLogger(UserDetailsServiceImplementation.class);

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserRoleRepo userRoleRepo;

    /**
     * Checks Username entered by user exists in database and provides password
     * information for verification
     *
     * @return the users details
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Necessary info gathered from database
        com.pgault04.entities.User user = this.userRepo.selectByUsername(username);
        // List initialised for authorities
        List<GrantedAuthority> grantList = new ArrayList<>();

        if (user != null) {
            // Gets the users role and adds it to a list if the user exists
            grantList.add(new SimpleGrantedAuthority(this.userRoleRepo.selectByUserRoleID(user.getUserRoleID()).getRole()));
            return new User(user.getUsername(), user.getPassword(), grantList);
        } else {
            // If user is not found an exception is thrown
            logger.debug("{}: not found.", username);
            throw new UsernameNotFoundException(username);
        }
    }
}

