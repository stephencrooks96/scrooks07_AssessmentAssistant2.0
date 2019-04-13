package com.pgault04.repositories;

import com.pgault04.entities.TutorRequests;
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
 * For the TutorRequest table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Repository
public class TutorRequestRepo {

    private static final long INSERT_CHECKER_CONSTANT = 0L;

    private static final Logger log = LogManager.getLogger(UserRepo.class);

    private final String insertSQL = "INSERT INTO TutorRequests (userID, reason, approved) values (:userID, :reason, :approved)";
    private final String updateSQL = "UPDATE TutorRequests SET reason=:reason, approved=:approved WHERE userID=:userID";
    private final String selectSQL = "SELECT * FROM TutorRequests WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "TutorRequests";
    private String deleteSQL = "DELETE FROM TutorRequests WHERE userID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts/Updates records in the tutor requests table
     *
     * @param tutorRequest object
     * @return object after insertion
     */
    public TutorRequests insert(TutorRequests tutorRequest) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(tutorRequest);
        if (tutorRequest.getTutorRequestID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new tutor request...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            tutorRequest.setTutorRequestID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New tutor request inserted: {}", tutorRequest.toString());
        } else {
            log.debug("Updating tutor request: {}", tutorRequest.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("UserRepo returning tutor request: {}", tutorRequest.toString());
        return tutorRequest;
    }

    /**
     * Select the tutor request by the user id
     *
     * @param userID the users id
     * @return the tutor request
     */
    public TutorRequests selectByUserID(Long userID) {
        log.debug("TutorRequestRepo selectByID: #{}", userID);
        String selectByUserIDSQL = selectSQL + "userID=?";
        List<TutorRequests> tutorRequests = tmpl.query(selectByUserIDSQL, new BeanPropertyRowMapper<>(TutorRequests.class), userID);

        log.debug("Query for tutor request: #{}, number of items: {}", userID, tutorRequests.size());

        if (tutorRequests.size() > 0) {
            return tutorRequests.get(0);
        }
        return null;
    }

    /**
     * Select a list of tutor requests based on approval status
     *
     * @param approved the approval status
     * @return the list of tutor requests
     */
    public List<TutorRequests> selectByApproved(Integer approved) {
        log.debug("TutorRequestRepo selectByApproved: {}", approved);
        String selectByApprovedSQL = selectSQL + "approved=?";
        List<TutorRequests> tutorRequests = tmpl.query(selectByApprovedSQL, new BeanPropertyRowMapper<>(TutorRequests.class), approved);

        log.debug("Query for tutor requests by approval status: {}, number of items: {}", approved, tutorRequests.size());

        return tutorRequests;
    }

    /**
     * @return the tutor requests
     */
    public List<TutorRequests> selectAll() {
        log.debug("TutorRequestRepo selectAll");
        List<TutorRequests> requests = tmpl.query("SELECT * FROM TutorRequests", new BeanPropertyRowMapper<>(TutorRequests.class));

        log.debug("Query for requests, number of items: {}", requests.size());
        return requests;
    }

    /**
     * Deletes a record from the database
     *
     * @param userID the user id
     */
    public void delete(Long userID) {
        log.debug("UserRepo delete #{}", userID);

        tmpl.update(deleteSQL, userID);
        log.debug("User deleted from database #{}", userID);
    }
}