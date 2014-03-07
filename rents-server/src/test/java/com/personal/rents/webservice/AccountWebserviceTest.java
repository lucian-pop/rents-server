package com.personal.rents.webservice;

import java.util.Date;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import junit.framework.TestCase;

import org.junit.After;

import com.personal.rents.model.Account;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;

public class AccountWebserviceTest extends TestCase {
	
	private static final String ACCOUNT_EMAIL = "initial.account@gmail.com";
	
	private static final String ACCOUNT_PASSWORD = "account password";

	private Account account;
	
	private WebTarget target;
	
	public AccountWebserviceTest() {
		target = TestUtil.buildWebTarget();
	}
	
	@After
	public void tearDown() throws Exception {
		if(account.getAccountEmail() != null) {
			TestUtil.deleteAccount(account);
		}
		
		super.tearDown();
	}

	public void testSignup() {
		account = new Account();
		account.setAccountType((byte) 0);
		account.setAccountExternalId("sadsadkjhfsdfsdfsdddddddddddddddf");
		account.setAccountEmail(ACCOUNT_EMAIL);
		account.setAccountPassword(ACCOUNT_PASSWORD);
		account.setAccountFirstname("account firstname");
		account.setAccountLastname("account lastname");
		account.setAccountPhone("+4 0100900900");
		account.setAccountSignupDate(new Date());
		Response response = target.path("account/signup").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(account));
		
		assertTrue("Status should be OK" , response.getStatus() == 
				WebserviceResponseStatus.OK.getCode());
		
		account = response.readEntity(Account.class);

		assertNotNull(account);
	}
	
	
	public void testLogin() {
		account = TestUtil.createAccount();

		Form form = new Form();
		form.param("email", ACCOUNT_EMAIL);
		form.param("password", ACCOUNT_PASSWORD);
		Response response = target.path("account/login").request(MediaType.APPLICATION_JSON).post(Entity.entity(form,
				MediaType.APPLICATION_FORM_URLENCODED));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		Account loginResult = response.readEntity(Account.class);
		
		assertNotNull(loginResult);
		assertTrue(loginResult.getAccountEmail().equals(account.getAccountEmail()));
	}

	public void testChangePassword() {
		account = TestUtil.createAccount();
		
		Form form = new Form();
		form.param("email", ACCOUNT_EMAIL);
		form.param("password", ACCOUNT_PASSWORD);
		form.param("newPassword", "some new password");
		
		Response response = target.path("account/changepassword").request(MediaType.APPLICATION_JSON).post(Entity.entity(form,
				MediaType.APPLICATION_FORM_URLENCODED));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		String tokenKey = response.readEntity(String.class);
		assertTrue(!account.getTokenKey().equals(tokenKey));
	}
}
