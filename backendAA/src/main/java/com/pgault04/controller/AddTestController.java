/**
 * 
 */
package com.pgault04.controller;

import java.security.Principal;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgault04.entities.Tests;
import com.pgault04.repositories.QuestionTypeRepo;
import com.pgault04.services.AddTestService;
import com.pgault04.services.EditTestService;
import com.pgault04.utilities.StringToDateUtil;

/**
 * @author Paul Gault - 40126005
 *
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("addTestControl")
public class AddTestController {

	/*
	@Autowired
	AddTestService addTestServ;

	@Autowired
	QuestionTypeRepo qTRepo;

	@Autowired
	EditTestService editTestServ;


	@RequestMapping(value = "/addTest", method = RequestMethod.GET)
	public String addTest(Model model, Principal principal, Long moduleID) {

		model.addAttribute("test", new Tests());
		return "addTest";
	}


	@RequestMapping(value = "/saveTest", method = RequestMethod.POST)
	public String saveTest(RedirectAttributes redirectAttrs, Model model, Principal principal, Tests test, Long moduleID) {

		Tests returnedTest = addTestServ.addTest(test, moduleID, principal.getName());

		if (returnedTest != null) {
			try {
				returnedTest.setStartDateTime(StringToDateUtil.convertReadableFormat(returnedTest.getStartDateTime()));
				returnedTest.setEndDateTime(StringToDateUtil.convertReadableFormat(returnedTest.getEndDateTime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			redirectAttrs.addFlashAttribute("test", returnedTest);
			redirectAttrs.addFlashAttribute("questionTypes", editTestServ.populateQuestionTypes());
			return "redirect:/editTest";

		} else {
			model.addAttribute("error", "Sorry the information you have entered is not valid.");
			model.addAttribute("moduleID", moduleID);
			return "addTest";
		}

	}
	*/
}
