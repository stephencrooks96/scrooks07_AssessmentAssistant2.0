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

import com.pgault04.entities.Tests;

@Component
public class TestsRepo {

	private static final int INSERT_CHECKER_CONSTANT = 0;

	private static final Logger log = LoggerFactory.getLogger(TestsRepo.class);

	private String tableName = "Tests";

	private final String insertSQL = "INSERT INTO " + tableName
			+ " (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades) values (:moduleID, :testTitle, :startDateTime, :endDateTime, :publishResults, :scheduled, :publishGrades)";

	private final String updateSQL = "UPDATE " + tableName + " SET moduleID=:moduleID, testTitle=:testTitle, "
			+ "startDateTime=:startDateTime, endDateTime=:endDateTime, publishResults=:publishResults, scheduled=:scheduled, publishGrades=:publishGrades " + "WHERE testID=:testID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE testID=?";

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
	public Tests insert(Tests test) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(test);

		if (test.getTestID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new test...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			test.setTestID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New tests inserted: " + test.toString());
		} else {
			log.debug("Updating test: " + test.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning test: " + test);
		return test;

	}

	/**
	 * 
	 * @param testsID
	 * @return testss
	 */
	public Tests selectByTestID(Long testID) {
		log.debug("TestsRepo selectByTestID: " + testID);
		String selectByTestIDSQL = selectSQL + "testID=?";
		List<Tests> tests = tmpl.query(selectByTestIDSQL, new BeanPropertyRowMapper<Tests>(Tests.class), testID);

		log.debug("Query for test: #" + testID + ", number of items: " + tests.size());
		
		if (tests != null && tests.size() > 0) {
			return tests.get(0);
		} 
		
		return null;
		
	}

	/**
	 * 
	 * @param email
	 * @return testss
	 */
	public List<Tests> selectByModuleID(Long moduleID) {
		log.debug("TestsRepo selectByModuleID: " + moduleID);
		String selectByModuleIDSQL = selectSQL + "moduleID=?";
		List<Tests> tests = tmpl.query(selectByModuleIDSQL, new BeanPropertyRowMapper<Tests>(Tests.class), moduleID);

		log.debug("Query for email: " + moduleID + ", number of items: " + tests.size());
		return tests;
	}

	/**
	 * 
	 * @param start
	 *            date time
	 * @return testss
	 */
	public List<Tests> selectByStartDateTime(String startDateTime) {
		log.debug("TestsRepo selectByStartDateTime: " + startDateTime);
		String selectByStartDateTimeSQL = selectSQL + "startDateTime=?";
		List<Tests> tests = tmpl.query(selectByStartDateTimeSQL, new BeanPropertyRowMapper<Tests>(Tests.class),
				startDateTime);

		log.debug("Query for start date time: " + startDateTime + ", number of items: " + tests.size());
		return tests;
	}

	/**
	 * 
	 * @param end
	 *            date time
	 * @return tests
	 */
	public List<Tests> selectByEndDateTime(String endDateTime) {
		log.debug("TestsRepo selectByEndDateTime: " + endDateTime);
		String selectByEndDateTimeSQL = selectSQL + "endDateTime=?";
		List<Tests> tests = tmpl.query(selectByEndDateTimeSQL, new BeanPropertyRowMapper<Tests>(Tests.class),
				endDateTime);

		log.debug("Query for end date time: " + endDateTime + ", number of items: " + tests.size());
		return tests;
	}

	/**
	 * 
	 * @param tests
	 */
	public void delete(Long testID) {
		log.debug("TestsRepo delete...");

		tmpl.update(deleteSQL, testID);
		log.debug("Tests deleted from database.");

	}
}
