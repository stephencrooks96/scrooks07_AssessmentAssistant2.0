package com.pgault04.rest;

import java.security.Principal;

import com.pgault04.pojos.TutorQuestionPojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pgault04.services.EditTestService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping(value = "/questionCreation")
public class EditTestRest {

	@Autowired
	EditTestService editTestServ;

	private static final Logger logger = LogManager.getLogger(EditTestRest.class);

	public EditTestRest() {}

	/**
	 * 
	 * @param questionData
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/newQuestion", method = RequestMethod.POST)
	public Boolean newQuestion(@RequestBody TutorQuestionPojo questionData, Principal principal) {

		try {
			
			// editTestServ.newQuestion(questionData, principal.getName());
			return true;
			
		} catch (Exception e) {
			
			return false;
		}
		

	}

	

}
