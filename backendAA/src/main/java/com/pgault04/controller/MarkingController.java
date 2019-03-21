package com.pgault04.controller;

import com.pgault04.entities.Alternative;
import com.pgault04.entities.Answer;
import com.pgault04.entities.CorrectPoint;
import com.pgault04.pojos.AnswerData;
import com.pgault04.pojos.MarkerAndReassigned;
import com.pgault04.pojos.MarkerWithChart;
import com.pgault04.pojos.ResultChartPojo;
import com.pgault04.services.MarkingService;
import com.pgault04.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Paul Gault
 * @since January 2019
 * Rest Controller for marking functions
 */
@RestController
@RequestMapping("/marking")
public class MarkingController {

    @Autowired
    MarkingService markingService;
    @Autowired
    TestService testService;

    /**
     * Default constructor
     */
    public MarkingController() {}

    /**
     * Rest endpoint gets data on each marker such as how many scripts the have/haven't marked
     * To be displayed in a chart on frontend
     *
     * @param principal - logged in user
     * @param testID    - the test to retrieve data for
     * @return the chart data
     */
    @CrossOrigin
    @RequestMapping(value = "/getMarkersData", method = RequestMethod.GET)
    public MarkerWithChart getMarkersData(Principal principal, Long testID) {
        return markingService.getMarkersData(testID, principal.getName());
    }

    /**
     * Rest endpoint to allow scripts to be reassigned among users.
     *
     * @param principal        - the logged in user
     * @param testID           - the test which the scripts to reassign belong to
     * @param reassignmentData - how many scripts, which question(s), who to reassign to and from
     * @return boolean flag indicating success or failure to reassign
     */
    @CrossOrigin
    @RequestMapping(value = "/reassignAnswers", method = RequestMethod.POST)
    public Boolean reassignAnswers(Principal principal, Long testID, @RequestBody List<MarkerAndReassigned> reassignmentData) {
        return markingService.reassignAnswers(testID, principal.getName(), reassignmentData);
    }

    /**
     * Used to edit an answer when marker manually changes it
     *
     * @param principal - logged in user
     * @param answer    - the answer to edit
     * @return the edited answer
     * @throws IllegalArgumentException - if the user isn't a valid marker
     */
    @CrossOrigin
    @RequestMapping(value = "/editAnswer", method = RequestMethod.POST)
    public Answer editAnswer(Principal principal, @RequestBody Answer answer) throws IllegalArgumentException {
        return markingService.editAnswer(principal.getName(), answer);
    }

    /**
     * Rest endpoint for marker manually editing the score for a students answer
     *
     * @param principal - the logged in user
     * @param answer    - the answer to edit
     * @return - the edited answer
     * @throws IllegalArgumentException - exception thrown if not a valid marker
     */
    @CrossOrigin
    @RequestMapping(value = "/editScore", method = RequestMethod.POST)
    public Answer editScore(Principal principal, @RequestBody Answer answer) throws IllegalArgumentException {
        return markingService.editScore(principal.getName(), answer);
    }

    /**
     * Rest endpoint for marker manually editing the feedback for a students answer
     *
     * @param principal - the logged in user
     * @param answer    - the answer to edit
     * @return - the edited answer
     * @throws IllegalArgumentException - exception thrown if not a valid marker
     */
    @CrossOrigin
    @RequestMapping(value = "/editFeedback", method = RequestMethod.POST)
    public Answer editFeedback(Principal principal, @RequestBody Answer answer) throws IllegalArgumentException {
        return markingService.editFeedback(principal.getName(), answer);
    }

    /**
     * Rest endpoint to add a new correct point to test during marking phase
     *
     * @param principal    - the logged in user
     * @param correctPoint - the correct point to add
     * @param testID       - the test in question
     * @return boolean flag to indicate success / failure
     * @throws Exception - if the user is not a tutor
     */
    @CrossOrigin
    @RequestMapping(value = "/addCorrectPoint", method = RequestMethod.POST)
    public Boolean addCorrectPoint(Principal principal, @RequestBody CorrectPoint correctPoint, Long testID) throws Exception {
        return markingService.addCorrectPoint(principal.getName(), correctPoint, testID);
    }

    /**
     * Rest endpoint to add a new alternative to test during marking phase
     *
     * @param principal   - the logged in user
     * @param alternative - the alternative to add
     * @param testID      - the test under question
     * @return boolean flag indicating success of failure
     * @throws Exception - if the user is not a tutor
     */
    @CrossOrigin
    @RequestMapping(value = "/addAlternative", method = RequestMethod.POST)
    public Boolean addAlternative(Principal principal, @RequestBody Alternative alternative, Long testID) throws Exception {
        return markingService.addAlternative(principal.getName(), alternative, testID);
    }

    /**
     * Approves / disapproves a score give to an answer
     *
     * @param principal - the logged in user
     * @param answerID  - the answer to approve / disapprove
     * @return the approved/disapproved answer
     * @throws IllegalArgumentException - if the logged in user is not a valid marker or tutor
     */
    @CrossOrigin
    @RequestMapping(value = "/approve", method = RequestMethod.GET)
    public Answer approve(Principal principal, Long answerID) throws IllegalArgumentException {
        return markingService.approve(principal.getName(), answerID);
    }

