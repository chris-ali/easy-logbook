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

import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.services.LogbookEntryService;
import com.chrisali.easylogbook.services.LogbookService;
import com.chrisali.easylogbook.validation.FormValidationGroup;

@Controller
public class LogbookController {

	@Autowired
	private LogbookService logbookService;
	
	@Autowired
	private LogbookEntryService logbookEntryService;
	
	@RequestMapping(value="/singlelogbook", method=RequestMethod.GET)
	public String showSingleLogbook(Principal principal, Model model, 
							  @RequestParam("id") int id) {
		Logbook logbook = logbookService.getLogbook(principal.getName(), id);
		model.addAttribute("logbook", logbook);
		
		List<LogbookEntry> logbookEntries = logbookEntryService.getLogbookEntries(id);
		model.addAttribute("logbookEntries", logbookEntries);
		
		LogbookEntry totals = logbookService.logbookTotals(principal.getName(), id);
		model.addAttribute("totals", totals);
		
		return "logbook/logbook";
	}
	
	@RequestMapping(value="/alllogbooks")
	public String showAllLogbooks(Principal principal, Model model) {
		List<Logbook> logbooks = logbookService.getLogbooks(principal.getName());
		
		model.addAttribute("logbooks", logbooks);
		
		return "logbook/logbooks";
	}
	
	@RequestMapping(value="/createlogbook")
	public String showCreateLogbook(Model model) {
		model.addAttribute("logbook", new Logbook());
		return "logbook/createlogbook";
	}
	
	@RequestMapping(value="/docreatelogbook")
	public String doCreateLogbook(@Validated(FormValidationGroup.class) Logbook logbook, 
								  BindingResult result, Model model, Principal principal) {
		if (result.hasErrors())
			return "logbook/createlogbook";
		
		if (logbookService.exists(principal.getName(), logbook.getName()))
			return "logbook/createlogbook";
		
		try {
			String username = principal.getName();
			logbook.getUser().setUsername(username);
			logbookService.createOrUpdate(logbook);
		} catch (DuplicateKeyException e) {
			result.rejectValue("name", "DuplicateKey.logbook.name");
			return "logbook/createlogbook";
		}
		
		model.addAttribute("logbook", logbook);
		
		return "logbook/logbookcreated";
	}
	
	@RequestMapping(value="/deletelogbook", method=RequestMethod.GET)
	public String deleteLogbook(Principal principal, @RequestParam("id") int id, Model model){
		Logbook logbook = logbookService.getLogbook(principal.getName(), id);
		model.addAttribute("logbook", logbook);
		
		List<LogbookEntry> logbookEntries = logbookEntryService.getLogbookEntries(logbook.getId());
		
		for(LogbookEntry entry : logbookEntries)
			logbookEntryService.delete(id, entry.getId());
		
		logbookService.delete(principal.getName(), id);
		
		return "logbook/logbookdeleted";
	}
}
