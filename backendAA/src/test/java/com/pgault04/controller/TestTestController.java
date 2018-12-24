package com.pgault04.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * @author Paul Gault - 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTestController {

    private TestController testController;

    @Before
    public void setUp() {
        testController = new TestController();
    }

    @Test
    public void testTestControllerDefaultConstructor() {
        assertNotNull(testController);
    }
}
