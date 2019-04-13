package com.pgault04.repositories;

import com.pgault04.entities.TestQuestion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Class to execute queries to database and receive information
 * For the TestQuestion table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Repository
public class TestQuestionRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    /**
     * Logs useful info for problem resolution
     */
    private static final Logger log = LogManager.getLogger(TestQuestionRepo.class);

    private final String insertSQL = "INSERT INTO TestQuestion (testID, questionID) values (:testID, :questionID)";
    private final String updateSQL = "UPDATE TestQuestion SET questionID=:questionID, testID=:testID WHERE testQuestionID=:testQuestionID";
    private final String selectSQL = "SELECT * FROM TestQuestion WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "TestQuestion";
    private String deleteSQL = "DELETE FROM TestQuestion WHERE testQuestionID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts/Updates TestQuestion record in table
     *
     * @param testQuestion object
     * @return object after insertion
     */
    public TestQuestion insert(TestQuestion testQuestion) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(testQuestion);
        if (testQuestion.getTestQuestionID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new testQuestion...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            testQuestion.setTestQuestionID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New associationType inserted: {}", testQuestion.toString());
        } else {
            log.debug("Updating associationType: {}", testQuestion.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning associationType: {}", testQuestion);
        return testQuestion;
    }

    /**
     * Selects the test question by its id
     *
     * @param testQuestionID object
     * @return list of test questions
     */
    public List<TestQuestion> selectByTestQuestionID(Long testQuestionID) {
        log.debug("TestQuestionRepo selectByTestQuestion: #{}", testQuestionID);
        String selectByTestQuestionIDSQL = selectSQL + "testQuestionID=?";
        List<TestQuestion> testQuestions = tmpl.query(selectByTestQuestionIDSQL,
                new BeanPropertyRowMapper<>(TestQuestion.class), testQuestionID);

        log.debug("Query for testQuestion: #{}, number of items: {}", testQuestionID, testQuestions.size());
        return testQuestions;
    }

    /**
     * Selects the test questions for a given test id
     *
     * @param testID the tests id
     * @return the list of test questions
     */
    public List<TestQuestion> selectByTestID(Long testID) {
        log.debug("TestQuestionRepo selectByTestID: #{}", testID);
        String selectByTestIDSQL = selectSQL + "testID=?";
        List<TestQuestion> testQuestions = tmpl.query(selectByTestIDSQL,
                new BeanPropertyRowMapper<>(TestQuestion.class), testID);

        log.debug("Query for testID: #{}, number of items: {}", testID, testQuestions.size());
        return testQuestions;
    }

    /**
     * Selects the test questions by a question id
     *
     * @param questionID the question id
     * @return the list of test questions
     */
    public List<TestQuestion> selectByQuestionID(Long questionID) {
        log.debug("TestQuestionRepo selectByID: #{}", questionID);
        String selectByQuestionIDSQL = selectSQL + "questionID=?";
        List<TestQuestion> testQuestions = tmpl.query(selectByQuestionIDSQL,
                new BeanPropertyRowMapper<>(TestQuestion.class), questionID);

        log.debug("Query for questionID: #{}, number of items: {}", questionID, testQuestions.size());
        return testQuestions;
    }

    /**
     * Deletes a test question from the database
     *
     * @param testQuestionID the items id
     */
    public void delete(Long testQuestionID) {
        log.debug("TestQuestionRepo delete #{}", testQuestionID);

        tmpl.update(deleteSQL, testQuestionID);
        log.debug("TestQuestion deleted from database #{}", testQuestionID);

    }
}