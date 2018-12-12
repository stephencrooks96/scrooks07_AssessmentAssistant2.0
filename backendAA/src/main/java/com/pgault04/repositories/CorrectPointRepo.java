/**
 * 
 */
package com.pgault04.repositories;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.pgault04.entities.CorrectPoint;

@Component
public class CorrectPointRepo {

	private static final int INSERT_CHECKER_CONSTANT = 0;

	private static final Logger log = LogManager.getLogger(CorrectPointRepo.class);

	private String tableName = "CorrectPoint";

	private final String insertSQL = "INSERT INTO " + tableName
			+ " (questionID, phrase, marksWorth, feedback) values (:questionID, :phrase, :marksWorth, :feedback)";

	private final String updateSQL = "UPDATE " + tableName + " SET questionID=:questionID, "
			+ "phrase=:phrase, marksWorth=:marksWorth, feedback=:feedback " + "WHERE correctPointID=:correctPointID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE correctPointID=?";

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
	public CorrectPoint insert(CorrectPoint correctPoint) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(correctPoint);

		if (correctPoint.getCorrectPointID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new correctPoint...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			correctPoint.setCorrectPointID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New correctPoint inserted: " + correctPoint.toString());
		} else {
			log.debug("Updating correctPoint: " + correctPoint.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning correctPoint: " + correctPoint);
		return correctPoint;

	}

	/**
	 * 
	 * @param correctPointID
	 * @return correctPoint
	 */
	public CorrectPoint selectByCorrectPointID(Long correctPointID) {
		log.debug("CorrectPointRepo selectByCorrectPointID: " + correctPointID);
		String selectByCorrectPointIDSQL = selectSQL + "answerID=?";
		List<CorrectPoint> correctPoints = tmpl.query(selectByCorrectPointIDSQL,
				new BeanPropertyRowMapper<CorrectPoint>(CorrectPoint.class), correctPointID);

		log.debug("Query for correct point: #" + correctPointID + ", number of items: " + correctPoints.size());
		return correctPoints.get(0);
	}

	/**
	 * 
	 * @param correctPointID
	 * @return correctPoints
	 */
	public List<CorrectPoint> selectByQuestionID(Long questionID) {
		log.debug("AnswerRepo selectByQuestionID: " + questionID);
		String selectByQuestionIDSQL = selectSQL + "questionID=?";
		List<CorrectPoint> correctPoints = tmpl.query(selectByQuestionIDSQL,
				new BeanPropertyRowMapper<CorrectPoint>(CorrectPoint.class), questionID);

		log.debug("Query for correct points: #" + questionID + ", number of items: " + correctPoints.size());
		return correctPoints;
	}

	/**
	 * 
	 * @param answer
	 */
	public void delete(Long correctPointID) {
		log.debug("CorrectPointRepo delete...");

		tmpl.update(deleteSQL, correctPointID);
		log.debug("CorrectPoint deleted from database.");

	}
}
