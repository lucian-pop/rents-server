package ro.fizbo.rents.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import ro.fizbo.rents.dao.RentDAO;
import ro.fizbo.rents.dao.param.RentsStatus;
import ro.fizbo.rents.listener.ApplicationManager;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Address;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentArchitecture;
import ro.fizbo.rents.model.RentForm;
import ro.fizbo.rents.model.RentSearch;
import ro.fizbo.rents.model.RentStatus;
import ro.fizbo.rents.model.RentType;
import ro.fizbo.rents.model.view.RentFavoriteView;
import ro.fizbo.rents.util.RentSearchTestUtil;
import ro.fizbo.rents.util.TestUtil;
import junit.framework.TestCase;

public class RentDAOTest extends TestCase {

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
	
	public void testInsertAccommodation() {
		Rent rent = TestUtil.addRent(account.getAccountId(), (byte) 1);
		
		TestUtil.deleteRent(rent);
	}
	
	public void testGetRentsByMapBoundaries() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsByMapBoundaries(TestUtil.MIN_LATITUDE, TestUtil.MAX_LATITUDE,
					TestUtil.MIN_LONGITUDE, TestUtil.MAX_LONGITUDE,
					RentStatus.AVAILABLE.getStatus(), RentForm.RENT.getForm(), TestUtil.PAGE_SIZE, "");
		} finally {
			session.close();
		}

		assertTrue(result.size() > 0);
		assertTrue(result.size() <= TestUtil.PAGE_SIZE);
		
		Address address = null;
		for(Rent rent : result) {
			address = rent.getAddress();
			
			assertTrue(address.getAddressLatitude() >= TestUtil.MIN_LATITUDE);
			assertTrue(address.getAddressLatitude() <= TestUtil.MAX_LATITUDE);
			assertTrue(address.getAddressLongitude() >= TestUtil.MIN_LONGITUDE);
			assertTrue(address.getAddressLongitude() <= TestUtil.MAX_LONGITUDE);
		}
	}
	
	public void testGetRentsNextPageByMapBoundaries() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsByMapBoundaries(TestUtil.MIN_LATITUDE, TestUtil.MAX_LATITUDE,
					TestUtil.MIN_LONGITUDE, TestUtil.MAX_LONGITUDE, 
					RentStatus.AVAILABLE.getStatus(), RentForm.RENT.getForm(), 
					TestUtil.PAGE_SIZE, "");
		} finally {
			session.close();
		}

		assertTrue(result.size() > 0);
		assertTrue(result.size() <= TestUtil.PAGE_SIZE);
		
		Rent lastRent = result.get(result.size() - 1);
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsNextPageByMapBoundaries(TestUtil.MIN_LATITUDE, 
					TestUtil.MAX_LATITUDE, TestUtil.MIN_LONGITUDE, TestUtil.MAX_LONGITUDE,
					lastRent.getRentAddDate(), lastRent.getRentId(),
					RentStatus.AVAILABLE.getStatus(), RentForm.RENT.getForm(),
					TestUtil.PAGE_SIZE, "");
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
			
			assertTrue(address.getAddressLatitude() >= TestUtil.MIN_LATITUDE);
			assertTrue(address.getAddressLatitude() <= TestUtil.MAX_LATITUDE);
			assertTrue(address.getAddressLongitude() >= TestUtil.MIN_LONGITUDE);
			assertTrue(address.getAddressLongitude() <= TestUtil.MAX_LONGITUDE);
		}
	}
	
	public void testGetNoOfRentsByMapBoundaries() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = 0;
		try {
			RentDAO rentDAO = (RentDAO) session.getMapper(RentDAO.class);
			result = rentDAO.getNoOfRentsByMapBoundaries(TestUtil.MIN_LATITUDE, 
					TestUtil.MAX_LATITUDE, TestUtil.MIN_LONGITUDE, TestUtil.MAX_LONGITUDE,
					RentStatus.AVAILABLE.getStatus(), RentForm.RENT.getForm());
		} finally {
			session.close();
		}
		
		assertTrue(result > 0);
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
	
	public void testSearchCount() {
		RentSearch rentSearch = RentSearchTestUtil.getRentSearch();
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		Integer count = -1;
		try {
			count = session.selectOne("RentMapper.searchCount", rentSearch);
			session.commit();
		} finally {
			session.close();
		}

		assertTrue(count > 0);
	}
	
	public void testNewSearchWithSurfaceInBounds() {
		int minSurface = 20;
		int maxSurface = 70;
		Rent lowRent = new Rent();
		Address lowAddress = new Address();
		lowAddress.setAddressLatitude(TestUtil.MIN_LATITUDE);
		lowAddress.setAddressLongitude(TestUtil.MIN_LONGITUDE);
		lowRent.setAddress(lowAddress);
		lowRent.setRentSurface(minSurface);
		lowRent.setRentStatus(RentStatus.AVAILABLE.getStatus());
		lowRent.setRentForm(RentForm.RENT.getForm());
		lowRent.setRentPetsAllowed(false);
		lowRent.setRentParkingPlace(false);
		lowRent.setRentSmokersAllowed(false);
		
		Rent highRent = new Rent();
		Address highAddress = new Address();
		highAddress.setAddressLatitude(TestUtil.MAX_LATITUDE);
		highAddress.setAddressLongitude(TestUtil.MAX_LONGITUDE);
		highRent.setRentSurface(maxSurface);
		highRent.setAddress(highAddress);
		highRent.setRentPetsAllowed(true);
		highRent.setRentParkingPlace(true);
		highRent.setRentSmokersAllowed(true);
		
		RentSearch rentSearch = new RentSearch();
		rentSearch.setPageSize(TestUtil.PAGE_SIZE);
		rentSearch.setLowRent(lowRent);
		rentSearch.setHighRent(highRent);
		rentSearch.setAppUrl(ApplicationManager.getAppURL());
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			result = session.selectList("RentMapper.search", rentSearch);
			session.commit();
		} finally {
			session.close();
		}
		
		assertNotNull(result);
		assertTrue(result.size() > 0);
		for(Rent rent : result) {
			assertTrue(rent.getRentSurface() >= minSurface);
			assertTrue(rent.getRentSurface() <= maxSurface);
		}
	}
	
	public void testNewSearchWithLowSurface() {
		int minSurface = 100;
		Rent lowRent = new Rent();
		Address lowAddress = new Address();
		lowAddress.setAddressLatitude(TestUtil.MIN_LATITUDE);
		lowAddress.setAddressLongitude(TestUtil.MIN_LONGITUDE);
		lowRent.setAddress(lowAddress);
		lowRent.setRentSurface(minSurface);
		lowRent.setRentStatus(RentStatus.AVAILABLE.getStatus());
		lowRent.setRentForm(RentForm.RENT.getForm());
		lowRent.setRentPetsAllowed(false);
		lowRent.setRentParkingPlace(false);
		lowRent.setRentSmokersAllowed(false);
		
		Rent highRent = new Rent();
		Address highAddress = new Address();
		highAddress.setAddressLatitude(TestUtil.MAX_LATITUDE);
		highAddress.setAddressLongitude(TestUtil.MAX_LONGITUDE);
		highRent.setAddress(highAddress);
		highRent.setRentPetsAllowed(true);
		highRent.setRentParkingPlace(true);
		highRent.setRentSmokersAllowed(true);
		
		RentSearch rentSearch = new RentSearch();
		rentSearch.setPageSize(TestUtil.PAGE_SIZE);
		rentSearch.setLowRent(lowRent);
		rentSearch.setHighRent(highRent);
		rentSearch.setAppUrl(ApplicationManager.getAppURL());
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			result = session.selectList("RentMapper.search", rentSearch);
			session.commit();
		} finally {
			session.close();
		}
		
		assertNotNull(result);
		assertTrue(result.size() > 0);
		for(Rent rent : result) {
			assertTrue(rent.getRentSurface() >= minSurface);
		}
	}
	
	public void testNewSearchWithMaxSurface() {
		int maxSurface = 70;
		Rent lowRent = new Rent();
		Address lowAddress = new Address();
		lowAddress.setAddressLatitude(TestUtil.MIN_LATITUDE);
		lowAddress.setAddressLongitude(TestUtil.MIN_LONGITUDE);
		lowRent.setAddress(lowAddress);
		lowRent.setRentStatus(RentStatus.AVAILABLE.getStatus());
		lowRent.setRentForm(RentForm.RENT.getForm());
		lowRent.setRentPetsAllowed(false);
		lowRent.setRentParkingPlace(false);
		lowRent.setRentSmokersAllowed(false);
		
		Rent highRent = new Rent();
		Address highAddress = new Address();
		highAddress.setAddressLatitude(TestUtil.MAX_LATITUDE);
		highAddress.setAddressLongitude(TestUtil.MAX_LONGITUDE);
		highRent.setRentSurface(maxSurface);
		highRent.setAddress(highAddress);
		highRent.setRentPetsAllowed(true);
		highRent.setRentParkingPlace(true);
		highRent.setRentSmokersAllowed(true);
		
		RentSearch rentSearch = new RentSearch();
		rentSearch.setPageSize(TestUtil.PAGE_SIZE);
		rentSearch.setLowRent(lowRent);
		rentSearch.setHighRent(highRent);
		rentSearch.setAppUrl(ApplicationManager.getAppURL());
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			result = session.selectList("RentMapper.search", rentSearch);
			session.commit();
		} finally {
			session.close();
		}

		assertNotNull(result);
		assertTrue(result.size() > 0);
		for(Rent rent : result) {
			assertTrue(rent.getRentSurface() <= maxSurface);
		}
	}
	
	public void testSearch() {
		RentSearch rentSearch = RentSearchTestUtil.getRentSearch();
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> rents = null;
		try {
			rents = session.selectList("RentMapper.search", rentSearch);
			session.commit();
		} finally {
			session.close();
		}

		assertTrue(rents.size() > 0);
		assertTrue(rents.size() <= rentSearch.getPageSize());
		for(Rent rent : rents) {
			assertTrue(rent.getAddress().getAddressLatitude() >= TestUtil.MIN_LATITUDE);
			assertTrue(rent.getAddress().getAddressLatitude() <= TestUtil.MAX_LATITUDE);
			assertTrue(rent.getAddress().getAddressLongitude() >= TestUtil.MIN_LONGITUDE);
			assertTrue(rent.getAddress().getAddressLongitude() <= TestUtil.MAX_LONGITUDE);
			assertTrue(rent.getRentSurface() >= RentSearchTestUtil.MIN_SURFACE);
			assertTrue(rent.getRentSurface() <= RentSearchTestUtil.MAX_SURFACE);
			assertTrue(rent.getRentPrice() >= RentSearchTestUtil.MIN_PRICE);
			assertTrue(rent.getRentPrice() <= RentSearchTestUtil.MAX_PRICE);
			assertTrue(rent.getRentRooms() >= RentSearchTestUtil.MIN_ROOMS);
			assertTrue(rent.getRentRooms() <= RentSearchTestUtil.MAX_ROOMS);
			assertTrue(rent.getRentBaths() >= RentSearchTestUtil.MIN_BATHS);
			assertTrue(rent.getRentBaths() <= RentSearchTestUtil.MAX_BATHS);
			assertTrue(rent.getRentType() == RentType.APARTMENT.getType());
			assertTrue(rent.getRentArchitecture() == RentArchitecture.DETACHED.getArchitecture());
		}
	}
	
	public void testSearchNextPage() {
		RentSearch rentSearch = RentSearchTestUtil.getRentSearch();
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> rents = null;
		try {
			rents = session.selectList("RentMapper.search", rentSearch);
			session.commit();
		} finally {
			session.close();
		}

		assertTrue(rents.size() > 0);
		assertTrue(rents.size() <= rentSearch.getPageSize()	);
		Rent lastRent = rents.get(rents.size() - 1);
		rentSearch.getHighRent().setRentAddDate(lastRent.getRentAddDate());
		rentSearch.getHighRent().setRentId(lastRent.getRentId());
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			rents = session.selectList("RentMapper.searchNextPage", rentSearch);
			session.commit();
		} finally {
			session.close();
		}

		assertTrue(rents.size() > 0);
		assertTrue(rents.size() <= TestUtil.PAGE_SIZE);
		Rent pageFirstRent = rents.get(0);
		assertTrue(pageFirstRent.getRentAddDate().getTime() <= lastRent.getRentAddDate().getTime());
		assertTrue((pageFirstRent.getRentAddDate().getTime() < lastRent.getRentAddDate().getTime())
				|| (pageFirstRent.getRentId() < lastRent.getRentId()));
		for(Rent rent : rents) {
			assertTrue(rent.getAddress().getAddressLatitude() >= TestUtil.MIN_LATITUDE);
			assertTrue(rent.getAddress().getAddressLatitude() <= TestUtil.MAX_LATITUDE);
			assertTrue(rent.getAddress().getAddressLongitude() >= TestUtil.MIN_LONGITUDE);
			assertTrue(rent.getAddress().getAddressLongitude() <= TestUtil.MAX_LONGITUDE);
			assertTrue(rent.getRentSurface() >= RentSearchTestUtil.MIN_SURFACE);
			assertTrue(rent.getRentSurface() <= RentSearchTestUtil.MAX_SURFACE);
			assertTrue(rent.getRentPrice() >= RentSearchTestUtil.MIN_PRICE);
			assertTrue(rent.getRentPrice() <= RentSearchTestUtil.MAX_PRICE);
			assertTrue(rent.getRentRooms() >= RentSearchTestUtil.MIN_ROOMS);
			assertTrue(rent.getRentRooms() <= RentSearchTestUtil.MAX_ROOMS);
			assertTrue(rent.getRentBaths() >= RentSearchTestUtil.MIN_BATHS);
			assertTrue(rent.getRentBaths() <= RentSearchTestUtil.MAX_BATHS);
			assertTrue(rent.getRentType() == RentType.APARTMENT.getType());
			assertTrue(rent.getRentArchitecture() == RentArchitecture.DETACHED.getArchitecture());
		}
	}
}
