package com.pgault04.repositories;

import com.pgault04.entities.QuestionMathLine;
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
public class QuestionMathLineRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    private static final Logger log = LogManager.getLogger(QuestionMathLineRepo.class);

    private final String insertSQL = "INSERT INTO QuestionMathLines (questionID, content, indexedAt) values (:questionID, :content, :indexedAt)";
    private final String updateSQL = "UPDATE QuestionMathLines SET questionID=:questionID, content=:content, indexedAt=:indexedAt WHERE questionMathLineID=:questionMathLineID";
    private final String selectSQL = "SELECT * FROM QuestionMathLines WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "QuestionMathLines";
    private String deleteSQL = "DELETE FROM QuestionMathLines WHERE questionMathLineID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts a question math line in to the database
     *
     * @param questionMathLine the questionMathLine
     * @return the questionMathLine after insertion
     */
    public QuestionMathLine insert(QuestionMathLine questionMathLine) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(questionMathLine);
        if (questionMathLine.getQuestionMathLineID() <= INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new question...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            questionMathLine.setQuestionMathLineID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New questionMathLine inserted: {}", questionMathLine.toString());
        } else {
            log.debug("Updating questionMathLine: {}", questionMathLine.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning questionMathLine: {}", questionMathLine);
        return questionMathLine;
    }

    /**
     * Selects math lines by question id
     *
     * @param questionID the question's id
     * @return the questionMathLines
     */
    public List<QuestionMathLine> selectByQuestionID(Long questionID) {
        log.debug("QuestionMathLineRepo selectByQuestionID: #{}", questionID);
        String selectByQuestionIDSQL = selectSQL + "questionID=? ORDER BY indexedAt";
        List<QuestionMathLine> mathLines = tmpl.query(selectByQuestionIDSQL,
                new BeanPropertyRowMapper<>(QuestionMathLine.class), questionID);

        log.debug("Query for question: #{}, number of items: ", questionID, mathLines.size());
        return mathLines;
    }

    /**
     * Selects the question math line by is unique key
     *
     * @param questionMathLineID the key
     * @return the questionMathLine
     */
    public QuestionMathLine selectByQuestionMathLineID(Long questionMathLineID) {
        log.debug("QuestionMathLineRepo selectByQuestionMathLineID: #{}", questionMathLineID);
        String selectByQuestionMathLineIDSQL = selectSQL + "questionMathLineID=?";
        List<QuestionMathLine> mathLines = tmpl.query(selectByQuestionMathLineIDSQL,
                new BeanPropertyRowMapper<>(QuestionMathLine.class), questionMathLineID);

        if (mathLines.size() > 0) {
            log.debug("Query for questionMathLine: #{}, number of items: ", questionMathLineID, mathLines.size());
            return mathLines.get(0);
        }
        return null;
    }

    /**
     * Deletes a record from the questionMathLine table
     *
     * @param questionMathLineID the questionMathLine's id
     */
    public void delete(Long questionMathLineID) {
        log.debug("QuestionMathLineRepo delete #{}", questionMathLineID);

        tmpl.update(deleteSQL, questionMathLineID);
        log.debug("QuestionMathLine deleted from database #{}", questionMathLineID);

    }
}
