package com.pgault04.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgault04.entities.CorrectPoint;
import com.pgault04.entities.Question;
import com.pgault04.entities.QuestionType;
import com.pgault04.entities.TestQuestion;
import com.pgault04.entities.User;
import com.pgault04.repositories.CorrectPointRepo;
import com.pgault04.repositories.QuestionRepo;
import com.pgault04.repositories.QuestionTypeRepo;
import com.pgault04.repositories.TestQuestionRepo;
import com.pgault04.repositories.UserRepo;

/**
 * 
 * @author Paul Gault - 40126005
 *
 */
@Service
public class EditTestService {

	@Autowired
	QuestionTypeRepo questionTypeRepo;
	
	@Autowired
	QuestionRepo questionRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	TestQuestionRepo testQuestionRepo;

	@Autowired
	CorrectPointRepo cpRepo;
	
	/**
	 * Communicates with the controller
	 * @return
	 */
	public String populateQuestionTypes() {

		StringBuilder sb = new StringBuilder();
		List<QuestionType> questionTypes = questionTypeRepo.selectAll();
		
		
		sb.append("<select id='sf1-select' style='width: 350px;' class='doc' data-ng-model=\"question.questionType\">");
		for (QuestionType qt : questionTypes) {
			
					sb.append("<option class='doc' value='");
					sb.append(qt.getQuestionTypeID());
					sb.append("' type='");
					sb.append(qt.getQuestionType());
					sb.append("'>");
					sb.append(qt.getQuestionType());
					sb.append("</option>");
				
		}
		sb.append("</select>");
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @param questionData
	 * @param username
	 * @return
	 */
	public void newQuestion(/*EditTestPojo questionData*/ String username) throws Exception {
		
		// Question question = questionData.getQuestion();
		// List<CorrectPoint> correctPoints = questionData.getCorrectPoints();
		User user = userRepo.selectByUsername(username);
		// question.setCreatorID(user.getUserID());

		// question = questionRepo.insert(question);
		// testQuestionRepo.insert(new TestQuestion(questionData.getTestID(), questionRepo.insert(question).getQuestionID()));

		// addCorrectPoints(correctPoints);
		
	}
	
	/**
	 * 
	 * @param correctPoints
	 * @return
	 */
	public void addCorrectPoints(List<CorrectPoint> correctPoints) throws Exception {
		if (correctPoints != null && correctPoints.size() > 0) {
				for (CorrectPoint cp : correctPoints) {

					cpRepo.insert(cp);
				}
			}
			
		
	}
	
	
}
