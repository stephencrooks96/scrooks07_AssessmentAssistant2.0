/**
 * 
 */
package com.pgault04.entities;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 *
 */
public class Message {

	/**
	 * Used as a checker for insertions and updates
	 */
	private static final long AUTO_INCREMENT_INITIALIZER_CONSTANT = -1L;

	private Long messageID = AUTO_INCREMENT_INITIALIZER_CONSTANT;

	private String content;

	private Long recipientID;

	private Long senderID;

	private String messageTimestamp;

	private Integer newMessage;

	/**
	 * Default constructor
	 */
	public Message() {
	}

	/**
	 * The constructor with args
	 * @param content the messages content
	 * @param recipientID the receiver
	 * @param senderID the sender
	 * @param messageTimestamp the timestamp of the message
	 * @param newMessage whether the message has been read of not
	 */
	public Message(String content, Long recipientID, Long senderID, String messageTimestamp, int newMessage) {
		this.setContent(content);
		this.setRecipientID(recipientID);
		this.setSenderID(senderID);
		this.setMessageTimestamp(messageTimestamp);
		this.setNewMessage(newMessage);
	}

	/**
	 * @return the messageID
	 */
	public Long getMessageID() {
		return messageID;
	}

	/**
	 * @param messageID
	 *            the messageID to set
	 */
	public void setMessageID(Long messageID) {
		this.messageID = messageID;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the recipientID
	 */
	public Long getRecipientID() {
		return recipientID;
	}

	/**
	 * @param recipientID
	 *            the recipientID to set
	 */
	public void setRecipientID(Long recipientID) {
		this.recipientID = recipientID;
	}

	/**
	 * @return the senderID
	 */
	public Long getSenderID() {
		return senderID;
	}

	/**
	 * @param senderID
	 *            the senderID to set
	 */
	public void setSenderID(Long senderID) {
		this.senderID = senderID;
	}

	/**
	 * @return the timeStamp
	 */
	public String getMessageTimestamp() {
		return messageTimestamp;
	}

	/**
	 * @param messageTimestamp
	 *            the timeStamp to set
	 */
	public void setMessageTimestamp(String messageTimestamp) {
		this.messageTimestamp = messageTimestamp;
	}

	/**
	 * @return the newMessage
	 */
	public Integer getNewMessage() {
		return newMessage;
	}

	/**
	 * @param newMessage
	 *            the newMessage to set
	 */
	public void setNewMessage(Integer newMessage) {
		this.newMessage = newMessage;
	}

	/**
	 * @return the object as a string
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Message{");
		sb.append("messageID=").append(messageID);
		sb.append(", content='").append(content).append('\'');
		sb.append(", recipientID=").append(recipientID);
		sb.append(", senderID=").append(senderID);
		sb.append(", messageTimestamp='").append(messageTimestamp).append('\'');
		sb.append(", newMessage=").append(newMessage);
		sb.append('}');
		return sb.toString();
	}
}
