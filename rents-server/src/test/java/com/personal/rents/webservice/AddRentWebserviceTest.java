package com.personal.rents.webservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.personal.rents.model.Account;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;

import junit.framework.TestCase;

public class AddRentWebserviceTest extends TestCase {
	
	private Rent rent; 
	
	private Account account;
	
	private WebTarget target;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		target = TestUtil.buildWebTarget();
		
		Address address = new Address();
		address.setStreetNo("68A");
		address.setStreetName("Observatorului");
		address.setNeighbourhood("Zorilor");
		address.setLocality("Cluj-Napoca");
		address.setAdmAreaL1("Cluj");
		address.setCountry("Romania");
		address.setLatitude(46.7667);
		address.setLongitude(23.5833);
		address.setBuilding("C3");
		address.setStaircase("2A");
		address.setFloor((short) 4);
		address.setAp("12B");
		
		rent = new Rent();
		rent.setAccountId(account.getId());
		rent.setAddress(address);
		rent.setPrice(500);
		rent.setSurface(120);
		rent.setRooms((short) 3);
		rent.setBaths((short) 3);
		rent.setParty((byte) 1);
		rent.setRentType((byte) 1);
		rent.setArchitecture((byte) 1);
		rent.setAge((short) 10);
		rent.setDescription("some dummy text here");
		rent.setPetsAllowed(true);
		rent.setPhone("0750110440");
		rent.setCreationDate(new Date());
		rent.setRentStatus((byte) 0);
	}

	@Override
	protected void tearDown() throws Exception {
		TestUtil.deleteRent(rent);
		TestUtil.deleteAccount(account);
		
		super.tearDown();
	}

	public void testAddRentWithoutImages() {
		Response response = target.path("addrent").request(MediaType.APPLICATION_JSON).post(Entity.json(rent));
		rent = response.readEntity(Rent.class);

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
	}
	
	public void testAddRentWithImages() {
		List<String> imageURIs = new ArrayList<String>();
		imageURIs.add("/images/1/123456789/1.jpg");
		imageURIs.add("/images/1/123456789/2.jpg");
		imageURIs.add("/images/1/123456789/3.jpg");
		imageURIs.add("/images/1/123456789/4.jpg");
		imageURIs.add("/images/1/123456789/5.jpg");
		
		rent.setImageURIs(imageURIs);
		
		Response response = target.path("addrent").request(MediaType.APPLICATION_JSON).post(Entity.json(rent));
		rent = response.readEntity(Rent.class);

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
	}
}
