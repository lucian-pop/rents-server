package com.personal.rents.webservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.personal.rents.dto.RentFavoriteViewsCounter;
import com.personal.rents.logic.TokenGenerator;
import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.model.view.RentFavoriteView;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;
import com.personal.rents.webservice.util.ContextConstants;
import com.personal.rents.webservice.util.GeneralConstants;

import junit.framework.TestCase;

public class UserFavoritesWebserviceTest extends TestCase {
	
	private static final String TEST_ACCOUNT_EMAIL = "test";
	
	private static final String TEST_ACCOUNT_PASSWORD = "test";
	
	private Account account;
	
	private List<Rent> rents  = new ArrayList<Rent>();
	
	private WebTarget target;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		for(int i = 0; i < TestUtil.PAGE_SIZE; i++) {
			Rent rent = TestUtil.addRent(account.getAccountId());
			rents.add(rent);
			TestUtil.addRentFavorite(account.getAccountId(), rent.getRentId());
		}
		
		target = TestUtil.buildWebTarget();
	}

	@Override
	protected void tearDown() throws Exception {
		for(Rent rent : rents) {
			TestUtil.deleteRentFavorite(account.getAccountId(), rent.getRentId());
			TestUtil.deleteRent(rent);
		}
		TestUtil.deleteAccount(account);

		super.tearDown();
	}
	
	public void testAddNewRentToFavorites() {
		Response response = target.path("rents/userfavorites/addrent")
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rents.get(0).getRentId()));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		Boolean result = response.readEntity(Boolean.class);
		assertTrue(result);
		
		TestUtil.deleteRentFavorite(account.getAccountId(), rents.get(0).getRentId());
	}
	
	public void testAddExistingRentToFavorites() {
		TestUtil.addRentFavorite(account.getAccountId(), rents.get(0).getRentId());
		
		Response response = target.path("rents/userfavorites/addrent")
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rents.get(0).getRentId()));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		Boolean result = response.readEntity(Boolean.class);
		assertFalse(result);
		
		TestUtil.deleteRentFavorite(account.getAccountId(), rents.get(0).getRentId());
	}
	
	public void testAddRentToFavoritesWithoutPrivileges() {
		Response response = target.path("rents/userfavorites/addrent")
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, TokenGenerator.generateToken())
				.post(Entity.json(rents.get(0).getRentId()));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.UNAUTHORIZED.getCode());
	}
	
	public void testGetUserFavoriteRents() {
		Form form = new Form();
		form.param("email", TEST_ACCOUNT_EMAIL);
		form.param("password", TEST_ACCOUNT_PASSWORD);
		Response response = target.path("login").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
		
		Account testAccount = response.readEntity(Account.class);
		response = target.path("rents/userfavorites")
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, testAccount.getTokenKey()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		RentFavoriteViewsCounter result = response.readEntity(RentFavoriteViewsCounter.class);
		assertNotNull(result);
		assertTrue(result.rentFavoriteViews.size() > 0);
		assertTrue(result.rentFavoriteViews.size() <= TestUtil.PAGE_SIZE);
	}
	
	public void testGetUserFavoriteRentsNextPage() {
		Form form = new Form();
		form.param("email", TEST_ACCOUNT_EMAIL);
		form.param("password", TEST_ACCOUNT_PASSWORD);
		Response response = target.path("login").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
		
		Account testAccount = response.readEntity(Account.class);
		String date = (new SimpleDateFormat(GeneralConstants.DATE_FORMAT)).format(new Date());
		response = target.path("rents/userfavorites/page")
				.queryParam("lastDate", date)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, testAccount.getTokenKey()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		List<RentFavoriteView> result = 
				response.readEntity(new GenericType<List<RentFavoriteView>>(){});
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}
	
	public void testDeleteUserFavoriteRents() {
		List<Integer> rentIds = new ArrayList<Integer>(rents.size());
		for(Rent rent : rents) {
			rentIds.add(rent.getRentId());
		}
		
		Response response = target.path("rents/userfavorites/delete")
				.request(MediaType.APPLICATION_JSON)
				.header("tokenKey", account.getTokenKey())
				.post(Entity.json(rentIds));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		int result = response.readEntity(Integer.class);
		assertTrue(result == rentIds.size());
	}
}
