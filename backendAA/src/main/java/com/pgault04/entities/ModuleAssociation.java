/**
 * 
 */
package com.pgault04.entities;

/**
 * @author paulgault
 *
 */
public class ModuleAssociation {

	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long associationID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

	private Long moduleID;

	private Long userID;

	private Long associationType;

	/**
	 * 
	 */
	public ModuleAssociation() {
	}

	/**
	 * @param associationID
	 * @param moduleID
	 * @param userID
	 * @param associationType
	 */
	public ModuleAssociation(Long moduleID, Long userID, Long associationType) {
		this.setModuleID(moduleID);
		this.setUserID(userID);
		this.setAssociationType(associationType);
	}

	/**
	 * @return the associationID
	 */
	public Long getAssociationID() {
		return associationID;
	}

	/**
	 * @param associationID
	 *            the associationID to set
	 */
	public void setAssociationID(Long associationID) {
		this.associationID = associationID;
	}

	/**
	 * @return the moduleID
	 */
	public Long getModuleID() {
		return moduleID;
	}

	/**
	 * @param moduleID
	 *            the moduleID to set
	 */
	public void setModuleID(Long moduleID) {
		this.moduleID = moduleID;
	}

	/**
	 * @return the userID
	 */
	public Long getUserID() {
		return userID;
	}

	/**
	 * @param userID
	 *            the userID to set
	 */
	public void setUserID(Long userID) {
		this.userID = userID;
	}

	/**
	 * @return the associationType
	 */
	public Long getAssociationType() {
		return associationType;
	}

	/**
	 * @param associationType
	 *            the associationType to set
	 */
	public void setAssociationType(Long associationType) {
		this.associationType = associationType;
	}

}
