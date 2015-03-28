package ro.fizbo.rents.webservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.dto.RentsCounter;
import ro.fizbo.rents.logic.TokenGenerator;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Currency;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.util.Constants;
import ro.fizbo.rents.util.TestUtil;
import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;
import ro.fizbo.rents.webservice.util.HeadersConstants;
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
		Response response = target.path("account/rents/added")
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(HeadersConstants.TOKEN_KEY, account.getTokenKey()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);

		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		assertTrue(rentsCounter.counter >= rentsCounter.rents.size());
	}
	
	public void testGetUserAddedRentsWithCurrency() {
		Response response = target.path("account/rents/added")
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(HeadersConstants.CURRENCY, Currency.RON.toString())
				.header(HeadersConstants.TOKEN_KEY, account.getTokenKey()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);

		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		assertTrue(rentsCounter.counter >= rentsCounter.rents.size());
		for(Rent rent : rentsCounter.rents) {
			assertEquals(Currency.RON.toString(), rent.getRentCurrency());
		}
	}
	
	public void testGetUserAddedRentsForUnauthorizedUser() {
		Response response = target.path("account/rents/added")
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(HeadersConstants.TOKEN_KEY, TokenGenerator.generateToken()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.UNAUTHORIZED.getCode());
	}
	
	public void testGetUserAddedRentsNextPage() {
		String date = (new SimpleDateFormat(Constants.DATE_FORMAT)).format(new Date());
		Response response = target.path("account/rents/added/page")
				.queryParam("lastRentDate", date)
				.queryParam("lastRentId", Integer.MAX_VALUE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(HeadersConstants.TOKEN_KEY, account.getTokenKey()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
	}
	
	public void testGetUserAddedRentsNextPageWithCurrency() {
		String date = (new SimpleDateFormat(Constants.DATE_FORMAT)).format(new Date());
		Response response = target.path("account/rents/added/page")
				.queryParam("lastRentDate", date)
				.queryParam("lastRentId", Integer.MAX_VALUE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(HeadersConstants.CURRENCY, Currency.RON.toString())
				.header(HeadersConstants.TOKEN_KEY, account.getTokenKey()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		List<Rent> rents = response.readEntity(new GenericType<List<Rent>>(){});
		assertNotNull(rents);
		assertTrue(rents.size() > 0);
		for(Rent rent : rents) {
			assertEquals(Currency.RON.toString(), rent.getRentCurrency());
		}
	}
	
	public void testGetUserAddedRentsNextPageForUnauthorizedUser() {
		String date = (new SimpleDateFormat(Constants.DATE_FORMAT)).format(new Date());
		Response response = target.path("account/rents/added/page")
				.queryParam("lastRentDate", date)
				.queryParam("lastRentId", Integer.MAX_VALUE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(HeadersConstants.TOKEN_KEY, TokenGenerator.generateToken()).get();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.UNAUTHORIZED.getCode());
	}
	
	public void testDeleteUserAddedRents() {
		List<Integer> rentIds = new ArrayList<Integer>(userAddedRents.size());
		for(Rent rent : userAddedRents) {
			rentIds.add(rent.getRentId());
		}
		
		Response response = target.path("account/rents/added/delete")
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
