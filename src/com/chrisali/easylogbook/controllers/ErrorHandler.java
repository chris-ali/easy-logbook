package com.chrisali.easylogbook.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
@Controller
public class ErrorHandler {
	
	/**
	 * @param e
	 * @return path to databaseerror.html if DataAccessException thrown
	 */
	@ExceptionHandler(DataAccessException.class)
	public String handleDatabaseException(DataAccessException e) {
		return "error/databaseerror";
	}
	
	/**
	 * @param e
	 * @return path to accessdenied.html if AccessDeniedException thrown
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public String handleAccessDeniedException(AccessDeniedException e) {
		return "error/accessdenied";
	}
	
	/**
	 * @return path to accessdenied.html
	 */
	@RequestMapping("/error/accessdenied")
	public String showAccessDenied() {
		return "error/accessdenied";
	}
}
