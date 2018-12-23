package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.Message;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMessage {

	private Message messageObj;

	private Long messageID, recipientID, senderID;

	private Integer newMessage;

	private String content, messageTimestamp;

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

	@Test
	public void testMessageDefaultConstructor() {
		assertNotNull(messageObj);
	}

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

	@Test
	public void testGetSetMessageID() {
		messageObj.setMessageID(messageID);
		assertEquals(messageID, messageObj.getMessageID());
	}

	@Test
	public void testGetSetContent() {
		messageObj.setContent(content);
		assertEquals(content, messageObj.getContent());
	}

	@Test
	public void testGetSetRecipientID() {
		messageObj.setRecipientID(recipientID);
		assertEquals(recipientID, messageObj.getRecipientID());
	}

	@Test
	public void testGetSetSenderID() {
		messageObj.setSenderID(senderID);
		assertEquals(senderID, messageObj.getSenderID());
	}

	@Test
	public void testGetSetMessageTimeStamp() {
		messageObj.setMessageTimestamp(messageTimestamp);
		assertEquals(messageTimestamp, messageObj.getMessageTimestamp());
	}

	@Test
	public void testGetSetNewMessage() {
		messageObj.setNewMessage(newMessage);
		assertEquals(newMessage, messageObj.getNewMessage());
	}

	@Test
	public void testToString() {
		messageObj = new Message(content, recipientID, senderID, messageTimestamp, newMessage);
		messageObj.setMessageID(messageID);
		assertEquals("Message{messageID=1, content='content', recipientID=2, senderID=3, messageTimestamp='timeStamp', newMessage=4}", messageObj.toString());
	}
}
