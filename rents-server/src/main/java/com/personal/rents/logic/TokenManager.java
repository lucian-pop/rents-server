package com.personal.rents.logic;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.personal.rents.dao.TokenDAO;
import com.personal.rents.listener.ApplicationManager;
import com.personal.rents.model.Token;

public final class TokenManager {

	private static Logger logger = Logger.getLogger(TokenManager.class);
	
	public static String createAuthToken(int accountId) {
		logger.info("Create access token for account with id" + accountId);
		
		// Generate token
		String tokenKey = TokenGenerator.generateToken();
		Token token = new Token();
		token.setAccountId(accountId);
		token.setTokenKey(tokenKey);
		token.setTokenCreationDate(new Date());
		
		// insert token into database
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try{
			TokenDAO tokenDAO = session.getMapper(TokenDAO.class);
			tokenDAO.insertToken(token);
			session.commit();
		} finally {
			session.close();
		}

		return tokenKey;
	}
	
	public static Token getToken(String tokenKey) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		Token token = null;
		try {
			TokenDAO tokenDAO = session.getMapper(TokenDAO.class);
			token = tokenDAO.getToken(tokenKey);
		} finally {
			session.close();
		}

		return token;
	}
	
	/**
	 * !!! Has to be implemented.
	 *
	 */
	public static String updateToken(int accountId) {
		return null;
	}
	
	/**
	 * !!! Has to be implemented.
	 *
	 */
	public static boolean isTokenValid(String token) {
		return true;
	}
	
	public static final String getValidToken(String tokenKey, int accountId) {
		if(tokenKey == null) {
			tokenKey = TokenManager.createAuthToken(accountId);
		} else {
			boolean invalidToken = !TokenManager.isTokenValid(tokenKey);
			if(invalidToken) {
				tokenKey = TokenManager.updateToken(accountId);
			}
		}
		
		return tokenKey;
	}
	
	public static void invalidateAuthToken(int accountId) {
	}
}
