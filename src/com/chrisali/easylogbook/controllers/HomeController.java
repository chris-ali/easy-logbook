package com.chrisali.easylogbook.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.services.UsersService;

@Controller
public class HomeController {
	
	@Autowired
	private UsersService usersService;
	
	@RequestMapping("/")
	public String showHome(Principal principal, Model model) {
		if (principal != null) {
			User user = usersService.getUser(principal.getName());
			model.addAttribute("user", user);
		}
		
		// Active class used on header fragment
		model.addAttribute("activeClassHome", "active");
		
		return "home/home";
	}
	
	@RequestMapping("/features")
	public String showFeatures(Principal principal, Model model) {
		if (principal != null) {
			User user = usersService.getUser(principal.getName());
			model.addAttribute("user", user);
		}

		// Active class used on header fragment
		model.addAttribute("activeClassFeatures", "active");
		
		return "home/features";
	}
	
	@RequestMapping("/admin")
	public String showAdmin(Model model) {
		List<User> users = usersService.getAllUsers();
		model.addAttribute("users", users);
		
		// Active class used on header fragment
		model.addAttribute("activeClassAdmin", "active");
		
		return "home/admin";
	}
}
