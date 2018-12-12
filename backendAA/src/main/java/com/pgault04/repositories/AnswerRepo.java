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

import com.pgault04.entities.Answer;

@Component
public class AnswerRepo {

	private static final int INSERT_CHECKER_CONSTANT = 0;

	private static final Logger log = LoggerFactory.getLogger(AnswerRepo.class);

	private String tableName = "Answer";

	private final String insertSQL = "INSERT INTO " + tableName
			+ " (questionID, answererID, markerID, testID, content, score) values (:questionID, :answererID, :markerID, :testID, :content, :score)";

	private final String updateSQL = "UPDATE " + tableName + " SET questionID=:questionID, "
			+ "answererID=:answererID, markerID=:markerID, testID=:testID, content=:content, score=:score "
			+ "WHERE answerID=:answerID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE answerID=?";

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
	public Answer insert(Answer answer) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(answer);

		if (answer.getAnswerID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new answer...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			answer.setAnswerID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New answer inserted: " + answer.toString());
		} else {
			log.debug("Updating answer: " + answer.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning answer: " + answer);
		return answer;

	}

	/**
	 * 
	 * @param answerID
	 * @return answers
	 */
	public List<Answer> selectByAnswerID(Long answerID) {
		log.debug("AnswerRepo selectByAnswerID: " + answerID);
		String selectByAnswerIDSQL = selectSQL + "answerID=?";
		List<Answer> answers = tmpl.query(selectByAnswerIDSQL, new BeanPropertyRowMapper<Answer>(Answer.class),
				answerID);

		log.debug("Query for answer: #" + answerID + ", number of items: " + answers.size());
		return answers;
	}
	
	

	/**
	 * 
	 * @param answerID
	 * @return answers
	 */
	public List<Answer> selectByQuestionID(Long questionID) {
		log.debug("AnswerRepo selectByQuestionID: " + questionID);
		String selectByQuestionIDSQL = selectSQL + "questionID=?";
		List<Answer> answers = tmpl.query(selectByQuestionIDSQL, new BeanPropertyRowMapper<Answer>(Answer.class),
				questionID);

		log.debug("Query for question: #" + questionID + ", number of items: " + answers.size());
		return answers;
	}
	
	/**
	 * 
	 * @param answerID
	 * @return answers
	 */
	public List<Answer> selectByTestID(Long testID) {
		log.debug("AnswerRepo selectByTestID: " + testID);
		String selectByTestIDSQL = selectSQL + "testID=?";
		List<Answer> answers = tmpl.query(selectByTestIDSQL, new BeanPropertyRowMapper<Answer>(Answer.class),
				testID);

		log.debug("Query for test: #" + testID + ", number of items: " + answers.size());
		return answers;
	}

	/**
	 * 
	 * @param answerID
	 * @return answers
	 */
	public List<Answer> selectByAnswererID(Long answererID) {
		log.debug("AnswerRepo selectByAnswererID: " + answererID);
		String selectByAnswererIDSQL = selectSQL + "answererID=?";
		List<Answer> answers = tmpl.query(selectByAnswererIDSQL, new BeanPropertyRowMapper<Answer>(Answer.class),
				answererID);

		log.debug("Query for question: #" + answererID + ", number of items: " + answers.size());
		return answers;
	}

	/**
	 * 
	 * @param answerID
	 * @return answers
	 */
	public List<Answer> selectByMarkerID(Long markerID) {
		log.debug("AnswerRepo selectByMarkerID: " + markerID);
		String selectByMarkerIDSQL = selectSQL + "markerID=?";
		List<Answer> answers = tmpl.query(selectByMarkerIDSQL, new BeanPropertyRowMapper<Answer>(Answer.class),
				markerID);

		log.debug("Query for question: #" + markerID + ", number of items: " + answers.size());
		return answers;
	}

	/**
	 * 
	 * @param answer
	 */
	public void delete(Long answerID) {
		log.debug("AnswerRepo delete...");

		tmpl.update(deleteSQL, answerID);
		log.debug("Answer deleted from database.");

	}
}
