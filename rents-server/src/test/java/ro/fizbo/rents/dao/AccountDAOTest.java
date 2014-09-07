package ro.fizbo.rents.dao;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.apache.ibatis.session.SqlSession;

import ro.fizbo.rents.dao.AccountDAO;
import ro.fizbo.rents.logic.TokenGenerator;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.util.TestUtil;

public class AccountDAOTest extends TestCase {

	private Account account;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
	}

	@Override
	protected void tearDown() throws Exception {
		TestUtil.deleteAccount(account);
		
		super.tearDown();
	}

	public void testAddAccountWithAllValues() {
		Date date = new Date();
		
		Account testAccount = new Account();
		testAccount.setAccountType((byte) 0);
		testAccount.setAccountExternalId("sadsadkjhsadas2dsa5dasd4asd5a5232323");
		testAccount.setAccountEmail("test@email.com");
		testAccount.setAccountPassword("dummypassword");
		testAccount.setAccountFirstname("Dummy firstname");
		testAccount.setAccountLastname("Dummy lastname");
		testAccount.setAccountPhone("+4 0100800800");
		testAccount.setAccountSignupDate(date);

		int result = -1;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountMapper = session.getMapper(AccountDAO.class);
			result = accountMapper.insertAccount(testAccount);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
		
		// delete the added account
		session = TestUtil.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountMapper = session.getMapper(AccountDAO.class);
			accountMapper.deleteAccount(testAccount.getAccountId());
			session.commit();
		} finally {
			session.close();
		}
		
	}
	
	public void testGetAccountById() {
		Account testAccount = null;

		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountMapper = session.getMapper(AccountDAO.class);
			testAccount = accountMapper.getAccountById(account.getAccountId());
			session.commit();
		} finally {
			session.close();
		}
		
		assertNotNull(testAccount);
	}

	public void testLogin() {
		Account testAccount = null;
		String email = account.getAccountEmail();
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			testAccount = accountDAO.getAccountByEmail(email);
		} finally {
			session.close();
		}
		
		assertNotNull(testAccount);
	}
	
	public void testGetAccountByEmail() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			account = accountDAO.getAccountByEmail(account.getAccountEmail());
		} finally {
			session.close();
		}
		
		assertNotNull(account);
	}
	
	public void testUpdatePassword() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		String newPassword = "some new password";
		String tokenKey = TokenGenerator.generateToken();
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			accountDAO.updatePassword(account.getAccountId(), newPassword, tokenKey, new Date());
			session.commit();
			
			// Get the updated account.
			account = accountDAO.getAccountById(account.getAccountId());
		} finally {
			session.close();
		}
		
		assertTrue(account.getAccountPassword().equals(newPassword));
		assertTrue(account.getTokenKey().equals(tokenKey));
	}
	
	public void testGetAccountByEmailOrPhoneWithMatchingEmail() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Account> result = null;
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			result = accountDAO.getAccountByEmailOrPhone(account.getAccountEmail(), 
					"some random number");
		} finally {
			session.close();
		}
		
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}
	
	public void testGetAccountByEmailOrPhoneWithMatchingPhone() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		List<Account> result = null;
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			result = accountDAO.getAccountByEmailOrPhone("some random email", account.getAccountPhone());
		} finally {
			session.close();
		}
		
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}
	
	public void testUpdateAccount() {
		String email = "updated.account@email.com";
		String phone = "0100111000";

		account.setAccountEmail(email);
		account.setAccountPhone(phone);
		int updated = -1;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			updated = session.update("AccountMapper.updateAccount", account);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(updated == 1);
	}
	
	public void testUpdateAccountExternalInfo() {
		account.setAccountExternalId("32456223565764353");
		account.setAccountFirstname("John Smith");
		
		int updated = -1;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			updated = session.update("AccountMapper.updateAccountExternalInfo", account);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(updated == 1);
	}
}
