package ro.fizbo.rents.dao;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;

import ro.fizbo.rents.logic.TokenGenerator;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentAbuse;
import ro.fizbo.rents.model.RentAbuseStatus;
import ro.fizbo.rents.util.EmailConstants;
import ro.fizbo.rents.util.TestUtil;
import junit.framework.TestCase;

public class RentAbuseDAOTest extends TestCase {
	
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
//		TestUtil.deleteRent(rent);
//		TestUtil.deleteAccount(account);
		
		super.tearDown();
	}

	public void testInsertRentAbuse() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		RentAbuse rentAbuse = new RentAbuse();
		rentAbuse.setRentAbuseTokenKey(TokenGenerator.generateToken());
		rentAbuse.setRentAbuseEmail(EmailConstants.ADMIN_REPORT_EMAIL);
		rentAbuse.setRentAbuseDate(new Date());
		rentAbuse.setRentAbuseDescription("Agentie imobiliara");
		rentAbuse.setRentAbuseStatus((byte) 0);
		rentAbuse.setRent(rent);
		int result = -1;
		try {
			result = session.insert("RentAbuseMapper.insertRentAbuse", rentAbuse);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
	}
	
	public void testUpdateRentAbuse() {
		String rentAbuseTokenKey = TokenGenerator.generateToken();
		RentAbuse rentAbuse = new RentAbuse();
		rentAbuse.setRentAbuseTokenKey(rentAbuseTokenKey);
		rentAbuse.setRentAbuseEmail(EmailConstants.ADMIN_REPORT_EMAIL);
		rentAbuse.setRentAbuseDate(new Date());
		rentAbuse.setRentAbuseDescription("Agentie imobiliara");
		rentAbuse.setRentAbuseStatus((byte) 0);
		rentAbuse.setRent(rent);
		int result = -1;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			result = session.insert("RentAbuseMapper.insertRentAbuse", rentAbuse);
			session.commit();
		} finally {
			session.close();
		}
		assertTrue(result == 1);
		
		rentAbuse.setRentAbuseStatus(RentAbuseStatus.RESOLVED_NOT_OK.getStatus());
		session = TestUtil.getSqlSessionFactory().openSession();
		int updatedRentRows = -1;
		try {
			result = session.update("RentAbuseMapper.updateRentAbuse", rentAbuse);
			updatedRentRows = session.update("RentMapper.disableRentForAbuse", rentAbuseTokenKey);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
		assertTrue(updatedRentRows == 1);
	}
}
