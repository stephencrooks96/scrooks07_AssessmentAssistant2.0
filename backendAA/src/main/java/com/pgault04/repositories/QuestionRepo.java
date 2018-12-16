/**
 *
 */
package com.pgault04.repositories;

import com.pgault04.entities.Question;
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

import java.util.List;

@Component
public class QuestionRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    private static final Logger log = LoggerFactory.getLogger(QuestionRepo.class);
    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;
    private String tableName = "Question";
    private final String insertSQL = "INSERT INTO " + tableName
            + " (questionType, questionContent, questionFigure, maxScore, modelAnswerID, creatorID) values (:questionType, :questionContent, :questionFigure, :maxScore, :modelAnswerID, :creatorID)";
    private final String updateSQL = "UPDATE " + tableName + " SET questionType=:questionType, "
            + "questionContent=:questionContent, questionFigure=:questionFigure, maxScore=:maxScore, modelAnswerID=:modelAnswerID, creatorID=:creatorID "
            + "WHERE questionID=:questionID";
    private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";
    private String deleteSQL = "DELETE FROM " + tableName + " WHERE questionID=?";

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
    public Question insert(Question question) {

        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(question);

        if (question.getQuestionID() < INSERT_CHECKER_CONSTANT) {

            // insert
            log.debug("Inserting new question...");

            KeyHolder keyHolder = new GeneratedKeyHolder();

            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            question.setQuestionID(keyHolder.getKey().longValue());

            // inserted
            log.debug("New question inserted: " + question.toString());
        } else {
            log.debug("Updating question: " + question.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning question: " + question);
        return question;

    }

    /**
     *
     * @param questionID
     * @return questions
     */
    public Question selectByQuestionID(Long questionID) {
        log.debug("QuestionRepo selectByQuestionID: " + questionID);
        String selectByQuestionIDSQL = selectSQL + "questionID=?";
        List<Question> questions = tmpl.query(selectByQuestionIDSQL,
                new BeanPropertyRowMapper<Question>(Question.class), questionID);

        if (questions.size() > 0) {
            log.debug("Query for question: #" + questionID + ", number of items: " + questions.size());
            return questions.get(0);
        }
        return null;
    }

    /**
     *
     * @param question
     */
    public void delete(Long questionID) {
        log.debug("QuestionRepo delete...");

        tmpl.update(deleteSQL, questionID);
        log.debug("Question deleted from database.");

    }
}
