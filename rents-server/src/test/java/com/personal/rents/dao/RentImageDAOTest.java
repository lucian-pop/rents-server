package com.personal.rents.dao;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentImage;
import com.personal.rents.util.TestUtil;

import junit.framework.TestCase;

public class RentImageDAOTest extends TestCase {
	
	private Account account;
	
	private Rent rent;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		rent = TestUtil.addRent(account.getAccountId());
	}

	@Override
	protected void tearDown() throws Exception {
		TestUtil.deleteRent(rent);
		TestUtil.deleteAccount(account);
		
		super.tearDown();
	}

	public void testInsertRentImage() {
		RentImage rentImage = new RentImage();
		rentImage.setRentId(rent.getRentId());
		rentImage.setRentImageURI("\\images\\1\\13123123213\\1.jpg");
		
		int result = -1;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentImageDAO rentImageDAO = session.getMapper(RentImageDAO.class);
			result = rentImageDAO.insertRentImage(rentImage);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
	}
	
	public void testUpdateRentImageURI() {
		RentImage rentImage = new RentImage();
		rentImage.setRentId(rent.getRentId());
		rentImage.setRentImageURI("\\images\\1\\13123123213\\1.jpg");
		
		int result = -1;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentImageDAO rentImageDAO = session.getMapper(RentImageDAO.class);
			result = rentImageDAO.insertRentImage(rentImage);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
		
		String newRentImageURI = "\\images\\1\\213213213213312312\\1.jpg";
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentImageDAO rentImageDAO = session.getMapper(RentImageDAO.class);
			result = rentImageDAO.updateRentImageURI(rentImage.getRentImageId(), newRentImageURI);
			
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
	}
	
}
