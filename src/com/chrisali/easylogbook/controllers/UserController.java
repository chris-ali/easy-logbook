package com.chrisali.easylogbook.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.chrisali.easylogbook.beans.Aircraft;
import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.services.AircraftService;
import com.chrisali.easylogbook.services.LogbookEntryService;
import com.chrisali.easylogbook.services.LogbookService;
import com.chrisali.easylogbook.services.UsersService;
import com.chrisali.easylogbook.validation.FormValidationGroup;

@Controller
@SessionAttributes("user")
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
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping("user/create")
	public String showCreateAccount(Model model) {
		model.addAttribute("user", new User());
		
		return "user/createaccount";
	}
	
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
	
	@RequestMapping("profile/view")
	public String showProfile(Principal principal, Model model) {
		User user = usersService.getUser(principal.getName());
		
		model.addAttribute("user", user);
		model.addAttribute("oldPassword", user.getPassword());
		model.addAttribute("passwordEncoder", passwordEncoder);
		
		return "profile/profile";
	}
	
	@RequestMapping("profile/details")
	public String showPilotDetails(Principal principal, Model model) {
		User user = usersService.getUser(principal.getName());
		
		model.addAttribute("user", user);
		
		return "profile/pilotdetails";
	}
	
	@RequestMapping("profile/update")
	public String doUpdateDetails(@Validated(FormValidationGroup.class) @ModelAttribute("user") User user, BindingResult result, Model model) {
		if (result.hasFieldErrors("username") || result.hasFieldErrors("name") || result.hasFieldErrors("email"))
			return "profile/profile";
		
		usersService.createOrUpdate(user);
		
		return "redirect:/profile/view?success=true";
	}
	
	@RequestMapping("profile/password")
	public String doUpdatePassword(@Validated(FormValidationGroup.class) User user, BindingResult result, Model model) {
		if (result.hasFieldErrors("password"))
			return "profile/profile";
		
		usersService.createOrUpdate(user);
		
		return "redirect:/profile/view?success=true";
	}

	@RequestMapping("user/close")
	public String doCloseAccount(Principal principal, BindingResult result, Model model) {
		String username = principal.getName();
		
		List<Logbook> logbooks = logbookService.getLogbooks(username);
		for (Logbook logbook : logbooks) {
			List<LogbookEntry> entries = logbookEntryService.getLogbookEntries(logbook.getId());

			for (LogbookEntry entry : entries)
				logbookEntryService.delete(logbook.getId(), entry.getId());
			
			logbookService.delete(username, logbook.getId());
		}
		
		List<Aircraft> aircraftList = aircraftService.getAircraft(username);
		for (Aircraft aircraft : aircraftList) 
			aircraftService.delete(username, aircraft.getId());
		
		usersService.delete(username);
		
		return "home/home";
	}
	
	@RequestMapping("user/login")
	public String showLogin() {
		return "user/login";
	}
	
	@RequestMapping("user/logout")
	public String showLoggedOut() {
		return "user/logout";
	}
}
