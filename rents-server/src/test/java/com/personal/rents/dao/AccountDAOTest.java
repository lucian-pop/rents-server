package com.personal.rents.dao;

import java.util.Date;

import junit.framework.TestCase;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.dao.AccountDAO;
import com.personal.rents.logic.TokenGenerator;
import com.personal.rents.model.Account;
import com.personal.rents.util.TestUtil;

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
		testAccount.setAccountEmail("test@gmail.com");
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
		String password = account.getAccountPassword();
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			testAccount = accountDAO.getAccount(email, password);
		} finally {
			session.close();
		}
		
		assertNotNull(testAccount);
		
		assertTrue(testAccount.getTokenKey().equals(account.getTokenKey()));
	}
	
	public void testGetAccountIdByEmailPassword() {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		Integer accountId = null;
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			accountId = accountDAO.getAccountId(account.getAccountEmail(), account.getAccountPassword());
		} finally {
			session.close();
		}
		
		assertNotNull(accountId);
		assertTrue((int) accountId == account.getAccountId());
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
}
