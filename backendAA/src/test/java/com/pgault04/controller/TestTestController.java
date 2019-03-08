package com.pgault04.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertNotNull;

/**
 * @author Paul Gault - 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTestController {

    private TestController testController;

    @Autowired
    TestController testControllerWired;

    @Before
    public void setUp() {
        testController = new TestController();
    }

    @Test
    public void testTestControllerDefaultConstructor() {
        assertNotNull(testController);
    }

    @Test
    public void testGetQuestionTypes() {
        assertNotNull(testControllerWired.getQuestionTypes());
    }
}
