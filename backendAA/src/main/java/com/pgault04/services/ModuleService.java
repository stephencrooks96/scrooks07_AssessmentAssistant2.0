package com.pgault04.services;

import com.pgault04.pojos.Performance;
import com.pgault04.pojos.TestAndGrade;
import com.pgault04.pojos.TestAndResult;
import com.pgault04.pojos.TestMarking;
import com.pgault04.entities.*;
import com.pgault04.repositories.*;
import com.pgault04.utilities.StringToDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ModuleService {

    private static final int SCHEDULED = 1;
    private static final int READY_FOR_REVIEW = 0;
    private static final int PUBLISH_TRUE = 1;

    @Autowired
    TestsRepo testsRepo;
    @Autowired
    ModuleAssociationRepo modAssocRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    AssociationTypeRepo associationTypeRepo;
    @Autowired
    AnswerRepo answerRepo;
    @Autowired
    TestResultRepo testResultRepo;
    @Autowired
    TestQuestionRepo testQuestionRepo;
    @Autowired
    QuestionRepo questionRepo;

    /**
     * @param moduleID
     * @return
     */
    public List<Tests> activeTests(Long moduleID) {
        Date now = new Date();
        List<Tests> tests = testsRepo.selectByModuleID(moduleID);
        List<Tests> activeTests = new ArrayList<>();

        for (Tests t : tests) {
            try {
                if (now.compareTo(StringToDateUtil.stringToDate(t.getStartDateTime())) >= 0
                        && now.compareTo(StringToDateUtil.stringToDate(t.getEndDateTime())) < 0
                        && t.getScheduled() == SCHEDULED) {
                    activeTests.add(t);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return activeTests;
    }

    /**
     * @param moduleID
     * @return
     */
    public List<Tests> scheduledTests(Long moduleID) {
        Date now = new Date();
        List<Tests> tests = testsRepo.selectByModuleID(moduleID);
        List<Tests> scheduledTests = new ArrayList<>();

        for (Tests t : tests) {
            try {
                if (t.getScheduled() == SCHEDULED
                        && now.compareTo(StringToDateUtil.stringToDate(t.getStartDateTime())) < 0) {
                    scheduledTests.add(t);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return scheduledTests;
    }

    /**
     * @param moduleID
     * @param username
     * @return
     */
    public List<TestMarking> marking(Long moduleID, String username, String associationType) {
        Date now = new Date();
        List<Tests> tests = testsRepo.selectByModuleID(moduleID);
        User user = userRepo.selectByUsername(username);
        List<TestMarking> tmList = new ArrayList<>();

        try {
            for (Tests t : tests) {
                if (now.compareTo(StringToDateUtil.stringToDate(t.getEndDateTime())) >= 0) {
                    List<Answer> answers = answerRepo.selectByTestID(t.getTestID());
                    int toBeMarkedByYou = 0, toBeMarkedByTAs = 0, marked = 0, totalForYou = 0, totalForTAs = 0;
                    for (Answer a : answers) {
                        if ("tutor".equalsIgnoreCase(associationType)) {
                            if (a.getMarkerID().equals(user.getUserID())) {
                                totalForYou++;
                                if (a.getScore() == null) {
                                    toBeMarkedByYou++;
                                } else {
                                    marked++;
                                }
                            } else {
                                totalForTAs++;
                                if (a.getScore() == null) {
                                    toBeMarkedByTAs++;
                                } else {
                                    marked++;
                                }
                            }
                        } else {
                            if (a.getMarkerID().equals(user.getUserID())) {
                                totalForYou++;
                                if (a.getScore() == null) {
                                    toBeMarkedByYou++;
                                } else {
                                    marked++;
                                }
                            }
                        }
                    }
                    TestMarking tm = new TestMarking(t, toBeMarkedByYou, toBeMarkedByTAs, marked, totalForYou, totalForTAs);
                    tmList.add(tm);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tmList;
    }

    /**
     * @param moduleID
     * @param username
     * @return
     */
    public List<TestAndGrade> activeResults(Long moduleID, String username) {

        List<Tests> tests = testsRepo.selectByModuleID(moduleID);
        User user = userRepo.selectByUsername(username);
        List<TestAndGrade> testAndGradeList = new ArrayList<>();

        for (Tests test : tests) {
            if (test.getPublishGrades() == PUBLISH_TRUE) {
                for (TestResult testResult : testResultRepo.selectByTestID(test.getTestID())) {
                    if (testResult.getStudentID().equals(user.getUserID())) {
                        List<Question> questions = new ArrayList<>();

                        for (TestQuestion testQuestion : testQuestionRepo.selectByTestID(test.getTestID())) {
                            List<Question> questionToAdd = questionRepo.selectByQuestionID(testQuestion.getQuestionID());
                            questions.add(questionToAdd.get(0));
                        }

                        double percentageScore = 0;
                        for (Question q : questions) {
                            percentageScore += q.getMaxScore();
                        }

                        String grade = checkGrade(testResult.getTestScore() / percentageScore * 100);

                        TestAndGrade tar = new TestAndGrade(test, grade);
                        testAndGradeList.add(tar);
                    }
                }
            }
        }
        return testAndGradeList;
    }

    /**
     * @param moduleID
     * @return
     */
    public List<Tests> testDrafts(Long moduleID) {
        List<Tests> tests = testsRepo.selectByModuleID(moduleID);
        List<Tests> testReturn = new ArrayList<>();

        for (Tests t : tests) {
            if (t.getScheduled() != SCHEDULED) {
                testReturn.add(t);
            }
        }
        return testReturn;
    }

    /**
     * @param moduleID
     * @return
     */
    public List<TestMarking> reviewMarking(Long moduleID) {

        Date now = new Date();
        List<Tests> tests = testsRepo.selectByModuleID(moduleID);
        List<TestMarking> tmList = new ArrayList<>();

        for (Tests t : tests) {
            try {
                if (now.compareTo(StringToDateUtil.stringToDate(t.getEndDateTime())) >= 0) {
                    int answersUnmarked = 0;
                    List<Answer> answers = answerRepo.selectByTestID(t.getTestID());
                    for (Answer a : answers) {
                        if (a.getScore() == null) {
                            answersUnmarked++;
                        }
                    }
                    if (answersUnmarked == READY_FOR_REVIEW) {
                        TestMarking tm = new TestMarking(t, 0, 0, answers.size(), 0, 0);
                        tmList.add(tm);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return tmList;
    }

    /**
     * @param moduleID
     * @param principal
     * @return
     */
    public List<Performance> generatePerformance(Long moduleID, Principal principal) {


        List<Tests> tests = testsRepo.selectByModuleID(moduleID);
        User user = userRepo.selectByUsername(principal.getName());
        List<Performance> performanceList = new ArrayList<>();

        for (Tests test : tests) {
            if (test.getPublishResults() == PUBLISH_TRUE) {
                double classAverage = 0.0;
                TestAndResult tar = null;
                List<TestResult> testResults = testResultRepo.selectByTestID(test.getTestID());
                for (TestResult testResult : testResults) {
                    if (testResult.getStudentID().equals(user.getUserID())) {
                        List<Question> questionList = new ArrayList<>();
                        for (TestQuestion testQuestion : testQuestionRepo.selectByTestID(test.getTestID())) {
                            List<Question> questionToAdd = questionRepo.selectByQuestionID(testQuestion.getQuestionID());
                            questionList.add(questionToAdd.get(0));
                        }
                        tar = new TestAndResult(test, testResult, questionList, null);
                    }
                    classAverage += testResult.getTestScore();
                }
                classAverage /= testResults.size();
                performanceList.add(new Performance(tar, classAverage));
            }
        }

        return performanceList;

    }

    /**
     * @param username
     * @param moduleID
     * @return
     */
    public String checkValidAssociation(String username, Long moduleID) {
        List<ModuleAssociation> ma = modAssocRepo.selectByModuleID(moduleID);
        User u = userRepo.selectByUsername(username);
        Long theAssociationTypeID = null;

        for (ModuleAssociation m : ma) {
            if (m.getUserID().equals(u.getUserID())) {
                theAssociationTypeID = m.getAssociationType();
            }
        }
        if (theAssociationTypeID != null) {
            return associationTypeRepo.selectByAssociationTypeID(theAssociationTypeID).getAssociationType();
        } else {
            return null;
        }

    }

    public String checkGrade(double score) {

        if (score > 89) {
            return "A*";
        } else if (score > 79) {
            return "A";
        } else if (score > 69) {
            return "B";
        } else if (score > 59) {
            return "C";
        } else if (score > 49) {
            return "D";
        } else {
            return "F";
        }
    }

}