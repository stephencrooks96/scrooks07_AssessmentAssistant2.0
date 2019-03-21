package com.pgault04.repositories;

import com.pgault04.entities.QuestionType;
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
 * For the QuestionType table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Component
public class QuestionTypeRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    /**
     * Logs useful info for problem resolution
     */
    private static final Logger log = LogManager.getLogger(QuestionTypeRepo.class);

    private final String insertSQL = "INSERT INTO QuestionType (questionType) values (:questionType)";
    private final String updateSQL = "UPDATE QuestionType SET questionType=:questionType WHERE questionTypeID=:questionTypeID";
    private final String selectSQL = "SELECT * FROM QuestionType WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "QuestionType";
    private String deleteSQL = "DELETE FROM QuestionType WHERE questionTypeID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts/updates question types in to the database
     *
     * @param questionType object
     * @return the object after insertion
     */
    public QuestionType insert(QuestionType questionType) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(questionType);
        if (questionType.getQuestionTypeID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new questionType...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            questionType.setQuestionTypeID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New questionType inserted: {}", questionType.toString());
        } else {
            log.debug("Updating questionType: {}", questionType.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("QuestionTypeRepo returning questionType: {}", questionType.toString());
        return questionType;
    }

    /**
     * Selects a question type by its id
     *
     * @param questionTypeID the question type's id
     * @return the question type
     */
    public QuestionType selectByQuestionTypeID(Long questionTypeID) {
        log.debug("QuestionTypeRepo selectByQuestionTypeID: #{}", questionTypeID);
        String selectByQuestionTypeIDSQL = selectSQL + "questionTypeID=?";
        List<QuestionType> questionTypes = tmpl.query(selectByQuestionTypeIDSQL,
                new BeanPropertyRowMapper<>(QuestionType.class), questionTypeID);

        if (questionTypes != null && questionTypes.size() > 0) {
            log.debug("Query for questionType: #{}, number of items: {}", questionTypeID, questionTypes.size());
            return questionTypes.get(0);
        }
        return null;
    }

    /**
     * Selects all question types
     *
     * @return the list of question types
     */
    public List<QuestionType> selectAll() {
        log.debug("QuestionTypeRepo selectAll");
        List<QuestionType> questionTypes = tmpl.query("SELECT * FROM QuestionType", new BeanPropertyRowMapper<>(QuestionType.class));

        log.debug("Query for question types, number of items: {}", questionTypes.size());
        return questionTypes;
    }

    /**
     * Select by the questions type
     *
     * @param questionType the question type
     * @return list of question types
     */
    public List<QuestionType> selectByQuestionType(String questionType) {
        log.debug("QuestionTypeRepo selectByQuestionType: {}", questionType);
        String selectByQuestionTypeSQL = selectSQL + "questionType=?";
        List<QuestionType> questionTypes = tmpl.query(selectByQuestionTypeSQL,
                new BeanPropertyRowMapper<>(QuestionType.class), questionType);

        log.debug("Query for questionType: #{}, number of items: {}", questionType, questionTypes.size());
        return questionTypes;
    }

    /**
     * Deletes a record from the database
     *
     * @param questionTypeID the question types id
     */
    public void delete(Long questionTypeID) {
        log.debug("QuestionTypeRepo delete #{}", questionTypeID);
        tmpl.update(deleteSQL, questionTypeID);
        log.debug("QuestionType deleted from database #{}", questionTypeID);
    }
}