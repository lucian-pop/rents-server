package com.personal.rents.webservice;

import java.util.Date;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.personal.rents.logic.TokenGenerator;
import com.personal.rents.model.Account;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;
import com.personal.rents.webservice.util.ContextConstants;

import junit.framework.TestCase;

public class RentWebserviceTest extends TestCase {
	
	private Rent rent; 
	
	private Account account;
	
	private WebTarget target;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		target = TestUtil.buildWebTarget();
		
		Address address = new Address();
		address.setAddressStreetNo("68A");
		address.setAddressStreetName("Observatorului");
		address.setAddressNeighbourhood("Zorilor");
		address.setAddressLocality("Cluj-Napoca");
		address.setAddressAdmAreaL1("Cluj");
		address.setAddressCountry("Romania");
		address.setAddressLatitude(46.7667);
		address.setAddressLongitude(23.5833);
		address.setAddressBuilding("C3");
		address.setAddressStaircase("2A");
		address.setAddressFloor((short) 4);
		address.setAddressAp("12B");
		
		rent = new Rent();
		rent.setAccountId(account.getAccountId());
		rent.setAddress(address);
		rent.setRentPrice(500);
		rent.setRentSurface(120);
		rent.setRentRooms((short) 3);
		rent.setRentBaths((short) 3);
		rent.setRentParty((byte) 1);
		rent.setRentType((byte) 1);
		rent.setRentArchitecture((byte) 1);
		rent.setRentAge((short) 10);
		rent.setRentDescription("some dummy text here");
		rent.setRentPetsAllowed(true);
		rent.setRentPhone("0750110440");
		rent.setRentAddDate(new Date());
		rent.setRentStatus((byte) 0);
	}

	@Override
	protected void tearDown() throws Exception {
		if(rent.getRentId() != null) {
			TestUtil.deleteRent(rent);
		}
		TestUtil.deleteAccount(account);
		
		super.tearDown();
	}

	public void testAddRent() {
		Response response = target.path("rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		assertTrue(rent.getRentId() != null);
	}
	
	public void testAddRentWithoutPrivileges() {
		Response response = target.path("rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, TokenGenerator.generateToken())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.UNAUTHORIZED.getCode());
	}
	
	public void testUpdateRent() {
		Response response = target.path("rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		
		response = target.path("rent/update").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));
		
		Integer result = response.readEntity(Integer.class);
		assertTrue(result == 1);
	}
}
