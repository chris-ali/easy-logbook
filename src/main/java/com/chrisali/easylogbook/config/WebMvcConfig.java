package com.chrisali.easylogbook.config;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	private static final Logger logger = Logger.getRootLogger();

	private static final String RESOURCES_LOCATION = "/resources/";
    private static final String RESOURCES_HANDLER = RESOURCES_LOCATION + "**";
    
	private static final String MESSAGE_SOURCE = "com.chrisali.easylogbook.messages.messages";
	private static final String MESSAGE_ENCODING = "UTF-8";
	
	private static final String VIEW_PREFIX = "/templates/";
	private static final String VIEW_SUFFIX = ".html";
	private static final String TEMPLATE_MODE = "HTML5";
	
	// ================================ Thymeleaf Setup ==============================
	
	@Bean
	public ViewResolver viewResolver() {
		logger.info("==================================");
		logger.info("Setting up Thymeleaf View Resolver");
		logger.info("==================================");
		
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding(MESSAGE_ENCODING);
		viewResolver.setOrder(1);
		
		return viewResolver;	
	}
	
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.addDialect(new SpringSecurityDialect());
		
		return templateEngine;
	}

	@Bean
	public ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix(VIEW_PREFIX);
		templateResolver.setSuffix(VIEW_SUFFIX);
		templateResolver.setTemplateMode(TEMPLATE_MODE);
		templateResolver.setCharacterEncoding(MESSAGE_ENCODING);
		templateResolver.setCacheable(false);
		
		return templateResolver;
	}

	// ===============================================================================
	
	@Bean(name = "messageSource")
	public MessageSource messageSource() {
		logger.info("==================================");
		logger.info("Setting up Message Source");
		logger.info("==================================");
		
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(MESSAGE_SOURCE);
		messageSource.setCacheSeconds(5);
		messageSource.setDefaultEncoding(MESSAGE_ENCODING);
		
		return messageSource;
	}
	
	@Bean(name = "validator")
	public Validator validator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		
		return validator;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		logger.info("==================================");
		logger.info("Setting up Resource Handlers");
		logger.info("==================================");
		
		registry.addResourceHandler(RESOURCES_HANDLER)
				.addResourceLocations(RESOURCES_LOCATION);
		
		super.addResourceHandlers(registry);
	}

	

}
