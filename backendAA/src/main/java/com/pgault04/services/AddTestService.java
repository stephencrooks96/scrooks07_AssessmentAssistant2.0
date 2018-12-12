/**
 * 
 */
package com.pgault04.services;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgault04.entities.Module;
import com.pgault04.entities.Tests;
import com.pgault04.entities.User;
import com.pgault04.repositories.ModuleRepo;
import com.pgault04.repositories.TestsRepo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.utilities.StringToDateUtil;

/**
 * @author Paul Gault - 40126005
 *
 */
@Service
public class AddTestService {

	@Autowired
	TestsRepo testRepo;

	@Autowired
	ModuleRepo modRepo;

	@Autowired
	UserRepo userRepo;

	/**
	public Tests addTest(Tests test, Long moduleID, String username) {

		test.setTestID(-1L);
		test.setScheduled(0);
		test.setPublishResults(0);
		test.setTestTitle(test.getTestTitle().trim());

		User user = userRepo.selectByUsername(username);
		Module module = modRepo.selectByModuleID(moduleID);

		try {

			// Parse exceptions could be thrown here
			test.setEndDateTime(StringToDateUtil.convertInputDateToCorrectFormat(test.getEndDateTime()));
			test.setStartDateTime(StringToDateUtil.convertInputDateToCorrectFormat(test.getStartDateTime()));

			if (test.getTestTitle().length() <= 50 
					&& test.getTestTitle().length() > 0 
					&& user != null 
					&& module != null
					&& user.getUserID() == module.getTutorUserID()) {

				return testRepo.insert(test);

			}

		} catch (ParseException e) {

			e.printStackTrace();

		}

		return null;

	}
	 */
}
