package com.pgault04.repositories;

import com.pgault04.entities.ModuleAssociation;
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
 * For the ModuleAssociation table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Repository
public class ModuleAssociationRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    /**
     * Logs useful info for problem resolution
     */
    private static final Logger log = LogManager.getLogger(ModuleAssociationRepo.class);

    private final String insertSQL = "INSERT INTO ModuleAssociation (moduleID, userID, associationType) values (:moduleID, :userID, :associationType)";
    private final String updateSQL = "UPDATE ModuleAssociation SET moduleID=:moduleID, userID=:userID, associationType=:associationType WHERE associationID=:associationID";
    private final String selectSQL = "SELECT * FROM ModuleAssociation WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "ModuleAssociation";
    private String deleteSQL = "DELETE FROM ModuleAssociation WHERE associationID=?";

    /**
     * @return the number of records in the tale
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Method to insert or update module associations in to the table
     *
     * @param moduleAssociation the module association
     * @return the module association after insertion/update
     */
    public ModuleAssociation insert(ModuleAssociation moduleAssociation) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(moduleAssociation);
        if (moduleAssociation.getAssociationID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new moduleAssociation...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            moduleAssociation.setAssociationID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New moduleAssociation inserted: {}", moduleAssociation.toString());
        } else {
            log.debug("Updating moduleAssociation: {}", moduleAssociation.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("ModuleRepo returning moduleAssociation: {}", moduleAssociation);
        return moduleAssociation;
    }

    /**
     * Selects all the module association based on its id
     *
     * @param associationID the association id
     * @return the association
     */
    public ModuleAssociation selectByID(Long associationID) {
        log.debug("ModuleAssociationRepo selectByModuleAssociationID: {}", associationID);
        String selectByAssociationIDSQL = selectSQL + "associationID=?";
        List<ModuleAssociation> moduleAssociations = tmpl.query(selectByAssociationIDSQL,
                new BeanPropertyRowMapper<>(ModuleAssociation.class), associationID);

        if (moduleAssociations != null && moduleAssociations.size() > 0) {
            log.debug("Query for moduleAssociation: #{}, number of items: {}", associationID, moduleAssociations.size());
            return moduleAssociations.get(0);
        }
        return null;
    }

    /**
     * Selects the module associations for a given module
     *
     * @param moduleID the module's id
     * @return a list of the module associations
     */
    public List<ModuleAssociation> selectByModuleID(Long moduleID) {
        log.debug("ModuleAssociationRepo selectByID: #{}", moduleID);
        String selectByModuleIDSQL = selectSQL + "moduleID=?";
        List<ModuleAssociation> moduleAssociations = tmpl.query(selectByModuleIDSQL,
                new BeanPropertyRowMapper<>(ModuleAssociation.class), moduleID);

        log.debug("Query for moduleID : #{}, number of items: {}", moduleID, moduleAssociations.size());
        return moduleAssociations;
    }

    /**
     * Selects a list of module associations for a given user
     *
     * @param userID the user's id
     * @return a list of the users module associations
     */
    public List<ModuleAssociation> selectByUserID(Long userID) {
        log.debug("ModuleAssociationRepo selectByID: #{}", userID);
        String selectByUserIDSQL = selectSQL + "userID=?";
        List<ModuleAssociation> moduleAssociations = tmpl.query(selectByUserIDSQL,
                new BeanPropertyRowMapper<>(ModuleAssociation.class), userID);

        log.debug("Query for user id: #{}, number of items: {}", userID, moduleAssociations.size());
        return moduleAssociations;
    }

    /**
     * Selects all the module associations based on which type of association they are
     *
     * @param associationType the associations types id
     * @return the list of module associations
     */
    public List<ModuleAssociation> selectByAssociationType(Long associationType) {
        log.debug("ModuleAssociationRepo selectByAssociationType: #{}", associationType);
        String selectByAssociationTypeSQL = selectSQL + "associationType=?";
        List<ModuleAssociation> moduleAssociations = tmpl.query(selectByAssociationTypeSQL,
                new BeanPropertyRowMapper<>(ModuleAssociation.class), associationType);

        log.debug("Query for year: #{}, number of items: {}", associationType, moduleAssociations.size());
        return moduleAssociations;
    }

    /**
     * Deletes a module associations from the database
     *
     * @param associationID the associations id
     */
    public void delete(Long associationID) {
        log.debug("ModuleAssociationRepo delete #{}", associationID);

        tmpl.update(deleteSQL, associationID);
        log.debug("ModuleAssociation deleted from database #{}", associationID);

    }
}