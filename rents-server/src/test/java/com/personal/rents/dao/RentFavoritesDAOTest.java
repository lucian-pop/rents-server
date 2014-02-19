package com.personal.rents.dao;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentFavorite;
import com.personal.rents.util.TestUtil;

import junit.framework.TestCase;

public class RentFavoritesDAOTest extends TestCase {
	
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
	
	public void testAddNewEntry() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = -1;
		try {
			RentFavoritesDAO rentFavoritesDAO = session.getMapper(RentFavoritesDAO.class);
			result = rentFavoritesDAO.addEntry(account.getAccountId(), rent.getRentId());
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
		
		TestUtil.deleteRentFavorite(account.getAccountId(), rent.getRentId());
	}
	
	public void testAddDuplicateEntry() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = -1;
		try {
			RentFavoritesDAO rentFavoritesDAO = session.getMapper(RentFavoritesDAO.class);
			result = rentFavoritesDAO.addEntry(account.getAccountId(), rent.getRentId());
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
		
		boolean isDuplicate = false;
		try {
			RentFavoritesDAO rentFavoritesDAO = session.getMapper(RentFavoritesDAO.class);
			result = rentFavoritesDAO.addEntry(account.getAccountId(), rent.getRentId());
			session.commit();
		} catch(RuntimeException e) {
			isDuplicate = true;
		} finally {
			session.close();
		}
		
		assertTrue(isDuplicate);
		
		TestUtil.deleteRentFavorite(account.getAccountId(), rent.getRentId());
	}
	
	public void testGetEntry() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = -1;
		try {
			RentFavoritesDAO rentFavoritesDAO = session.getMapper(RentFavoritesDAO.class);
			result = rentFavoritesDAO.addEntry(account.getAccountId(), rent.getRentId());
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);

		session = TestUtil.getSqlSessionFactory().openSession();
		RentFavorite rentFavorite = null;
		try {
			RentFavoritesDAO rentFavoritesDAO = session.getMapper(RentFavoritesDAO.class);
			rentFavorite = rentFavoritesDAO.getEntry(account.getAccountId(), rent.getRentId());
		} finally {
			session.close();
		}
		
		assertNotNull(rentFavorite);
		
		TestUtil.deleteRentFavorite(account.getAccountId(), rent.getRentId());
	}

}
