package com.personal.rents.webservice;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.personal.rents.logic.TokenGenerator;
import com.personal.rents.model.Account;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;
import com.personal.rents.webservice.util.ContextConstants;

import junit.framework.TestCase;

public class AuthorizationWebserviceTest extends TestCase {
	
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

	public void testIsAuthorizedForAuthorizedRequest() {
		Response response = target.path("auth").request()
				.header(ContextConstants.ACCOUNT_ID, account.getAccountId())
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
	}
	
	public void testIsAuthorizedForUnauthorizedRequest() {
		Response response = target.path("auth").request()
				.header(ContextConstants.ACCOUNT_ID, account.getAccountId())
				.header(ContextConstants.TOKEN_KEY, TokenGenerator.generateToken()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.UNAUTHORIZED.getCode());
	}
}
