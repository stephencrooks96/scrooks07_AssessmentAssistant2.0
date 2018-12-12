package com.pgault04.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pgault04.entities.Tests;
import com.pgault04.repositories.TestsRepo;

public class GeneralTestRest {
	
	@Autowired
	TestsRepo testRepo;

	private static final Logger logger = LogManager.getLogger(EditTestRest.class);

	public GeneralTestRest() {}

	/**
	 * 
	 * @param questionData
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/selectTestByTestID", method = RequestMethod.POST)
	public Tests newQuestion(Long testID) {
		logger.debug("/selectTestByTestID selected - returning test.");
		return testRepo.selectByTestID(testID);
		

	}
}
