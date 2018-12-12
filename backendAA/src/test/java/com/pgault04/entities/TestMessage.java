/**
 * 
 */
package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.Message;

/**
 * @author paulgault
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMessage {

	private Message messageObj;

	private Long messageID, recipientID, senderID;

	private Integer newMessage;

	private String content, messageTimestamp;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		messageObj = new Message();

		messageID = 1L;
		recipientID = 2L;
		senderID = 3L;
		newMessage = 4;

		content = "content";
		messageTimestamp = "timeStamp";
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Message#Message()}.
	 */
	@Test
	public void testMessageDefaultConstructor() {
		assertNotNull(messageObj);
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Message#Message(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, int)}.
	 */
	@Test
	public void testMessageIntegerStringIntegerIntegerStringInt() {
		messageObj = null;
		messageObj = new Message(content, recipientID, senderID, messageTimestamp, newMessage);

		assertNotNull(messageObj);
		assertEquals(content, messageObj.getContent());
		assertEquals(recipientID, messageObj.getRecipientID());
		assertEquals(senderID, messageObj.getSenderID());
		assertEquals(messageTimestamp, messageObj.getMessageTimestamp());
		assertEquals(newMessage, messageObj.getNewMessage());

	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Message#getMessageID()}.
	 */
	@Test
	public void testGetSetMessageID() {
		messageObj.setMessageID(messageID);
		assertEquals(messageID, messageObj.getMessageID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Message#getContent()}.
	 */
	@Test
	public void testGetSetContent() {
		messageObj.setContent(content);
		assertEquals(content, messageObj.getContent());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Message#getRecipientID()}.
	 */
	@Test
	public void testGetSetRecipientID() {
		messageObj.setRecipientID(recipientID);
		assertEquals(recipientID, messageObj.getRecipientID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Message#getSenderID()}.
	 */
	@Test
	public void testGetSetSenderID() {
		messageObj.setSenderID(senderID);
		assertEquals(senderID, messageObj.getSenderID());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Message#getMessageTimeStamp()}.
	 */
	@Test
	public void testGetSetMessageTimeStamp() {
		messageObj.setMessageTimestamp(messageTimestamp);
		assertEquals(messageTimestamp, messageObj.getMessageTimestamp());
	}

	/**
	 * Test method for
	 * {@link pgault04.entities.Message#getNewMessage()}.
	 */
	@Test
	public void testGetSetNewMessage() {
		messageObj.setNewMessage(newMessage);
		assertEquals(newMessage, messageObj.getNewMessage());
	}

}
