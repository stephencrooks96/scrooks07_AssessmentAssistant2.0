/**
 * 
 */
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
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.User;
import com.pgault04.entities.UserRole;
import com.pgault04.repositories.UserRepo;
import com.pgault04.repositories.UserRoleRepo;
import com.pgault04.utilities.PasswordEncrypt;

/**
 * @author Paul Gault - 40126005
 *
 */
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

	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		tmpl.execute("DELETE FROM TestQuestion");
		tmpl.execute("DELETE FROM Answer");
		tmpl.execute("DELETE FROM Question");
		tmpl.execute("DELETE FROM QuestionType");
		tmpl.execute("DELETE FROM TestResult");
		tmpl.execute("DELETE FROM Tests");
		tmpl.execute("DELETE FROM ModuleAssociation");
		tmpl.execute("DELETE FROM Module");
		tmpl.execute("DELETE FROM AssociationType");
		tmpl.execute("DELETE FROM Message");
		tmpl.execute("DELETE FROM Password");
		tmpl.execute("DELETE FROM TimeModifier");
		tmpl.execute("DELETE FROM User");
		tmpl.execute("DELETE FROM UserRole");

		this.invalidUserName = "";
		this.userRole = new UserRole("ROLE_User");
		this.validUserName = "paul";
		this.password = "password";
		this.firstName = "firstName";
		this.lastName = "lastName";
		this.enabled = 1;

		userRole = userRoleRepo.insert(userRole);

		user = new User(validUserName, password, firstName, lastName, enabled, userRole.getUserRoleID());
		userRepo.insert(user);

	}

	/**
	 * Test method for
	 * {@link com.pgault04.services.UserDetailsServiceImplementation#loadUserByUsername(java.lang.String)}.
	 */
	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameInvalid() {

		userDetailServ.loadUserByUsername(invalidUserName);
	}

	/**
	 * Test method for
	 * {@link com.pgault04.services.UserDetailsServiceImplementation#loadUserByUsername(java.lang.String)}.
	 */
	@Test
	public void testLoadUserByUsernameValid() {

		UserDetails userDetails = userDetailServ.loadUserByUsername(validUserName);

		assertThat(userDetails.getPassword().matches(PasswordEncrypt.encrypt(password)));
		assertEquals(validUserName, userDetails.getUsername());
		assertEquals("[" + userRole.getRole() + "]", userDetails.getAuthorities().toString());
	}

}