    /**
     * Gets all marking data required for a certain user to mark all answers allocated to them
     *
     * @param principal - the logged in user
     * @param testID    - the test in question
     * @return the collection of data
     * @throws SQLException - if blob for question image is in an invalid format
     */
    @CrossOrigin
    @RequestMapping(value = "/getScriptsMarker", method = RequestMethod.GET)
    public List<AnswerData> getScriptsMarker(Principal principal, Long testID) throws SQLException {
        return markingService.getScriptsMarker(testID, principal.getName());
    }

    /**
     * Gets all marking data required for a tutor to mark / review all answers
     *
     * @param principal - the logged in user
     * @param testID    - the test in question
     * @return the collection of data
     * @throws IllegalAccessException - if the user is not a valid tutor
     * @throws SQLException           - if blob for question image is in an invalid format
     */
    @CrossOrigin
    @RequestMapping(value = "/getScriptsTutor", method = RequestMethod.GET)
    public List<AnswerData> getScriptsTutor(Principal principal, Long testID) throws IllegalAccessException, SQLException {
        return markingService.getScriptsTutor(testID, principal.getName());
    }

    /**
     * Rest endpoint to set test to be publishing grades and feedback to students
     *
     * @param principal - the logged in user
     * @param testID    - the test in question
     * @return whether it was a success or not
     * @throws IllegalAccessException - if user is not tutor
     * @throws SQLException           - if blob for question image is in an invalid format
     */
    @CrossOrigin
    @RequestMapping(value = "/publishGrades", method = RequestMethod.GET)
    public Boolean publishGrades(Principal principal, Long testID) throws IllegalAccessException, SQLException {
        return markingService.publishGrades(testID, principal.getName());
    }

    /**
     * Rest endpoint to set test to be publishing results and feedback to students
     *
     * @param principal - the logged in user
     * @param testID    - the test in question
     * @return whether it was a success or not
     * @throws IllegalAccessException - if user is not tutor
     * @throws SQLException           - if blob for question image is in an invalid format
     */
    @CrossOrigin
    @RequestMapping(value = "/publishResults", method = RequestMethod.GET)
    public Boolean publishResults(Principal principal, Long testID) throws IllegalAccessException, SQLException {
        return markingService.publishResults(testID, principal.getName());
    }

    /**
     * Rest endpoint to gather the correct points for a given question
     *
     * @param principal  - the logged in user
     * @param questionID - the question
     * @param testID     - the test
     * @return - the list of correct points
     */
    @CrossOrigin
    @RequestMapping(value = "/getCorrectPoints", method = RequestMethod.GET)
    public List<CorrectPoint> getCorrectPoints(Principal principal, Long questionID, Long testID) {
        return markingService.findCorrectPoints(questionID, principal.getName(), testID);
    }

    /**
     * Rest endpoint to remove a correct point from the mark scheme
     *
     * @param principal      - the logged in user
     * @param correctPointID - the correct point to remove
     * @param testID         - the test
     * @return success / failure
     * @throws Exception if user is not the tutor
     */
    @CrossOrigin
    @RequestMapping(value = "/removeCorrectPoint", method = RequestMethod.DELETE)
    public Boolean removeCorrectPoints(Principal principal, Long correctPointID, Long testID) throws Exception {
        return markingService.removeCorrectPoint(principal.getName(), correctPointID, testID, true);
    }

    /**
     * Rest endpoint to generate chart that shows how every student in class performed in test
     *
     * @param principal - the logged in user
     * @param testID    - the test
     * @return the chart data
     */
    @CrossOrigin
    @RequestMapping(value = "/getResultChart", method = RequestMethod.GET)
    public ResultChartPojo getResultChart(Principal principal, Long testID) {
        return markingService.generateResultChart(testID, principal.getName());
    }

    /**
     * Rest endpoint to generate chart data to show how the class performed in average in each question
     *
     * @param principal - the logged in user
     * @param testID    - the test
     * @return the chart data
     */
    @CrossOrigin
    @RequestMapping(value = "/getQuestionsResultChart", method = RequestMethod.GET)
    public List<ResultChartPojo> getQuestionResultChart(Principal principal, Long testID) {
        return markingService.generateQuestionResultChart(testID, principal.getName());
    }

    /**
     * Rest endpoint to remove an alternative during marking phase
     *
     * @param principal     - the logged in user
     * @param alternativeID - the alternative
     * @param testID        - the test
     * @return success / failure
     * @throws Exception if the user is not the tutor
     */
    @CrossOrigin
    @RequestMapping(value = "/removeAlternative", method = RequestMethod.DELETE)
    public Boolean removeAlternative(Principal principal, Long alternativeID, Long testID) throws Exception {
        return markingService.removeAlternative(principal.getName(), alternativeID, testID);
    }
}