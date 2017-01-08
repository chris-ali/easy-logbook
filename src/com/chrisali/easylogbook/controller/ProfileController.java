package com.chrisali.easylogbook.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.chrisali.easylogbook.model.User;
import com.chrisali.easylogbook.service.UsersService;
import com.chrisali.easylogbook.validation.FormValidationGroup;

@Controller
@SessionAttributes("user")
public class ProfileController {
	
	@Autowired
	private UsersService usersService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Shows profile overview page. Adds {@link User} object to Model.
	 * 
	 * @param principal
	 * @param model
	 * @return path 
	 */
	@RequestMapping("profile/view")
	public String showProfile(Principal principal, Model model) {
		User user = usersService.getUser(principal.getName());
		
		model.addAttribute("user", user);
		
		return "profile/profile";
	}
	
	/**
	 * Does update of {@link User} information. Uses specific members of binding results fields to ensure that object is only judged on parameters
	 * user edited in form. Adds user object to Model
	 * 
	 * @param user
	 * @param result
	 * @param model
	 * @return redirect to showProfile() with GET parameters showing update of user info succeeded or path to profile.html
	 */
	@RequestMapping("profile/update")
	public String doUpdateProfile(@Validated(FormValidationGroup.class) @ModelAttribute("user") User user, BindingResult result, Model model) {
		if (result.hasFieldErrors("username") || result.hasFieldErrors("name") || result.hasFieldErrors("email"))
			return "profile/profile";
		
		usersService.createOrUpdate(user);
		
		return "redirect:/profile/view?update=true&user=true";
	}
	
	/**
	 * Does update of {@link User} password. Gets user object from database so that old password can be verified with user's input, returning
	 * form validation errors for password or old password
	 * 
	 * @param user
	 * @param result
	 * @param model
	 * @return redirect to showProfile() with GET parameters showing update of password succeeded or path to profile.html
	 */
	@RequestMapping("profile/password")
	public String doUpdatePassword(@Validated(FormValidationGroup.class) User user, BindingResult result, Model model) {
		User userFromDatabase = usersService.getUser(user.getUsername());
		
		if (!passwordEncoder.matches(user.getOldPassword(), userFromDatabase.getPassword())) {
			result.rejectValue("oldPassword", "UnmatchedOldPassword.user.oldPassword");
			return "profile/profile";
		}
		
		user.setOldPassword("");
		
		if (result.hasFieldErrors("password"))
			return "profile/profile";
		
		usersService.createOrUpdate(user);
		
		return "redirect:/profile/view?update=true&pass=true";
	}
	
}
