package ro.fizbo.rents.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import ro.fizbo.rents.dao.RentFavoriteDAO;
import ro.fizbo.rents.dao.param.RentsFavorites;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentFavorite;
import ro.fizbo.rents.util.TestUtil;
import junit.framework.TestCase;

public class RentFavoriteDAOTest extends TestCase {
	
	private Account account;
	
	private List<Rent> rents = new ArrayList<Rent>(TestUtil.PAGE_SIZE + 1);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		for(int i = 0; i < TestUtil.PAGE_SIZE + 1; i++) {
			Rent rent = TestUtil.addRent(account.getAccountId());
			rents.add(rent);
		}
		
	}

	@Override
	protected void tearDown() throws Exception {
		for(Rent rent : rents) {
			TestUtil.deleteRent(rent);
		}
		TestUtil.deleteAccount(account);

		super.tearDown();
	}
	
	public void testAddNewEntry() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = -1;
		try {
			RentFavoriteDAO rentFavoritesDAO = session.getMapper(RentFavoriteDAO.class);
			result = rentFavoritesDAO.addEntry(account.getAccountId(), rents.get(0).getRentId(),
					new Date());
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
		
		TestUtil.deleteRentFavorite(account.getAccountId(), rents.get(0).getRentId());
	}
	
	public void testAddDuplicateEntry() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = -1;
		try {
			RentFavoriteDAO rentFavoritesDAO = session.getMapper(RentFavoriteDAO.class);
			result = rentFavoritesDAO.addEntry(account.getAccountId(), rents.get(0).getRentId(),
					new Date());
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
		
		boolean isDuplicate = false;
		try {
			RentFavoriteDAO rentFavoritesDAO = session.getMapper(RentFavoriteDAO.class);
			result = rentFavoritesDAO.addEntry(account.getAccountId(), rents.get(0).getRentId(),
					new Date());
			session.commit();
		} catch(RuntimeException e) {
			isDuplicate = true;
		} finally {
			session.close();
		}
		
		assertTrue(isDuplicate);
		
		TestUtil.deleteRentFavorite(account.getAccountId(), rents.get(0).getRentId());
	}
	
	public void testGetEntry() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = -1;
		try {
			RentFavoriteDAO rentFavoritesDAO = session.getMapper(RentFavoriteDAO.class);
			result = rentFavoritesDAO.addEntry(account.getAccountId(), rents.get(0).getRentId(),
					new Date());
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);

		session = TestUtil.getSqlSessionFactory().openSession();
		RentFavorite rentFavorite = null;
		try {
			RentFavoriteDAO rentFavoritesDAO = session.getMapper(RentFavoriteDAO.class);
			rentFavorite = rentFavoritesDAO.getEntry(account.getAccountId(), rents.get(0).getRentId());
		} finally {
			session.close();
		}
		
		assertNotNull(rentFavorite);
		
		TestUtil.deleteRentFavorite(account.getAccountId(), rents.get(0).getRentId());
	}
	
	public void testGetNoOfUserFavoriteRents() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentFavoriteDAO rentFavoriteDAO = session.getMapper(RentFavoriteDAO.class);
			for(Rent rent : rents) {
				rentFavoriteDAO.addEntry(account.getAccountId(), rent.getRentId(), new Date());
			}
			session.commit();
		} finally {
			session.close();
		}
		
		int result = -1;
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentFavoriteDAO rentFavoriteDAO = session.getMapper(RentFavoriteDAO.class);
			result = rentFavoriteDAO.getNoOfUserFavoriteRents(account.getAccountId());
		} finally {
			session.close();
		}
		
		assertTrue(result == rents.size());
		
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentFavoriteDAO rentFavoriteDAO = session.getMapper(RentFavoriteDAO.class);
			for(Rent rent : rents) {
				rentFavoriteDAO.deleteEntry(account.getAccountId(), rent.getRentId());
			}
			session.commit();
		} finally {
			session.close();
		}
	}
	
	
	public void testDeleteUserFavoriteRents() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentFavoriteDAO rentFavoriteDAO = session.getMapper(RentFavoriteDAO.class);
			for(Rent rent : rents) {
				rentFavoriteDAO.addEntry(account.getAccountId(), rent.getRentId(), new Date());
			}
			session.commit();
		} finally {
			session.close();
		}
		
		RentsFavorites rentsFavorites = new RentsFavorites();
		rentsFavorites.accountId = account.getAccountId();
		List<Integer> rentIds = new ArrayList<Integer>();
		for(Rent rent : rents) {
			rentIds.add(rent.getRentId());
		}
		rentsFavorites.rentIds = rentIds;
		
		int deleteCount = -1;
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			deleteCount = session.delete("RentFavoriteMapper.deleteFavoriteRents", rentsFavorites);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(deleteCount == rents.size());
	}
}
