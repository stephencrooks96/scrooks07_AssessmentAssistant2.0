/**
 * 
 */
package com.pgault04.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgault04.services.EditTestService;

/**
 * @author Paul Gault - 40126005
 *
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("editTestControl")
public class EditTestController {

	@Autowired
	EditTestService editTestServ;

	/**
	 * Login method
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editTest", method = RequestMethod.GET)
	public String addQuestions(RedirectAttributes redirectedAttributes, Long testID) {

		redirectedAttributes.addAttribute("questionTypes", editTestServ.populateQuestionTypes());
		redirectedAttributes.addAttribute("testID", testID);

		return "editTest";
	}

	
}
