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

import com.pgault04.entities.ModuleAssociation;

@Component
public class ModuleAssociationRepo {

	private static final int INSERT_CHECKER_CONSTANT = 0;

	private static final Logger log = LoggerFactory.getLogger(ModuleAssociationRepo.class);

	private String tableName = "ModuleAssociation";

	private final String insertSQL = "INSERT INTO " + tableName
			+ " (moduleID, userID, associationType) values (:moduleID, :userID, :associationType)";

	private final String updateSQL = "UPDATE " + tableName
			+ " SET moduleID=:moduleID, userID=:userID, associationType=:associationType "
			+ "WHERE associationID=:associationID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE associationID=?";

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
	public ModuleAssociation insert(ModuleAssociation moduleAssociation) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(moduleAssociation);

		if (moduleAssociation.getAssociationID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new moduleAssociation...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			moduleAssociation.setAssociationID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New moduleAssociation inserted: " + moduleAssociation.toString());
		} else {
			log.debug("Updating moduleAssociation: " + moduleAssociation.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning moduleAssociation: " + moduleAssociation);
		return moduleAssociation;

	}

	/**
	 * 
	 * @param moduleAssociationID
	 * @return moduleAssociations
	 */
	public List<ModuleAssociation> selectByAssociationID(Long associationID) {
		log.debug("ModuleAssociationRepo selectByModuleAssociationID: " + associationID);
		String selectByAssociationIDSQL = selectSQL + "associationID=?";
		List<ModuleAssociation> moduleAssociations = tmpl.query(selectByAssociationIDSQL,
				new BeanPropertyRowMapper<ModuleAssociation>(ModuleAssociation.class), associationID);

		log.debug("Query for moduleAssociation: #" + associationID + ", number of items: " + moduleAssociations.size());
		return moduleAssociations;
	}

	/**
	 * 
	 * @param email
	 * @return moduleAssociations
	 */
	public List<ModuleAssociation> selectByModuleID(Long moduleID) {
		log.debug("ModuleAssociationRepo selectByModuleID: " + moduleID);
		String selectByModuleIDSQL = selectSQL + "moduleID=?";
		List<ModuleAssociation> moduleAssociations = tmpl.query(selectByModuleIDSQL,
				new BeanPropertyRowMapper<ModuleAssociation>(ModuleAssociation.class), moduleID);

		log.debug("Query for moduleID : #" + moduleID + ", number of items: " + moduleAssociations.size());
		return moduleAssociations;
	}

	/**
	 * 
	 * @param firstName
	 * @return moduleAssociations
	 */
	public List<ModuleAssociation> selectByUserID(Long userID) {
		log.debug("ModuleAssociationRepo selectByUserID: " + userID);
		String selectByUserIDSQL = selectSQL + "userID=?";
		List<ModuleAssociation> moduleAssociations = tmpl.query(selectByUserIDSQL,
				new BeanPropertyRowMapper<ModuleAssociation>(ModuleAssociation.class), userID);

		log.debug("Query for user id: " + userID + ", number of items: " + moduleAssociations.size());
		return moduleAssociations;
	}

	/**
	 * 
	 * @param lastName
	 * @return moduleAssociations
	 */
	public List<ModuleAssociation> selectByAssociationType(Long associationType) {
		log.debug("ModuleAssociationRepo selectByAssociationType: " + associationType);
		String selectByAssociationTypeSQL = selectSQL + "associationType=?";
		List<ModuleAssociation> moduleAssociations = tmpl.query(selectByAssociationTypeSQL,
				new BeanPropertyRowMapper<ModuleAssociation>(ModuleAssociation.class), associationType);

		log.debug("Query for year: " + associationType + ", number of items: " + moduleAssociations.size());
		return moduleAssociations;
	}

	/**
	 * 
	 * @param moduleAssociation
	 */
	public void delete(Long associationID) {
		log.debug("ModuleAssociationRepo delete...");

		tmpl.update(deleteSQL, associationID);
		log.debug("ModuleAssociation deleted from database.");

	}
}
