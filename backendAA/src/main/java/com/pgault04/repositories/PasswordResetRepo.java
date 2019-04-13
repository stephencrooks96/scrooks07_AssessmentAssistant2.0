package com.pgault04.repositories;

import com.pgault04.entities.PasswordReset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Class to execute queries to database and receive information
 * For the PasswordReset table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Repository
public class PasswordResetRepo {

    /**
     * Logs useful info for problem resolution
     */
    private static final Logger log = LogManager.getLogger(PasswordResetRepo.class);

    private final String insertSQL = "INSERT INTO PasswordReset (userID, resetString) values (:userID, :resetString)";
    private final String updateSQL = "UPDATE PasswordReset SET resetString=:resetString WHERE userID=:userID";
    private final String selectSQL = "SELECT * FROM PasswordReset WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "PasswordReset";
    private String deleteSQL = "DELETE FROM PasswordReset WHERE userID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts or updates a password reset object in the database
     *
     * @param passwordReset the password reset object
     * @return the password reset object after insertion
     */
    public PasswordReset insert(PasswordReset passwordReset) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(passwordReset);
        if (selectByID(passwordReset.getUserID()) == null) {
            // insert
            log.debug("Inserting new passwordReset...");

            namedparamTmpl.update(insertSQL, namedParams);

            // inserted
            log.debug("New passwordReset inserted: {}", passwordReset.toString());
        } else {
            log.debug("Updating passwordReset: {}", passwordReset.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning passwordReset: {}", passwordReset);
        return passwordReset;
    }

    /**
     * Selects the password reset record based on the users id
     *
     * @param userID the users id
     * @return the password reset object for the user
     */
    public PasswordReset selectByID(Long userID) {
        log.debug("PasswordRepo selectByID: #{}", userID);
        String selectByPasswordIDSQL = selectSQL + "userID=?";
        List<PasswordReset> passwordResets = tmpl.query(selectByPasswordIDSQL,
                new BeanPropertyRowMapper<>(PasswordReset.class), userID);

        if (passwordResets != null && passwordResets.size() > 0) {
            log.debug("Query for password: #{}, number of items: {}", userID, passwordResets.size());
            return passwordResets.get(0);
        }
        return null;
    }

    /**
     * Deletes the password reset record from the table
     *
     * @param userID the primary key
     */
    public void delete(Long userID) {
        log.debug("PasswordRepo delete #{}", userID);

        tmpl.update(deleteSQL, userID);
        log.debug("PasswordReset deleted from database #{}", userID);

    }
}