package com.chrisali.easylogbook.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chrisali.easylogbook.model.Aircraft;
import com.chrisali.easylogbook.model.Logbook;
import com.chrisali.easylogbook.model.LogbookEntry;
import com.chrisali.easylogbook.model.PilotDetail;
import com.chrisali.easylogbook.model.User;
import com.chrisali.easylogbook.service.AircraftService;
import com.chrisali.easylogbook.service.LogbookEntryService;
import com.chrisali.easylogbook.service.LogbookService;
import com.chrisali.easylogbook.service.PilotDetailsService;
import com.chrisali.easylogbook.service.UsersService;
import com.chrisali.easylogbook.service.PilotDetailsService.PilotDetailsType;
import com.chrisali.easylogbook.validation.FormValidationGroup;

@Controller
public class UserController {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private LogbookService logbookService;
	
	@Autowired
	private AircraftService aircraftService;
	
	@Autowired
	private LogbookEntryService logbookEntryService;
	
	@Autowired
	private PilotDetailsService pilotDetailsService;
	
	/**
	 * Shows {@link User} creation page. Adds new user object to model
	 * 
	 * @param model
	 * @return path to createaccount.html
	 */
	@RequestMapping("user/create")
	public String showCreateAccount(Model model) {
		model.addAttribute("user", new User());
		
		return "user/createaccount";
	}
	
	/**
	 * Does creation of {@link User}. Rejects creation and shows form validation errors if binding result finds errors, username
	 * already exists or DuplicateKeyException is thrown. User is added to Model
	 * 
	 * @param user
	 * @param result
	 * @param model
	 * @return path to accountcreated.html
	 */
	@RequestMapping(value="user/docreate", method=RequestMethod.POST)
	public String doCreateAccount(@Validated(FormValidationGroup.class) User user, BindingResult result, Model model) {
		if (result.hasErrors())
			return "user/createaccount";
		
		user.setEnabled(true);
		user.setAuthority("ROLE_USER");
		
		if (usersService.exists(user.getUsername())) {
			result.rejectValue("username", "DuplicateKey.user.username");
			return "user/createaccount";
		}
		
		try {
			usersService.createOrUpdate(user);
		} catch (DuplicateKeyException e) {
			result.rejectValue("username", "DuplicateKey.user.username");
			return "user/createaccount";
		}
		
		model.addAttribute("user", user);
		
		return "user/accountcreated";
	}

	/**
	 * Does deletion of {@link User} from database. Removes all {@link Logbook}, {@link LogbookEntry}, 
	 * {@link Aircraft} and {@link PilotDetail} data tied to account
	 * 
	 * @param principal
	 * @return path to home.html
	 */
	@RequestMapping("user/close")
	public String doCloseAccount(Principal principal) {
		String username = principal.getName();
		
		List<Logbook> logbooks = logbookService.getLogbooks(username);
		for (Logbook logbook : logbooks) {
			List<LogbookEntry> entries = logbookEntryService.getAllLogbookEntries(logbook.getId());

			for (LogbookEntry entry : entries)
				logbookEntryService.delete(logbook.getId(), entry.getId());
			
			logbookService.delete(username, logbook.getId());
		}
		
		List<Aircraft> aircraftList = aircraftService.getAircraft(username);
		for (Aircraft aircraft : aircraftList) 
			aircraftService.delete(username, aircraft.getId());
		
		List<PilotDetail> pilotDetails = pilotDetailsService.getPilotDetails(username, PilotDetailsType.ALL);
		for (PilotDetail detail : pilotDetails)
			pilotDetailsService.delete(username, detail.getId());
		
		usersService.delete(username);
		
		return "home/home";
	}
	
	/**
	 * @return path to login.html
	 */
	@RequestMapping("user/login")
	public String showLogin() {
		return "user/login";
	}
	
	/**
	 * @return path to logout.html
	 */
	@RequestMapping("user/logout")
	public String showLoggedOut() {
		return "user/logout";
	}
}