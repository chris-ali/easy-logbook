package com.chrisali.easylogbook.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@RequestMapping("aircraft/overview")
	public String showAllAircraft(Principal principal, Model model, 
								  @RequestParam(value="page", required=false) Integer page,
								  @RequestParam(value="results", required=false) Integer resultsSize) {
		
		// Get all aircraft tied to user
		List<Aircraft> aircraftList = aircraftService.getAircraft(principal.getName());
		model.addAttribute("aircraftList", aircraftList);
		
		// Active class used on header fragment
		model.addAttribute("activeClassAircraft", "active");
		
		return "aircraft/aircraft";
	}
	
	@RequestMapping(value="aircraft/create")
	public String showCreateAircraft(Model model) {
		model.addAttribute("aircraft", new Aircraft());
		return "aircraft/createaircraft";
	}
	
	@RequestMapping(value="aircraft/docreate")
	public String doCreateAircraft(@Validated(FormValidationGroup.class) Aircraft aircraft, 
								  BindingResult result, Model model, Principal principal) {
		if (result.hasErrors())
			return "aircraft/createaircraft";
		
		if (aircraftService.exists(principal.getName(), aircraft.getTailNumber())) {
			result.rejectValue("name", "DuplicateKey.aircraft.name");
			return "aircraft/createaircraft";
		}
		
		try {
			String username = principal.getName();
			aircraft.getUser().setUsername(username);
			aircraftService.createOrUpdate(aircraft);
		} catch (DuplicateKeyException e) {
			result.rejectValue("name", "DuplicateKey.aircraft.name");
			return "aircraft/createaircraft";
		}
		
		model.addAttribute("aircraft", aircraft);
		
		// Active class used on header fragment
		model.addAttribute("activeClassAircraft", "active");
		
		return "redirect:/aircraft/overview";
	}
	
	@RequestMapping(value="aircraft/delete")
	public String deleteAircraft(Principal principal, Model model, @RequestParam("id") int aircraftId){
		
		// Get all aircraft tied to user
		String username = principal.getName();
		Aircraft aircraft = aircraftService.getAircraft(username, aircraftId);
		List<Logbook> logbooks = logbookService.getLogbooks(username);
		
		// Delete all logbook entries tied to aircraft before deleting aircraft
		for (Logbook logbook : logbooks) {
		
			List<LogbookEntry> logbookEntries = logbookEntryService.getLogbookEntriesByAircraft(logbook.getId(), aircraft.getId());
			
			for(LogbookEntry entry : logbookEntries)
				logbookEntryService.delete(logbook.getId(), entry.getId());
		}
		
		aircraftService.delete(principal.getName(), aircraftId);
		
		// Active class used on header fragment
		model.addAttribute("activeClassAircraft", "active");
		
		return "redirect:/aircraft/overview";
	}
	
	@RequestMapping(value="aircraft/edit", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public String doEditAircraft(Principal principal, @RequestBody Map<String, Object> editDetails) {
		String username = principal.getName();
		
		String aircraftId = (String)editDetails.get("id");
		String make = (String)editDetails.get("make");
		String modelName = (String)editDetails.get("model");
		String tailNumber = (String)editDetails.get("tailNumber");
		
		Aircraft aircraft = aircraftService.getAircraft(username, Integer.valueOf(aircraftId));
		aircraft.setMake(make);
		aircraft.setModel(modelName);
		aircraft.setTailNumber(tailNumber);
		
		aircraftService.createOrUpdate(aircraft);
		
		return "redirect:/aircraft/overview";
	}
}
