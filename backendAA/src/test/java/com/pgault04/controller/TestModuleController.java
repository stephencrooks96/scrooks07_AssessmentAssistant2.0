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
public class TestModuleController {

    private ModuleController moduleController;
    @Autowired
    ModuleController moduleControllerWired;

    @Before
    public void setUp() {
        moduleController = new ModuleController();
    }

    @Test
    public void testModuleControllerDefaultConstructor() {
        assertNotNull(moduleController);
    }

    @Test
    public void testGetModuleByID() {
        assertNotNull(moduleControllerWired.getModuleByID(1L));
    }
}
