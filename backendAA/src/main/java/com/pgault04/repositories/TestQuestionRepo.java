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

import com.pgault04.entities.TestQuestion;

@Component
public class TestQuestionRepo {

	private static final int INSERT_CHECKER_CONSTANT = 0;

	private static final Logger log = LoggerFactory.getLogger(TestQuestionRepo.class);

	private String tableName = "TestQuestion";

	private final String insertSQL = "INSERT INTO " + tableName + " (testID, questionID) values (:testID, :questionID)";

	private final String updateSQL = "UPDATE " + tableName + " SET questionID=:questionID, testID=:testID "
			+ "WHERE testQuestionID=:testQuestionID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE testQuestionID=?";

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
	public TestQuestion insert(TestQuestion testQuestion) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(testQuestion);

		if (testQuestion.getTestQuestionID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new testQuestion...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			testQuestion.setTestQuestionID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New associationType inserted: " + testQuestion.toString());
		} else {
			log.debug("Updating associationType: " + testQuestion.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning associationType: " + testQuestion);
		return testQuestion;

	}

	/**
	 * 
	 * @param associationTypeID
	 * @return associationTypes
	 */
	public List<TestQuestion> selectByTestQuestionID(Long testQuestionID) {
		log.debug("TestQuestionRepo selectByTestQuestion: " + testQuestionID);
		String selectByTestQuestionIDSQL = selectSQL + "testQuestionID=?";
		List<TestQuestion> testQuestions = tmpl.query(selectByTestQuestionIDSQL,
				new BeanPropertyRowMapper<TestQuestion>(TestQuestion.class), testQuestionID);

		log.debug("Query for testQuestion: #" + testQuestionID + ", number of items: " + testQuestions.size());
		return testQuestions;
	}

	/**
	 * 
	 * @param email
	 * @return associationTypes
	 */
	public List<TestQuestion> selectByTestID(Long testID) {
		log.debug("TestQuestionRepo selectByTestID: " + testID);
		String selectByTestIDSQL = selectSQL + "testID=?";
		List<TestQuestion> testQuestions = tmpl.query(selectByTestIDSQL,
				new BeanPropertyRowMapper<TestQuestion>(TestQuestion.class), testID);

		log.debug("Query for testID: " + testID + ", number of items: " + testQuestions.size());
		return testQuestions;
	}

	/**
	 * 
	 * @param email
	 * @return associationTypes
	 */
	public List<TestQuestion> selectByQuestionID(Long questionID) {
		log.debug("TestQuestionRepo selectByQuestionID: " + questionID);
		String selectByQuestionIDSQL = selectSQL + "questionID=?";
		List<TestQuestion> testQuestions = tmpl.query(selectByQuestionIDSQL,
				new BeanPropertyRowMapper<TestQuestion>(TestQuestion.class), questionID);

		log.debug("Query for questionID: " + questionID + ", number of items: " + testQuestions.size());
		return testQuestions;
	}

	/**
	 * 
	 * @param associationType
	 */
	public void delete(Long testQuestionID) {
		log.debug("TestQuestionRepo delete...");

		tmpl.update(deleteSQL, testQuestionID);
		log.debug("TestQuestion deleted from database.");

	}
}
