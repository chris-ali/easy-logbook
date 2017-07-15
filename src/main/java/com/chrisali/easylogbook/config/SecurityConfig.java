package com.chrisali.easylogbook.config;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger log = Logger.getRootLogger();
	private static final int TOKEN_VALIDITY = 3600 * 24 * 14; // Token is valid for two weeks
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		repo.setDataSource(dataSource);
		
		return repo;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("==================================");
		log.info("Setting up Authentication Manager");
		log.info("==================================");
		
		auth
			.jdbcAuthentication()
				.authoritiesByUsernameQuery("SELECT username, authority FROM users WHERE BINARY username = ?")
				.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE BINARY username = ?")
				.passwordEncoder(passwordEncoder())
				.dataSource(dataSource);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("==================================");
		log.info("Setting up Web Security");
		log.info("==================================");
		
		http
			.httpBasic()
					.and()
			.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/profile/**", "/details/**", "/aircraft/**", "/logbook/**", "/entry/**").authenticated()
				.antMatchers("/resources/**", "/user/**", "/error/**", "/features", "/").permitAll()
				.antMatchers("/**").denyAll()
					.and()
			.formLogin()
				.loginPage("/user/login")
				.failureUrl("/user/login?error")
				.loginProcessingUrl("/user/login")
					.and()
			.logout()
				.logoutUrl("/user/logout")
				.logoutSuccessUrl("/user/login?loggedout")
				.invalidateHttpSession(true)
					.and()
			.rememberMe()
				.tokenRepository(tokenRepository())
				.rememberMeParameter("remember-me")
				.rememberMeCookieName("easyLogbookRememberMe")
				.tokenValiditySeconds(TOKEN_VALIDITY) 
					.and()
			.csrf()
					.and()
			.exceptionHandling()
				.accessDeniedPage("/error/accessdenied");
	}
}
