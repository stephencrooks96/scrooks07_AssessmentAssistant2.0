package com.pgault04.repositories;

import com.pgault04.entities.Tests;
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
 * For the Tests table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Repository
public class TestsRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    private static final Logger log = LogManager.getLogger(TestsRepo.class);

    private final String insertSQL = "INSERT INTO Tests (moduleID, testTitle, startDateTime, endDateTime, publishResults, scheduled, publishGrades, practice) values (:moduleID, :testTitle, :startDateTime, :endDateTime, :publishResults, :scheduled, :publishGrades, :practice)";
    private final String updateSQL = "UPDATE Tests SET moduleID=:moduleID, testTitle=:testTitle, startDateTime=:startDateTime, endDateTime=:endDateTime, publishResults=:publishResults, scheduled=:scheduled, publishGrades=:publishGrades, practice=:practice WHERE testID=:testID";
    private final String selectSQL = "SELECT * FROM Tests WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "Tests";
    private String deleteSQL = "DELETE FROM Tests WHERE testID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Insert/Update record in tests table
     *
     * @param test object
     * @return object after insertion
     */
    public Tests insert(Tests test) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(test);
        if (test.getTestID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new test...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            test.setTestID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New tests inserted: {}", test.toString());
        } else {
            log.debug("Updating test: {}", test.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning test: {}", test);
        return test;
    }

    /**
     * Selects test by its id
     *
     * @param testID the id
     * @return the test object
     */
    public Tests selectByTestID(Long testID) {
        log.debug("TestsRepo selectByTestID: #{}", testID);
        String selectByTestIDSQL = selectSQL + "testID=?";
        List<Tests> tests = tmpl.query(selectByTestIDSQL, new BeanPropertyRowMapper<>(Tests.class), testID);

        log.debug("Query for test: #{}, number of items: {}", testID, tests.size());

        if (tests.size() > 0) {
            return tests.get(0);
        }
        return null;
    }

    /**
     * Selects tests by module id
     *
     * @param moduleID the module id
     * @return list of tests belonging to the module
     */
    public List<Tests> selectByModuleID(Long moduleID) {
        log.debug("TestsRepo selectByID: #{}", moduleID);
        String selectByModuleIDSQL = selectSQL + "moduleID=?";
        List<Tests> tests = tmpl.query(selectByModuleIDSQL, new BeanPropertyRowMapper<>(Tests.class), moduleID);

        log.debug("Query for tests - module id: #{}, number of items: {}", moduleID, tests.size());
        return tests;
    }

    /**
     * Selects tests from database based on their start time
     *
     * @param startDateTime the start time
     * @return list of tests
     */
    public List<Tests> selectByStartDateTime(String startDateTime) {
        log.debug("TestsRepo selectByStartDateTime: #{}", startDateTime);
        String selectByStartDateTimeSQL = selectSQL + "startDateTime=?";
        List<Tests> tests = tmpl.query(selectByStartDateTimeSQL, new BeanPropertyRowMapper<>(Tests.class),
                startDateTime);

        log.debug("Query for start date time: {}, number of items: {}", startDateTime, tests.size());
        return tests;
    }

    /**
     * Select from the database based on the end time
     *
     * @param endDateTime end time
     * @return list of tests
     */
    public List<Tests> selectByEndDateTime(String endDateTime) {
        log.debug("TestsRepo selectByEndDateTime: {}", endDateTime);
        String selectByEndDateTimeSQL = selectSQL + "endDateTime=?";
        List<Tests> tests = tmpl.query(selectByEndDateTimeSQL, new BeanPropertyRowMapper<>(Tests.class),
                endDateTime);

        log.debug("Query for end date time: {}, number of items: {}", endDateTime, tests.size());
        return tests;
    }

    /**
     * Deletes a test from the database
     *
     * @param testID the test id
     */
    public void delete(Long testID) {
        log.debug("TestsRepo delete #{}", testID);

        tmpl.update(deleteSQL, testID);
        log.debug("Tests deleted from database #{}", testID);

    }
}