package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 *
 */
public class Module {

    /**
     * Used as a checker for insertions and updates
     */
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
     * The constructor with args
     *
     * @param moduleName the module
     * @param moduleDescription description of module
     * @param tutorUserID the tutor
     * @param year the year
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

    /**
     * @return the object as a string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Module{");
        sb.append("moduleID=").append(moduleID);
        sb.append(", moduleName='").append(moduleName).append('\'');
        sb.append(", moduleDescription='").append(moduleDescription).append('\'');
        sb.append(", tutorUserID=").append(tutorUserID);
        sb.append(", year=").append(year);
        sb.append('}');
        return sb.toString();
    }
}
