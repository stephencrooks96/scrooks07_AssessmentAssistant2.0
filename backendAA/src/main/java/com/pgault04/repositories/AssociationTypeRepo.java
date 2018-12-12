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

import com.pgault04.entities.AssociationType;

@Component
public class AssociationTypeRepo {

	private static final int INSERT_CHECKER_CONSTANT = 0;

	private static final Logger log = LoggerFactory.getLogger(AssociationTypeRepo.class);

	private String tableName = "AssociationType";

	private final String insertSQL = "INSERT INTO " + tableName
			+ " (associationType) values (:associationType)";

	private final String updateSQL = "UPDATE " + tableName
			+ " SET associationType=:associationType "
			+ "WHERE associationTypeID=:associationTypeID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE associationTypeID=?";

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
	public AssociationType insert(AssociationType associationType) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(associationType);

		if (associationType.getAssociationTypeID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new associationType...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			associationType.setAssociationTypeID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New associationType inserted: " + associationType.toString());
		} else {
			log.debug("Updating associationType: " + associationType.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning associationType: " + associationType);
		return associationType;

	}

	/**
	 * 
	 * @param associationID
	 * @return associations
	 */
	public List<AssociationType> selectAll() {
		log.debug("AssociationTypeRepo selectAll");
		List<AssociationType> associationTypes = tmpl.query("SELECT * FROM AssociationType", new BeanPropertyRowMapper<AssociationType>(AssociationType.class));

		log.debug("Query for associations, number of items: " + associationTypes.size());
		return associationTypes;
	}
	/**
	 * 
	 * @param associationTypeID
	 * @return associationTypes
	 */
	public AssociationType selectByAssociationTypeID(Long associationTypeID) {
		log.debug("AssociationTypeRepo selectByAssociationTypeID: " + associationTypeID);
		String selectByAssociationTypeIDSQL = selectSQL + "associationTypeID=?";
		List<AssociationType> associationTypes = tmpl.query(selectByAssociationTypeIDSQL,
				new BeanPropertyRowMapper<AssociationType>(AssociationType.class), associationTypeID);

		log.debug("Query for associationType: #" + associationTypeID + ", number of items: " + associationTypes.size());
		if (associationTypes.size() > 0) {
			return associationTypes.get(0);
		} else {
			return null;
		}

	}

	/**
	 * 
	 * @param email
	 * @return associationTypes
	 */
	public List<AssociationType> selectByAssociationType(String associationType) {
		log.debug("AssociationTypeRepo selectByAssociationType: " + associationType);
		String selectByAssociationTypeSQL = selectSQL + "associationType=?";
		List<AssociationType> associationTypes = tmpl.query(selectByAssociationTypeSQL,
				new BeanPropertyRowMapper<AssociationType>(AssociationType.class), associationType);

		log.debug("Query for associationType: " + associationType + ", number of items: " + associationTypes.size());
		return associationTypes;
	}

	/**
	 * 
	 * @param associationType
	 */
	public void delete(Long associationTypeID) {
		log.debug("AssociationTypeRepo delete...");

		tmpl.update(deleteSQL, associationTypeID);
		log.debug("AssociationType deleted from database.");

	}
}
