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

    private String commencementDate;

    private String endDate;

    private Integer approved;

    /**
     * Default Constructor
     */
    public Module() {
    }

    /**
     * Constructor with args
     * @param moduleName modules name
     * @param moduleDescription the description of the module
     * @param tutorUserID the tutor
     * @param commencementDate the start date
     * @param endDate the end date
     * @param approved whether the module has been approved yet or not
     */
    public Module(String moduleName, String moduleDescription, Long tutorUserID, String commencementDate, String endDate, Integer approved) {

        this.setModuleName(moduleName);
        this.setModuleDescription(moduleDescription);
        this.setTutorUserID(tutorUserID);
        this.setCommencementDate(commencementDate);
        this.setEndDate(endDate);
        this.setApproved(approved);
    }

    public Integer getApproved() { return approved; }

    public void setApproved(Integer approved) { this.approved = approved; }

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
     * @param tutorUserID the tutor
     */
    public void setTutorUserID(Long tutorUserID) {
        this.tutorUserID = tutorUserID;
    }

    /**
     * @return start date
     */
    public String getCommencementDate() {
        return commencementDate;
    }

    /**
     * @param commencementDate start date
     */
    public void setCommencementDate(String commencementDate) {
        this.commencementDate = commencementDate;
    }

    /**
     * @return end date
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate end date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /*
     * the object as a string
     */

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Module{");
        sb.append("moduleID=").append(moduleID);
        sb.append(", moduleName='").append(moduleName).append('\'');
        sb.append(", moduleDescription='").append(moduleDescription).append('\'');
        sb.append(", tutorUserID=").append(tutorUserID);
        sb.append(", commencementDate='").append(commencementDate).append('\'');
        sb.append(", endDate='").append(endDate).append('\'');
        sb.append(", approved=").append(approved);
        sb.append('}');
        return sb.toString();
    }
}