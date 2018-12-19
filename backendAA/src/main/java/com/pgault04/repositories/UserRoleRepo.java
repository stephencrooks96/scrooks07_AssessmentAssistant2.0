package com.pgault04.repositories;

import com.pgault04.entities.UserRole;
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
public class UserRoleRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    private static final Logger log = LogManager.getLogger(UserRoleRepo.class);

    private final String insertSQL = "INSERT INTO UserRole (role) values (:role)";
    private final String updateSQL = "UPDATE UserRole SET role=:role WHERE userRoleID=:userRoleID";
    private final String selectSQL = "SELECT * FROM UserRole WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "UserRole";
    private String deleteSQL = "DELETE FROM UserRole WHERE userRoleID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts/Updates a user role in the table
     *
     * @param userRole object
     * @return the object after insertion
     */
    public UserRole insert(UserRole userRole) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(userRole);
        if (userRole.getUserRoleID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new userRole...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            userRole.setUserRoleID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New userRole inserted: {}", userRole.toString());
        } else {
            log.debug("Updating userRole: {}", userRole.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning userRole: {}", userRole);
        return userRole;
    }

    /**
     * Selects all user roles from the table
     *
     * @return all the user roles
     */
    public List<UserRole> selectAll() {
        log.debug("UserRoleRepo selectAll");
        List<UserRole> userRoles = tmpl.query("SELECT * FROM UserRole",
                new BeanPropertyRowMapper<>(UserRole.class));

        log.debug("Query for users, number of items: {}", userRoles.size());
        return userRoles;
    }

    /**
     * Selects by user role id
     *
     * @param userRoleID the id
     * @return the user role
     */
    public UserRole selectByUserRoleID(Long userRoleID) {
        log.debug("UserRoleRepo selectByUserRoleID: #{}", userRoleID);
        String selectByUserRoleIDSQL = selectSQL + "userRoleID=?";
        List<UserRole> userRoles = tmpl.query(selectByUserRoleIDSQL,
                new BeanPropertyRowMapper<>(UserRole.class), userRoleID);

        if (userRoles != null && userRoles.size() > 0) {
            log.debug("Query for userRole: #{}" + userRoleID + ", number of items: {}", userRoleID, userRoles.size());
            return userRoles.get(0);
        }
        return null;
    }

    /**
     * Selects by the role
     *
     * @param role the role as string
     * @return the UserRole
     */
    public List<UserRole> selectByRole(String role) {
        log.debug("UserRoleRepo selectByRole: {}" + role);
        String selectByUserRoleSQL = selectSQL + "role=?";
        List<UserRole> userRoles = tmpl.query(selectByUserRoleSQL, new BeanPropertyRowMapper<>(UserRole.class),
                role);

        log.debug("Query for userRole: {}, number of items: {}", role, userRoles.size());
        return userRoles;
    }

    /**
     * Deletes a user role from the database
     *
     * @param userRoleID the id
     */
    public void delete(Long userRoleID) {
        log.debug("UserRoleRepo delete #{}", userRoleID);

        tmpl.update(deleteSQL, userRoleID);
        log.debug("UserRole deleted from database #{}", userRoleID);

    }
}
