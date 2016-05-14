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
import org.springframework.web.bind.annotation.RequestParam;

import com.chrisali.easylogbook.beans.Aircraft;
import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.services.AircraftService;
import com.chrisali.easylogbook.services.LogbookEntryService;
import com.chrisali.easylogbook.services.LogbookService;
import com.chrisali.easylogbook.validation.FormValidationGroup;

@Controller
public class AircraftController {

	@Autowired
	private AircraftService aircraftService;
	
	@Autowired
	private LogbookService logbookService;
	
	@Autowired
	private LogbookEntryService logbookEntryService;
	
	@RequestMapping("/aircraft")
	public String showAircraft(Principal principal, Model model) {
		List<Aircraft> aircraft = aircraftService.getAircraft(principal.getName());
		
		model.addAttribute("aircraft", aircraft);
		
		return "aircraft";
	}
	
	@RequestMapping("/aircraft")
	public String showAircraft(Principal principal, Model model, 
							   @RequestParam("aircraftId") int aircraftId) {
		Aircraft aircraft = aircraftService.getAircraft(principal.getName(), aircraftId);
		model.addAttribute("aircraft", aircraft);
		
		return "aircraft";
	}
	
	@RequestMapping(value="/createaircraft")
	public String showCreateLogbook(Model model) {
		model.addAttribute("aircraft", new Aircraft());
		return "createaircraft";
	}
	
	@RequestMapping(value="/docreateaircraft")
	public String doCreateLogbook(@Validated(FormValidationGroup.class) Aircraft aircraft, 
								  BindingResult result, Model model, Principal principal) {
		if (result.hasErrors())
			return "createaircraft";
		
		if (aircraftService.exists(principal.getName(), aircraft.getId()))
			return "createaircraft";
		
		try {
			String username = principal.getName();
			aircraft.getUser().setUsername(username);
			aircraftService.createOrUpdate(aircraft);
		} catch (DuplicateKeyException e) {
			result.rejectValue("name", "DuplicateKey.aircraft.name");
			return "createaircraft";
		}
		
		model.addAttribute("aircraft", aircraft);
		
		return "aircraftcreated";
	}
	
	/*
	@RequestMapping(value="/deleteaircraft")
	public String deleteLogbook(Principal principal, @RequestParam("aircraftId") int aircraftId){
		
		String username = principal.getName();
		Aircraft aircraft = aircraftService.getAircraft(username, aircraftId);
		List<Logbook> logbooks = logbookService.getLogbooks(username);
		
		for (logbook)
		
			List<LogbookEntry> logbookEntries = logbookEntryService.getLogbookEntries(logbook.getId());
			
			for(LogbookEntry entry : logbookEntries)
				logbookEntryService.delete(logbookId, entry.getId());
		
		aircraftService.delete(principal.getName(), aircraftId);
		
		return "aircraftdeleted";
	}
	*/
}
