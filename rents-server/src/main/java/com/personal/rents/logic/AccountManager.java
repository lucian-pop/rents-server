package com.personal.rents.logic;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.dao.AccountDAO;
import com.personal.rents.listener.ApplicationManager;
import com.personal.rents.model.Account;

public final class AccountManager {
	
	public static Account createAccount(Account account) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountMapper = session.getMapper(AccountDAO.class);
			accountMapper.insertAccount(account);
			session.commit();
		} finally {
			session.close();
		}
		
		// Set password field to null. We use the token for authorization.
		account.setPassword(null);
		
		// Generate token and save in db for later authorization.
		String tokenKey = TokenManager.createAuthToken(account.getId());
		account.setTokenKey(tokenKey);
		
		return account;
	}

	public static Account login(String email, String password) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		Account account = null;
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			account = accountDAO.login(email, password);
		} finally {
			session.close();
		}

		// Make sure the account gets a valid authorization token.
		String tokenKey = TokenManager.getValidToken(account.getTokenKey(), account.getId());
		account.setTokenKey(tokenKey);
		
		return account;
	}
	
	public static String changePassword(String email, String password, String newPassword) {
		String tokenKey = null;
		
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			
			Integer accountId = accountDAO.getAccountId(email, password);
			if(accountId == null) {
				return null;
			}
			
			tokenKey = TokenGenerator.generateToken();
			int result = accountDAO.updatePassword(accountId, newPassword, tokenKey, new Date());
			session.commit();

			if(result != 2) {
				return null;
			}
		} finally {
			session.close();
		}
		
		return tokenKey;
	}
	
}
