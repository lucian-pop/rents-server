package ro.fizbo.rents.webservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.dto.RentsCounter;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Currency;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentForm;
import ro.fizbo.rents.util.Constants;
import ro.fizbo.rents.util.TestUtil;
import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;
import ro.fizbo.rents.webservice.util.ContextConstants;
import junit.framework.TestCase;

public class GetRentsWebserviceTest extends TestCase {
	
	private static final int NO_RENTS = 20;
	
	private WebTarget target;
	
	private Account account;

	private List<Rent> rents = new ArrayList<Rent>(NO_RENTS);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		
		Rent rent = null;
		for(int i = 0; i < NO_RENTS; i++) {
			if(i % 2 == 0) {
				rent = TestUtil.addRent(account.getAccountId());
			} else {
				rent = TestUtil.addRent(account.getAccountId(), RentForm.ACCOMMODATION.getForm());
			}
			
			rents.add(rent);
		}

		target = TestUtil.buildWebTarget();
	}

	@Override
	protected void tearDown() throws Exception {
		for(Rent rent : rents) {
			TestUtil.deleteRent(rent);
		}
		
		TestUtil.deleteAccount(account);

		super.tearDown();
	}

	public void testGetRentsByMapBoundariesWithoutCurrency() {
		Response response = target.path("rents/map/0")
				.queryParam("minLatitude", TestUtil.MIN_LATITUDE)
				.queryParam("maxLatitude", TestUtil.MAX_LATITUDE)
				.queryParam("minLongitude", TestUtil.MIN_LONGITUDE)
				.queryParam("maxLongitude", TestUtil.MAX_LONGITUDE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON).get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);

		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		assertTrue(rentsCounter.counter >= rentsCounter.rents.size());
	}
	
	public void testGetRentsByMapBoundariesWithCurrency() {
		Response response = target.path("rents/map/0")
				.queryParam("minLatitude", TestUtil.MIN_LATITUDE)
				.queryParam("maxLatitude", TestUtil.MAX_LATITUDE)
				.queryParam("minLongitude", TestUtil.MIN_LONGITUDE)
				.queryParam("maxLongitude", TestUtil.MAX_LONGITUDE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.CURRENCY, Currency.RON.toString()).get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);

		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		assertTrue(rentsCounter.counter >= rentsCounter.rents.size());
		for(Rent rent : rentsCounter.rents) {
			assertTrue(rent.getRentCurrency().equals(Currency.RON.toString()));
		}
	}
	
	public void testGetAccommodationsByMapBoundariesWithoutCurrency() {
		Response response = target.path("rents/map/1")
				.queryParam("minLatitude", TestUtil.MIN_LATITUDE)
				.queryParam("maxLatitude", TestUtil.MAX_LATITUDE)
				.queryParam("minLongitude", TestUtil.MIN_LONGITUDE)
				.queryParam("maxLongitude", TestUtil.MAX_LONGITUDE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON).get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);

		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		assertTrue(rentsCounter.counter >= rentsCounter.rents.size());
	}
	
	public void testGetAccommodationsByMapBoundariesWithCurrency() {
		Response response = target.path("rents/map/1")
				.queryParam("minLatitude", TestUtil.MIN_LATITUDE)
				.queryParam("maxLatitude", TestUtil.MAX_LATITUDE)
				.queryParam("minLongitude", TestUtil.MIN_LONGITUDE)
				.queryParam("maxLongitude", TestUtil.MAX_LONGITUDE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON).header(ContextConstants.CURRENCY,
						Currency.RON.toString()).get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);

		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		assertTrue(rentsCounter.counter >= rentsCounter.rents.size());
		for(Rent rent : rentsCounter.rents) {
			assertTrue(rent.getRentCurrency().equals(Currency.RON.toString()));
		}
	}
	
	public void testGetRentsNextPageByMapBoundariesWithoutCurrency() {
		String date = (new SimpleDateFormat(Constants.DATE_FORMAT)).format(new Date());
		Response response = target.path("rents/map/page/0")
				.queryParam("minLatitude", TestUtil.MIN_LATITUDE)
				.queryParam("maxLatitude", TestUtil.MAX_LATITUDE)
				.queryParam("minLongitude", TestUtil.MIN_LONGITUDE)
				.queryParam("maxLongitude", TestUtil.MAX_LONGITUDE).queryParam("lastRentDate", date)
				.queryParam("lastRentId", Integer.MAX_VALUE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON).get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		List<Rent> rents = response.readEntity(new GenericType<List<Rent>>(){});
		assertTrue(rents.size() > 0);
		assertTrue(rents.size() <= TestUtil.PAGE_SIZE);
	}
	
	public void testGetRentsNextPageByMapBoundariesWithCurrency() {
		String date = (new SimpleDateFormat(Constants.DATE_FORMAT)).format(new Date());
		Response response = target.path("rents/map/page/0")
				.queryParam("minLatitude", TestUtil.MIN_LATITUDE)
				.queryParam("maxLatitude", TestUtil.MAX_LATITUDE)
				.queryParam("minLongitude", TestUtil.MIN_LONGITUDE)
				.queryParam("maxLongitude", TestUtil.MAX_LONGITUDE).queryParam("lastRentDate", date)
				.queryParam("lastRentId", Integer.MAX_VALUE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.CURRENCY, Currency.RON.toString()).get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		List<Rent> rents = response.readEntity(new GenericType<List<Rent>>(){});
		assertTrue(rents.size() > 0);
		assertTrue(rents.size() <= TestUtil.PAGE_SIZE);
		for(Rent rent : rents) {
			assertTrue(rent.getRentCurrency().equals(Currency.RON.toString()));
		}
	}
	
	public void testGetAccommodationsNextPageByMapBoundariesWithoutCurrency() {
		String date = (new SimpleDateFormat(Constants.DATE_FORMAT)).format(new Date());
		Response response = target.path("rents/map/page/1")
				.queryParam("minLatitude", TestUtil.MIN_LATITUDE)
				.queryParam("maxLatitude", TestUtil.MAX_LATITUDE)
				.queryParam("minLongitude", TestUtil.MIN_LONGITUDE)
				.queryParam("maxLongitude", TestUtil.MAX_LONGITUDE).queryParam("lastRentDate", date)
				.queryParam("lastRentId", Integer.MAX_VALUE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON).get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
	}
	
	public void testGetAccommodationsNextPageByMapBoundariesWithCurrency() {
		String date = (new SimpleDateFormat(Constants.DATE_FORMAT)).format(new Date());
		Response response = target.path("rents/map/page/1")
				.queryParam("minLatitude", TestUtil.MIN_LATITUDE)
				.queryParam("maxLatitude", TestUtil.MAX_LATITUDE)
				.queryParam("minLongitude", TestUtil.MIN_LONGITUDE)
				.queryParam("maxLongitude", TestUtil.MAX_LONGITUDE).queryParam("lastRentDate", date)
				.queryParam("lastRentId", Integer.MAX_VALUE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON).
				header(ContextConstants.CURRENCY, Currency.RON.toString()).get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		List<Rent> rents = response.readEntity(new GenericType<List<Rent>>(){});
		assertTrue(rents.size() > 0);
		assertTrue(rents.size() <= TestUtil.PAGE_SIZE);
		for(Rent rent : rents) {
			assertTrue(rent.getRentCurrency().equals(Currency.RON.toString()));
		}
	}

}
