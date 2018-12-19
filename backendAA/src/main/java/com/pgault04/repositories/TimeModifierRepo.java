package com.pgault04.repositories;

import com.pgault04.entities.TimeModifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Component
public class TimeModifierRepo {

    private static final Logger log = LogManager.getLogger(TimeModifierRepo.class);

    private final String insertSQL = "INSERT INTO TimeModifier (userID, timeModifier) values (:userID, :timeModifier)";
    private final String updateSQL = "UPDATE TimeModifier SET userID=:userID, timeModifier=:timeModifier WHERE userID=:userID";
    private final String selectSQL = "SELECT * FROM TimeModifier WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "TimeModifier";
    private String deleteSQL = "DELETE FROM TimeModifier WHERE userID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts/Updates records in the table
     *
     * @param timeModifier object
     * @return object after insertion
     */
    public TimeModifier insert(TimeModifier timeModifier) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(timeModifier);
        if (selectByUserID(timeModifier.getUserID()) == null) {
            // insert
            log.debug("Inserting new timeModifier...");

            namedparamTmpl.update(insertSQL, namedParams);

            // inserted
            log.debug("New timeModifier inserted: {}", timeModifier.toString());
        } else {
            log.debug("Updating timeModifier: {}", timeModifier.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning timeModifier: {}", timeModifier);
        return timeModifier;
    }

    /**
     * Select from the database for a given user
     *
     * @param userID the users id
     * @return the time modifier
     */
    public TimeModifier selectByUserID(Long userID) {
        log.debug("TimeModifierRepo selectByUserID: #{}", userID);
        String selectByUserIDSQL = selectSQL + "userID=?";
        List<TimeModifier> timeModifiers = tmpl.query(selectByUserIDSQL,
                new BeanPropertyRowMapper<>(TimeModifier.class), userID);

        if (timeModifiers != null && timeModifiers.size() > 0) {
            log.debug("Query for timeModifier: #{}, number of items: {}", userID, timeModifiers.size());
            return timeModifiers.get(0);
        }
        return null;
    }

    /**
     * Deletes a record from the table
     *
     * @param timeModifierID the id
     */
    public void delete(Long timeModifierID) {
        log.debug("TimeModifierRepo delete #{}", timeModifierID);

        tmpl.update(deleteSQL, timeModifierID);
        log.debug("TimeModifier deleted from database #{}", timeModifierID);

    }
}
