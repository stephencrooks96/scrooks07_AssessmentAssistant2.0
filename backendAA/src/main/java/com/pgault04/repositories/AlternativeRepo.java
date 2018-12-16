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

import com.pgault04.entities.Alternative;
import com.pgault04.entities.CorrectPoint;

@Component
public class AlternativeRepo {

	private static final int INSERT_CHECKER_CONSTANT = 0;

	private static final Logger log = LogManager.getLogger(AlternativeRepo.class);

	private String tableName = "Alternative";

	private final String insertSQL = "INSERT INTO " + tableName
			+ " (correctPointID, alternativePhrase) values (:correctPointID, :alternativePhrase)";

	private final String updateSQL = "UPDATE " + tableName + " SET correctPointID=:correctPointID, "
			+ "alternativePhrase=:alternativePhrase " + "WHERE alternativeID=:alternativeID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE alternativeID=?";

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
	public Alternative insert(Alternative alternative) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(alternative);

		if (alternative.getAlternativeID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new alternative...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			alternative.setAlternativeID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New alternative inserted: " + alternative.toString());
		} else {
			log.debug("Updating alternative: " + alternative.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning alternative: " + alternative);
		return alternative;

	}
	
	/**
	 * 
	 * @param alternativeID
	 * @return alternatives
	 */
	public Alternative selectByAlternativeID(Long alternativeID) {
		log.debug("AlternativeRepo selectByAlternativeID: " + alternativeID);
		String selectByQuestionIDSQL = selectSQL + "alternativeID=?";
		List<Alternative> alternatives = tmpl.query(selectByQuestionIDSQL,
				new BeanPropertyRowMapper<>(Alternative.class), alternativeID);

		if (alternatives != null && alternatives.size() > 0) {
			log.debug("Query for Alternative: #" + alternativeID + ", number of items: " + alternatives.size());
			return alternatives.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @param correctPointID
	 * @return correctPoint
	 */
	public List<Alternative> selectByCorrectPointID(Long correctPointID) {
		log.debug("CorrectPointRepo selectByCorrectPointID: " + correctPointID);
		String selectByCorrectPointIDSQL = selectSQL + "correctPointID=?";
		List<Alternative> alternatives = tmpl.query(selectByCorrectPointIDSQL,
				new BeanPropertyRowMapper<>(Alternative.class), correctPointID);

		log.debug("Query for alternatives, number of items: " + alternatives.size());
		return alternatives;
	}

	/**
	 * 
	 * @param answer
	 */
	public void delete(Long alternativeID) {
		log.debug("AlternativeRepo delete...");

		tmpl.update(deleteSQL, alternativeID);
		log.debug("Alternative deleted from database.");

	}
}

