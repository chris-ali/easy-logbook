package com.chrisali.easylogbook.controllers;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chrisali.easylogbook.beans.PilotDetail;
import com.chrisali.easylogbook.services.PilotDetailsService;
import com.chrisali.easylogbook.services.PilotDetailsService.PilotDetailsType;
import com.chrisali.easylogbook.services.UsersService;
import com.chrisali.easylogbook.validation.FormValidationGroup;

@Controller
@SessionAttributes("pilotDetail")
public class PilotDetailsController {

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private PilotDetailsService pilotDetailsService;
	
	/**
	 * Shows pilot information page. Adds all {@link PilotDetail} lists to Model
	 * 
	 * @param principal
	 * @param model
	 * @return path to pilotdetails.html
	 */
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
		
		return "details/pilotdetails";
	}
	
	/**
	 * Uses error, edit and {@link PilotDetail} id as arguments to determine whether to create a new {@link PilotDetail}
	 * or use preexisting {@link PilotDetail} to add to {@link Model}. Adds type of pilot detail to determine visible fields
	 * on form
	 * 
	 * @param type
	 * @param id of pilot detail (not required)
	 * @param error (not required)
	 * @param edit (not required)
	 * @param principal
	 * @param model
	 * @return path to createdetails.html
	 */
	@RequestMapping(value="details/create")
	public String showCreateDetails(@RequestParam("type") String type,
									@RequestParam(value="id", required=false) Integer id,
									@RequestParam(value="error", required=false) Boolean error,
									@RequestParam(value="edit", required=false) Boolean edit,
									Principal principal,
									Model model) {
		// Add new pilot detail to model if it hasn't already been done and error and edit aren't specified
		if (error == null && edit == null) {
			PilotDetail newDetail = new PilotDetail(usersService.getUser(principal.getName()), "");
			model.addAttribute("pilotDetail", newDetail);
		}
		
		// If edit detail selected, get detail corresponding to id and add to model
		if (id != null && edit != null)
			model.addAttribute("pilotDetail", pilotDetailsService.getPilotDetail(principal.getName(), id));
		
		model.addAttribute("type", type); // determines what fields are visible on createdetails page
		
		return "details/createdetails";
	}
	
	/**
	 * Uses {@link BindingResult} to validate {@link PilotDetail} form submission, which is sent back via {@link RedirectAttributes} 
	 * redirect along with binding results if errors are detected, and persisted using a session attribute tag on {@link PilotDetailsController}. 
	 * The errors it picks up are a function of which {@link PilotDetailsType} enum is specified. Uses {@link HttpServletRequest} to get
	 * type parameter to use as switch parameter to determine what field errors to judge pilot detail object on
	 * 
	 * @param pilotDetail
	 * @param result
	 * @param redirect
	 * @param request
	 * @param model
	 * @return redirect to showPilotDetails() or to showCreateDetails() if errors found
	 */
	@RequestMapping("details/docreate")
	public String doCreateDetails(@Validated(FormValidationGroup.class) @ModelAttribute("pilotDetail") PilotDetail pilotDetail, 
								  BindingResult result,
								  RedirectAttributes redirect,
								  HttpServletRequest request,
								  Model model) {
		// Get pilot detail type from HTTP request GET
		String type = (String) request.getParameter("type");
		// Switch using pilotDetailType to reject result if certain fields are filled out
		// Prevents incorrect rejections from occurring if fields on other tab pages are
		// not filled out
		switch (PilotDetailsType.valueOf(type)) {
		case ENDORSEMENTS:
			if (result.hasFieldErrors("date") || result.hasFieldErrors("endorsement")) {
				redirect.addFlashAttribute("org.springframework.validation.BindingResult.pilotDetail", result);
				redirect.addFlashAttribute("pilotDetail", pilotDetail);
				return "redirect:/details/create?error=true&type=" + type;
			}
			break;
		case EXAMINATIONS:
		case LICENSES:
		case MEDICALS:
			if (result.hasFieldErrors("date")) {
				redirect.addFlashAttribute("org.springframework.validation.BindingResult.pilotDetail", result);
				redirect.addFlashAttribute("pilotDetail", pilotDetail);
				return "redirect:/details/create?error=true&type=" + type;
			}
			break;
		case TYPERATINGS:
			if (result.hasFieldErrors("date") || result.hasFieldErrors("typeRating")) {
				redirect.addFlashAttribute("org.springframework.validation.BindingResult.pilotDetail", result);
				redirect.addFlashAttribute("pilotDetail", pilotDetail);
				return "redirect:/details/create?error=true&type=" + type;
			}
			break;
		default:
			return "redirect:/details/view";
		}
		
		pilotDetailsService.createOrUpdate(pilotDetail);
		
		return "redirect:/details/view";
	}
	
	/**
	 * Deletes {@link PilotDetail} object from database
	 * 
	 * @param principal
	 * @param id of pilot detail
	 * @return redirect to showPilotDetails()
	 */
	@RequestMapping("details/delete")
	public String doDeleteDetails(Principal principal, @RequestParam("id") int id) {
				
		pilotDetailsService.delete(principal.getName(), id);
		
		return "redirect:/details/view";
	}
	
}
