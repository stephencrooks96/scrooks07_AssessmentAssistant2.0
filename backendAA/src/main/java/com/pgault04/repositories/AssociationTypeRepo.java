package com.pgault04.repositories;

import com.pgault04.entities.AssociationType;
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
 * For the AssociationType table table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Repository
public class AssociationTypeRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    /**
     * Logs useful info for problem resolution
     */
    private static final Logger log = LogManager.getLogger(AssociationTypeRepo.class);

    private final String insertSQL = "INSERT INTO AssociationType (associationType) values (:associationType)";
    private final String updateSQL = "UPDATE AssociationType SET associationType=:associationType WHERE associationTypeID=:associationTypeID";
    private final String selectSQL = "SELECT * FROM AssociationType WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "AssociationType";
    private String deleteSQL = "DELETE FROM AssociationType WHERE associationTypeID=?";

    /**
     * @return the number of rows in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Method to insert and update an association type in the database
     *
     * @param associationType the association type
     * @return the returned association type after insertion
     */
    public AssociationType insert(AssociationType associationType) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(associationType);
        if (associationType.getAssociationTypeID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new associationType...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            associationType.setAssociationTypeID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New associationType inserted: {}", associationType.toString());
        } else {
            log.debug("Updating associationType: {}", associationType.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("AssociationTypeRepo returning associationType: {}", associationType);
        return associationType;
    }

    /**
     * Selects all association types from the database
     *
     * @return the list of association types
     */
    public List<AssociationType> selectAll() {
        log.debug("AssociationTypeRepo selectAll");
        List<AssociationType> associationTypes = tmpl.query("SELECT * FROM AssociationType", new BeanPropertyRowMapper<>(AssociationType.class));

        log.debug("Query for associations, number of items: {}", associationTypes.size());
        return associationTypes;
    }

    /**
     * Selects an association type by it's id
     *
     * @param associationTypeID the association type id
     * @return the association type
     */
    public AssociationType selectByID(Long associationTypeID) {
        log.debug("AssociationTypeRepo selectByID: {}", associationTypeID);
        String selectByAssociationTypeIDSQL = selectSQL + "associationTypeID=?";
        List<AssociationType> associationTypes = tmpl.query(selectByAssociationTypeIDSQL,
                new BeanPropertyRowMapper<>(AssociationType.class), associationTypeID);

        log.debug("Query for associationType: #{}, number of items: {}", associationTypeID, associationTypes.size());
        if (associationTypes.size() > 0) {
            return associationTypes.get(0);
        } else {
            return null;
        }
    }

    /**
     * Selects an association type by its string value
     *
     * @param associationType the string value of the association type
     * @return list of association types with this name
     */
    public List<AssociationType> selectByAssociationType(String associationType) {
        log.debug("AssociationTypeRepo selectByAssociationType: {}", associationType);
        String selectByAssociationTypeSQL = selectSQL + "associationType=?";
        List<AssociationType> associationTypes = tmpl.query(selectByAssociationTypeSQL,
                new BeanPropertyRowMapper<>(AssociationType.class), associationType);

        log.debug("Query for associationType: {}, number of items: {}", associationType, associationTypes.size());
        return associationTypes;
    }

    /**
     * Method to delete an association type from the database
     *
     * @param associationTypeID the id of the item to be deleted
     */
    public void delete(Long associationTypeID) {
        log.debug("AssociationTypeRepo delete #{}", associationTypeID);

        tmpl.update(deleteSQL, associationTypeID);
        log.debug("AssociationType deleted from database #{}", associationTypeID);

    }
}