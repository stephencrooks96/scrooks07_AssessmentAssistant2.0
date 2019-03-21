package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 * Represents the Tests table in database
 */
public class Tests {

    /**
     * Used as a checker for insertions and updates
     */
    private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

    private Long testID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

    private Long moduleID;

    private String testTitle;

    private String startDateTime;

    private String endDateTime;

    private Integer publishResults;

    private Integer scheduled;

    private Integer publishGrades;

    private Integer practice;

    /**
     * Default constructor
     */
    public Tests() {}

    /**
     * Constructor with args
     *
     * @param moduleID       the module
     * @param testTitle      the tests title
     * @param startDateTime  start time
     * @param endDateTime    end time
     * @param publishResults whether result should be available to students
     * @param scheduled      whether the test is ready for release
     * @param publishGrades  whether grades should be published to students
     * @param practice       whether the test is a practice test or not
     */
    public Tests(Long moduleID, String testTitle, String startDateTime, String endDateTime, Integer publishResults,
                 Integer scheduled, Integer publishGrades, Integer practice) {
        this.setModuleID(moduleID);
        this.setTestTitle(testTitle);
        this.setStartDateTime(startDateTime);
        this.setEndDateTime(endDateTime);
        this.setPublishResults(publishResults);
        this.setScheduled(scheduled);
        this.setPublishGrades(publishGrades);
        this.setPractice(practice);
    }

    /**
     * @return the publishGrades
     */
    public Integer getPublishGrades() {
        return publishGrades;
    }

    /**
     * @param publishGrades the publishGrades to set
     */
    public void setPublishGrades(Integer publishGrades) {
        this.publishGrades = publishGrades;
    }

    /**
     * @return the testID
     */
    public Long getTestID() {
        return testID;
    }

    /**
     * @param testID the testID to set
     */
    public void setTestID(Long testID) {
        this.testID = testID;
    }

    /**
     * @return the moduleID
     */
    public Long getModuleID() {
        return moduleID;
    }

    /**
     * @param moduleID the moduleID to set
     */
    public void setModuleID(Long moduleID) {
        this.moduleID = moduleID;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }

    /**
     * @return the startDateTime
     */
    public String getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime the startDateTime to set
     */
    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the endDateTime
     */
    public String getEndDateTime() {
        return endDateTime;
    }

    /**
     * @param endDateTime the endDateTime to set
     */
    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * @return the publishResults
     */
    public Integer getPublishResults() {
        return publishResults;
    }

    /**
     * @param publishResults the publishResults to set
     */
    public void setPublishResults(Integer publishResults) {
        this.publishResults = publishResults;
    }

    /**
     * @return the scheduled
     */
    public Integer getScheduled() {
        return scheduled;
    }

    /**
     * @param scheduled the scheduled to set
     */
    public void setScheduled(Integer scheduled) {
        this.scheduled = scheduled;
    }

    /**
     * @return - whether it is a practice test or not
     */
    public Integer getPractice() { return practice; }

    /**
     * @param practice whether it is a practice test or not
     */
    public void setPractice(Integer practice) { this.practice = practice; }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tests{");
        sb.append("testID=").append(testID);
        sb.append(", moduleID=").append(moduleID);
        sb.append(", testTitle='").append(testTitle).append('\'');
        sb.append(", startDateTime='").append(startDateTime).append('\'');
        sb.append(", endDateTime='").append(endDateTime).append('\'');
        sb.append(", publishResults=").append(publishResults);
        sb.append(", scheduled=").append(scheduled);
        sb.append(", publishGrades=").append(publishGrades);
        sb.append(", practice=").append(practice);
        sb.append('}');
        return sb.toString();
    }
}