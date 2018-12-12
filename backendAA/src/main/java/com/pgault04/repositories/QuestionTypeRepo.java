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

import com.pgault04.entities.QuestionType;

@Component
public class QuestionTypeRepo {

	private static final int INSERT_CHECKER_CONSTANT = 0;

	private static final Logger log = LoggerFactory.getLogger(QuestionTypeRepo.class);

	private String tableName = "QuestionType";

	private final String insertSQL = "INSERT INTO " + tableName + " (questionType) values (:questionType)";

	private final String updateSQL = "UPDATE " + tableName + " SET questionType=:questionType "
			+ "WHERE questionTypeID=:questionTypeID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE questionTypeID=?";

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
	public QuestionType insert(QuestionType questionType) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(questionType);

		if (questionType.getQuestionTypeID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new questionType...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			questionType.setQuestionTypeID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New questionType inserted: " + questionType.toString());
		} else {
			log.debug("Updating questionType: " + questionType.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning questionType: " + questionType);
		return questionType;

	}

	/**
	 * 
	 * @param questionTypeID
	 * @return questionTypes
	 */
	public List<QuestionType> selectByQuestionTypeID(Long questionTypeID) {
		log.debug("QuestionTypeRepo selectByQuestionTypeID: " + questionTypeID);
		String selectByQuestionTypeIDSQL = selectSQL + "questionTypeID=?";
		List<QuestionType> questionTypes = tmpl.query(selectByQuestionTypeIDSQL,
				new BeanPropertyRowMapper<QuestionType>(QuestionType.class), questionTypeID);

		log.debug("Query for questionType: #" + questionTypeID + ", number of items: " + questionTypes.size());
		return questionTypes;
	}
	
	/**
	 * 
	 * @param userID
	 * @return users
	 */
	public List<QuestionType> selectAll() {
		log.debug("QuestionTypeRepo selectAll");
		List<QuestionType> questionTypes = tmpl.query("SELECT * FROM QuestionType", new BeanPropertyRowMapper<QuestionType>(QuestionType.class));

		log.debug("Query for question types, number of items: " + questionTypes.size());
		return questionTypes;
	}

	/**
	 * 
	 * @param email
	 * @return questionTypes
	 */
	public List<QuestionType> selectByQuestionType(String questionType) {
		log.debug("QuestionTypeRepo selectByQuestionType: " + questionType);
		String selectByQuestionTypeSQL = selectSQL + "questionType=?";
		List<QuestionType> questionTypes = tmpl.query(selectByQuestionTypeSQL,
				new BeanPropertyRowMapper<QuestionType>(QuestionType.class), questionType);

		log.debug("Query for questionType: " + questionType + ", number of items: " + questionTypes.size());
		return questionTypes;
	}

	/**
	 * 
	 * @param questionType
	 */
	public void delete(Long questionTypeID) {
		log.debug("QuestionTypeRepo delete...");

		tmpl.update(deleteSQL, questionTypeID);
		log.debug("QuestionType deleted from database.");

	}
}
