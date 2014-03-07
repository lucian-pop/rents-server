package com.personal.rents.logic;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.personal.rents.dao.AccountDAO;
import com.personal.rents.dao.TokenDAO;
import com.personal.rents.listener.ApplicationManager;
import com.personal.rents.model.Account;
import com.personal.rents.model.Token;
import com.personal.rents.util.PasswordHashing;
import com.personal.rents.webservice.exception.AuthenticationException;
import com.personal.rents.webservice.exception.OperationFailedException;
import com.personal.rents.webservice.exception.AccountConflictException;

public final class AccountManager {
	
	private static Logger logger = Logger.getLogger(AccountManager.class);
	
	public static Account createAccount(Account account) {
		// Create a token to be linked with the account.
		Token token = new Token();
		token.setTokenKey(TokenGenerator.generateToken());
		token.setTokenCreationDate(new Date());
		
		String hashedPassword = createHashedPassword(account.getAccountPassword());
		account.setAccountPassword(hashedPassword);
		
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			Account existingAccount = accountDAO.getAccountByEmailOrPhone(account.getAccountEmail(),
					account.getAccountPhone());
			if(existingAccount != null) {
				throw new AccountConflictException();
			}
			
			// Create account.
			int insertCount = -1;
			int retry = 0;
			while (insertCount != 1 && retry < 3) {
				insertCount = accountDAO.insertAccount(account);
				++retry;
			}

			// Save token for account.
			if (account.getAccountId() != null) {
				token.setAccountId(account.getAccountId());
				TokenDAO tokenDAO = session.getMapper(TokenDAO.class);
				
				insertCount = -1;
				retry = 0;
				while(insertCount != 1 && retry < 3) {
					insertCount = tokenDAO.insertToken(token);
					++retry;
				}
			}

			session.commit();
		} catch (AccountConflictException accountConflictException) {
			logger.error("Email or phone are already registered. Failed to create account for email" 
					+ account.getAccountEmail());
			
			throw accountConflictException;
		} catch (RuntimeException runtimeException) {
			logger.error("Unable to create account with email " + account.getAccountEmail(), runtimeException);
			session.rollback();
			
			throw new OperationFailedException();
		} finally {
			session.close();
		}

		renewAccountCredentials(account, token.getTokenKey());
		
		return account;
	}

	public static Account login(String email, String password) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		Account account = null;
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			account = accountDAO.getAccountByEmail(email);
		} finally {
			session.close();
		}

		if(account == null || !isPasswordValid(password, account.getAccountPassword())) {
			throw new AuthenticationException();
		}
		
		// Set password field to null. We use the token for authorization.
		String tokenKey = TokenManager.getNewTokenKey(account.getAccountId());
		renewAccountCredentials(account, tokenKey);
		
		return account;
	}
	
	public static String changePassword(String email, String password, String newPassword) {
		String tokenKey = TokenGenerator.generateToken();
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		int updatesCount = -1;
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			Account account = accountDAO.getAccountByEmail(email);
			
			if(account == null || !isPasswordValid(password, account.getAccountPassword())) {
				throw new AuthenticationException();
			}
			
			String hashedNewPassword = createHashedPassword(newPassword);

			int retry = 0;
			while(updatesCount != 2 && retry < 3) {
				updatesCount = accountDAO.updatePassword(account.getAccountId(),  hashedNewPassword, 
						tokenKey, new Date());
				++retry;
			}
			
			session.commit();
		} finally {
			if(updatesCount != 2) {
				session.rollback();
			}

			session.close();
		}

		if(updatesCount != 2) {
			throw new OperationFailedException();
		}
		
		return tokenKey;
	}
	
	private static void renewAccountCredentials(Account account, String tokenKey) {
		account.setAccountId(null);
		account.setAccountPassword(null);
		account.setTokenKey(tokenKey);
	}
	
	private static boolean isPasswordValid(String providedPassword, String accountPassword) {
		boolean isPasswordValid = false;
		try {
			isPasswordValid = PasswordHashing.validatePassword(providedPassword, accountPassword);
		} catch (NoSuchAlgorithmException e) {
			throw new OperationFailedException();
		} catch (InvalidKeySpecException e) {
			throw new OperationFailedException();
		}
		
		return isPasswordValid;
	}
	
	private static String createHashedPassword(String password) {
		String hashedPassword = null;
		try {
			hashedPassword = PasswordHashing.createHashString(password);
		} catch (NoSuchAlgorithmException e) {
			throw new OperationFailedException();
		} catch (InvalidKeySpecException e) {
			throw new OperationFailedException();
		}
		
		return hashedPassword;
	}
}
