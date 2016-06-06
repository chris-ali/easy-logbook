package com.chrisali.easylogbook.controllers;

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

import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.services.UsersService;
import com.chrisali.easylogbook.validation.FormValidationGroup;

@Controller
@SessionAttributes("user")
public class ProfileController {
	
	@Autowired
	private UsersService usersService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping("profile/view")
	public String showProfile(Principal principal, Model model) {
		User user = usersService.getUser(principal.getName());
		
		model.addAttribute("user", user);
		
		return "profile/profile";
	}
	
	@RequestMapping("profile/update")
	public String doUpdateProfile(@Validated(FormValidationGroup.class) @ModelAttribute("user") User user, BindingResult result, Model model) {
		if (result.hasFieldErrors("username") || result.hasFieldErrors("name") || result.hasFieldErrors("email"))
			return "profile/profile";
		
		usersService.createOrUpdate(user);
		
		return "redirect:/profile/view?update=true&user=true";
	}
	
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
