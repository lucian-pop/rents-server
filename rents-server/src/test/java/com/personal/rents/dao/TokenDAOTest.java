package com.personal.rents.dao;

import java.util.Date;

import junit.framework.TestCase;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.logic.TokenGenerator;
import com.personal.rents.model.Account;
import com.personal.rents.model.Token;
import com.personal.rents.util.TestUtil;

public class TokenDAOTest extends TestCase {
	
	private Account account;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		account = TestUtil.createAccountWithoutToken();
	}

	@Override
	protected void tearDown() throws Exception {
		TestUtil.deleteAccount(account);
		
		super.tearDown();
	}
	
	public void testAddToken() {
		Token token = new Token();
		token.setAccountId(account.getAccountId());
		token.setTokenKey(TokenGenerator.generateToken());
		token.setTokenCreationDate(new Date());
		
		int result = -1;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			TokenDAO tokenDAO = session.getMapper(TokenDAO.class);
			result = tokenDAO.insertToken(token);
			session.commit();
		} finally {
			session.close();
		}
		
		assertTrue(result == 1);
	}
	
	public void testGetToken() {
		// Add token first.
		Token token = new Token();
		String tokenKey = TokenGenerator.generateToken();
		token.setAccountId(account.getAccountId());
		token.setTokenKey(tokenKey);
		token.setTokenCreationDate(new Date());
		
		Token resultToken = null;
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			TokenDAO tokenDAO = session.getMapper(TokenDAO.class);
			tokenDAO.insertToken(token);
			session.commit();
			
			// Get token key.
			resultToken = tokenDAO.getToken(tokenKey);
		} finally {
			session.close();
		}
		
		assertNotNull(resultToken);
		System.out.println(resultToken.getAccountId() + " " + account.getAccountId());

		assertEquals(resultToken.getAccountId(), account.getAccountId());
	}
}
