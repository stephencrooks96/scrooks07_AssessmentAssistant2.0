package com.pgault04.controller;

import com.pgault04.entities.Tests;
import com.pgault04.pojos.Marker;
import com.pgault04.pojos.MarkerAndReassigned;
import com.pgault04.pojos.MarkerWithChart;
import com.pgault04.services.MarkingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
}
