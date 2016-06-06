package com.chrisali.easylogbook.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chrisali.easylogbook.beans.PilotDetail;
import com.chrisali.easylogbook.services.PilotDetailsService;
import com.chrisali.easylogbook.services.PilotDetailsService.PilotDetailsType;

@Controller
public class PilotDetailsController {

	@Autowired
	private PilotDetailsService pilotDetailsService;

	@RequestMapping("details/view")
	public String showPilotDetails(Principal principal, Model model) {
		
		String username = principal.getName();
		
		List<PilotDetail> pilotLicenseDetails = pilotDetailsService.getPilotDetails(username, PilotDetailsType.LICENSES);
		List<PilotDetail> pilotMedicalDetails = pilotDetailsService.getPilotDetails(username, PilotDetailsType.MEDICALS);
		List<PilotDetail> pilotExamDetails = pilotDetailsService.getPilotDetails(username, PilotDetailsType.EXAMINATIONS);
		List<PilotDetail> pilotTypeRatingDetails = pilotDetailsService.getPilotDetails(username, PilotDetailsType.TYPERATINGS);
		List<PilotDetail> pilotEndorsementDetails = pilotDetailsService.getPilotDetails(username, PilotDetailsType.ENDORSEMENTS);
		
		model.addAttribute("pilotLicenseDetails", pilotLicenseDetails);
		model.addAttribute("pilotMedicalDetails", pilotMedicalDetails);
		model.addAttribute("pilotExamDetails", pilotExamDetails);
		model.addAttribute("pilotTypeRatingDetails", pilotTypeRatingDetails);
		model.addAttribute("pilotEndorsementDetails", pilotEndorsementDetails);
		
		return "profile/pilotdetails";
	}
	
	@RequestMapping("details/update")
	public String doUpdateDetails(@Validated PilotDetail pilotDetail, @RequestParam("type") PilotDetailsType type, BindingResult result, Model model) {
		// Switch using pilotDetailType to reject result if certain fields are filled out
		// Prevents needless rejections from occurring if fields on other tab pages are
		// not filled out
		switch (type) {
		case ENDORSEMENTS:
			if (result.hasFieldErrors("date") || result.hasFieldErrors("endorsement"))
				return "profile/pilotdetails";
			break;
		case EXAMINATIONS:
		case LICENSES:
		case MEDICALS:
			if (result.hasFieldErrors("date"))
				return "profile/pilotdetails";
			break;
		case TYPERATINGS:
			if (result.hasFieldErrors("date") || result.hasFieldErrors("typeRating"))
				return "profile/pilotdetails";
			break;
		default:
			return "redirect:/details/view";
		}
		
		pilotDetailsService.createOrUpdate(pilotDetail);
		
		return "redirect:/details/view";
	}
	
	@RequestMapping("details/delete")
	public String doDeleteDetails(Principal principal, @RequestParam("id") int id) {
				
		pilotDetailsService.delete(principal.getName(), id);
		
		return "redirect:/details/view";
	}
	
}
