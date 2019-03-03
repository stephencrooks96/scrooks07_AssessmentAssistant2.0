package com.pgault04.repositories;

import com.pgault04.entities.Inputs;
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

@Component
public class InputsRepo {

    private static final int INSERT_CHECKER_CONSTANT = 0;

    private static final Logger log = LogManager.getLogger(InputsRepo.class);

    private final String insertSQL = "INSERT INTO Inputs (inputValue, inputIndex, answerID, math) values (:inputValue, :inputIndex, :answerID, :math)";
    private final String updateSQL = "UPDATE Inputs SET inputValue=:inputValue, inputIndex=:inputIndex, answerID=:answerID, math=:math WHERE inputID=:inputID";
    private final String selectSQL = "SELECT * FROM Inputs WHERE ";

    @Autowired
    JdbcTemplate tmpl;
    @Autowired
    NamedParameterJdbcTemplate namedparamTmpl;

    private String tableName = "Inputs";
    private String deleteSQL = "DELETE FROM Inputs WHERE inputID=?";

    /**
     * @return the number of records in the table
     */
    public Integer rowCount() {
        return tmpl.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }

    /**
     * Inserts/Updates inputs in to the database
     *
     * @param input the input object
     * @return the input object after insertion
     */
    public Inputs insert(Inputs input) {
        BeanPropertySqlParameterSource namedParams = new BeanPropertySqlParameterSource(input);
        if (input.getInputID() < INSERT_CHECKER_CONSTANT) {
            // insert
            log.debug("Inserting new input...");

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedparamTmpl.update(insertSQL, namedParams, keyHolder);
            input.setInputID(Objects.requireNonNull(keyHolder.getKey()).longValue());

            // inserted
            log.debug("New input inserted: {}", input.toString());
        } else {
            log.debug("Updating input: {}", input.toString());
            namedparamTmpl.update(updateSQL, namedParams);
        }
        log.info("JdbcRepo returning input: {}", input);
        return input;
    }

    /**
     * Selects a input by it's question id
     *
     * @param inputID the input's id
     * @return the input
     */
    public Inputs selectByInputID(Long inputID) {
        log.debug("InputsRepo selectByInputID: #{}", inputID);
        String selectByInputIDSQL = selectSQL + "inputID=?";
        List<Inputs> inputs = tmpl.query(selectByInputIDSQL,
                new BeanPropertyRowMapper<>(Inputs.class), inputID);

        if (inputs.size() > 0) {
            log.debug("Query for question: #{}, number of items: ", inputID, inputs.size());
            return inputs.get(0);
        }
        return null;
    }

    /**
     * Selects inputs based on the answer they belong to
     *
     * @param answerID the answer's id
     * @return the list of inputs
     */
    public List<Inputs> selectByAnswerID(Long answerID) {
        log.debug("InputsRepo selectByAnswerID: #{}", answerID);
        String selectByAnswerIDSQL = selectSQL + "answerID=?";
        List<Inputs> inputs = tmpl.query(selectByAnswerIDSQL,
                new BeanPropertyRowMapper<>(Inputs.class), answerID);

        log.debug("Query for inputs with answer id: #{}, number of items: {}", answerID, inputs.size());
        return inputs;
    }

    /**
     * Deletes a record from the inputs table
     *
     * @param inputID the input's id
     */
    public void delete(Long inputID) {
        log.debug("InputsRepo deletes #{}", inputID);

        tmpl.update(deleteSQL, inputID);
        log.debug("Input deleted from database #{}", inputID);

    }
}
