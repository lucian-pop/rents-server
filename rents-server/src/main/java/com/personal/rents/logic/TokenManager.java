package com.personal.rents.logic;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.dao.TokenDAO;
import com.personal.rents.listener.ApplicationManager;
import com.personal.rents.model.Token;

public final class TokenManager {
	
	public static Token getToken(String tokenKey) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		Token token = null;
		try {
			TokenDAO tokenDAO = session.getMapper(TokenDAO.class);
			token = tokenDAO.getTokenByTokenKey(tokenKey);
		} finally {
			session.close();
		}

		return token;
	}
	
	public static String getNewTokenKey(int accountId) {
		String newTokenKey = TokenGenerator.generateToken();

		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			TokenDAO tokenDAO = session.getMapper(TokenDAO.class);
			Token token = tokenDAO.getTokenByAccountId(accountId);
			
			if(token == null) {
				token = new Token();
				token.setTokenKey(newTokenKey);
				token.setTokenCreationDate(new Date());
				token.setAccountId(accountId);
				
				int retry = 0;
				int insertCount = -1;
				while(insertCount != 1 && retry < 3) {
					insertCount = tokenDAO.insertToken(token);
					++retry;
				}
				
			} else {
				int retry = 0;
				int insertCount = -1;
				while(insertCount != 1 && retry < 3) {
					insertCount = tokenDAO.updateTokenKey(token.getTokenId(), newTokenKey,
							new Date());
					++retry;
				}
			}
			
			session.commit();
		} finally {
			session.close();
		}

		return newTokenKey;
	}
}
