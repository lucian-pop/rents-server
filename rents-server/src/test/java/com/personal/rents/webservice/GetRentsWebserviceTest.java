package com.personal.rents.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.personal.rents.dao.RentDAO;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;

import junit.framework.TestCase;

public class GetRentsWebserviceTest extends TestCase {

	private static final double MIN_LATITUDE = 46.7379424563698;

	private static final double MAX_LATITUDE = 46.76499396368981;

	private static final double MIN_LONGITUDE = 23.56791313737631;
	
	private static final double MAX_LONGITUDE = 23.59537862241268;
	
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
		Response response = target.path("rents/light").queryParam("minLatitude", MIN_LATITUDE)
				.queryParam("maxLatitude", MAX_LATITUDE).queryParam("minLongitude", MIN_LONGITUDE)
				.queryParam("maxLongitude", MAX_LONGITUDE)
				.request(MediaType.APPLICATION_JSON).get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);

		assertTrue(rentsCounter.rents.size() > 0);
		
		assertTrue(rentsCounter.rents.size() <= RentDAO.MAX_NO_RESULTS);
		
		assertTrue(rentsCounter.counter >= rentsCounter.rents.size());
	}
	
}
