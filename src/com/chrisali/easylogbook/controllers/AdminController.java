package com.chrisali.easylogbook.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chrisali.easylogbook.beans.Aircraft;
import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.services.AircraftService;
import com.chrisali.easylogbook.services.LogbookEntryService;
import com.chrisali.easylogbook.services.LogbookService;
import com.chrisali.easylogbook.services.UsersService;

@Controller
public class AdminController {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private LogbookService logbookService;
	
	@Autowired
	private AircraftService aircraftService;
	
	@Autowired
	private LogbookEntryService logbookEntryService;

	@RequestMapping("/admin/view")
	public String showAdmin(Model model, 
							@RequestParam(value="page", required=false) Integer pageNumber,
							@RequestParam(value="results", required=false) Integer resultsSize) {
		pageNumber = (pageNumber == null) ? 0 : pageNumber;
		resultsSize = (resultsSize == null) ? 10 : resultsSize;
		List<User> usersList = usersService.getAllUsers(pageNumber, resultsSize);
		model.addAttribute("usersList", usersList);
		
		// Active class used on header fragment
		model.addAttribute("activeClassAdmin", "active");
		
		return "admin/admin";
	}
	
	@RequestMapping(value="/admin/edituser", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public String doAdminEdit(@RequestBody Map<String, Object> editDetails) {
		String username = (String)editDetails.get("username");
		String enabled = (String)editDetails.get("enabled");
		String authority = (String)editDetails.get("authority");
		
		User user = usersService.getUser(username);
		user.setAuthority(authority);
		user.setEnabled(Boolean.valueOf(enabled));
		
		usersService.createOrUpdate(user);
		
		return "redirect:/admin/view";
	}
	
	@RequestMapping("/admin/delete")
	public String doCloseAccount(Principal principal,
								 @RequestParam("username") String username) {
		
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
		
		return "redirect:/admin/view";
	}
}
