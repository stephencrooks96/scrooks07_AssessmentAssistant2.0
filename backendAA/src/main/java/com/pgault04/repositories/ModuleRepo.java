/**
 *
 */
package com.pgault04.repositories;

import com.pgault04.entities.Module;
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
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Component
public class ModuleRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    private static final Logger log = LogManager.getLogger(ModuleRepo.class);

    private final String insertSQL = "INSERT INTO Modules (moduleName, moduleDescription, tutorUserID, commencementDate, endDate, approved) values (:moduleName, :moduleDescription, :tutorUserID, :commencementDate, :endDate, :approved)";
    private final String updateSQL = "UPDATE Modules SET moduleName=:moduleName, moduleDescription=:moduleDescription, tutorUserID=:tutorUserID, commencementDate=:commencementDate, endDate=:endDate, approved=:approved WHERE moduleID=:moduleID";
    private final String selectSQL = "SELECT * FROM Modules WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "Modules";
    private String deleteSQL = "DELETE FROM Modules WHERE moduleID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts or updates records in to the module table
     * @param module the module
     * @return the module after insertion/update
     */
    public Module insert(Module module) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(module);
        if (module.getModuleID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new module...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            module.setModuleID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New module inserted: {}", module.toString());
        } else {
            log.debug("Updating module: {}", module.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("ModuleRepo returning module: {}", module);
        return module;
    }

    /**
     * Selects a module from the database based on its id
     * @param moduleID the module's id
     * @return the module
     */
    public Module selectByModuleID(Long moduleID) {
        log.debug("ModuleRepo selectByModuleID: {}", moduleID);
        String selectByModuleIDSQL = selectSQL + "moduleID=?";
        List<Module> modules = tmpl.query(selectByModuleIDSQL, new BeanPropertyRowMapper<>(Module.class), moduleID);

        log.debug("Query for module: #{}, number of items: {}", moduleID, modules.size());
        if (modules.size() > 0) {
            return modules.get(0);
        }
        return null;
    }

    public List<Module> selectByApproved(Integer approved) {
        log.debug("ModuleRepo selectByApproved: {}", approved);
        String selectByApprovedSQL = selectSQL + "approved=?";
        List<Module> modules = tmpl.query(selectByApprovedSQL, new BeanPropertyRowMapper<>(Module.class), approved);

        log.debug("Query for module by approval status: {}, number of items: {}", approved, modules.size());

        return modules;
    }

    /**
     * Selects a list of modules based on a name
     * @param moduleName the modules name
     * @return the list of modules
     */
    public List<Module> selectByModuleName(String moduleName) {
        log.debug("ModuleRepo selectByModuleName: #{}", moduleName);
        String selectByModuleNameSQL = selectSQL + "moduleName=?";
        List<Module> modules = tmpl.query(selectByModuleNameSQL, new BeanPropertyRowMapper<>(Module.class), moduleName);

        log.debug("Query for module name: #{}, number of items: {}", moduleName, modules.size());
        return modules;
    }

    /**
     * Selects a list of modules based on the tutor
     * @param tutorID the tutor's id
     * @return the list of modules
     */
    public List<Module> selectByTutorID(Long tutorID) {
        log.debug("ModuleRepo selectByTutorID: #{}", tutorID);
        String selectByTutorIDSQL = selectSQL + "tutorUserID=?";
        List<Module> modules = tmpl.query(selectByTutorIDSQL, new BeanPropertyRowMapper<>(Module.class), tutorID);

        log.debug("Query for tutor id: #{}, number of items: {}" + modules.size());
        return modules;
    }

    /**
     * Deletes a record from the module table
     * @param moduleID the module id
     */
    public void delete(Long moduleID) {
        log.debug("ModuleRepo delete #{}", moduleID);

        tmpl.update(deleteSQL, moduleID);
        log.debug("Module deleted from database #{}", moduleID);

    }
}

