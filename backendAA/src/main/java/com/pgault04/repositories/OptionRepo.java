package com.pgault04.repositories;

import com.pgault04.entities.Option;
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
 * For the Option table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Component
public class OptionRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    /**
     * Logs useful info for problem resolution
     */
    private static final Logger log = LogManager.getLogger(OptionRepo.class);

    private final String insertSQL = "INSERT INTO OptionTbl (questionID, optionContent, worthMarks, feedback) values (:questionID, :optionContent, :worthMarks, :feedback)";
    private final String updateSQL = "UPDATE OptionTbl SET questionID=:questionID, optionContent=:optionContent, worthMarks=:worthMarks, feedback=:feedback WHERE optionID=:optionID";
    private final String selectSQL = "SELECT * FROM OptionTbl WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "OptionTbl";
    private String deleteSQL = "DELETE FROM OptionTbl WHERE optionID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts and updates records in to the Options table
     *
     * @param option the option
     * @return the option after insertion
     */
    public Option insert(Option option) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(option);
        if (option.getOptionID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new option...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            option.setOptionID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New option inserted: {}", option.toString());
        } else {
            log.debug("Updating alternative: {}", option.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning alternative: {}", option);
        return option;
    }

    /**
     * Selects an option from the database based on its id
     *
     * @param optionID the option's id
     * @return the option
     */
    public Option selectByOptionID(Long optionID) {
        log.debug("OptionRepo selectByOptionID: #{}", optionID);
        String selectByOptionIDSQL = selectSQL + "optionID=?";
        List<Option> options = tmpl.query(selectByOptionIDSQL,
                new BeanPropertyRowMapper<>(Option.class), optionID);

        if (options != null && options.size() > 0) {
            log.debug("Query for Option: #{}, number of items: {}", optionID, options.size());
            return options.get(0);
        }
        return null;
    }

    /**
     * Selects the option's based on a question id
     *
     * @param questionID the questions id
     * @return the list of options
     */
    public List<Option> selectByQuestionID(Long questionID) {
        log.debug("OptionRepo selectByQuestionID: #{}", questionID);
        String selectByQuestionIDSQL = selectSQL + "questionID=?";
        List<Option> options = tmpl.query(selectByQuestionIDSQL,
                new BeanPropertyRowMapper<>(Option.class), questionID);
        log.debug("Query for alternatives, number of items: {}", options.size());
        return options;
    }

    /**
     * Deletes an option from the database
     *
     * @param optionID the options id
     */
    public void delete(Long optionID) {
        log.debug("OptionRepo delete #{}", optionID);
        tmpl.update(deleteSQL, optionID);
        log.debug("Option deleted from database #{}", optionID);
    }
}