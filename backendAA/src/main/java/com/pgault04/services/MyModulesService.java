package com.pgault04.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgault04.entities.Module;
import com.pgault04.entities.ModuleAssociation;
import com.pgault04.entities.User;
import com.pgault04.repositories.ModuleAssociationRepo;
import com.pgault04.repositories.ModuleRepo;
import com.pgault04.repositories.UserRepo;

/**
 * 
 * @author Paul Gault - 40126005
 * 
 *         Class to facilitate population of data to the My Modules template
 *
 */
@Service
public class MyModulesService {

	@Autowired
	UserRepo userRepo;

	@Autowired
	ModuleRepo moduleRepo;

	@Autowired
	ModuleAssociationRepo modAssocRepo;

	/**
	 * Displays all the modules that a given user is associated with as string of
	 * HTML5 tags
	 * 
	 * @param username
	 * @return moduleMessage
	 */
	public List<Module> myModules(String username) {

		User user = userRepo.selectByUsername(username);
		List<ModuleAssociation> modAssocs = modAssocRepo.selectByUserID(user.getUserID());
		List<Module> modules = new ArrayList<Module>();

		StringBuilder sb = new StringBuilder();

		for (ModuleAssociation m : modAssocs) {
			modules.add(moduleRepo.selectByModuleID(m.getModuleID()));
		}

		return modules;
		/*
		sb.append("<div class='row'>\n");
		
		for (Module m : modules) {
				
			sb.append("<div class='col-sm-12'>\n");
			sb.append("<a href=\"/moduleHome?moduleID=");
			sb.append(m.getModuleID());
			sb.append("\" style=\"width: 100%; padding: 0;\">\n");
			sb.append("<div class='card fluid' style='margin-left: 2%; margin-right: 2%;'>\n");
			sb.append("<div class='section'>\n");
			sb.append(m.getModuleName());
			sb.append("\n</div>\n");
			sb.append("</div>\n");
			sb.append("</a>\n");
			sb.append("</div>\n");

			
			}
		
		sb.append("</div>");

		return sb.toString();
		*/
	}

}
