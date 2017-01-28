package com.chrisali.easylogbook.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile("test")
@EnableTransactionManagement
public class DataSourceTestConfig {

	@Value("${jdbc.test.driver}")
    private String driver;
    
	@Value("${jdbc.test.url}")
    private String url;
    
    @Value("${jdbc.test.username}")
    private String username;
    
    @Value("${jdbc.test.password}")
    private String password;
    
    @Value("${hibernate.test.dialect}")
    private String dialect;
    
    @Value("${hibernate.test.show_sql}")
    private String showSql;
    
    @Value("${hibernate.test.format_sql}")
    private String formatSql;
    
    @Value("${hibernate.test.use_sql_comments}")
    private String useSqlComments;

    @Value("${hibernate.test.hbm2ddl.auto}")
    private String hbm2ddlAuto;
	
	@Bean(name = "testDataSource")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		
		return dataSource;
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] {"com.chrisali.easylogbook.model",
													   "com.chrisali.easylogbook.dao",
													   "com.chrisali.easylogbook.service"});
		sessionFactory.setHibernateProperties(hibernateProperties());
		
		return sessionFactory;
	}

	private Properties hibernateProperties() {
		return new Properties() {
			private static final long serialVersionUID = -37648756350167903L;
			{
				setProperty("hibernate.dialect", dialect);
				setProperty("hibernate.show_sql", showSql);
				setProperty("hibernate.format_sql", formatSql);
				setProperty("hibernate.use_sql_comments", useSqlComments);
				//setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
			}
		};
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager(SessionFactory sessionFactory) {
		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(dataSource());
		
		return txManager;
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
}
