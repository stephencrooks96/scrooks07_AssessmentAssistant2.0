package com.pgault04.repositories;

import com.pgault04.entities.Question;
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
public class QuestionRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    private static final Logger log = LogManager.getLogger(QuestionRepo.class);

    private final String insertSQL = "INSERT INTO Question (questionType, questionContent, questionFigure, maxScore, creatorID, allThatApply) values (:questionType, :questionContent, :questionFigure, :maxScore, :creatorID, :allThatApply)";
    private final String updateSQL = "UPDATE Question SET questionType=:questionType, questionContent=:questionContent, questionFigure=:questionFigure, maxScore=:maxScore, creatorID=:creatorID, allThatApply=:allThatApply WHERE questionID=:questionID";
    private final String selectSQL = "SELECT * FROM Question WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "Question";
    private String deleteSQL = "DELETE FROM Question WHERE questionID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts/Updates questions in to the database
     *
     * @param question the question object
     * @return the question object after insertion
     */
    public Question insert(Question question) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(question);
        if (question.getQuestionID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new question...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            question.setQuestionID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New question inserted: {}", question.toString());
        } else {
            log.debug("Updating question: {}", question.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning question: {}", question);
        return question;
    }

    /**
     * Selects a question by it's question id
     *
     * @param questionID the question's id
     * @return the question
     */
    public Question selectByQuestionID(Long questionID) {
        log.debug("QuestionRepo selectByQuestionID: #{}", questionID);
        String selectByQuestionIDSQL = selectSQL + "questionID=?";
        List<Question> questions = tmpl.query(selectByQuestionIDSQL,
                new BeanPropertyRowMapper<>(Question.class), questionID);

        if (questions.size() > 0) {
            log.debug("Query for question: #{}, number of items: ", questionID, questions.size());
            return questions.get(0);
        }
        return null;
    }

    /**
     * Selects questions based on who created them
     *
     * @param creatorID the creator's userid
     * @return the list of questions
     */
    public List<Question> selectByCreatorID(Long creatorID) {
        log.debug("QuestionRepo selectByCreatorID: #{}", creatorID);
        String selectByCreatorIDSQL = selectSQL + "creatorID=?";
        List<Question> questions = tmpl.query(selectByCreatorIDSQL,
                new BeanPropertyRowMapper<>(Question.class), creatorID);

        log.debug("Query for question with creator id: #{}, number of items: {}", creatorID, questions.size());
        return questions;
    }

    /**
     * Deletes a record from the question table
     *
     * @param questionID the questions id
     */
    public void delete(Long questionID) {
        log.debug("QuestionRepo deletes #{}", questionID);

        tmpl.update(deleteSQL, questionID);
        log.debug("Question deleted from database #{}", questionID);

    }
}
