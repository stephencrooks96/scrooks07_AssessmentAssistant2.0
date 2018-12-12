/**
 * 
 */
package com.pgault04.entities;

/**
 * @author paulgault
 *
 */
public class AssociationType {

	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long associationTypeID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

	private String associationType;

	/**
	 * 
	 */
	public AssociationType() {
	}

	/**
	 * @param associationTypeID
	 * @param associationType
	 * @param description
	 */
	public AssociationType(String associationType) {
		this.setAssociationType(associationType);
	}

	/**
	 * @return the associationTypeID
	 */
	public Long getAssociationTypeID() {
		return associationTypeID;
	}

	/**
	 * @param associationTypeID
	 *            the associationTypeID to set
	 */
	public void setAssociationTypeID(Long associationTypeID) {
		this.associationTypeID = associationTypeID;
	}

	/**
	 * @return the associationType
	 */
	public String getAssociationType() {
		return associationType;
	}

	/**
	 * @param associationType
	 *            the associationType to set
	 */
	public void setAssociationType(String associationType) {
		this.associationType = associationType;
	}

	

}
