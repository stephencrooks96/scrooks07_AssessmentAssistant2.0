package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
public class ModuleAssociation {


	/**
	 * Used as a checker for insertions and updates
	 */
	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long associationID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

	private Long moduleID;

	private Long userID;

	private Long associationType;

	/**
	 * Default constructor
	 */
	public ModuleAssociation() {
	}

	/**
	 * Constructor with args
	 * @param moduleID the module
	 * @param userID the user
	 * @param associationType the users association to the module
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

	/**
	 * @return the object as a string
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ModuleAssociation{");
		sb.append("associationID=").append(associationID);
		sb.append(", moduleID=").append(moduleID);
		sb.append(", userID=").append(userID);
		sb.append(", associationType=").append(associationType);
		sb.append('}');
		return sb.toString();
	}
}
