package com.chrisali.easylogbook.controllers;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chrisali.easylogbook.beans.Aircraft;
import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.services.AircraftService;
import com.chrisali.easylogbook.services.LogbookEntryService;
import com.chrisali.easylogbook.services.LogbookService;
import com.chrisali.easylogbook.validation.FormValidationGroup;

@Controller
@SessionAttributes("logbookEntry")
public class LogbookEntryController {
	@Autowired
	private AircraftService aircraftService;

	@Autowired
	private LogbookService logbookService;
	
	@Autowired
	private LogbookEntryService logbookEntryService;
	
	/**
	 * Uses error, edit and {@link LogbookEntry} id as arguments to determine whether to create a new {@link LogbookEntry}
	 * or use preexisting {@link LogbookEntry} to add to {@link Model}
	 * 
	 * @param logbookId
	 * @param entryId
	 * @param error
	 * @param edit
	 * @param model
	 * @param principal
	 * @return mapping to createentry page
	 */
	@RequestMapping(value="entry/create")
	public String showCreateLogbookEntry(@RequestParam("logbookId") Integer logbookId,
										 @RequestParam(value="entryId", required=false) Integer entryId,
										 @RequestParam(value="error", required=false) Boolean error,
										 @RequestParam(value="edit", required=false) Boolean edit,
										 Model model, 
										 Principal principal) {
		List<Aircraft> aircraftList = aircraftService.getAircraft(principal.getName());
		Logbook logbook = logbookService.getLogbook(principal.getName(), logbookId);
		
		model.addAttribute("aircraftList", aircraftList);
		model.addAttribute("logbook", logbook);
		
		// Add new logbook entry to model if it hasn't already been done
		if (error == null && edit == null) {
			LogbookEntry freshLogbookEntry = new LogbookEntry();
			freshLogbookEntry.setLogbook(logbook);
			model.addAttribute("logbookEntry", freshLogbookEntry);
		} 
		
		// If edit entry selected, get entry corresponding to entryId and add to model
		if (entryId != null && edit != null) {
			LogbookEntry editLogbookEntry = logbookEntryService.getLogbookEntry(logbookId, entryId);
			editLogbookEntry.setLogbook(logbook);
			model.addAttribute("logbookEntry", editLogbookEntry);
		}
		
		return "entry/createentry";
	}
	
	/**
	 * Uses {@link BindingResult} to validate {@link LogbookEntry} form submission, which is sent back via {@link RedirectAttributes} 
	 * redirect along with binding results if errors are detected.
	 * 
	 * @param logbookEntry
	 * @param result
	 * @param redirect
	 * @param model
	 * @param principal
	 * @param request
	 * @return redirect to create/view logbook entries
	 */
	@RequestMapping(value="entry/docreate", method=RequestMethod.POST)
	public String doCreateLogbookEntry(@Validated(FormValidationGroup.class) @ModelAttribute("logbookEntry") LogbookEntry logbookEntry, 
								  		BindingResult result, 
								  		RedirectAttributes redirect, 
								  		Model model, 
								  		Principal principal, 
								  		HttpServletRequest request) {
		int logbookId = logbookEntry.getLogbook().getId();
		int aircraftId = Integer.valueOf(request.getParameter("aircraftId"));
		
		logbookEntry.setLogbook(logbookService.getLogbook(principal.getName(), logbookId));
		logbookEntry.setAircraft(aircraftService.getAircraft(principal.getName(), aircraftId));
		
		if (result.hasErrors()) {
			redirect.addFlashAttribute("org.springframework.validation.BindingResult.logbookEntry", result);
			redirect.addFlashAttribute("logbookEntry", logbookEntry);
			return "redirect:/entry/create?logbookId=" + logbookId + "&error=true";
		}
		
		try {
			logbookEntryService.createOrUpdate(logbookEntry);
		} catch (DuplicateKeyException e) {
			redirect.addFlashAttribute("org.springframework.validation.BindingResult.messageForm", result);
			redirect.addFlashAttribute("logbookEntry", logbookEntry);
			return "redirect:/entry/create?logbookId=" + logbookId + "&error=true";
		}
		
		// Active class used on header fragment
		model.addAttribute("activeClassLogbook", "active");
		
		return "redirect:/logbook/show?id=" + logbookId;
	}
	
	@RequestMapping(value="entry/delete", method=RequestMethod.GET)
	public String deleteLogbook(Principal principal, 
								@RequestParam("logbookId") int logbookId, 
								@RequestParam("entryId") int entryId, 
								Model model){
		// Get logbook attached to user
		Logbook logbook = logbookService.getLogbook(principal.getName(), logbookId);
		model.addAttribute("logbook", logbook);
		
		logbookEntryService.delete(logbookId, entryId);
		
		// Active class used on header fragment
		model.addAttribute("activeClassLogbook", "active");
		
		return "redirect:/logbook/show?id=" + logbook.getId();
	}
}
