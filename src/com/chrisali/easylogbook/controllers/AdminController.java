package com.chrisali.easylogbook.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.services.UsersService;

@Controller
public class AdminController {
	
	@Autowired
	private UsersService usersService;

	@RequestMapping("/admin")
	public String showAdmin(Model model) {
		List<User> users = usersService.getAllUsers();
		model.addAttribute("users", users);
		
		// Active class used on header fragment
		model.addAttribute("activeClassAdmin", "active");
		
		return "admin/admin";
	}
}
