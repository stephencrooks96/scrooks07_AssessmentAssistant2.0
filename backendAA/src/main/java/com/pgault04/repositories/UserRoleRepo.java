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
import org.springframework.stereotype.Component;

import com.pgault04.entities.UserRole;

@Component
public class UserRoleRepo {

	private static final int INSERT_CHECKER_CONSTANT = 0;

	private static final Logger log = LoggerFactory.getLogger(UserRoleRepo.class);

	private String tableName = "UserRole";

	private final String insertSQL = "INSERT INTO " + tableName + " (role) values (:role)";

	private final String updateSQL = "UPDATE " + tableName + " SET role=:role "
			+ "WHERE userRoleID=:userRoleID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE userRoleID=?";

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
	public UserRole insert(UserRole userRole) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(userRole);

		if (userRole.getUserRoleID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new userRole...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			userRole.setUserRoleID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New userRole inserted: " + userRole.toString());
		} else {
			log.debug("Updating userRole: " + userRole.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning userRole: " + userRole);
		return userRole;

	}

	/**
	 * 
	 * @param userID
	 * @return users
	 */
	public List<UserRole> selectAll() {
		log.debug("UserRoleRepo selectAll");
		List<UserRole> userRoles = tmpl.query("SELECT * FROM UserRole",
				new BeanPropertyRowMapper<UserRole>(UserRole.class));

		log.debug("Query for users, number of items: " + userRoles.size());
		return userRoles;
	}

	/**
	 * 
	 * @param userRoleID
	 * @return userRoles
	 */
	public UserRole selectByUserRoleID(Long userRoleID) {
		log.debug("UserRoleRepo selectByUserRoleID: " + userRoleID);
		String selectByUserRoleIDSQL = selectSQL + "userRoleID=?";
		List<UserRole> userRoles = tmpl.query(selectByUserRoleIDSQL,
				new BeanPropertyRowMapper<UserRole>(UserRole.class), userRoleID);

		log.debug("Query for userRole: #" + userRoleID + ", number of items: " + userRoles.size());
		return userRoles.get(0);
	}

	/**
	 * 
	 * @param email
	 * @return userRoles
	 */
	public List<UserRole> selectByRole(String role) {
		log.debug("UserRoleRepo selectByRole: " + role);
		String selectByUserRoleSQL = selectSQL + "role=?";
		List<UserRole> userRoles = tmpl.query(selectByUserRoleSQL, new BeanPropertyRowMapper<UserRole>(UserRole.class),
				role);

		log.debug("Query for userRole: " + role + ", number of items: " + userRoles.size());
		return userRoles;
	}

	/**
	 * 
	 * @param userRole
	 */
	public void delete(Long userRoleID) {
		log.debug("UserRoleRepo delete...");

		tmpl.update(deleteSQL, userRoleID);
		log.debug("UserRole deleted from database.");

	}
}
