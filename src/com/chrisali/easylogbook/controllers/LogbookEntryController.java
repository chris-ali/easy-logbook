package com.chrisali.easylogbook.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.chrisali.easylogbook.beans.Aircraft;
import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.services.AircraftService;
import com.chrisali.easylogbook.services.LogbookEntryService;
import com.chrisali.easylogbook.services.LogbookService;
import com.chrisali.easylogbook.validation.FormValidationGroup;

@Controller
public class LogbookEntryController {
	@Autowired
	private AircraftService aircraftService;

	@Autowired
	private LogbookService logbookService;
	
	@Autowired
	private LogbookEntryService logbookEntryService;
		
	@RequestMapping(value="entry/create")
	public String showCreateLogbookEntry(Model model, Principal principal) {
		List<Aircraft> aircraftList = aircraftService.getAircraft(principal.getName());
		model.addAttribute("aircraftList", aircraftList);
		model.addAttribute("logbookEntry", new LogbookEntry());
		return "logbook/createentry";
	}
	
	@RequestMapping(value="entry/docreate")
	public String doCreateLogbookEntry(@Validated(FormValidationGroup.class) Logbook logbook, 
								  		BindingResult result, Model model, Principal principal) {
		if (result.hasErrors())
			return "logbook/createentry";
		
		if (logbookService.exists(principal.getName(), logbook.getName()))
			return "logbook/createentry";
		
		try {
			String username = principal.getName();
			logbook.getUser().setUsername(username);
			logbookService.createOrUpdate(logbook);
		} catch (DuplicateKeyException e) {
			result.rejectValue("name", "DuplicateKey.logbook.name");
			return "logbook/createentry";
		}
		
		return "redirect:/logbook/show?id=" + logbook.getId();
	}
	
	@RequestMapping(value="entry/delete", method=RequestMethod.GET)
	public String deleteLogbook(Principal principal, @RequestParam("logbookId") int logbookId, 
								@RequestParam("entryId") int entryId, Model model){
		// Get logbook attached to user
		Logbook logbook = logbookService.getLogbook(principal.getName(), logbookId);
		model.addAttribute("logbook", logbook);
		
		logbookEntryService.delete(logbookId, entryId);
		
		// Active class used on header fragment
		model.addAttribute("activeClassLogbook", "active");
		
		return "redirect:/logbook/show?id=" + logbook.getId();
	}
}
