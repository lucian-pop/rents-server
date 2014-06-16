package ro.fizbo.rents.dao;

import java.util.Date;

import junit.framework.TestCase;

import org.apache.ibatis.session.SqlSession;

import ro.fizbo.rents.dao.TokenDAO;
import ro.fizbo.rents.logic.TokenGenerator;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Token;
import ro.fizbo.rents.util.TestUtil;

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
	
	public void testGetTokenByAccountId() {
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
			
			resultToken = tokenDAO.getTokenByAccountId(account.getAccountId());
		} finally {
			session.close();
		}
		
		assertNotNull(resultToken);
		assertEquals(resultToken.getAccountId(), account.getAccountId());
	}
	
	public void testUpdateTokenKey() {
		// Add token first.
		Token token = new Token();
		String tokenKey = TokenGenerator.generateToken();
		token.setAccountId(account.getAccountId());
		token.setTokenKey(tokenKey);
		token.setTokenCreationDate(new Date());
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		int updateCount = -1;
		try {
			TokenDAO tokenDAO = session.getMapper(TokenDAO.class);
			tokenDAO.insertToken(token);
			session.commit();
			
			updateCount = tokenDAO.updateTokenKey(token.getAccountId(), 
					TokenGenerator.generateToken(), new Date());
		} finally {
			session.close();
		}
		
		assertTrue(updateCount == 1);
	}
}
