package com.personal.rents.webservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import junit.framework.TestCase;

import com.personal.rents.dto.RentsCounter;
import com.personal.rents.model.Account;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentAge;
import com.personal.rents.model.RentArchitecture;
import com.personal.rents.model.RentParty;
import com.personal.rents.model.RentSearch;
import com.personal.rents.model.RentStatus;
import com.personal.rents.model.RentType;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;

public class RentsSearchWebServiceTest extends TestCase {
	
	private static final double MIN_LATITUDE = -90;

	private static final double MAX_LATITUDE = 90;

	private static final double MIN_LONGITUDE = -180;
	
	private static final double MAX_LONGITUDE = 180;
	
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
	
	public void testSearchWithClosedRanges() {
		int minPrice = 100;
		int maxPrice = 5000;
		int minSurface = 20;
		int maxSurface = 500;
		short minRooms = 2;
		short maxRooms = 6;
		short minBaths = 1;
		short maxBaths = 5;
		byte rentParty = RentParty.INDIVIDUAL.getParty();
		byte rentType = RentType.APARTMENT.getType();
		byte rentArchitecture = RentArchitecture.DETACHED.getArchitecture();
		short rentAge = RentAge.NEW.getAge();
		boolean rentPetsAllowed = false;
		byte rentStatus = RentStatus.AVAILABLE.getStatus();
		
		RentSearch rentSearch = new RentSearch();
		Rent lowRent = new Rent();
		Rent highRent = new Rent();
		Address lowAddress = new Address();
		Address highAddress = new Address();
		lowAddress.setAddressLatitude(MIN_LATITUDE);
		lowAddress.setAddressLongitude(MIN_LONGITUDE);
		highAddress.setAddressLatitude(MAX_LATITUDE);
		highAddress.setAddressLongitude(MAX_LONGITUDE);
		
		lowRent.setAddress(lowAddress);
		lowRent.setRentPrice(minPrice);
		lowRent.setRentSurface(minSurface);
		lowRent.setRentRooms(minRooms);
		lowRent.setRentBaths(minBaths);
		lowRent.setRentParty(rentParty);
		lowRent.setRentType(rentType);
		lowRent.setRentArchitecture(rentArchitecture);
		lowRent.setRentAge(rentAge);
		lowRent.setRentPetsAllowed(rentPetsAllowed);
		lowRent.setRentStatus(rentStatus);
		
		highRent.setAddress(highAddress);
		highRent.setRentPrice(maxPrice);
		highRent.setRentSurface(maxSurface);
		highRent.setRentRooms(maxRooms);
		highRent.setRentBaths(maxBaths);
		highRent.setRentParty(rentParty);
		highRent.setRentType(rentType);
		highRent.setRentArchitecture(rentArchitecture);
		highRent.setRentAge(rentAge);
		highRent.setRentPetsAllowed(rentPetsAllowed);
		
		rentSearch.setLowRent(lowRent);
		rentSearch.setHighRent(highRent);
		rentSearch.setPageSize(TestUtil.PAGE_SIZE);

		Response response = target.path("rents/search").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(rentSearch));
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);
		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		assertTrue(rentsCounter.counter >= rentsCounter.rents.size());
	}
	
	public void testSearchWithLargeRanges() {
		int minPrice = 0;
		int maxPrice = Integer.MAX_VALUE;
		int minSurface = 0;
		int maxSurface = Integer.MAX_VALUE;
		short minRooms = 1;
		short maxRooms = Short.MAX_VALUE;
		short minBaths = 1;
		short maxBaths = Short.MAX_VALUE;
		boolean rentPetsAllowed = false;
		byte rentStatus = RentStatus.AVAILABLE.getStatus();
		
		RentSearch rentSearch = new RentSearch();
		Rent lowRent = new Rent();
		Rent highRent = new Rent();
		Address lowAddress = new Address();
		Address highAddress = new Address();
		lowAddress.setAddressLatitude(MIN_LATITUDE);
		lowAddress.setAddressLongitude(MIN_LONGITUDE);
		highAddress.setAddressLatitude(MAX_LATITUDE);
		highAddress.setAddressLongitude(MAX_LONGITUDE);
		
		lowRent.setAddress(lowAddress);
		lowRent.setRentPrice(minPrice);
		lowRent.setRentSurface(minSurface);
		lowRent.setRentRooms(minRooms);
		lowRent.setRentBaths(minBaths);
		lowRent.setRentParty(Byte.MIN_VALUE);
		lowRent.setRentType(Byte.MIN_VALUE);
		lowRent.setRentArchitecture(Byte.MIN_VALUE);
		lowRent.setRentAge(Short.MIN_VALUE);
		lowRent.setRentPetsAllowed(rentPetsAllowed);
		lowRent.setRentStatus(rentStatus);
		
		highRent.setAddress(highAddress);
		highRent.setRentPrice(maxPrice);
		highRent.setRentSurface(maxSurface);
		highRent.setRentRooms(maxRooms);
		highRent.setRentBaths(maxBaths);
		highRent.setRentParty(Byte.MAX_VALUE);
		highRent.setRentType(Byte.MAX_VALUE);
		highRent.setRentArchitecture(Byte.MAX_VALUE);
		highRent.setRentAge(Short.MAX_VALUE);
		highRent.setRentPetsAllowed(rentPetsAllowed);

		rentSearch.setLowRent(lowRent);
		rentSearch.setHighRent(highRent);
		rentSearch.setPageSize(TestUtil.PAGE_SIZE);

		Response response = target.path("rents/search").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(rentSearch));
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		
		RentsCounter rentsCounter = response.readEntity(RentsCounter.class);
		assertTrue(rentsCounter.rents.size() > 0);
		assertTrue(rentsCounter.rents.size() <= TestUtil.PAGE_SIZE);
		assertTrue(rentsCounter.counter >= rentsCounter.rents.size());
	}
	
	public void testSearchNextPage() {
		int minPrice = 0;
		int maxPrice = Integer.MAX_VALUE;
		int minSurface = 0;
		int maxSurface = Integer.MAX_VALUE;
		short minRooms = 1;
		short maxRooms = Short.MAX_VALUE;
		short minBaths = 1;
		short maxBaths = Short.MAX_VALUE;
		boolean rentPetsAllowed = false;
		byte rentStatus = RentStatus.AVAILABLE.getStatus();
		
		RentSearch rentSearch = new RentSearch();
		Rent lowRent = new Rent();
		Rent highRent = new Rent();
		Address lowAddress = new Address();
		Address highAddress = new Address();
		lowAddress.setAddressLatitude(MIN_LATITUDE);
		lowAddress.setAddressLongitude(MIN_LONGITUDE);
		highAddress.setAddressLatitude(MAX_LATITUDE);
		highAddress.setAddressLongitude(MAX_LONGITUDE);
		
		lowRent.setAddress(lowAddress);
		lowRent.setRentPrice(minPrice);
		lowRent.setRentSurface(minSurface);
		lowRent.setRentRooms(minRooms);
		lowRent.setRentBaths(minBaths);
		lowRent.setRentParty(Byte.MIN_VALUE);
		lowRent.setRentType(Byte.MIN_VALUE);
		lowRent.setRentArchitecture(Byte.MIN_VALUE);
		lowRent.setRentAge(Short.MIN_VALUE);
		lowRent.setRentPetsAllowed(rentPetsAllowed);
		lowRent.setRentStatus(rentStatus);
		
		highRent.setAddress(highAddress);
		highRent.setRentPrice(maxPrice);
		highRent.setRentSurface(maxSurface);
		highRent.setRentRooms(maxRooms);
		highRent.setRentBaths(maxBaths);
		highRent.setRentParty(Byte.MAX_VALUE);
		highRent.setRentType(Byte.MAX_VALUE);
		highRent.setRentArchitecture(Byte.MAX_VALUE);
		highRent.setRentAge(Short.MAX_VALUE);
		highRent.setRentPetsAllowed(true);
		highRent.setRentAddDate(new Date());
		highRent.setRentId(Integer.MAX_VALUE);
		
		rentSearch.setLowRent(lowRent);
		rentSearch.setHighRent(highRent);
		rentSearch.setPageSize(TestUtil.PAGE_SIZE);

		Response response = target.path("rents/search/page").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(rentSearch));

		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
	}
}
