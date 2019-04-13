package com.pgault04.repositories;

import com.pgault04.entities.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMessageRepo {

    private static final long SENDER_ID_IN_DB = 2L;

    private static final long RECIPIENT_ID_IN_DB = 1L;

    @Autowired
    MessageRepo messageRepo;

    private Message message;
    private String content, messageTimestamp;
    private Integer newMessage;

    @Before
    public void setUp() throws Exception {
        this.content = "content";
        this.messageTimestamp = "2018-10-10 10:10:10";
        this.newMessage = 0;
        message = new Message(content, RECIPIENT_ID_IN_DB, SENDER_ID_IN_DB, messageTimestamp, newMessage);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = messageRepo.rowCount();
        // Inserts one message to table
        messageRepo.insert(message);
        // Checks one value is registered as in the table
        assertTrue(messageRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        // Inserts one message to table
        Message returnedMessage = messageRepo.insert(message);
        Message messages = messageRepo.selectByID(returnedMessage.getMessageID());
        assertNotNull(messages);
        // Updates the message in the table
        returnedMessage.setNewMessage(1);
        // Inserts one message to table
        messageRepo.insert(returnedMessage);
        messages = messageRepo.selectByID(returnedMessage.getMessageID());
        assertEquals(1, messages.getNewMessage().intValue());
    }

    @Test
    public void testSelectByMessageID() {
        // Inserts one message to table
        Message returnedMessage = messageRepo.insert(message);
        Message messages = messageRepo.selectByID(returnedMessage.getMessageID());
        assertNotNull(messages);
    }

    @Test
    public void testSelectByRecipientID() {
        // Inserts one message to table
        messageRepo.insert(message);
        List<Message> messages = messageRepo.selectByRecipientID(message.getRecipientID());
        assertTrue(messages.size() > 0);
    }

    @Test
    public void testSelectBySenderID() {
        // Inserts one message to table
        messageRepo.insert(message);
        List<Message> messages = messageRepo.selectBySenderID(message.getSenderID());
        assertTrue(messages.size() > 0);
    }

    @Test
    public void testDelete() {
        // Inserts one message to table
        Message returnedMessage = messageRepo.insert(message);
        messageRepo.delete(returnedMessage.getMessageID());
        Message messages = messageRepo.selectByID(returnedMessage.getMessageID());
        assertNull(messages);
    }
}