package com.personal.rents.webservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.personal.rents.dto.RentsCounter;
import com.personal.rents.logic.TokenGenerator;
import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;
import com.personal.rents.webservice.util.ContextConstants;
import com.personal.rents.webservice.util.GeneralConstants;

import junit.framework.TestCase;

public class UserAddedRentsWebserviceTest extends TestCase {

	private Account account;
	
	private WebTarget target;
	
	private List<Rent> userAddedRents;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		target = TestUtil.buildWebTarget();
		
		userAddedRents = new ArrayList<Rent>(TestUtil.PAGE_SIZE + 1);
		for(int i = 0; i < TestUtil.PAGE_SIZE + 1; i++) {
			Rent rent = TestUtil.addRent(account.getAccountId());
			userAddedRents.add(rent);
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
		for(Rent rent : userAddedRents) {
			TestUtil.deleteRent(rent);
		}
		
		TestUtil.deleteAccount(account);
		
		super.tearDown();
	}
	
	public void testGetUserAddedRentsForAuthorizedUser() {
		Response response = target.path("rents/useradded")
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);

		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		assertTrue(rentsCounter.counter >= rentsCounter.rents.size());
	}
	
	public void testGetUserAddedRentsForUnauthorizedUser() {
		Response response = target.path("rents/useradded")
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, TokenGenerator.generateToken()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.UNAUTHORIZED.getCode());
	}
	
	public void testGetUserAddedRentsNextPage() {
		String date = (new SimpleDateFormat(GeneralConstants.DATE_FORMAT)).format(new Date());
		Response response = target.path("rents/useradded/page")
				.queryParam("lastRentDate", date)
				.queryParam("lastRentId", Integer.MAX_VALUE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
	}
	
	public void testGetUserAddedRentsNextPageForUnauthorizedUser() {
		String date = (new SimpleDateFormat(GeneralConstants.DATE_FORMAT)).format(new Date());
		Response response = target.path("rents/useradded/page")
				.queryParam("lastRentDate", date)
				.queryParam("lastRentId", Integer.MAX_VALUE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, TokenGenerator.generateToken()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.UNAUTHORIZED.getCode());
	}
	
	public void testDeleteUserAddedRents() {
		List<Integer> rentIds = new ArrayList<Integer>(userAddedRents.size());
		for(Rent rent : userAddedRents) {
			rentIds.add(rent.getRentId());
		}
		
		Response response = target.path("rents/useradded/delete")
				.request(MediaType.APPLICATION_JSON)
				.header("tokenKey", account.getTokenKey())
				.post(Entity.json(rentIds));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		int result = response.readEntity(Integer.class);
		assertTrue(result == rentIds.size());
		
		for(Rent rent: userAddedRents) {
			TestUtil.deleteRent(rent);
		}
	}
}
