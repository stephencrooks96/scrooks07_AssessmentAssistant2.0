package com.pgault04.pojos;

/**
 * @author Paul Gault - 40126005
 * @since Jan 2019
 * Pojo to allow collection of associate info to be sent to/from front end
 */
public class Associate {

    private String associateType;
    private String username;
    private String firstName;
    private String lastName;

    /**
     * Default constructor
     */
    public Associate() {}

    /**
     * Constructor with arguments
     *
     * @param associateType - the type of association
     * @param username      - the user
     * @param firstName     - their first name
     * @param lastName      - their last name
     */
    public Associate(String associateType, String username, String firstName, String lastName) {
        this.setAssociateType(associateType);
        this.setUsername(username);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    /**
     * @return gets the association type
     */
    public String getAssociateType() { return associateType; }

    /**
     * @param associateType sets the association type
     */
    public void setAssociateType(String associateType) { this.associateType = associateType; }

    /**
     * @return gets the username of the associate
     */
    public String getUsername() { return username; }

    /**
     * @param username - sets the username for the associate
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * @return gets the associates first name
     */
    public String getFirstName() { return firstName; }

    /**
     * @param firstName sets the associates first name
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /**
     * @return gets the associates last name
     */
    public String getLastName() { return lastName; }

    /**
     * @param lastName sets the associates last name
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Associate{");
        sb.append("associateType='").append(associateType).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}