/**
 * 
 */
package com.pgault04.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pgault04.repositories.UserRepo;
import com.pgault04.repositories.UserRoleRepo;

/**
 * @author Paul Gault - 40126005
 * 
 *         Class to facilitate allowing users to log in to the system
 *
 */
@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

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

		com.pgault04.entities.User user = this.userRepo.selectByUsername(username);
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		
		if (user != null) {
			
			String roleName = this.userRoleRepo.selectByUserRoleID(user.getUserRoleID()).getRole();
			GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
			grantList.add(authority);
		} else {
			
			logger.debug(user + ": not found.");
			throw new UsernameNotFoundException(username);
		}

		return new User(user.getUsername(), user.getPassword(), grantList);


	}
}

