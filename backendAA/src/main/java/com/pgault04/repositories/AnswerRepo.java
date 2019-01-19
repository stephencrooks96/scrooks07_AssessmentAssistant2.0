package com.pgault04.repositories;

import com.pgault04.entities.Answer;
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

import java.util.List;
import java.util.Objects;

@Component
public class AnswerRepo {

    /**
     * Logs useful info for problem resolution
     */
    private static final Logger log = LogManager.getLogger(AnswerRepo.class);

    private static final int INSERT_CHECKER_CONSTANT = 0;

    private final String insertSQL = "INSERT INTO Answer (questionID, answererID, markerID, testID, content, score, feedback, markerApproved, tutorApproved) values (:questionID, :answererID, :markerID, :testID, :content, :score, :feedback, :markerApproved, :tutorApproved)";
    private final String updateSQL = "UPDATE Answer SET questionID=:questionID, answererID=:answererID, markerID=:markerID, testID=:testID, content=:content, score=:score, feedback=:feedback, markerApproved=:markerApproved, tutorApproved=:tutorApproved WHERE answerID=:answerID";
    private final String selectSQL = "SELECT * FROM Answer WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "Answer";
    private String deleteSQL = "DELETE FROM Answer WHERE answerID=?";

    /**
     * @return the number of rows in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts or updates the alternative in db based on a check of the id
     *
     * @param answer the answer
     * @return the returned answer after insertion
     */
    public Answer insert(Answer answer) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(answer);
        if (answer.getAnswerID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new answer...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            answer.setAnswerID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New answer inserted: {}", answer.toString());
        } else {
            log.debug("Updating answer: {}", answer.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("AnswerRepo returning answer: {}", answer.toString());
        return answer;

    }

    /**
     * Method for selecting answer by its unique id
     *
     * @param answerID the answer id
     * @return the returned answer
     */
    public Answer selectByAnswerID(Long answerID) {
        log.debug("AnswerRepo selectByAnswerID: {}", answerID);
        String selectByAnswerIDSQL = selectSQL + "answerID=?";
        List<Answer> answers = tmpl.query(selectByAnswerIDSQL, new BeanPropertyRowMapper<>(Answer.class),
                answerID);

        if (answers != null && answers.size() > 0) {
            log.debug("Query for answer: #{}, number of items: {}", answerID, answers.size());
            return answers.get(0);
        }
        return null;
    }

    /**
     * Method to select answers by their question id
     *
     * @param questionID the question id
     * @return the list of answers
     */
    public List<Answer> selectByQuestionID(Long questionID) {
        log.debug("AnswerRepo selectByQuestionID: {}", questionID);
        String selectByQuestionIDSQL = selectSQL + "questionID=?";
        List<Answer> answers = tmpl.query(selectByQuestionIDSQL, new BeanPropertyRowMapper<>(Answer.class),
                questionID);

        log.debug("Query for question: #{}, number of items: {}", questionID, answers.size());
        return answers;
    }

    /**
     * Method to select answers by the test id
     *
     * @param testID the test id
     * @return the list of answers
     */
    public List<Answer> selectByTestID(Long testID) {
        log.debug("AnswerRepo selectByTestID: {}", testID);
        String selectByTestIDSQL = selectSQL + "testID=?";
        List<Answer> answers = tmpl.query(selectByTestIDSQL, new BeanPropertyRowMapper<>(Answer.class),
                testID);

        log.debug("Query for test: #{}, number of items: {}", testID, answers.size());
        return answers;
    }

    /**
     * Selects the answered by a given answerer
     *
     * @param answererID the answerer's id
     * @return the list of answers
     */
    public List<Answer> selectByAnswererID(Long answererID) {
        log.debug("AnswerRepo selectByAnswererID: {}", answererID);
        String selectByAnswererIDSQL = selectSQL + "answererID=?";
        List<Answer> answers = tmpl.query(selectByAnswererIDSQL, new BeanPropertyRowMapper<>(Answer.class),
                answererID);

        log.debug("Query for question: #{}, number of items: {}", answererID, answers.size());
        return answers;
    }

    /**
     * Selects the answers by a given marker
     *
     * @param markerID the markers id
     * @return the list of answers
     */
    public List<Answer> selectByMarkerID(Long markerID) {
        log.debug("AnswerRepo selectByMarkerID: {}", markerID);
        String selectByMarkerIDSQL = selectSQL + "markerID=?";
        List<Answer> answers = tmpl.query(selectByMarkerIDSQL, new BeanPropertyRowMapper<>(Answer.class),
                markerID);

        log.debug("Query for question: #{}, number of items: {}", markerID, answers.size());
        return answers;
    }

    /**
     * Method to delete an answer from the database
     *
     * @param answerID the answer id
     */
    public void delete(Long answerID) {
        log.debug("AnswerRepo delete #{}", answerID);
        tmpl.update(deleteSQL, answerID);
        log.debug("Answer #{} deleted from database.", answerID);

    }
}
