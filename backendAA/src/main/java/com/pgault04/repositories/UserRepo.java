package com.pgault04.repositories;

import com.pgault04.entities.User;
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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

/**
 * Class to execute queries to database and receive information
 * For the User table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Repository
@Transactional
public class UserRepo {

    private static final long INSERT_CHECKER_CONSTANT = 0L;

    private static final Logger log = LogManager.getLogger(UserRepo.class);

    private final String insertSQL = "INSERT INTO Users (username, password, firstName, lastName, enabled, userRoleID, tutor) values (:username, :password, :firstName, :lastName, :enabled, :userRoleID, :tutor)";
    private final String updateSQL = "UPDATE Users SET username=:username, password=:password, firstName=:firstName, lastName=:lastName, enabled=:enabled, userRoleID=:userRoleID, tutor=:tutor WHERE userID=:userID";
    private final String selectSQL = "SELECT * FROM Users WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "Users";
    private String deleteSQL = "DELETE FROM Users WHERE userID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts/Updates records in the user table
     *
     * @param user object
     * @return object after insertion
     */
    public User insert(User user) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(user);
        if (user.getUserID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new user...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            user.setUserID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New user inserted: {}", user.toString());
        } else {
            log.debug("Updating user: {}", user.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("UserRepo returning user: {}", user);
        return user;
    }

    /**
     * Selects all users from the table
     *
     * @return the list of users
     */
    public List<User> selectAll() {
        log.debug("UserRepo selectAll");
        List<User> users = tmpl.query("SELECT * FROM Users", new BeanPropertyRowMapper<>(User.class));

        log.debug("Query for users, number of items: {}", users.size());
        return users;
    }

    /**
     * Select the user by the user id
     *
     * @param userID the users id
     * @return the user
     */
    public User selectByUserID(Long userID) {
        log.debug("UserRepo selectByUserID: #{}", userID);
        String selectByUserIDSQL = selectSQL + "userID=?";
        List<User> users = tmpl.query(selectByUserIDSQL, new BeanPropertyRowMapper<>(User.class), userID);

        log.debug("Query for user: #{}, number of items: {}", userID, users.size());

        if (users.size() > 0) {
            return users.get(0);
        }
        return null;

    }

    /**
     * Selects the user by their username
     *
     * @param username the username
     * @return the user
     */
    public User selectByUsername(String username) {
        log.debug("UserRepo selectByUsername: {}", username);
        String selectByUsernameSQL = selectSQL + "username=?";
        List<User> users = tmpl.query(selectByUsernameSQL, new BeanPropertyRowMapper<>(User.class), username);

        log.debug("Query for username: {}, number of items: {}", username, users.size());

        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    /**
     * Selects the user by first name
     *
     * @param firstName the users first name
     * @return the list of users with this name
     */
    public List<User> selectByFirstName(String firstName) {
        log.debug("UserRepo selectByFirstName: {}", firstName);
        String selectByFirstNameSQL = selectSQL + "firstName=?";
        List<User> users = tmpl.query(selectByFirstNameSQL, new BeanPropertyRowMapper<>(User.class), firstName);

        log.debug("Query for first name: {}, number of items: {}", firstName, users.size());
        return users;
    }

    /**
     * Selects the user by last name
     *
     * @param lastName the users last name
     * @return the users with this name
     */
    public List<User> selectByLastName(String lastName) {
        log.debug("UserRepo selectByLastName: {}", lastName);
        String selectByLastNameSQL = selectSQL + "lastName=?";
        List<User> users = tmpl.query(selectByLastNameSQL, new BeanPropertyRowMapper<>(User.class), lastName);

        log.debug("Query for last name: {}, number of items: {}", lastName, users.size());
        return users;
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