package com.pgault04.repositories;

import com.pgault04.entities.CorrectPoint;
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

/**
 * @author Paul Gault 40126005
 * @since December 2018
 */
@Component
public class CorrectPointRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    private static final Logger log = LogManager.getLogger(CorrectPointRepo.class);

    private final String insertSQL = "INSERT INTO CorrectPoint (questionID, phrase, marksWorth, feedback, indexedAt) values (:questionID, :phrase, :marksWorth, :feedback, :indexedAt)";
    private final String updateSQL = "UPDATE CorrectPoint SET questionID=:questionID, phrase=:phrase, marksWorth=:marksWorth, feedback=:feedback, indexedAt=:indexedAt WHERE correctPointID=:correctPointID";
    private final String selectSQL = "SELECT * FROM CorrectPoint WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "CorrectPoint";
    private String deleteSQL = "DELETE FROM CorrectPoint WHERE correctPointID=?";

    /**
     * @return the number of rows in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts or updates a correct point in to the database
     * @param correctPoint the correct point
     * @return the returned correct point after insertion
     */
    public CorrectPoint insert(CorrectPoint correctPoint) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(correctPoint);
        if (correctPoint.getCorrectPointID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new correctPoint...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            correctPoint.setCorrectPointID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New correctPoint inserted: {}", correctPoint.toString());
        } else {
            log.debug("Updating correctPoint: {}", correctPoint.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("CorrectPointRepo returning correctPoint: {}", correctPoint.toString());
        return correctPoint;
    }

    /**
     * Selects a correct point from the db by its id
     * @param correctPointID the correct point's id
     * @return the correct point
     */
    public CorrectPoint selectByCorrectPointID(Long correctPointID) {
        log.debug("CorrectPointRepo selectByCorrectPointID: {}", correctPointID);
        String selectByCorrectPointIDSQL = selectSQL + "correctPointID=?";
        List<CorrectPoint> correctPoints = tmpl.query(selectByCorrectPointIDSQL,
                new BeanPropertyRowMapper<>(CorrectPoint.class), correctPointID);

        if (correctPoints != null && correctPoints.size() > 0) {
            log.debug("Query for correct point: #{}, number of items: {}", correctPointID, correctPoints.size());
            return correctPoints.get(0);
        }
        return null;
    }

    /**
     * Selects a list of correct points from the db based on their question id
     * @param questionID the question id
     * @return the list of correct points
     */
    public List<CorrectPoint> selectByQuestionID(Long questionID) {
        log.debug("AnswerRepo selectByQuestionID: {}", questionID);
        String selectByQuestionIDSQL = selectSQL + "questionID=?";
        List<CorrectPoint> correctPoints = tmpl.query(selectByQuestionIDSQL,
                new BeanPropertyRowMapper<>(CorrectPoint.class), questionID);

        log.debug("Query for correct points: #{}, number of items: {}", questionID, correctPoints.size());
        return correctPoints;
    }

    /**
     * Deletes a correct point from the database
     * @param correctPointID the correct point's id
     */
    public void delete(Long correctPointID) {
        log.debug("CorrectPointRepo delete #{}", correctPointID);

        tmpl.update(deleteSQL, correctPointID);
        log.debug("CorrectPoint deleted from database #{}", correctPointID);

    }
}
