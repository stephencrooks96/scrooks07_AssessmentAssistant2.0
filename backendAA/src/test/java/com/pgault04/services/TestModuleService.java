package com.pgault04.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * @author Paul Gault - 40126005
 * @since November 2018
 */
@Sql("/tests.sql")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestModuleService {

    @Autowired
    ModuleService moduleService;

    @Test
    public void testCheckGrade() {
        for (double loop = 0; loop <= 100; loop++) {

            if (loop <= 49) {
                assertEquals("F", moduleService.checkGrade(loop));
            }
            if (loop > 49 && loop <= 59) {
                assertEquals("D", moduleService.checkGrade(loop));
            }
            if (loop > 59 && loop <= 69) {
                assertEquals("C", moduleService.checkGrade(loop));
            }
            if (loop > 69 && loop <= 79) {
                assertEquals("B", moduleService.checkGrade(loop));
            }
            if (loop > 79 && loop <= 89) {
                assertEquals("A", moduleService.checkGrade(loop));
            }
            if (loop > 89) {
                assertEquals("A*", moduleService.checkGrade(loop));
            }
        }
    }
}
