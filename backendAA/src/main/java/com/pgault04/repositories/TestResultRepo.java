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

import com.pgault04.entities.TestResult;

@Component
public class TestResultRepo {

	private static final int INSERT_CHECKER_CONSTANT = 0;

	private static final Logger log = LoggerFactory.getLogger(TestResultRepo.class);

	private String tableName = "TestResult";

	private final String insertSQL = "INSERT INTO " + tableName
			+ " (testID, studentID, testScore) values (:testID, :studentID, :testScore)";

	private final String updateSQL = "UPDATE " + tableName + " SET testID=:testID, "
			+ "studentID=:studentID, testScore=:testScore " + "WHERE testResultID=:testResultID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE testResultID=?";

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
	public TestResult insert(TestResult testResult) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(testResult);

		if (testResult.getTestResultID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new test...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			testResult.setTestResultID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New tests inserted: " + testResult.toString());
		} else {
			log.debug("Updating test: " + testResult.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning test: " + testResult);
		return testResult;

	}

	/**
	 * 
	 * @param testsID
	 * @return testss
	 */
	public List<TestResult> selectByTestResultID(Long testResultID) {
		log.debug("TestResultRepo selectByTestResultID: " + testResultID);
		String selectByTestResultIDSQL = selectSQL + "testResultID=?";
		List<TestResult> tests = tmpl.query(selectByTestResultIDSQL,
				new BeanPropertyRowMapper<TestResult>(TestResult.class), testResultID);

		log.debug("Query for test result: #" + testResultID + ", number of items: " + tests.size());
		return tests;
	}

	/**
	 * 
	 * @param testsID
	 * @return testss
	 */
	public List<TestResult> selectByTestID(Long testID) {
		log.debug("TestsRepo selectByTestID: " + testID);
		String selectByTestIDSQL = selectSQL + "testID=?";
		List<TestResult> tests = tmpl.query(selectByTestIDSQL, new BeanPropertyRowMapper<TestResult>(TestResult.class), testID);

		log.debug("Query for test: #" + testID + ", number of items: " + tests.size());
		return tests;
	}

	/**
	 * 
	 * @param email
	 * @return testss
	 */
	public List<TestResult> selectByStudentID(Long studentID) {
		log.debug("TestResultRepo selectByStudentID: " + studentID);
		String selectByStudentIDSQL = selectSQL + "studentID=?";
		List<TestResult> tests = tmpl.query(selectByStudentIDSQL,
				new BeanPropertyRowMapper<TestResult>(TestResult.class), studentID);

		log.debug("Query for student: " + studentID + ", number of items: " + tests.size());
		return tests;
	}

	/**
	 * 
	 * @param tests
	 */
	public void delete(Long testResultID) {
		log.debug("TestResultRepo delete...");

		tmpl.update(deleteSQL, testResultID);
		log.debug("TestResult deleted from database.");

	}
}
