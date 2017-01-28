package com.chrisali.easylogbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.chrisali.easylogbook.Application;

@Configuration
@PropertySource("classpath:com/chrisali/easylogbook/config/datasource.properties")
@PropertySource("classpath:log4j.properties")
@ComponentScan(basePackageClasses = Application.class)
public class ApplicationConfig {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
