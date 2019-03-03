package com.pgault04.pojos;

public class Associate {

    private String associateType;
    private String username;
    private String firstName;
    private String lastName;

    public Associate() {
    }

    public Associate(String associateType, String username, String firstName, String lastName) {
        this.setAssociateType(associateType);
        this.setUsername(username);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public String getAssociateType() {
        return associateType;
    }

    public void setAssociateType(String associateType) {
        this.associateType = associateType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }
}
