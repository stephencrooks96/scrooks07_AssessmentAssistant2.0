/**
 * 
 */
package com.pgault04.repositories;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.pgault04.entities.Module;

@Component
public class ModuleRepo {

	private static final int INSERT_CHECKER_CONSTANT = 0;

	private static final Logger log = LoggerFactory.getLogger(ModuleRepo.class);

	private String tableName = "Module";

	private final String insertSQL = "INSERT INTO " + tableName
			+ " (moduleName, moduleDescription, tutorUserID, year) values (:moduleName, :moduleDescription, :tutorUserID, :year)";

	private final String updateSQL = "UPDATE " + tableName + " SET moduleName=:moduleName, "
			+ "moduleDescription=:moduleDescription, tutorUserID=:tutorUserID, year=:year " + "WHERE moduleID=:moduleID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE moduleID=?";

	@Autowired
	JdbcTemplate tmpl;

	@Autowired
	NamedParameterJdbcTemplate namedparamTmpl;

	/**
	 * Finds the amount of records in table
	 * 
	 * @return
	 */
	public Integer rowCount() {
		return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
	}

	/**
	 * returns the object given if id less than one then it will be inserted,
	 * otherwise updated
	 * 
	 * @param price
	 * @return
	 */
	public Module insert(Module module) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(module);

		if (module.getModuleID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new module...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			module.setModuleID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New module inserted: " + module.toString());
		} else {
			log.debug("Updating module: " + module.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning module: " + module);
		return module;

	}

	/**
	 * 
	 * @param moduleID
	 * @return modules
	 */
	public Module selectByModuleID(Long moduleID) {
		log.debug("ModuleRepo selectByModuleID: " + moduleID);
		String selectByModuleIDSQL = selectSQL + "moduleID=?";
		List<Module> modules = tmpl.query(selectByModuleIDSQL, new BeanPropertyRowMapper<Module>(Module.class), moduleID);

		log.debug("Query for module: #" + moduleID + ", number of items: " + modules.size());
		if (modules.size() > 0) {
			return modules.get(0);
		} else {
			return null;
		}
		
	}

	/**
	 * 
	 * @param email
	 * @return modules
	 */
	public List<Module> selectByModuleName(String moduleName) {
		log.debug("ModuleRepo selectByModuleName: " + moduleName);
		String selectByModuleNameSQL = selectSQL + "moduleName=?";
		List<Module> modules = tmpl.query(selectByModuleNameSQL, new BeanPropertyRowMapper<Module>(Module.class), moduleName);

		log.debug("Query for module name: " + moduleName + ", number of items: " + modules.size());
		return modules;
	}

	/**
	 * 
	 * @param firstName
	 * @return modules
	 */
	public List<Module> selectByTutorID(Long tutorID) {
		log.debug("ModuleRepo selectByTutorID: " + tutorID);
		String selectByTutorIDSQL = selectSQL + "tutorUserID=?";
		List<Module> modules = tmpl.query(selectByTutorIDSQL, new BeanPropertyRowMapper<Module>(Module.class), tutorID);

		log.debug("Query for tutor id: " + tutorID + ", number of items: " + modules.size());
		return modules;
	}

	/**
	 * 
	 * @param lastName
	 * @return modules
	 */
	public List<Module> selectByYear(Integer year) {
		log.debug("ModuleRepo selectByYear: " + year);
		String selectByYearSQL = selectSQL + "year=?";
		List<Module> modules = tmpl.query(selectByYearSQL, new BeanPropertyRowMapper<Module>(Module.class), year);

		log.debug("Query for year: " + year + ", number of items: " + modules.size());
		return modules;
	}
	
	

	/**
	 * 
	 * @param module
	 */
	public void delete(Long moduleID) {
		log.debug("ModuleRepo delete...");

		tmpl.update(deleteSQL, moduleID);
		log.debug("Module deleted from database.");

	}
}

