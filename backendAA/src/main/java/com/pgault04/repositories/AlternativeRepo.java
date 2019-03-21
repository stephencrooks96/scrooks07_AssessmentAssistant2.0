package com.pgault04.repositories;

import com.pgault04.entities.Alternative;
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
 * Class to execute queries to database and receive information
 * For the Alternatives table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Component
public class AlternativeRepo {

    /**
     * Logs useful info for problem resolution
     */
    private static final Logger log = LogManager.getLogger(AlternativeRepo.class);

    private static final int INSERT_CHECKER_CONSTANT = 0;

    private final String selectSQL = "SELECT * FROM Alternative WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    /**
     * @return the number of rows in this table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM Alternative", Integer.class);
    }

    /**
     * Inserts or updates the alternative in db based on a check of the id
     *
     * @param alternative the alternative object
     * @return the returned alternative object
     */
    public Alternative insert(Alternative alternative) {

        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(alternative);
        if (alternative.getAlternativeID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new alternative...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            String insertSQL = "INSERT INTO Alternative (correctPointID, alternativePhrase, math) values (:correctPointID, :alternativePhrase, :math)";
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            alternative.setAlternativeID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.info("New alternative inserted: {}", alternative.toString());
        } else {
            log.debug("Updating alternative: {}", alternative.toString());
            String updateSQL = "UPDATE Alternative SET correctPointID=:correctPointID, alternativePhrase=:alternativePhrase, math=:math WHERE alternativeID=:alternativeID";
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("AlternativeRepo returning alternative: {}", alternative);
        return alternative;
    }

    /**
     * Method to select by alternative id from db
     *
     * @param alternativeID the alternative id
     * @return the found alternative
     */
    public Alternative selectByAlternativeID(Long alternativeID) {
        log.debug("AlternativeRepo selectByAlternativeID: " + alternativeID);
        String selectByQuestionIDSQL = selectSQL + "alternativeID=?";
        List<Alternative> alternatives = tmpl.query(selectByQuestionIDSQL,
                new BeanPropertyRowMapper<>(Alternative.class), alternativeID);

        if (alternatives != null && alternatives.size() > 0) {
            log.debug("Query for Alternative: #{}, number of items: {}", alternativeID, alternatives.size());
            return alternatives.get(0);
        }
        return null;
    }

    /**
     * Method to select alternatives for a correct point id
     *
     * @param correctPointID the correct points id
     * @return the list of alternatives
     */
    public List<Alternative> selectByCorrectPointID(Long correctPointID) {
        log.debug("CorrectPointRepo selectByCorrectPointID: {}", correctPointID);
        String selectByCorrectPointIDSQL = selectSQL + "correctPointID=?";
        List<Alternative> alternatives = tmpl.query(selectByCorrectPointIDSQL,
                new BeanPropertyRowMapper<>(Alternative.class), correctPointID);

        log.debug("Query for alternatives, number of items: {}", alternatives.size());
        return alternatives;
    }

    /**
     * Method to delete an alternative from the db
     *
     * @param alternativeID the alternatives id
     */
    public void delete(Long alternativeID) {
        log.debug("AlternativeRepo delete #{}.", alternativeID);
        String deleteSQL = "DELETE FROM Alternative WHERE alternativeID=?";
        tmpl.update(deleteSQL, alternativeID);
        log.debug("Alternative #{} deleted from database.", alternativeID);
    }
}