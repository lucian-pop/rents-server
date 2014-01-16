package com.personal.rents.webservice;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.personal.rents.model.Account;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;

import junit.framework.TestCase;

public class LoginWebserviceTest extends TestCase {
	
	private Account account;

	private WebTarget target;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		target = TestUtil.buildWebTarget();
	}

	@Override
	protected void tearDown() throws Exception {
		TestUtil.deleteAccount(account);
		
		super.tearDown();
	}


	public void testLogin() {
		Form form = new Form();
		form.param("email", account.getAccountEmail());
		form.param("password", account.getAccountPassword());
		Response response = target.path("login").request(MediaType.APPLICATION_JSON).post(Entity.entity(form,
				MediaType.APPLICATION_FORM_URLENCODED));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		Account loginResult = response.readEntity(Account.class);
		
		assertTrue(loginResult.getAccountEmail().equals(account.getAccountEmail()));
		assertNotNull(loginResult.getTokenKey());
		assertTrue(loginResult.getTokenKey().equals(account.getTokenKey()));
	}
}
