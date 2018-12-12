/**
 * 
 */
package com.pgault04.repositories;

import static org.junit.Assert.*;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.Message;
import com.pgault04.repositories.MessageRepo;

/**
 * @author paulgault
 *
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

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.content = "content";
		this.messageTimestamp = "1000-10-10 10:10:10.111";
		this.newMessage = 0;
		message = new Message(content, RECIPIENT_ID_IN_DB, SENDER_ID_IN_DB, messageTimestamp, newMessage);

	}

	/**
	 * Test method for Row Count
	 */
	@Test
	public void testRowCount() {

		int rowCountBefore = messageRepo.rowCount().intValue();
		
		// Inserts one message to table
		messageRepo.insert(message);

		// Checks one value is registered as in the table
		assertTrue(messageRepo.rowCount().intValue() > rowCountBefore);

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.MessageRepo#insert(pgault04.entities.Message)}.
	 */
	@Test
	public void testInsert() {

		// Inserts one message to table
		Message returnedMessage = messageRepo.insert(message);

		List<Message> messages = messageRepo.selectByMessageID(returnedMessage.getMessageID());

		assertEquals(1, messages.size());

		// Updates the message in the table
		returnedMessage.setNewMessage(1);

		// Inserts one message to table
		messageRepo.insert(returnedMessage);

		messages = messageRepo.selectByMessageID(returnedMessage.getMessageID());

		assertEquals(1, messages.get(0).getNewMessage().intValue());

	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.MessageRepo#selectByMessageID(java.lang.Long)}.
	 */
	@Test
	public void testSelectByMessageID() {
		// Inserts one message to table
		Message returnedMessage = messageRepo.insert(message);

		List<Message> messages = messageRepo.selectByMessageID(returnedMessage.getMessageID());

		assertEquals(1, messages.size());
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.MessageRepo#selectByRecipientID(java.lang.String)}.
	 */
	@Test
	public void testSelectByRecipientID() {
		// Inserts one message to table
		messageRepo.insert(message);

		List<Message> messages = messageRepo.selectByRecipientID(message.getRecipientID());

		assertTrue(messages.size() > 0);
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.MessageRepo#selectBySenderID(java.lang.String)}.
	 */
	@Test
	public void testSelectBySenderID() {
		// Inserts one message to table
		messageRepo.insert(message);

		List<Message> messages = messageRepo.selectBySenderID(message.getSenderID());

		assertTrue(messages.size() > 0);
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.MessageRepo#selectByNewMessage(java.lang.String)}.
	 */
	@Test
	public void testSelectByNewMessage() {
		// Inserts one message to table
		messageRepo.insert(message);

		List<Message> messages = messageRepo.selectByNewMessage(message.getNewMessage());

		assertTrue(messages.size() > 0);
	}

	/**
	 * Test method for
	 * {@link pgault04.repositories.MessageRepo#delete(java.lang.Long)}.
	 */
	@Test
	public void testDelete() {
		// Inserts one message to table
		Message returnedMessage = messageRepo.insert(message);

		messageRepo.delete(returnedMessage.getMessageID());

		List<Message> messages = messageRepo.selectByMessageID(returnedMessage.getMessageID());

		assertEquals(0, messages.size());
	}

}
