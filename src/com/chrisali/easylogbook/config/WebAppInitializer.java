package com.chrisali.easylogbook.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer, HttpSessionListener {

	private static final String CONFIG_LOCATION = "com.chrisali.easylogbook.config";
	private static final String MAPPING_URL = "/";
	private static final String DISPLAY_NAME = "EasyLogbook";
	
	private static final Logger logger = Logger.getRootLogger();
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		logger.debug("==================================");
		logger.debug("Initializing Web Application");
		logger.debug("==================================");
		
		AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
		appContext.setConfigLocation(CONFIG_LOCATION);
		appContext.setDisplayName(DISPLAY_NAME);
		appContext.setServletContext(servletContext);
		appContext.getEnvironment().setDefaultProfiles("prod");
		appContext.register(WebMvcConfig.class);
		appContext.refresh();
		
		servletContext.setInitParameter("defaultHtmlEscape", "true");
		servletContext.addListener(new ContextLoaderListener(appContext));
		servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class)
						.addMappingForUrlPatterns(null, false, "/*");
		
		Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(appContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping(MAPPING_URL);
	}

	@Override
	public void sessionCreated(HttpSessionEvent session) {
		session.getSession().setMaxInactiveInterval(10*60);
		logger.debug("Session Created");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent session) {
		logger.debug("Session Destroyed");	
	}
}
