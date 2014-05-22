package com.personal.rents.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.dao.param.RentsStatus;
import com.personal.rents.model.Account;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentAge;
import com.personal.rents.model.RentArchitecture;
import com.personal.rents.model.RentParty;
import com.personal.rents.model.RentStatus;
import com.personal.rents.model.RentType;
import com.personal.rents.model.view.RentFavoriteView;
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
		Rent rent = TestUtil.addRent(account.getAccountId());
		
		TestUtil.deleteRent(rent);
	}
	
	public void testGetRentsByMapBoundaries() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsByMapBoundaries(MIN_LATITUDE, MAX_LATITUDE, MIN_LONGITUDE,
					MAX_LONGITUDE, RentStatus.AVAILABLE.getStatus(), TestUtil.PAGE_SIZE, "");
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
					MAX_LONGITUDE, RentStatus.AVAILABLE.getStatus(), TestUtil.PAGE_SIZE, "");
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
					RentStatus.AVAILABLE.getStatus(), TestUtil.PAGE_SIZE, "");
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
					rentArchitecture, rentAge, rentAge, rentPetsAllowed, true, rentStatus);
		} finally {
			session.close();
		}
		
		assertTrue(result > 0);
	}
	
	public void testSearch() {
		int minPrice = 100;
		int maxPrice = 10000;
		int minSurface = 50;
		int maxSurface = 1000;
		short minRooms = 1;
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
					rentArchitecture, rentAge, rentAge, rentPetsAllowed, true, rentStatus,
					TestUtil.PAGE_SIZE,"");
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
			assertTrue(resultRent.getRentType() == rentType);
			assertTrue(resultRent.getRentArchitecture() == rentArchitecture);
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
					maxArchitecture, minAge, maxAge, rentPetsAllowed, true, rentStatus,
					TestUtil.PAGE_SIZE, "");
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
					maxArchitecture, minAge, maxAge, rentPetsAllowed, true, rentStatus, 
					lastRent.getRentAddDate(), lastRent.getRentId(), TestUtil.PAGE_SIZE, "");
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
	
	public void testGetRentDetails() {
		Rent rent = TestUtil.addRent(account.getAccountId());
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		Rent resultRent = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			resultRent = rentDAO.getDetailedRent(rent.getRentId(), "");
		} finally {
			session.close();
		}
		
		assertTrue(resultRent != null);
		assertTrue(resultRent.getAccountId().intValue() == account.getAccountId().intValue());
		assertTrue(resultRent.getRentPrice() != null);
		assertTrue(resultRent.getRentSurface() != null);
		assertTrue(resultRent.getRentRooms() != null);
		assertTrue(resultRent.getRentBaths() != null);
		assertTrue(resultRent.getRentParty() != null);
		assertTrue(resultRent.getRentType() != null);
		assertTrue(resultRent.getRentArchitecture() != null);
		assertTrue(resultRent.getRentAge() != null);
		assertTrue(resultRent.getRentDescription() != null);
		assertTrue(resultRent.isRentPetsAllowed() != null);
		assertTrue(resultRent.getRentPhone() != null);
		assertTrue(resultRent.getRentAddDate() != null);
		assertTrue(resultRent.getRentStatus() != null);
		
		Address resultAddress = rent.getAddress();
		assertTrue(resultAddress != null);
		assertTrue(resultAddress.getAddressStreetNo() != null);
		assertTrue(resultAddress.getAddressStreetName() != null);
		assertTrue(resultAddress.getAddressNeighbourhood() != null);
		assertTrue(resultAddress.getAddressLocality() != null);
		assertTrue(resultAddress.getAddressAdmAreaL1() != null);
		assertTrue(resultAddress.getAddressCountry() != null);
		assertTrue(resultAddress.getAddressLatitude() != null);
		assertTrue(resultAddress.getAddressLongitude() != null);
		assertTrue(resultAddress.getAddressBuilding() != null);
		assertTrue(resultAddress.getAddressStaircase() != null);
		assertTrue(resultAddress.getAddressFloor() != null);
		assertTrue(resultAddress.getAddressAp() != null);

		TestUtil.deleteRent(rent);
	}
	
	public void testGetNoOfUserAddedRents() {
		Rent rent = TestUtil.addRent(account.getAccountId());
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = -1;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getNoOfUserAddedRents(account.getAccountId(), RentStatus.AVAILABLE.getStatus());
		} finally {
			session.close();
		}
		
		assertTrue(result > 0);
		
		TestUtil.deleteRent(rent);
	}
	
	public void testGetUserAddedRents() {
		List<Rent> userAddedRents = new ArrayList<Rent>(TestUtil.PAGE_SIZE + 1);
		for(int i = 0; i < TestUtil.PAGE_SIZE + 1; i++) {
			Rent rent = TestUtil.addRent(account.getAccountId());
			userAddedRents.add(rent);
		}
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getUserAddedRents(account.getAccountId(), 
					RentStatus.AVAILABLE.getStatus(), TestUtil.PAGE_SIZE, "");
		} finally {
			session.close();
		}
		
		assertTrue(result.size() > 0);
		assertTrue(result.size() <= TestUtil.PAGE_SIZE);
		
		for(Rent rent : userAddedRents) {
			TestUtil.deleteRent(rent);
		}
	}
	
	public void testGetUserAddedRentsNextPage() {
		List<Rent> userAddedRents = new ArrayList<Rent>(TestUtil.PAGE_SIZE + 1);
		for(int i = 0; i < TestUtil.PAGE_SIZE + 1; i++) {
			Rent rent = TestUtil.addRent(account.getAccountId());
			userAddedRents.add(rent);
		}
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getUserAddedRents(account.getAccountId(), 
					RentStatus.AVAILABLE.getStatus(), TestUtil.PAGE_SIZE, "");
		} finally {
			session.close();
		}
		
		assertTrue(result.size() > 0);
		assertTrue(result.size() <= TestUtil.PAGE_SIZE);
		
		Rent lastRent = result.get(result.size() - 1);
		session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> nextPageResult = null;
		try { 
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			nextPageResult = rentDAO.getUserAddedRentsNextPage(account.getAccountId(),
					RentStatus.AVAILABLE.getStatus(), lastRent.getRentAddDate(),
					lastRent.getRentId(), TestUtil.PAGE_SIZE, "");
		} finally {
			session.close();
		}
		
		assertTrue(nextPageResult.size() > 0);
		assertTrue(nextPageResult.size() <= TestUtil.PAGE_SIZE);
		
		Rent pageFirstRent = nextPageResult.get(0);
		assertTrue(pageFirstRent.getRentAddDate().getTime() <= lastRent.getRentAddDate().getTime());
		assertTrue((pageFirstRent.getRentAddDate().getTime() < lastRent.getRentAddDate().getTime())
				|| (pageFirstRent.getRentId() < lastRent.getRentId()));
		
		for(Rent rent : userAddedRents) {
			TestUtil.deleteRent(rent);
		}
	}
	
	public void testUpdateRentsStatus() {
		Rent firstRent = TestUtil.addRent(account.getAccountId());
		Rent secondRent = TestUtil.addRent(account.getAccountId());

		List<Integer> rentIds = new ArrayList<Integer>();
		rentIds.add(firstRent.getRentId());
		rentIds.add(secondRent.getRentId());
		
		RentsStatus  param = new RentsStatus();
		param.status = RentStatus.AVAILABLE.getStatus();
		param.rentIds = rentIds;

		int updated = -1;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			updated = session.update("RentMapper.updateRentsStatus", param);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(updated == 2);
		
		TestUtil.deleteRent(firstRent);
		TestUtil.deleteRent(secondRent);
	}

	public void testGetUserFavoriteRents() {
		int accountId = 2;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<RentFavoriteView> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getUserFavoriteRents(accountId, Integer.MAX_VALUE, "");
		} finally {
			session.close();
		}
		
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}
	
	public void testGetUserFavoriteRentsNextPage() {
		int accountId = 2;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<RentFavoriteView> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getUserFavoriteRents(accountId, TestUtil.PAGE_SIZE, "");
		} finally {
			session.close();
		}
		
		assertNotNull(result);
		assertTrue(result.size() > 0);
		assertTrue(result.size() <= TestUtil.PAGE_SIZE);
		
		RentFavoriteView lastRentFavorite = result.get(result.size() - 1);
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getUserFavoriteRentsNextPage(accountId, lastRentFavorite.getRentFavoriteAddDate(),
					TestUtil.PAGE_SIZE, "");
		} finally {
			session.close();
		}
		
		assertNotNull(result);
		assertTrue(result.size() > 0);
		assertTrue(result.size() <= TestUtil.PAGE_SIZE);
		
		RentFavoriteView firstNextFavorite = result.get(0);
		assertTrue(firstNextFavorite.getRentFavoriteAddDate().getTime()
				< lastRentFavorite.getRentFavoriteAddDate().getTime());
	}
	
	public void testUpdateRent() {
		Rent rent = TestUtil.addRent(account.getAccountId());

		rent.setRentPrice(450);
		rent.setRentSurface(80);
		rent.setRentRooms((short) 3);
		rent.setRentBaths((short) 3);
		rent.setRentParty((byte) 0);
		rent.setRentType((byte) 0);
		rent.setRentArchitecture((byte) 0);
		rent.setRentAge((short) 0);
		rent.setRentDescription("");
		rent.setRentPetsAllowed(false);
		rent.setRentPhone("0744555666");
		rent.setRentAddDate(new Date());
		int updated = -1;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			updated = session.update("RentMapper.updateRent", rent);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(updated == 1);
		
		TestUtil.deleteRent(rent);
	}
}
