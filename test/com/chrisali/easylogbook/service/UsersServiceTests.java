package com.chrisali.easylogbook.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import com.chrisali.easylogbook.model.User;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/config/dao-context.xml",
									"classpath:com/chrisali/easylogbook/config/service-context.xml",
									"classpath:com/chrisali/easylogbook/config/security-context.xml" })
@TestExecutionListeners(listeners = { ServletTestExecutionListener.class,
							       	  DependencyInjectionTestExecutionListener.class,
							          DirtiesContextTestExecutionListener.class,
							          TransactionalTestExecutionListener.class,
							          WithSecurityContextTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class UsersServiceTests extends ServiceTestData implements ServiceTests {
	
	@Before
	public void init() {
		clearDatabase();
	}
	
	@Test
	@WithMockUser(username="admin", roles={"ADMIN","USER"})
	@Override
	public void testCreateRetrieve() {
		
		usersService.createOrUpdate(user1);
		
		List<User> users1 = usersService.getPaginatedUsers(0, 10);
		
		assertEquals("One user should be created and retrieved", 1, (long)usersService.getTotalNumberUsers());
		assertEquals("Inserted user should match retrieved", user1, users1.get(0));
		
		usersService.createOrUpdate(user2);
		usersService.createOrUpdate(user3);
		usersService.createOrUpdate(user4);
		
		assertEquals("Four users should be created and retrieved", 4, (long)usersService.getTotalNumberUsers());
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testCreateRetrieveNoAuth() {
		testCreateRetrieve();
	}
	
	@Test
	@Override
	public void testExists() {
		usersService.createOrUpdate(user2);
		
		assertTrue("User should exist in database", usersService.exists(user2.getUsername()));
		assertFalse("User should not exist in database", usersService.exists("notAUser"));
	}
	
	@Override
	public void testExistsNoAuth() {}
	
	@Test
	@WithMockUser(username="admin", roles={"ADMIN"})
	@Override
	public void testDelete() {
		addTestData();
		assertEquals("Four users should be created and retrieved", 4, (long)usersService.getTotalNumberUsers());
		
		assertTrue("User be deleted from database", usersService.delete(user2.getUsername()));
		assertTrue("User be deleted from database", usersService.delete(user1.getUsername()));
		
		assertEquals("Two users should be left in database", 2, (long)usersService.getTotalNumberUsers());
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testDeleteNoAuth() {
		testDelete();
	}
	
	@Test
	@WithMockUser(username="admin", roles={"USER","ADMIN"})
	@Override
	public void testUpdate() {
		addTestData();
		assertEquals("Four users should be created and retrieved", 4, (long)usersService.getTotalNumberUsers());
		
		user2.setName("Chris Ali");
		usersService.createOrUpdate(user2);
		User updatedUser2 = usersService.getUser(user2.getUsername());
		
		assertEquals("Users should be equal", user2, updatedUser2);
		
		user3.setEmail("test@test.com");
		usersService.createOrUpdate(user3);
		User updatedUser3 = usersService.getUser(user3.getUsername());
		
		assertEquals("Users should be equal", user3, updatedUser3);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testUpdateNoAuth() {
		testUpdate();
	}
}
