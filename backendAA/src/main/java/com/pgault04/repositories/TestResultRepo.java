package com.pgault04.repositories;

import com.pgault04.entities.TestResult;
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
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Component
public class TestResultRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    private static final Logger log = LogManager.getLogger(TestResultRepo.class);

    private final String insertSQL = "INSERT INTO TestResult (testID, studentID, testScore) values (:testID, :studentID, :testScore)";
    private final String updateSQL = "UPDATE TestResult SET testID=:testID, studentID=:studentID, testScore=:testScore WHERE testResultID=:testResultID";
    private final String selectSQL = "SELECT * FROM TestResult WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "TestResult";
    private String deleteSQL = "DELETE FROM TestResult WHERE testResultID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts/Updates records in the table
     *
     * @param testResult the object
     * @return the object after insertion
     */
    public TestResult insert(TestResult testResult) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(testResult);
        if (testResult.getTestResultID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new test...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            testResult.setTestResultID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New tests inserted: {}", testResult.toString());
        } else {
            log.debug("Updating test: {}", testResult.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning test: {}", testResult);
        return testResult;
    }

    /**
     * Selects record from row based on its id
     *
     * @param testResultID the id
     * @return the object
     */
    public TestResult selectByTestResultID(Long testResultID) {
        log.debug("TestResultRepo selectByTestResultID: #{}", testResultID);
        String selectByTestResultIDSQL = selectSQL + "testResultID=?";
        List<TestResult> tests = tmpl.query(selectByTestResultIDSQL,
                new BeanPropertyRowMapper<>(TestResult.class), testResultID);

        if (tests != null && tests.size() > 0) {
            log.debug("Query for test result: #{}, number of items: {}", testResultID, tests.size());
            return tests.get(0);
        }
        return null;
    }

    /**
     * Selects the results by given test
     *
     * @param testID the test id
     * @return the list of results
     */
    public List<TestResult> selectByTestID(Long testID) {
        log.debug("TestsRepo selectByTestID: #{}", testID);
        String selectByTestIDSQL = selectSQL + "testID=?";
        List<TestResult> tests = tmpl.query(selectByTestIDSQL, new BeanPropertyRowMapper<>(TestResult.class), testID);

        log.debug("Query for test: #{}, number of items: {}", testID, tests.size());
        return tests;
    }

    /**
     * Selects results by a student
     *
     * @param studentID the students id
     * @return the list of results
     */
    public List<TestResult> selectByStudentID(Long studentID) {
        log.debug("TestResultRepo selectByStudentID: #{}", studentID);
        String selectByStudentIDSQL = selectSQL + "studentID=?";
        List<TestResult> tests = tmpl.query(selectByStudentIDSQL,
                new BeanPropertyRowMapper<>(TestResult.class), studentID);

        log.debug("Query for student: #{}, number of items: {}", studentID, tests.size());
        return tests;
    }

    /**
     * Deletes record from database
     *
     * @param testResultID the id
     */
    public void delete(Long testResultID) {
        log.debug("TestResultRepo delete #{}", testResultID);

        tmpl.update(deleteSQL, testResultID);
        log.debug("TestResult deleted from database #{}", testResultID);

    }
}
