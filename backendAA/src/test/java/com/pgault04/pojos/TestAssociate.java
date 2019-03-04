package com.pgault04.pojos;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAssociate {

    private Associate associate;
    private String associateType;
    private String username;
    private String firstName;
    private String lastName;

    @Before
    public void setUp() throws Exception {
        this.associate = new Associate();
        this.associateType = "type";
        this.username = "username";
        this.firstName = "firstname";
        this.lastName = "lastname";
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(associate);
    }

    @Test
    public void testConstructorWithArgs() {
        associate = null;
        associate = new Associate(associateType, username, firstName, lastName);

        assertNotNull(associate);
        assertEquals(associateType, associate.getAssociateType());
        assertEquals(username, associate.getUsername());
        assertEquals(firstName, associate.getFirstName());
        assertEquals(lastName, associate.getLastName());
    }

    @Test
    public void testToString() {
        associate = new Associate(associateType, username, firstName, lastName);
        assertEquals("Associate{associateType='type', username='username', firstName='firstname', lastName='lastname'}", associate.toString());
    }
}
