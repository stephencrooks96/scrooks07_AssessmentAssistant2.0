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

import com.pgault04.entities.TimeModifier;

@Component
public class TimeModifierRepo {

	private static final int INSERT_CHECKER_CONSTANT = 1;

	private static final Logger log = LoggerFactory.getLogger(TimeModifierRepo.class);

	private String tableName = "TimeModifier";

	private final String insertSQL = "INSERT INTO " + tableName
			+ " (userID, timeModifier) values (:userID, :timeModifier)";

	private final String updateSQL = "UPDATE " + tableName + " SET userID=:userID, " + "timeModifier=:timeModifier "
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
	public TimeModifier insert(TimeModifier timeModifier) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(timeModifier);

		if (selectByUserID(timeModifier.getUserID()).size() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new timeModifier...");

			namedparamTmpl.update(insertSQL, namedParams);

			// inserted
			log.debug("New timeModifier inserted: " + timeModifier.toString());
		} else {
			log.debug("Updating timeModifier: " + timeModifier.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning timeModifier: " + timeModifier);
		return timeModifier;

	}

	/**
	 * 
	 * @param timeModifierID
	 * @return timeModifiers
	 */
	public List<TimeModifier> selectByUserID(Long userID) {
		log.debug("TimeModifierRepo selectByUserID: " + userID);
		String selectByUserIDSQL = selectSQL + "userID=?";
		List<TimeModifier> timeModifiers = tmpl.query(selectByUserIDSQL,
				new BeanPropertyRowMapper<TimeModifier>(TimeModifier.class), userID);

		log.debug("Query for timeModifier: #" + userID + ", number of items: " + timeModifiers.size());
		return timeModifiers;
	}

	/**
	 * 
	 * @param timeModifier
	 */
	public void delete(Long timeModifierID) {
		log.debug("TimeModifierRepo delete...");

		tmpl.update(deleteSQL, timeModifierID);
		log.debug("TimeModifier deleted from database.");

	}
}
