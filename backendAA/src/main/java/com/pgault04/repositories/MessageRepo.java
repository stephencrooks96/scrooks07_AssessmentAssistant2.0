package com.pgault04.repositories;

import com.pgault04.entities.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Class to execute queries to database and receive information
 * For the Message table table
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
@Component
public class MessageRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    /**
     * Logs useful info for problem resolution
     */
    private static final Logger log = LogManager.getLogger(MessageRepo.class);

    private final String insertSQL = "INSERT INTO Message (content, recipientID, senderID, messageTimestamp, newMessage) values (:content, :recipientID, :senderID, :messageTimestamp, :newMessage)";
    private final String updateSQL = "UPDATE Message SET content=:content, recipientID=:recipientID, senderID=:senderID, messageTimestamp=:messageTimestamp, newMessage=:newMessage WHERE messageID=:messageID";
    private final String selectSQL = "SELECT * FROM Message WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "Message";
    private String deleteSQL = "DELETE FROM Message WHERE messageID=?";

    /**
     * @return the number of rows in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Method to insert and update records in the Message table
     *
     * @param message the message to update/insert
     * @return the returned message after insertion
     */
    public Message insert(Message message) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(message);
        if (message.getMessageID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new message...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            message.setMessageID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New message inserted: {}", message.toString());
        } else {
            log.debug("Updating message: {}", message.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("MessageRepo returning message: {}", message);
        return message;
    }

    /**
     * Selects message from the database based on it's id
     *
     * @param messageID the message's id
     * @return the returned message
     */
    public Message selectByMessageID(Long messageID) {
        log.debug("MessageRepo selectByMessageID: {}", messageID);
        String selectByMessageIDSQL = selectSQL + "messageID=?";
        List<Message> messages = tmpl.query(selectByMessageIDSQL, new BeanPropertyRowMapper<>(Message.class),
                messageID);

        if (messages != null && messages.size() > 0) {
            log.debug("Query for message: #{}, number of items: {}", messageID, messages.size());
            return messages.get(0);
        }
        return null;
    }

    /**
     * Selects a list of message based on who received them
     *
     * @param recipientID the recipient's id
     * @return the list of messages
     */
    public List<Message> selectByRecipientID(Long recipientID) {
        log.debug("MessageRepo selectByRecipientID: {}", recipientID);
        String selectByRecipientIDSQL = selectSQL + "recipientID=?";
        List<Message> messages = tmpl.query(selectByRecipientIDSQL, new BeanPropertyRowMapper<>(Message.class),
                recipientID);

        log.debug("Query for recipientID: #{}, number of items: {}", recipientID, messages.size());
        return messages;
    }

    /**
     * Selects a list of messages based on who sent them
     *
     * @param senderID the sender's id
     * @return the list of messages
     */
    public List<Message> selectBySenderID(Long senderID) {
        log.debug("MessageRepo selectBySenderID: {}", senderID);
        String selectBySenderIDSQL = selectSQL + "senderID=?";
        List<Message> messages = tmpl.query(selectBySenderIDSQL, new BeanPropertyRowMapper<>(Message.class),
                senderID);

        log.debug("Query for senderID: #{}, number of items: {}", senderID, messages.size());
        return messages;
    }

    /**
     * Deletes a message from the database
     *
     * @param messageID the message id
     */
    public void delete(Long messageID) {
        log.debug("MessageRepo delete #{}", messageID);

        tmpl.update(deleteSQL, messageID);
        log.debug("Message deleted from database #{}", messageID);

    }
}