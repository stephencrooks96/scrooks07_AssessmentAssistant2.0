/**
 *
 */
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

@Component
public class OptionRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    private static final Logger log = LogManager.getLogger(OptionRepo.class);

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "Optiontbl";
    private final String insertSQL = "INSERT INTO " + tableName
            + " (questionID, optionContent, correct) values (:questionID, :optionContent, :correct)";
    private final String updateSQL = "UPDATE " + tableName + " SET questionID=:questionID, "
            + "optionContent=:optionContent, correct=:correct " + "WHERE optionID=:optionID";
    private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";
    private String deleteSQL = "DELETE FROM " + tableName + " WHERE optionID=?";

    /**
     * Finds the amount of records in table
     *
     * @return
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    public Option insert(Option option) {

        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(option);

        if (option.getOptionID() < INSERT_CHECKER_CONSTANT) {

            // insert
            log.debug("Inserting new option...");

            KeyHolder keyHolder = new GeneratedKeyHolder();

            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            option.setOptionID(keyHolder.getKey().longValue());

            // inserted
            log.debug("New option inserted: " + option.toString());
        } else {
            log.debug("Updating alternative: " + option.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning alternative: " + option);
        return option;

    }

    public Option selectByOptionID(Long optionID) {
        log.debug("OptionRepo selectByOptionID: " + optionID);
        String selectByOptionIDSQL = selectSQL + "optionID=?";
        List<Option> options = tmpl.query(selectByOptionIDSQL,
                new BeanPropertyRowMapper<>(Option.class), optionID);

        if (options != null && options.size() > 0) {
            log.debug("Query for Option: #" + optionID + ", number of items: " + options.size());
            return options.get(0);
        }
        return null;
    }

    public List<Option> selectByQuestionID(Long questionID) {
        log.debug("OptionRepo selectByQuestionID: " + questionID);
        String selectByQuestionIDSQL = selectSQL + "questionID=?";
        List<Option> options = tmpl.query(selectByQuestionIDSQL,
                new BeanPropertyRowMapper<>(Option.class), questionID);

        log.debug("Query for alternatives, number of items: " + options.size());
        return options;
    }

    /**
     *
     * @param answer
     */
    public void delete(Long optionID) {
        log.debug("OptionRepo delete...");

        tmpl.update(deleteSQL, optionID);
        log.debug("Option deleted from database.");

    }
}

