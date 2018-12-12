/**
 * 
 */
package com.pgault04.repositories;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pgault04.entities.User;
import com.pgault04.utilities.PasswordEncrypt;

@Repository
@Transactional
public class UserRepo {

	private static final long INSERT_CHECKER_CONSTANT = 0L;

	private static final Logger log = LoggerFactory.getLogger(UserRepo.class);

	private String tableName = "User";

	private final String insertSQL = "INSERT INTO " + tableName
			+ " (username, password, firstName, lastName, enabled, userRoleID) values (:username, :password, :firstName, :lastName, :enabled, :userRoleID)";

	private final String updateSQL = "UPDATE " + tableName + " SET username=:username "
			+ ", password=:password, firstName=:firstName, lastName=:lastName, enabled=:enabled, userRoleID=:userRoleID " + "WHERE userID=:userID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE userID=?";

	@Autowired
	JdbcTemplate tmpl;

	@Autowired
	NamedParameterJdbcTemplate namedparamTmpl;

	/**
	 * Finds the amount of records in table
	 * 
	 * @return
	 */
	public Integer rowCount() {
		return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
	}

	/**
	 * returns the object given if id less than one then it will be inserted,
	 * otherwise updated
	 * 
	 * @param price
	 * @return
	 */
	public User insert(User user) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(user);
		user.setPassword(PasswordEncrypt.encrypt(user.getPassword()));
		
		if (user.getUserID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new user...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			user.setUserID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New user inserted: " + user.toString());
		} else {
			log.debug("Updating user: " + user.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning user: " + user);
		return user;
		

	}
	
	
	/**
	 * 
	 * @param userID
	 * @return users
	 */
	public List<User> selectAll() {
		log.debug("UserRepo selectAll");
		List<User> users = tmpl.query("SELECT * FROM User", new BeanPropertyRowMapper<User>(User.class));

		log.debug("Query for users, number of items: " + users.size());
		return users;
	}

	/**
	 * 
	 * @param userID
	 * @return users
	 */
	public User selectByUserID(Long userID) {
		log.debug("UserRepo selectByUserID: " + userID);
		String selectByUserIDSQL = selectSQL + "userID=?";
		List<User> users = tmpl.query(selectByUserIDSQL, new BeanPropertyRowMapper<User>(User.class), userID);

		log.debug("Query for user: #" + userID + ", number of items: " + users.size());
		
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param username
	 * @return users
	 */
	public User selectByUsername(String username) {
		log.debug("UserRepo selectByUsername: " + username);
		String selectByUsernameSQL = selectSQL + "username=?";
		List<User> users = tmpl.query(selectByUsernameSQL, new BeanPropertyRowMapper<User>(User.class), username);

		log.debug("Query for username: " + username + ", number of items: " + users.size());
		
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param firstName
	 * @return users
	 */
	public List<User> selectByFirstName(String firstName) {
		log.debug("UserRepo selectByFirstName: " + firstName);
		String selectByFirstNameSQL = selectSQL + "firstName=?";
		List<User> users = tmpl.query(selectByFirstNameSQL, new BeanPropertyRowMapper<User>(User.class), firstName);

		log.debug("Query for first name: " + firstName + ", number of items: " + users.size());
		return users;
	}

	/**
	 * 
	 * @param lastName
	 * @return users
	 */
	public List<User> selectByLastName(String lastName) {
		log.debug("UserRepo selectByLastName: " + lastName);
		String selectByLastNameSQL = selectSQL + "lastName=?";
		List<User> users = tmpl.query(selectByLastNameSQL, new BeanPropertyRowMapper<User>(User.class), lastName);

		log.debug("Query for last name: " + lastName + ", number of items: " + users.size());
		return users;
	}

	

	/**
	 * 
	 * @param user
	 */
	public void delete(Long userID) {
		log.debug("UserRepo delete...");

		tmpl.update(deleteSQL, userID);
		log.debug("User deleted from database.");

	}
}
