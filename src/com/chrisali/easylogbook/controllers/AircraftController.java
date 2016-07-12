package com.chrisali.easylogbook.controllers;

import java.security.Principal;
import java.util.HashMap;
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
	
	/**
	 * Shows all {@link Aircraft} tied to single user, using pagination to organize list. 
	 * Adds list of aircraft, total time logged on aircraft and active CSS header class to model
	 * 
	 * @param principal
	 * @param model
	 * @param pageNumber (not required, defaults to page 1)
	 * @param resultsSize (not required, defaults to 10 per page)
	 * @return path to aircraft.html
	 */
	@RequestMapping("aircraft/overview")
	public String showAllAircraft(Principal principal, Model model, 
								  @RequestParam(value="page", required=false) Integer pageNumber,
								  @RequestParam(value="results", required=false) Integer resultsSize) {
		
		// Get all aircraft tied to user
		List<Aircraft> aircraftList = aircraftService.getAircraft(principal.getName());
		model.addAttribute("aircraftList", aircraftList);
		
		// Create list of totals for each aircraft belonging to user
		Map<String, Float> aircraftTotals = new HashMap<>();
		for (Aircraft aircraft : aircraftList)
			aircraftTotals.put(aircraft.getTailNumber(), 
							   aircraftService.loggedTimeAircraft(aircraft.getId()));
		model.addAttribute("aircraftTotals", aircraftTotals);
		
		// Active class used on header fragment
		model.addAttribute("activeClassAircraft", "active");
		
		return "aircraft/aircraft";
	}
	
	/**
	 * Shows create aircraft page; adds new aircraft object to model
	 * 
	 * @param model
	 * @return path to createaircraft.html
	 */
	@RequestMapping(value="aircraft/create")
	public String showCreateAircraft(Model model) {
		model.addAttribute("aircraft", new Aircraft());
		return "aircraft/createaircraft";
	}
	
	/**
	 * Does creation of {@link Aircraft} into database; if specified parameters violate BindingResult object,
	 * aircraft already exists in database or DuplicateKeyException is thrown createaircraft.html's path
	 * will be returned with form errors. Adds aircraft object and active CSS header class to model  
	 * 
	 * @param aircraft
	 * @param result
	 * @param model
	 * @param principal
	 * @return path to createaircraft.html or redirects to showAllAircraft()
	 */
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
	
	/**
	 * Deletes specified {@link Aircraft} from database; deletes all {@link LogbookEntry} tied to aircraft object beforehand.
	 * Adds active CSS header class to model.
	 * 
	 * @param principal
	 * @param model
	 * @param aircraftId
	 * @return redirect to showAllAircraft()
	 */
	@RequestMapping(value="aircraft/delete")
	public String deleteAircraft(Principal principal, Model model, @RequestParam("id") int aircraftId){
		
		// Get all logbooks tied to user
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
	
	/**
	 * Does edit of {@link Aircraft} using JSON data passed in via Map. Edits all logbook entries tied to aircraft object beforehand.
	 * Adds active CSS header class to model  
	 * 
	 * @param principal
	 * @param model
	 * @param editDetails
	 * @return
	 */
	@RequestMapping(value="aircraft/edit", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public String doEditAircraft(Principal principal, Model model, @RequestBody Map<String, Object> editDetails) {
		String username = principal.getName();
		
		// Data parsed from JSON
		String aircraftId = (String)editDetails.get("id");
		String make = (String)editDetails.get("make");
		String modelName = (String)editDetails.get("model");
		String tailNumber = (String)editDetails.get("tailNumber");
		
		// Update details of aircraft
		Aircraft aircraft = aircraftService.getAircraft(username, Integer.valueOf(aircraftId));
		aircraft.setMake(make);
		aircraft.setModel(modelName);
		aircraft.setTailNumber(tailNumber);
		
		// Edit all logbook entries tied to aircraft in db before editing aircraft in db
		List<Logbook> logbooks = logbookService.getLogbooks(username);
		
		for (Logbook logbook : logbooks) {
			List<LogbookEntry> logbookEntries = logbookEntryService.getLogbookEntriesByAircraft(logbook.getId(), aircraft.getId());
			
			for(LogbookEntry entry : logbookEntries) {
				entry.setAircraft(aircraft);
				logbookEntryService.createOrUpdate(entry);
			}
		}
		
		aircraftService.createOrUpdate(aircraft);
		
		// Active class used on header fragment
		model.addAttribute("activeClassAircraft", "active");
		
		return "redirect:/aircraft/overview";
	}
}
