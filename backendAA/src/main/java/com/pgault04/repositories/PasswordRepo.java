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
import org.springframework.stereotype.Component;

import com.pgault04.entities.Password;

@Component
public class PasswordRepo {

	private static final int INSERT_CHECKER_CONSTANT = 1;

	private static final Logger log = LoggerFactory.getLogger(PasswordRepo.class);

	private String tableName = "Password";

	private final String insertSQL = "INSERT INTO " + tableName
			+ " (userID, resetString) values (:userID, :resetString)";

	private final String updateSQL = "UPDATE " + tableName + " SET resetString=:resetString "
			+ "WHERE userID=:userID";

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
	public Password insert(Password password) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(password);
		if (selectByUserID(password.getUserID()).size() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new password...");

			namedparamTmpl.update(insertSQL, namedParams);

			// inserted
			log.debug("New password inserted: " + password.toString());
		} else {
			log.debug("Updating password: " + password.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning password: " + password);
		return password;

	}

	/**
	 * 
	 * @param passwordID
	 * @return passwords
	 */
	public List<Password> selectByUserID(Long userID) {
		log.debug("PasswordRepo selectByUserID: " + userID);
		String selectByPasswordIDSQL = selectSQL + "userID=?";
		List<Password> passwords = tmpl.query(selectByPasswordIDSQL,
				new BeanPropertyRowMapper<Password>(Password.class), userID);

		log.debug("Query for password: #" + userID + ", number of items: " + passwords.size());
		return passwords;
	}

	/**
	 * 
	 * @param password
	 */
	public void delete(Long userID) {
		log.debug("PasswordRepo delete...");

		tmpl.update(deleteSQL, userID);
		log.debug("Password deleted from database.");

	}
}