package com.chrisali.easylogbook.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chrisali.easylogbook.model.PilotDetail;
import com.chrisali.easylogbook.model.User;
import com.chrisali.easylogbook.model.enums.PilotExamination;
import com.chrisali.easylogbook.service.PilotDetailsService;
import com.chrisali.easylogbook.service.UsersService;
import com.chrisali.easylogbook.service.PilotDetailsService.PilotDetailsType;

@Controller
public class HomeController {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private PilotDetailsService pilotDetailsService;
	
	/**
	 * Shows home page. If user logged in, gets current date and compares it to set of upcoming currency expirations logged from
	 * getUpcominExpirations from {@link PilotDetailsService}. This is used to alert the user of upcoming expirations via Bootstrap modal.
	 * Adds active CSS header class and logged in user to model 
	 * 
	 * @param principal
	 * @param model
	 * @return path to home.html
	 */
	@RequestMapping("/")
	public String showHome(Principal principal, Model model) {
		Set<PilotExamination> upcomingExpirations = new LinkedHashSet<>();
		
		if (principal != null) {
			String username = principal.getName();
			User user = usersService.getUser(username);
			
			List<PilotDetail> pilotExaminationDetails = pilotDetailsService.getPilotDetails(username, PilotDetailsType.EXAMINATIONS);
			upcomingExpirations = pilotDetailsService.getUpcomingExpirations(pilotExaminationDetails, LocalDate.now());
			
			model.addAttribute("user", user);
		}
		
		model.addAttribute("upcomingExpirations", upcomingExpirations);
		
		// Active class used on header fragment
		model.addAttribute("activeClassHome", "active");
		
		return "home/home";
	}
	
	/**
	 * Shows features page on homepage. Adds active CSS class and logged in user to model
	 * 
	 * @param principal
	 * @param model
	 * @return path to home.html
	 */
	@RequestMapping("/features")
	public String showFeatures(Principal principal, Model model) {
		if (principal != null) {
			User user = usersService.getUser(principal.getName());
			model.addAttribute("user", user);
		}

		// Active class used on header fragment
		model.addAttribute("activeClassFeatures", "active");
		
		return "home/features";
	}
}
