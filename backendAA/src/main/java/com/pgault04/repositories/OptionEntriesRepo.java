package com.pgault04.repositories;

import com.pgault04.entities.OptionEntries;
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
 * For the OptionEntries table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Component
public class OptionEntriesRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    /**
     * Logs useful info for problem resolution
     */
    private static final Logger log = LogManager.getLogger(OptionEntriesRepo.class);

    private final String insertSQL = "INSERT INTO OptionEntries (optionID, answerID) values (:optionID, :answerID)";
    private final String updateSQL = "UPDATE OptionEntries SET optionID=:optionID, answerID=:answerID WHERE optionEntryID=:optionEntryID";
    private final String selectSQL = "SELECT * FROM OptionEntries WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "OptionEntries";
    private String deleteSQL = "DELETE FROM OptionEntries WHERE optionEntryID=?";

    public OptionEntriesRepo() {}

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts/Updates option entries in to the database
     *
     * @param optionEntry the option entry object
     * @return the option entry object after insertion
     */
    public OptionEntries insert(OptionEntries optionEntry) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(optionEntry);
        if (optionEntry.getOptionEntryID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new option entry...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            optionEntry.setOptionEntryID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New option entry inserted: {}", optionEntry.toString());
        } else {
            log.debug("Updating option entry: {}", optionEntry.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning optionEntry: {}", optionEntry);
        return optionEntry;
    }

    /**
     * Selects a option entry by it's option entry id
     *
     * @param optionEntryID the option entry's id
     * @return the option entry
     */
    public OptionEntries selectByOptionEntryID(Long optionEntryID) {
        log.debug("OptionEntriesRepo selectByInputID: #{}", optionEntryID);
        String selectByOptionEntriesIDSQL = selectSQL + "optionEntryID=?";
        List<OptionEntries> optionEntries = tmpl.query(selectByOptionEntriesIDSQL,
                new BeanPropertyRowMapper<>(OptionEntries.class), optionEntryID);

        if (optionEntries.size() > 0) {
            log.debug("Query for option entries: #{}, number of items: ", optionEntryID, optionEntries.size());
            return optionEntries.get(0);
        }
        return null;
    }

    /**
     * Selects option entries based on the answer they belong to
     *
     * @param answerID the answer's id
     * @return the list of option entries
     */
    public List<OptionEntries> selectByAnswerID(Long answerID) {
        log.debug("OptionEntriesRepo selectByAnswerID: #{}", answerID);
        String selectByAnswerIDSQL = selectSQL + "answerID=?";
        List<OptionEntries> optionEntries = tmpl.query(selectByAnswerIDSQL,
                new BeanPropertyRowMapper<>(OptionEntries.class), answerID);

        log.debug("Query for option entry with answer id: #{}, number of items: {}", answerID, optionEntries.size());
        return optionEntries;
    }

    /**
     * Deletes a record from the option entries table
     *
     * @param optionEntryID the option entry's id
     */
    public void delete(Long optionEntryID) {
        log.debug("OptionEntriesRepo deletes #{}", optionEntryID);

        tmpl.update(deleteSQL, optionEntryID);
        log.debug("Option entry deleted from database #{}", optionEntryID);

    }
}