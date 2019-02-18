package com.pgault04.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.User;
import com.pgault04.entities.UserRole;
import com.pgault04.repositories.UserRepo;
import com.pgault04.repositories.UserRoleRepo;
import com.pgault04.utilities.PasswordEncrypt;

import javax.transaction.Transactional;

/**
 * @author Paul Gault - 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserDetailsSerivceImplementation {

	@Autowired
	JdbcTemplate tmpl;

	@Autowired
	UserDetailsServiceImplementation userDetailServ;

	@Autowired
	UserRepo userRepo;

	@Autowired
	UserRoleRepo userRoleRepo;

	private String invalidUserName, validUserName;

	private User user;
	private String password, firstName, lastName;
	private Integer enabled;

	private UserRole userRole;

	@Before
	public void setUp() throws Exception {
		this.invalidUserName = "";
		this.userRole = new UserRole("ROLE_User");
		this.validUserName = "paul";
		this.password = "password";
		this.firstName = "firstName";
		this.lastName = "lastName";
		this.enabled = 1;
		userRole = userRoleRepo.insert(userRole);
		user = new User(validUserName, password, firstName, lastName, enabled, userRole.getUserRoleID(), 0);
		userRepo.insert(user);
	}

	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameInvalid() {
		userDetailServ.loadUserByUsername(invalidUserName);
	}

	@Test
	public void testLoadUserByUsernameValid() {
		UserDetails userDetails = userDetailServ.loadUserByUsername(validUserName);
		assertThat(userDetails.getPassword().matches(PasswordEncrypt.encrypt(password)));
		assertEquals(validUserName, userDetails.getUsername());
		assertEquals("[" + userRole.getRole() + "]", userDetails.getAuthorities().toString());
	}
}