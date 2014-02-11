package com.personal.rents.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.model.Account;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentAge;
import com.personal.rents.model.RentArchitecture;
import com.personal.rents.model.RentParty;
import com.personal.rents.model.RentStatus;
import com.personal.rents.model.RentType;
import com.personal.rents.util.TestUtil;

import junit.framework.TestCase;

public class RentDAOTest extends TestCase {
	
	private static final double MIN_LATITUDE = -90;

	private static final double MAX_LATITUDE = 90;

	private static final double MIN_LONGITUDE = -180;
	
	private static final double MAX_LONGITUDE = 180;

	private Account account;
	
	private Address address;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		address = TestUtil.addAddress();
	}

	@Override
	protected void tearDown() throws Exception {
		TestUtil.deleteAccount(account);
		TestUtil.deleteAddress(address);
		
		super.tearDown();
	}

	public void testInsertRent() {
		Rent rent = new Rent();
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
		
		int result = -1;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.insertRent(rent);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
		
		// delete the added rent
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rentDAO.deleteRent(rent.getRentId());
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void testGetRentsByMapBoundaries() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsByMapBoundaries(MIN_LATITUDE, MAX_LATITUDE, MIN_LONGITUDE,
					MAX_LONGITUDE, RentStatus.AVAILABLE.getStatus(), TestUtil.PAGE_SIZE);
		} finally {
			session.close();
		}

		assertTrue(result.size() > 0);
		assertTrue(result.size() <= TestUtil.PAGE_SIZE);
		
		Address address = null;
		for(Rent rent : result) {
			address = rent.getAddress();
			
			assertTrue(address.getAddressLatitude() >= MIN_LATITUDE);
			assertTrue(address.getAddressLatitude() <= MAX_LATITUDE);
			assertTrue(address.getAddressLongitude() >= MIN_LONGITUDE);
			assertTrue(address.getAddressLongitude() <= MAX_LONGITUDE);
		}
	}
	
	public void testGetRentsNextPageByMapBoundaries() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsByMapBoundaries(MIN_LATITUDE, MAX_LATITUDE, MIN_LONGITUDE,
					MAX_LONGITUDE, RentStatus.AVAILABLE.getStatus(), TestUtil.PAGE_SIZE);
		} finally {
			session.close();
		}

		assertTrue(result.size() > 0);
		assertTrue(result.size() <= TestUtil.PAGE_SIZE);
		
		Rent lastRent = result.get(result.size() - 1);
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsNextPageByMapBoundaries(MIN_LATITUDE, MAX_LATITUDE,
					MIN_LONGITUDE, MAX_LONGITUDE, lastRent.getRentAddDate(), lastRent.getRentId(),
					RentStatus.AVAILABLE.getStatus(), TestUtil.PAGE_SIZE);
		} finally {
			session.close();
		}

		assertTrue(result.size() > 0);
		assertTrue(result.size() <= TestUtil.PAGE_SIZE);
		
		Rent pageFirstRent = result.get(0);
		assertTrue(pageFirstRent.getRentAddDate().getTime() <= lastRent.getRentAddDate().getTime());
		assertTrue((pageFirstRent.getRentAddDate().getTime() < lastRent.getRentAddDate().getTime())
				|| (pageFirstRent.getRentId() < lastRent.getRentId()));
		
		Address address = null;
		for(Rent rent : result) {
			address = rent.getAddress();
			
			assertTrue(address.getAddressLatitude() >= MIN_LATITUDE);
			assertTrue(address.getAddressLatitude() <= MAX_LATITUDE);
			assertTrue(address.getAddressLongitude() >= MIN_LONGITUDE);
			assertTrue(address.getAddressLongitude() <= MAX_LONGITUDE);
		}
	}
	
	public void testGetNoOfRentsByMapBoundaries() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = 0;
		try {
			RentDAO rentDAO = (RentDAO) session.getMapper(RentDAO.class);
			result = rentDAO.getNoOfRentsByMapBoundaries(MIN_LATITUDE, MAX_LATITUDE, MIN_LONGITUDE,
					MAX_LONGITUDE, RentStatus.AVAILABLE.getStatus());
		} finally {
			session.close();
		}
		
		assertTrue(result > 0);
	}
	
	public void testSearchResultSize() {
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

		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = 0;
		try {
			RentDAO rentDAO = (RentDAO) session.getMapper(RentDAO.class);
			result = rentDAO.searchResultSize(MIN_LATITUDE, MAX_LATITUDE, MIN_LONGITUDE,
					MAX_LONGITUDE, minPrice, maxPrice, minSurface, maxSurface, minRooms, maxRooms,
					minBaths, maxBaths, rentParty, rentParty, rentType, rentType, rentArchitecture,
					rentArchitecture, rentAge, rentAge, rentPetsAllowed, rentStatus);
		} finally {
			session.close();
		}
		
		assertTrue(result > 0);
	}
	
	public void testSearch() {
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
		
		// retrieve rents that meet out criteria.
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> results = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			results = rentDAO.search(MIN_LATITUDE, MAX_LATITUDE, MIN_LONGITUDE,
					MAX_LONGITUDE, minPrice, maxPrice, minSurface, maxSurface, minRooms, maxRooms,
					minBaths, maxBaths, rentParty, rentParty, rentType, rentType, rentArchitecture,
					rentArchitecture, rentAge, rentAge, rentPetsAllowed, rentStatus,
					TestUtil.PAGE_SIZE);
		} finally {
			session.close();
		}

		assertTrue(results.size() > 0);
		assertTrue(results.size() <= TestUtil.PAGE_SIZE);
		
		// verify if the results meet our criteria.
		for(Rent resultRent : results) {
			assertTrue(resultRent.getAddress().getAddressLatitude() <=  MAX_LATITUDE);
			assertTrue(resultRent.getAddress().getAddressLatitude() >=  MIN_LATITUDE);
			assertTrue(resultRent.getAddress().getAddressLongitude() <=  MAX_LONGITUDE);
			assertTrue(resultRent.getAddress().getAddressLongitude() >=  MIN_LONGITUDE);
			assertTrue(resultRent.getRentPrice() <=  maxPrice);
			assertTrue(resultRent.getRentPrice() >=  minPrice);
			assertTrue(resultRent.getRentSurface() <= maxSurface);
			assertTrue(resultRent.getRentSurface() >= minSurface);
			assertTrue(resultRent.getRentRooms() <=  maxRooms);
			assertTrue(resultRent.getRentRooms() >=  minRooms);
			assertTrue(resultRent.getRentBaths() <=  maxBaths);
			assertTrue(resultRent.getRentBaths() >=  minBaths);
			assertTrue(resultRent.getRentParty() == rentParty);
			assertTrue(resultRent.getRentType() == rentType);
			assertTrue(resultRent.getRentArchitecture() == rentArchitecture);
			assertTrue(resultRent.getRentAge() == rentAge);
			assertTrue(resultRent.isRentPetsAllowed() == rentPetsAllowed);
			assertTrue(resultRent.getRentStatus() == rentStatus);
		}
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
		byte minParty = RentParty.INDIVIDUAL.getParty();
		byte maxParty = RentParty.REALTOR.getParty();
		byte minType = RentType.APARTMENT.getType();
		byte maxType = RentType.OFFICE.getType();
		byte minArchitecture = RentArchitecture.DETACHED.getArchitecture();
		byte maxArchitecture = RentArchitecture.UNDETACHED.getArchitecture();
		short minAge = RentAge.NEW.getAge();
		short maxAge = RentAge.OLD.getAge();
		boolean rentPetsAllowed = false;
		byte rentStatus = RentStatus.AVAILABLE.getStatus();
		
		// retrieve rents that meet out criteria.
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> results = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			results = rentDAO.search(MIN_LATITUDE, MAX_LATITUDE, MIN_LONGITUDE,
					MAX_LONGITUDE, minPrice, maxPrice, minSurface, maxSurface, minRooms, maxRooms,
					minBaths, maxBaths, minParty, maxParty, minType, maxType, minArchitecture,
					maxArchitecture, minAge, maxAge, rentPetsAllowed, rentStatus,
					TestUtil.PAGE_SIZE);
		} finally {
			session.close();
		}

		assertTrue(results.size() > 0);
		assertTrue(results.size() <= TestUtil.PAGE_SIZE);
		
		Rent lastRent = results.get(results.size() - 1);
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			results = rentDAO.searchNextPage(MIN_LATITUDE, MAX_LATITUDE, MIN_LONGITUDE,
					MAX_LONGITUDE, minPrice, maxPrice, minSurface, maxSurface, minRooms, maxRooms,
					minBaths, maxBaths, minParty, maxParty, minType, maxType, minArchitecture,
					maxArchitecture, minAge, maxAge, rentPetsAllowed, rentStatus, 
					lastRent.getRentAddDate(), lastRent.getRentId(), TestUtil.PAGE_SIZE);
		} finally {
			session.close();
		}

		assertTrue(results.size() > 0);
		assertTrue(results.size() <= TestUtil.PAGE_SIZE);
		
		Rent pageFirstRent = results.get(0);
		assertTrue(pageFirstRent.getRentAddDate().getTime() <= lastRent.getRentAddDate().getTime());
		assertTrue((pageFirstRent.getRentAddDate().getTime() < lastRent.getRentAddDate().getTime())
				|| (pageFirstRent.getRentId() < lastRent.getRentId()));
		
		Address address = null;
		for(Rent rent : results) {
			address = rent.getAddress();
			
			assertTrue(address.getAddressLatitude() >= MIN_LATITUDE);
			assertTrue(address.getAddressLatitude() <= MAX_LATITUDE);
			assertTrue(address.getAddressLongitude() >= MIN_LONGITUDE);
			assertTrue(address.getAddressLongitude() <= MAX_LONGITUDE);
		}
	}
}
