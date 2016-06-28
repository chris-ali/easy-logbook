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
public class LogbookController {
	@Autowired
	private AircraftService aircraftService;

	@Autowired
	private LogbookService logbookService;
	
	@Autowired
	private LogbookEntryService logbookEntryService;
	
	@RequestMapping(value="logbook/show", method=RequestMethod.GET)
	public String showSingleLogbook(Principal principal, Model model, 
									@RequestParam("id") int id,
									@RequestParam(value="page", required=false) Integer pageNumber,
									@RequestParam(value="results", required=false) Integer resultsSize) {
		pageNumber = (pageNumber == null) ? 0 : 
			 		 (pageNumber <= 0)    ? 0 : --pageNumber;
		resultsSize = (resultsSize == null) ? 10 : resultsSize;
		
		// All aircraft tied to user
		List<Aircraft> aircraftList = aircraftService.getAircraft(principal.getName());
		model.addAttribute("aircraftList", aircraftList);
		
		// Single logbook to view on page
		Logbook logbook = logbookService.getLogbook(principal.getName(), id);
		model.addAttribute("logbook", logbook);
		
		// Entries tied to single logbook above for a certain page from request param
		List<LogbookEntry> logbookEntries = logbookEntryService.getPaginatedLogbookEntries(id, pageNumber, resultsSize);
		Integer numberPages = (int) ((logbookEntryService.getTotalNumberLogbookEntries(id) + resultsSize) / resultsSize);
		
		model.addAttribute("logbookEntries", logbookEntries);
		model.addAttribute("numberPages", numberPages);
		
		// Totals for single logbook above
		LogbookEntry totals = logbookService.logbookTotals(principal.getName(), id);
		model.addAttribute("totals", totals);
		
		// Active class used on header fragment
		model.addAttribute("activeClassLogbook", "active");
		
		return "logbook/logbook";
	}
	
	@RequestMapping(value="logbook/overview")
	public String showAllLogbooks(Principal principal, Model model) {
		// All logbooks tied to user
		List<Logbook> logbooks = logbookService.getLogbooks(principal.getName());
		model.addAttribute("logbooks", logbooks);
		
		// Get totals for each logbook in user's list, add them to a map
		Map<Integer, LogbookEntry> logbookTotals = new HashMap<>();
		for (Logbook logbook : logbooks)
			logbookTotals.put(logbook.getId(), logbookService.logbookTotals(principal.getName(), logbook.getId()));
		model.addAttribute("logbookTotals", logbookTotals);

		// Totals for all logbooks belonging to user
		LogbookEntry overallTotals = logbookService.overallLogbookTotals(principal.getName());
		model.addAttribute("overallTotals", overallTotals);
		
		// Active class used on header fragment
		model.addAttribute("activeClassLogbook", "active");
		
		return "logbook/logbooks";
	}
	
	@RequestMapping(value="logbook/create")
	public String showCreateLogbook(Model model) {
		model.addAttribute("logbook", new Logbook());
		return "logbook/createlogbook";
	}
	
	@RequestMapping(value="logbook/docreate")
	public String doCreateLogbook(@Validated(FormValidationGroup.class) Logbook logbook, 
								  BindingResult result, Model model, Principal principal) {
		if (result.hasErrors())
			return "logbook/createlogbook";
		
		if (logbookService.exists(principal.getName(), logbook.getName())) {
			result.rejectValue("name", "DuplicateKey.logbook.name");
			return "logbook/createlogbook";
		}
		
		try {
			String username = principal.getName();
			logbook.getUser().setUsername(username);
			logbookService.createOrUpdate(logbook);
		} catch (DuplicateKeyException e) {
			result.rejectValue("name", "DuplicateKey.logbook.name");
			return "logbook/createlogbook";
		}
		
		model.addAttribute("logbook", logbook);
		
		return "redirect:/logbook/overview";
	}
	
	@RequestMapping(value="logbook/delete", method=RequestMethod.GET)
	public String deleteLogbook(Principal principal, @RequestParam("id") int id, Model model){
		// Get logbook attached to user
		Logbook logbook = logbookService.getLogbook(principal.getName(), id);
		model.addAttribute("logbook", logbook);
		
		// Delete all entries in logbook before deleting logbook 
		List<LogbookEntry> logbookEntries = logbookEntryService.getAllLogbookEntries(logbook.getId());
		
		for(LogbookEntry entry : logbookEntries)
			logbookEntryService.delete(id, entry.getId());
		
		logbookService.delete(principal.getName(), id);
		
		// Active class used on header fragment
		model.addAttribute("activeClassLogbook", "active");
		
		return "redirect:/logbook/overview";
	}
	
	@RequestMapping(value="logbook/edit", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public String editLogbook(Principal principal, @RequestBody Map<String, Object> editDetails) {
		String username = principal.getName();
		String id = (String)editDetails.get("id");
		String name = (String)editDetails.get("name");
		
		Logbook logbook = logbookService.getLogbook(username, Integer.parseInt(id));
		logbook.setName(name);
		
		logbookService.createOrUpdate(logbook);
		
		return "redirect:/logbook/overview";
	}
}
