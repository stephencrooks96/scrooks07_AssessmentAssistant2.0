/**
 * 
 */
package com.pgault04.entities;

/**
 * @author paulgault
 *
 */
public class Module {

	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long moduleID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

	private String moduleName;

	private String moduleDescription;

	private Long tutorUserID;

	private Integer year;

	/**
	 * Default Constructor
	 */
	public Module() {
	}

	/**
	 * @param moduleID
	 * @param moduleName
	 * @param moduleDescription
	 * @param tutorUserID
	 * @param year
	 */
	public Module(String moduleName, String moduleDescription, Long tutorUserID, Integer year) {
		
		this.setModuleName(moduleName);
		this.setModuleDescription(moduleDescription);
		this.setTutorUserID(tutorUserID);
		this.setYear(year);
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
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName
	 *            the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the moduleDescription
	 */
	public String getModuleDescription() {
		return moduleDescription;
	}

	/**
	 * @param moduleDescription
	 *            the moduleDescription to set
	 */
	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
	}

	/**
	 * @return the tutorUserID
	 */
	public Long getTutorUserID() {
		return tutorUserID;
	}

	/**
	 * @param tutorUserID
	 *            the tutorUserID to set
	 */
	public void setTutorUserID(Long tutorUserID) {
		this.tutorUserID = tutorUserID;
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

}
