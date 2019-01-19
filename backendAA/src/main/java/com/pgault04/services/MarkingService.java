package com.pgault04.services;

import com.pgault04.entities.*;
import com.pgault04.pojos.Marker;
import com.pgault04.pojos.MarkerAndReassigned;
import com.pgault04.pojos.MarkerWithChart;
import com.pgault04.repositories.AnswerRepo;
import com.pgault04.repositories.ModuleAssociationRepo;
import com.pgault04.repositories.TestsRepo;
import com.pgault04.repositories.UserRepo;
import com.pgault04.utilities.ChartUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Paul Gault 40126005
 * @since January 2019
 */
@Service
public class MarkingService {

    @Autowired
    TestsRepo testsRepo;

    @Autowired
    ModuleAssociationRepo moduleAssociationRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ModuleService modService;

    @Autowired
    AnswerRepo answerRepo;

    public MarkerWithChart getMarkersData(Long testID, String username) {

        Tests test = testsRepo.selectByTestID(testID);
        List<Marker> markers = new ArrayList<>();
        if (AssociationType.TUTOR == modService.checkValidAssociation(username, test.getModuleID())) {
            List<ModuleAssociation> modAssocs = moduleAssociationRepo.selectByModuleID(test.getModuleID());

            for (ModuleAssociation m : modAssocs) {
                if (m.getAssociationType() != AssociationType.STUDENT) {
                    User marker = userRepo.selectByUserID(m.getUserID());
                    Integer marked = 0, unmarked = 0;
                    String markerType = "";

                    if (m.getAssociationType() == AssociationType.TEACHING_ASSISTANT) {
                        markerType = "Assistant";
                    } else if (m.getAssociationType() == AssociationType.TUTOR) {
                        markerType = "Tutor";
                    }

                    List<Answer> allAnswers = answerRepo.selectByTestID(testID);
                    List<Answer> answers = new ArrayList<>();
                    for (Answer a : allAnswers) {
                        if (a.getMarkerID().equals(marker.getUserID())) {
                            answers.add(a);
                            if (a.getMarkerApproved() == 0) {
                                unmarked++;
                            } else {
                                marked++;
                            }
                        }
                    }

                    markers.add(new Marker(test, marker, markerType, answers, marked, unmarked));
                }
            }
            List<String> labels = new LinkedList<>();
            List<Integer> data = new LinkedList<>();
            List<String> colours = new LinkedList<>();
            for (Marker ms : markers) {
                labels.add(ms.getMarker().getFirstName() + " " + ms.getMarker().getLastName());
                data.add(ms.getScripts().size());
                colours.add(ChartUtil.chartColourGenerate());
            }

            return new MarkerWithChart(markers, labels, data, colours);
        }
        return null;
    }

    public Boolean reassignAnswers(Long testID, String username, List<MarkerAndReassigned> reassignmentData) {

        Tests test = testsRepo.selectByTestID(testID);
        if (AssociationType.TUTOR == modService.checkValidAssociation(username, test.getModuleID())) {

            for (MarkerAndReassigned m : reassignmentData) {
                if (m.getNumberToReassign() != null && m.getNumberToReassign() != 0) {
                    List<Answer> answers = answerRepo.selectByMarkerID(m.getPreviousMarkerID());
                    List<Answer> testAnswers = new ArrayList<>();
                    for (Answer a : answers) {
                        if (a.getTestID().equals(testID) && a.getMarkerApproved() == 0) {
                            testAnswers.add(a);
                        }
                    }
                    List<Answer> questionAnswers = new ArrayList<>();
                    if (m.getSpecifyQuestion() == 0) {
                        questionAnswers = testAnswers;
                    } else {
                        for (Answer a : testAnswers) {
                            if (a.getQuestionID().equals(m.getSpecifyQuestion())) {
                                questionAnswers.add(a);
                            }
                        }
                    }
                    int reassignLevel = (questionAnswers.size() * (m.getNumberToReassign().intValue() / 100));
                    for (int loop = 0; loop < questionAnswers.size(); loop++) {
                        if (loop < reassignLevel) {
                            questionAnswers.get(loop).setMarkerID(m.getMarkerID());
                            answerRepo.insert(questionAnswers.get(loop));
                        }
                    }
                }
            }
            return true;
        }
        return null;
    }


}
