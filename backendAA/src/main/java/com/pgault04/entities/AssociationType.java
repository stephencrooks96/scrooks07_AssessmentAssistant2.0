package com.pgault04.entities;

/**
 * Class to represent association types
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
public class AssociationType {

    /**
     * Tutor association type constant
     */
    public static final long TUTOR = 1L;

    /**
     * Student association type constant
     */
    public static final long STUDENT = 2L;

    /**
     * Teaching Assistant association type constant
     */
    public static final long TEACHING_ASSISTANT = 3L;

    /**
     * Used as a checker for insertions and updates
     */
    private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

    private Long associationTypeID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

    private String associationType;

    /**
     * Default constructor
     */
    public AssociationType() {
    }

    /**
     * Constructor with args
     *
     * @param associationType the association type
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
     * @param associationTypeID the associationTypeID to set
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
     * @param associationType the associationType to set
     */
    public void setAssociationType(String associationType) {
        this.associationType = associationType;
    }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AssociationType{");
        sb.append("associationTypeID=").append(associationTypeID);
        sb.append(", associationType='").append(associationType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}