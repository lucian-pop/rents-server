package ro.fizbo.rents.webservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.dto.RentsCounter;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.util.TestUtil;
import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;
import ro.fizbo.rents.webservice.util.GeneralConstants;
import junit.framework.TestCase;

public class GetRentsWebserviceTest extends TestCase {
	
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
	}

	@Override
	protected void tearDown() throws Exception {
		for(Rent rent : rents) {
			TestUtil.deleteRent(rent);
		}
		
		TestUtil.deleteAccount(account);

		super.tearDown();
	}

	public void testGetRentsByMapBoundaries() {
		Response response = target.path("rents/map")
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
	
	public void testGetRentsNextPageByMapBoundaries() {
		String date = (new SimpleDateFormat(GeneralConstants.DATE_FORMAT)).format(new Date());
		Response response = target.path("rents/map/page")
				.queryParam("minLatitude", TestUtil.MIN_LATITUDE)
				.queryParam("maxLatitude", TestUtil.MAX_LATITUDE)
				.queryParam("minLongitude", TestUtil.MIN_LONGITUDE)
				.queryParam("maxLongitude", TestUtil.MAX_LONGITUDE).queryParam("lastRentDate", date)
				.queryParam("lastRentId", Integer.MAX_VALUE)
				.queryParam("pageSize", TestUtil.PAGE_SIZE)
				.request(MediaType.APPLICATION_JSON).get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
	}

}
