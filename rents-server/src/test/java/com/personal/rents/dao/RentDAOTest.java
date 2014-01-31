package com.personal.rents.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.model.Account;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.util.TestUtil;

import junit.framework.TestCase;

public class RentDAOTest extends TestCase {
	
	private static final double MIN_LATITUDE = 46.7379424563698;

	private static final double MAX_LATITUDE = 46.76499396368981;

	private static final double MIN_LONGITUDE = 23.56791313737631;
	
	private static final double MAX_LONGITUDE = 23.59537862241268;

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
	
	public void testGetLightRentsByMapBoundaries() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsByMapBoundaries(MIN_LATITUDE, MAX_LATITUDE, MIN_LONGITUDE,
					MAX_LONGITUDE, TestUtil.PAGE_SIZE);
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
		
		Rent rent = result.get(0);
		address = rent.getAddress();
//		System.out.println("********Rent id: " + rent.getRentId());
//		System.out.println("********Rent price: " + rent.getRentPrice());
//		System.out.println("********Rent surface: " + rent.getRentSurface());
//		System.out.println("********Rent rooms: " + rent.getRentRooms());
//		System.out.println("********Rent baths: " + rent.getRentBaths());
//		System.out.println("********Rent party: " + rent.getRentParty());
//		System.out.println("********Rent type: " + rent.getRentType());
//		System.out.println("********Rent architecture: " + rent.getRentArchitecture());
//		System.out.println("********Rent age: " + rent.getRentAge());
//		System.out.println("********Rent add date: " + rent.getRentAddDate());
//		System.out.println("********Address id: " + address.getAddressId());
//		System.out.println("********Address street no: " + address.getAddressStreetNo());
//		System.out.println("********Address street name: " + address.getAddressStreetName());
//		System.out.println("********Address latitude: " + address.getAddressLatitude());
//		System.out.println("********Address longitude: " + address.getAddressLongitude());
	}
	
	public void testGetLightRentsNextPageByMapBoundaries() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsByMapBoundaries(MIN_LATITUDE, MAX_LATITUDE, MIN_LONGITUDE,
					MAX_LONGITUDE, TestUtil.PAGE_SIZE);
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
					TestUtil.PAGE_SIZE);
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
		
		Rent rent = result.get(0);
		address = rent.getAddress();
	}
	
	public void testGetNoOfRentsByMapBoundaries() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = 0;
		try {
			RentDAO rentDAO = (RentDAO) session.getMapper(RentDAO.class);
			result = rentDAO.getNoOfRentsByMapBoundaries(MIN_LATITUDE, MAX_LATITUDE, MIN_LONGITUDE,
					MAX_LONGITUDE);
		} finally {
			session.close();
		}
		
		assertTrue(result > 0);
	}
}
