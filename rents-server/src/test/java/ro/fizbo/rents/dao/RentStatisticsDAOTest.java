package ro.fizbo.rents.dao;

import org.apache.ibatis.session.SqlSession;

import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.util.TestUtil;
import junit.framework.TestCase;

public class RentStatisticsDAOTest extends TestCase {
	
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
	
	public void testInsertUpdate() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int result = 0;
		try {
			RentStatisticsDAO rentStatisticsDAO = session.getMapper(RentStatisticsDAO.class);
			result = rentStatisticsDAO.updateInsert(rent.getRentId());
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
	}
}
