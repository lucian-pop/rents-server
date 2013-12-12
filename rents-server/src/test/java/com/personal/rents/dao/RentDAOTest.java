package com.personal.rents.dao;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.model.Account;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.util.TestUtil;

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
		Rent rent = new Rent();
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
			rentDAO.deleteRent(rent.getId());
			session.commit();
		} finally {
			session.close();
		}
	}
}
