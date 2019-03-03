package com.pgault04.pojos;

public class AssociationPojo {

    private String email;
    private String associationType;

    public AssociationPojo() {}

    public AssociationPojo(String email, String associationType) {
        this.email = email;
        this.associationType = associationType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAssociationType() {
        return associationType;
    }

    public void setAssociationType(String associationType) {
        this.associationType = associationType;
    }
}
