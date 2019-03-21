package com.pgault04.repositories;

import com.pgault04.entities.UserSession;
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

/**
 * Class to execute queries to database and receive information
 * For the UserSessions table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Repository
public class UserSessionsRepo {

    private static final Logger log = LogManager.getLogger(UserRepo.class);

    private final String insertSQL = "INSERT INTO UserSessions (username, token, lastActive) values (:username, :token, :lastActive)";
    private final String updateSQL = "UPDATE UserSessions SET token=:token, lastActive=:lastActive WHERE username=:username";
    private final String selectSQL = "SELECT * FROM UserSessions WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "UserSessions";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts/Updates records in the user table
     *
     * @param userSession object
     * @return object after insertion
     */
    public UserSession insert(UserSession userSession) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(userSession);
        if (selectByUsername(userSession.getUsername()) == null) {
            // insert
            log.debug("Inserting new user session...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);

            // inserted
            log.debug("New user session inserted: {}", userSession.toString());
        } else {
            log.debug("Updating user session: {}", userSession.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("UserSessionRepo returning userSession: {}", userSession);
        return userSession;
    }

    /**
     * Selects the user by their username
     *
     * @param username the username
     * @return the user
     */
    public UserSession selectByUsername(String username) {
        log.debug("UserSessionRepo selectByUsername: {}", username);
        String selectByUsernameSQL = selectSQL + "username=?";
        List<UserSession> users = tmpl.query(selectByUsernameSQL, new BeanPropertyRowMapper<>(UserSession.class), username);

        log.debug("Query for username: {}, number of items: {}", username, users.size());

        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }
}