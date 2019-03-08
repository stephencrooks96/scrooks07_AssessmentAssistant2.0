package com.pgault04.repositories;

import com.pgault04.entities.Answer;
import com.pgault04.entities.Inputs;
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

@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestInputsRepo {

    private static final long ANSWER_ID_IN_DB = 1L;

    @Autowired
    InputsRepo inputsRepo;
    private Inputs inputs;
    private String inputValue;
    private Integer inputIndex, math;

    @Before
    public void setUp() throws Exception {
        this.inputValue = "value";
        this.inputIndex = 1;
        this.math = 0;
        inputs = new Inputs(inputValue, inputIndex, ANSWER_ID_IN_DB, math);
    }

    @Test
    public void testRowCount() {
        int rowCountBefore = inputsRepo.rowCount();
        // Inserts one inputs to table
        inputsRepo.insert(inputs);
        // Checks one value is registered as in the table
        assertTrue(inputsRepo.rowCount() > rowCountBefore);
    }

    @Test
    public void testInsert() {
        // Inserts one inputs to table
        Inputs input = inputsRepo.insert(inputs);
        assertNotNull(input);
        assertNotEquals(-1L, input.getInputID(), 0);
        // Updates the inputs in the table
        String updateCheck = "value 2";
        input.setInputValue(updateCheck);
        // Inserts one inputs to table
        Inputs updatedInput = inputsRepo.insert(input);
        assertEquals(input.getInputID(), updatedInput.getInputID());
        assertEquals(updateCheck, updatedInput.getInputValue());
    }

    @Test
    public void testSelectByAnswerID() {
        // Inserts one inputs to table
        Inputs input = inputsRepo.insert(inputs);
        List<Inputs> inputs = inputsRepo.selectByAnswerID(input.getAnswerID());
        assertTrue(inputs.toString().contains(input.toString()));
    }

    @Test
    public void testSelectByInputID() {
        // Inserts one inputs to table
        Inputs input = inputsRepo.insert(inputs);
        input = inputsRepo.selectByInputID(input.getInputID());
        assertNotNull(input);
    }

    @Test
    public void testDelete() {
        // Inserts one inputs to table
        Inputs input = inputsRepo.insert(inputs);
        inputsRepo.delete(input.getInputID());
        input = inputsRepo.selectByInputID(input.getInputID());
        assertNull(input);
    }
}
