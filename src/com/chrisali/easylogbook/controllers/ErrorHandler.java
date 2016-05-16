package com.chrisali.easylogbook.controllers;

import java.nio.file.AccessDeniedException;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {
	
	@ExceptionHandler(DataAccessException.class)
	public String handleDatabaseException(DataAccessException e) {
		return "error/databaseerror";
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public String handleAccessDeniedException(AccessDeniedException e) {
		return "error/accessdenied";
	}
}
