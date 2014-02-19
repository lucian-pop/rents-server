package com.personal.rents.webservice;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.personal.rents.logic.TokenGenerator;
import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;
import com.personal.rents.webservice.util.ContextConstants;

import junit.framework.TestCase;

public class RentFavoritesWebserviceTest extends TestCase {
	
	private Account account;
	
	private Rent rent;
	
	private WebTarget target;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		rent = TestUtil.addRent(account.getAccountId());
		
		target = TestUtil.buildWebTarget();
	}

	@Override
	protected void tearDown() throws Exception {
		TestUtil.deleteRent(rent);
		TestUtil.deleteAccount(account);

		super.tearDown();
	}
	
	public void testAddNewRentToFavorites() {
		Response response = target.path("rentfavorites/addrent")
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.ACCOUNT_ID, account.getAccountId())
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent.getRentId()));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		Boolean result = response.readEntity(Boolean.class);
		assertTrue(result);
		
		TestUtil.deleteRentFavorite(account.getAccountId(), rent.getRentId());
	}
	
	public void testAddExistingRentToFavorites() {
		TestUtil.addRentFavorite(account.getAccountId(), rent.getRentId());
		
		Response response = target.path("rentfavorites/addrent")
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.ACCOUNT_ID, account.getAccountId())
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent.getRentId()));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		Boolean result = response.readEntity(Boolean.class);
		assertFalse(result);
		
		TestUtil.deleteRentFavorite(account.getAccountId(), rent.getRentId());
	}
	
	public void testAddRentToFavoritesWithoutPrivileges() {
		Response response = target.path("rentfavorites/addrent")
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.ACCOUNT_ID, account.getAccountId())
				.header(ContextConstants.TOKEN_KEY, TokenGenerator.generateToken())
				.post(Entity.json(rent.getRentId()));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.UNAUTHORIZED.getCode());
	}
}
