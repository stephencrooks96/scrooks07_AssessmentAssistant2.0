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
 */
@RestController
@RequestMapping("/marking")
public class MarkingController {

    @Autowired
    MarkingService markingService;

    @Autowired
    TestService testService;

    @CrossOrigin
    @RequestMapping(value = "/getMarkersData", method = RequestMethod.GET)
    public MarkerWithChart getMarkersData(Principal principal, Long testID) {
        return markingService.getMarkersData(testID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/reassignAnswers", method = RequestMethod.POST)
    public Boolean reassignAnswers(Principal principal, Long testID, @RequestBody List<MarkerAndReassigned> reassignmentData) {
        return markingService.reassignAnswers(testID, principal.getName(), reassignmentData);
    }

    @CrossOrigin
    @RequestMapping(value = "/editAnswer", method = RequestMethod.POST)
    public Answer editAnswer(Principal principal, @RequestBody Answer answer) throws IllegalArgumentException {
        return markingService.editAnswer(principal.getName(), answer);
    }

    @CrossOrigin
    @RequestMapping(value = "/editScore", method = RequestMethod.POST)
    public Answer editScore(Principal principal, @RequestBody Answer answer) throws IllegalArgumentException {
        return markingService.editScore(principal.getName(), answer);
    }

    @CrossOrigin
    @RequestMapping(value = "/editFeedback", method = RequestMethod.POST)
    public Answer editFeedback(Principal principal, @RequestBody Answer answer) throws IllegalArgumentException {
        return markingService.editFeedback(principal.getName(), answer);
    }

    @CrossOrigin
    @RequestMapping(value = "/addCorrectPoint", method = RequestMethod.POST)
    public Boolean addCorrectPoint(Principal principal, @RequestBody CorrectPoint correctPoint, Long testID) throws Exception {
        return markingService.addCorrectPoint(principal.getName(), correctPoint, testID);
    }

    @CrossOrigin
    @RequestMapping(value = "/addAlternative", method = RequestMethod.POST)
    public Boolean addAlternative(Principal principal, @RequestBody Alternative alternative, Long testID) throws Exception {
        return markingService.addAlternative(principal.getName(), alternative, testID);
    }

    @CrossOrigin
    @RequestMapping(value = "/approve", method = RequestMethod.GET)
    public Answer approve(Principal principal, Long answerID) throws IllegalArgumentException {
        return markingService.approve(principal.getName(), answerID);
    }

    @CrossOrigin
    @RequestMapping(value = "/getScriptsMarker", method = RequestMethod.GET)
    public List<AnswerData> getScriptsMarker(Principal principal, Long testID) throws SQLException {
        return markingService.getScriptsMarker(testID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/getScriptsTutor", method = RequestMethod.GET)
    public List<AnswerData> getScriptsTutor(Principal principal, Long testID) throws IllegalAccessException, SQLException {
        return markingService.getScriptsTutor(testID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/publishGrades", method = RequestMethod.GET)
    public Boolean publishGrades(Principal principal, Long testID) throws IllegalAccessException, SQLException {
        return markingService.publishGrades(testID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/publishResults", method = RequestMethod.GET)
    public Boolean publishResults(Principal principal, Long testID) throws IllegalAccessException, SQLException {
        return markingService.publishResults(testID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/getCorrectPoints", method = RequestMethod.GET)
    public List<CorrectPoint> getCorrectPoints(Principal principal, Long questionID, Long testID) {
        return markingService.findCorrectPoints(questionID, principal.getName(), testID);
    }

    @CrossOrigin
    @RequestMapping(value = "/removeCorrectPoint", method = RequestMethod.DELETE)
    public Boolean removeCorrectPoints(Principal principal, Long correctPointID, Long testID) throws Exception {
        return markingService.removeCorrectPoint(principal.getName(), correctPointID, testID, true);
    }

    @CrossOrigin
    @RequestMapping(value = "/getResultChart", method = RequestMethod.GET)
    public ResultChartPojo getResultChart(Principal principal, Long testID) {
        return markingService.generateResultChart(testID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/getQuestionsResultChart", method = RequestMethod.GET)
    public List<ResultChartPojo> getQuestionResultChart(Principal principal, Long testID) {
        return markingService.generateQuestionResultChart(testID, principal.getName());
    }

    @CrossOrigin
    @RequestMapping(value = "/removeAlternative", method = RequestMethod.DELETE)
    public Boolean removeAlternative(Principal principal, Long alternativeID, Long testID) throws Exception {
        return markingService.removeAlternative(principal.getName(), alternativeID, testID);
    }
}
