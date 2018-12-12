/**
 * 
 */
package com.pgault04.repositories;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.pgault04.entities.Message;

@Component
public class MessageRepo {

	private static final int INSERT_CHECKER_CONSTANT = 0;

	private static final Logger log = LoggerFactory.getLogger(MessageRepo.class);

	private String tableName = "Message";

	private final String insertSQL = "INSERT INTO " + tableName
			+ " (content, recipientID, senderID, messageTimestamp, newMessage) values (:content, :recipientID, :senderID, :messageTimestamp, :newMessage)";

	private final String updateSQL = "UPDATE " + tableName + " SET content=:content, "
			+ "recipientID=:recipientID, senderID=:senderID, messageTimestamp=:messageTimestamp, newMessage=:newMessage "
			+ "WHERE messageID=:messageID";

	private final String selectSQL = "SELECT * FROM " + tableName + " WHERE ";

	private String deleteSQL = "DELETE FROM " + tableName + " WHERE messageID=?";

	@Autowired
	JdbcTemplate tmpl;

	@Autowired
	NamedParameterJdbcTemplate namedparamTmpl;

	/**
	 * Finds the amount of records in table
	 * 
	 * @return
	 */
	public Integer rowCount() {
		return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
	}

	/**
	 * returns the object given if id less than one then it will be inserted,
	 * otherwise updated
	 * 
	 * @param price
	 * @return
	 */
	public Message insert(Message message) {

		BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(message);

		if (message.getMessageID() < INSERT_CHECKER_CONSTANT) {

			// insert
			log.debug("Inserting new message...");

			KeyHolder keyHolder = new GeneratedKeyHolder();

			namedparamTmpl.update(insertSQL, namedParams, keyHolder);
			message.setMessageID(keyHolder.getKey().longValue());

			// inserted
			log.debug("New message inserted: " + message.toString());
		} else {
			log.debug("Updating message: " + message.toString());
			namedparamTmpl.update(updateSQL, namedParams);
		}
		log.info("JdbcRepo returning message: " + message);
		return message;

	}

	/**
	 * 
	 * @param messageID
	 * @return messages
	 */
	public List<Message> selectByMessageID(Long messageID) {
		log.debug("MessageRepo selectByMessageID: " + messageID);
		String selectByMessageIDSQL = selectSQL + "messageID=?";
		List<Message> messages = tmpl.query(selectByMessageIDSQL, new BeanPropertyRowMapper<Message>(Message.class),
				messageID);

		log.debug("Query for message: #" + messageID + ", number of items: " + messages.size());
		return messages;
	}

	/**
	 * 
	 * @param recipientID
	 * @return messages
	 */
	public List<Message> selectByRecipientID(Long recipientID) {
		log.debug("MessageRepo selectByRecipientID: " + recipientID);
		String selectByRecipientIDSQL = selectSQL + "recipientID=?";
		List<Message> messages = tmpl.query(selectByRecipientIDSQL, new BeanPropertyRowMapper<Message>(Message.class),
				recipientID);

		log.debug("Query for recipientID: " + recipientID + ", number of items: " + messages.size());
		return messages;
	}

	/**
	 * 
	 * @param senderID
	 * @return messages
	 */
	public List<Message> selectBySenderID(Long senderID) {
		log.debug("MessageRepo selectBySenderID: " + senderID);
		String selectBySenderIDSQL = selectSQL + "senderID=?";
		List<Message> messages = tmpl.query(selectBySenderIDSQL, new BeanPropertyRowMapper<Message>(Message.class),
				senderID);

		log.debug("Query for senderID: " + senderID + ", number of items: " + messages.size());
		return messages;
	}

	/**
	 * 
	 * @param newMessage
	 * @return messages
	 */
	public List<Message> selectByNewMessage(Integer newMessage) {
		log.debug("MessageRepo selectByNewMessage: " + newMessage);
		String selectByNewMessageSQL = selectSQL + "newMessage=?";
		List<Message> messages = tmpl.query(selectByNewMessageSQL, new BeanPropertyRowMapper<Message>(Message.class),
				newMessage);

		log.debug("Query for first name: " + newMessage + ", number of items: " + messages.size());
		return messages;
	}

	/**
	 * 
	 * @param message
	 */
	public void delete(Long messageID) {
		log.debug("MessageRepo delete...");

		tmpl.update(deleteSQL, messageID);
		log.debug("Message deleted from database.");

	}
}
