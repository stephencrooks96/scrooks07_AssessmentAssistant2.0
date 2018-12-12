package com.pgault04.services;

import com.pgault04.entities.Module;
import com.pgault04.entities.Tests;
import com.pgault04.entities.User;
import com.pgault04.repositories.ModuleRepo;
import com.pgault04.repositories.TestsRepo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.utilities.StringToDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

/**
 *
 */
@Service
public class TestService {

    @Autowired
    TestsRepo testRepo;

    @Autowired
    ModuleRepo modRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ModuleService modServ;

    /**
     * Method primes input data to be entered in to the database Ensures data size
     * limits are enforced
     * <p>
     * Ensures input data is correct for a new test and not tampered with on front
     * end i.e. testID=-1L to prevent user from updating a pre-existing test without
     * permission, ensures test is not scheduled for release immediately, ensures
     * results are not readied for publish immediately.
     *
     * @param test
     * @param username
     * @return Test object or null
     */
    public Tests addTest(Tests test, String username) {
        test.setTestID(-1L);
        test.setScheduled(0);
        test.setPublishResults(0);
        test.setTestTitle(test.getTestTitle().trim());
        User user = userRepo.selectByUsername(username);
        Module module = modRepo.selectByModuleID(test.getModuleID());

        try {
            // Parse exceptions could be thrown here
            test.setEndDateTime(StringToDateUtil.convertInputDateToCorrectFormat(test.getEndDateTime()));
            test.setStartDateTime(StringToDateUtil.convertInputDateToCorrectFormat(test.getStartDateTime()));
            if (test.getTestTitle().length() <= 50
                    && test.getTestTitle().length() > 0
                    && user != null
                    && module != null
                    && user.getUserID().equals(module.getTutorUserID())) {
                return primeTestForUserView(testRepo.insert(test));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Tests getByTestIDTutorView(String username, Long testID) {

        Tests test = testRepo.selectByTestID(testID);

        if ("tutor".equals(modServ.checkValidAssociation(username, test.getModuleID()))) {
            return primeTestForUserView(test);
        }
        return null;
    }

    public Tests primeTestForUserView(Tests test) {
        if (test != null) {
            try {
                test.setStartDateTime(StringToDateUtil.convertReadableFormat(test.getStartDateTime()));
                test.setEndDateTime(StringToDateUtil.convertReadableFormat(test.getEndDateTime()));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return test;
        } else {
            return null;
        }
    }
}
