package ro.fizbo.rents.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.dto.RentsCounter;
import ro.fizbo.rents.logic.CurrencyManager;
import ro.fizbo.rents.logic.CurrencyManagerMock;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Currency;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentSearch;
import ro.fizbo.rents.util.RentSearchTestUtil;
import ro.fizbo.rents.util.TestUtil;
import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;
import ro.fizbo.rents.webservice.util.ContextConstants;
import junit.framework.TestCase;

public class RentsSearchWebServiceTest extends TestCase {
	
	private static final int NO_RENTS = 6;
	
	private WebTarget target;
	
	private Account account;

	private List<Rent> rents = new ArrayList<Rent>(NO_RENTS);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		
		Rent rent = null;
		for(int i = 0; i < NO_RENTS; i++) {
			rent = TestUtil.addRent(account.getAccountId());
			rents.add(rent);
		}

		target = TestUtil.buildWebTarget();
		
		CurrencyManagerMock.updateConversionRates();
	}

	@Override
	protected void tearDown() throws Exception {
		for(Rent rent : rents) {
			TestUtil.deleteRent(rent);
		}
		
		TestUtil.deleteAccount(account);

		super.tearDown();
	}
	
	public void testSearch() {
		RentSearch rentSearch = RentSearchTestUtil.getRentSearch();
		Response response = target.path("rents/search").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(rentSearch));
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);
		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		assertTrue(rentsCounter.counter >= rentsCounter.rents.size());
	}
	
	public void testSearchWithCurrency() {
		RentSearch rentSearch = RentSearchTestUtil.getRentSearch();
		Response response = target.path("rents/search").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.CURRENCY, Currency.RON.toString())
				.post(Entity.json(rentSearch));
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);
		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		assertTrue(rentsCounter.counter >= rentsCounter.rents.size());
		for(Rent rent : rentsCounter.rents) {
			assertEquals(Currency.RON.toString(), rent.getRentCurrency());
			CurrencyManager.convertRentPrice(Currency.EUR.toString(), rent);
			assertTrue(rent.getRentPrice() >= rentSearch.getLowRent().getRentPrice());
			assertTrue(rent.getRentPrice() <= rentSearch.getHighRent().getRentPrice());
		}
	}
	
	public void testSearchNextPageWithoutCurrency() {
		RentSearch rentSearch = RentSearchTestUtil.getRentSearch();
		Response response = target.path("rents/search").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(rentSearch));
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);
		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		
		Rent lastRent = rentsCounter.rents.get(rentsCounter.rents.size() - 1);
		rentSearch.getHighRent().setRentAddDate(lastRent.getRentAddDate());
		rentSearch.getHighRent().setRentId(lastRent.getRentId());
		response = target.path("rents/search/page").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(rentSearch));

		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		List<Rent> rents = response.readEntity(new GenericType<List<Rent>>(){});
		assertTrue(rents.size() > 0);
		assertTrue(rents.size() <= TestUtil.PAGE_SIZE);
		for(Rent rent : rents) {
			assertEquals(Currency.EUR.toString(), rent.getRentCurrency());
			CurrencyManager.convertRentPrice(Currency.EUR.toString(), rent);
			assertTrue(rent.getRentPrice() >= rentSearch.getLowRent().getRentPrice());
			assertTrue(rent.getRentPrice() <= rentSearch.getHighRent().getRentPrice());
		}
	}
	
	public void testSearchNextPageWithCurrency() {		
		RentSearch rentSearch = RentSearchTestUtil.getRentSearch();
		Response response = target.path("rents/search").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.CURRENCY, Currency.RON.toString())
				.post(Entity.json(rentSearch));
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);
		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		
		Rent lastRent = rentsCounter.rents.get(rentsCounter.rents.size() - 1);
		rentSearch.getHighRent().setRentAddDate(lastRent.getRentAddDate());
		rentSearch.getHighRent().setRentId(lastRent.getRentId());
		response = target.path("rents/search/page").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.CURRENCY, Currency.RON.toString())
				.post(Entity.json(rentSearch));

		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		List<Rent> rents = response.readEntity(new GenericType<List<Rent>>(){});
		assertTrue(rents.size() > 0);
		assertTrue(rents.size() <= TestUtil.PAGE_SIZE);
		for(Rent rent : rents) {
			assertEquals(Currency.RON.toString(), rent.getRentCurrency());
			CurrencyManager.convertRentPrice(Currency.EUR.toString(), rent);
			assertTrue(rent.getRentPrice() >= rentSearch.getLowRent().getRentPrice());
			assertTrue(rent.getRentPrice() <= rentSearch.getHighRent().getRentPrice());
		}
	}
}
